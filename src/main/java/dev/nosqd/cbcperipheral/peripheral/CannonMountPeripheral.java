package dev.nosqd.cbcperipheral.peripheral;

import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.lua.LuaFunction;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IPeripheral;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import rbasamoyai.createbigcannons.cannon_control.cannon_mount.CannonMountBlockEntity;
import rbasamoyai.createbigcannons.cannon_control.contraption.AbstractMountedCannonContraption;
import rbasamoyai.createbigcannons.cannon_control.contraption.PitchOrientedContraptionEntity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class CannonMountPeripheral implements IPeripheral {
    private static final Field F_CANNON_YAW;
    private static final Field F_CANNON_PITCH;
    private static final Field F_RUNNING;
    private static final Method M_ASSEMBLE;
    private static final Method M_FIRE_SHOT;

    static {
        try {
            F_CANNON_YAW = CannonMountBlockEntity.class.getDeclaredField("cannonYaw");
            F_CANNON_YAW.setAccessible(true);

            F_CANNON_PITCH = CannonMountBlockEntity.class.getDeclaredField("cannonPitch");
            F_CANNON_PITCH.setAccessible(true);

            F_RUNNING = CannonMountBlockEntity.class.getDeclaredField("running");
            F_RUNNING.setAccessible(true);

            M_ASSEMBLE = CannonMountBlockEntity.class.getDeclaredMethod("assemble");
            M_ASSEMBLE.setAccessible(true);

            M_FIRE_SHOT = AbstractMountedCannonContraption.class.getDeclaredMethod(
                    "fireShot",
                    ServerLevel.class,
                    PitchOrientedContraptionEntity.class
            );
            M_FIRE_SHOT.setAccessible(true);

        } catch (NoSuchFieldException | NoSuchMethodException e) {
            throw new RuntimeException(
                    "[cbcperipheral] Reflection setup failed — CBC may have renamed fields/methods. " +
                            "Check cannonYaw, cannonPitch, running, assemble(), fireShot().", e
            );
        }
    }

    private final CannonMountBlockEntity mount;

    public CannonMountPeripheral(CannonMountBlockEntity mount) {
        this.mount = mount;
    }

    private float rawYaw() {
        try { return (float) F_CANNON_YAW.get(mount); }
        catch (IllegalAccessException e) { return 0f; }
    }

    private float rawPitch() {
        try { return (float) F_CANNON_PITCH.get(mount); }
        catch (IllegalAccessException e) { return 0f; }
    }

    private boolean rawRunning() {
        try { return (boolean) F_RUNNING.get(mount); }
        catch (IllegalAccessException e) { return false; }
    }

    @Nonnull
    @Override
    public String getType() {
        return "cbc_cannon_mount";
    }

    @Override
    public boolean equals(@Nullable IPeripheral other) {
        if (other instanceof CannonMountPeripheral o) return this.mount == o.mount;
        return false;
    }

    @LuaFunction(mainThread = true)
    public final boolean assemble() throws LuaException {
        if (rawRunning()) return false;
        try {
            M_ASSEMBLE.invoke(mount);
        } catch (Exception e) {
            Throwable cause = e.getCause() != null ? e.getCause() : e;
            throw new LuaException("Assembly failed: " + cause.getMessage());
        }
        PitchOrientedContraptionEntity contraption = mount.getContraption();
        if (contraption != null) {
            ((AbstractMountedCannonContraption) contraption.getContraption())
                    .onRedstoneUpdate(
                            (ServerLevel) mount.getLevel(),
                            contraption,
                            false,
                            0,
                            mount
                    );
        }
        return true;
    }

    @LuaFunction(mainThread = true)
    public final void fire() throws LuaException {
        PitchOrientedContraptionEntity contraption = mount.getContraption();
        if (contraption == null) throw new LuaException("Cannon is not assembled");
        if (!(mount.getLevel() instanceof ServerLevel serverLevel))
            throw new LuaException("Cannot fire from client side");
        try {
            M_FIRE_SHOT.invoke(contraption.getContraption(), serverLevel, contraption);
        } catch (Exception e) {
            Throwable cause = e.getCause() != null ? e.getCause() : e;
            throw new LuaException("Fire failed: " + cause.getMessage());
        }
    }

    @LuaFunction(mainThread = true)
    public final boolean disassemble() {
        if (!rawRunning()) return false;
        mount.disassemble();
        return true;
    }

    @LuaFunction
    public final boolean isRunning() {
        return rawRunning();
    }

    @LuaFunction
    public final double getYaw() {
        return rawYaw();
    }
    @LuaFunction
    public final double getPitch() {
        return rawPitch();
    }

    @LuaFunction(mainThread = true)
    public final void setYaw(double yaw) {
        mount.setYaw((float) yaw);
    }
    @LuaFunction(mainThread = true)
    public final void setPitch(double pitch) {
        mount.setPitch((float) pitch);
    }

    @LuaFunction
    public final int getX() {
        return mount.getControllerBlockPos().getX();
    }
    @LuaFunction
    public final int getY() {
        return mount.getControllerBlockPos().getY();
    }
    @LuaFunction
    public final int getZ() {
        return mount.getControllerBlockPos().getZ();
    }

    @LuaFunction
    public final String getDirection() {
        return mount.getControllerState()
                .getValue(BlockStateProperties.HORIZONTAL_FACING)
                .toString();
    }
}