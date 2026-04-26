package dev.nosqd.cbcperipheral;

import dan200.computercraft.api.peripheral.PeripheralCapability;
import dev.nosqd.cbcperipheral.peripheral.CannonMountPeripheral;
import dev.nosqd.cbcperipheral.peripheral.FixedCannonMountPeripheral;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.ModContainer;
import rbasamoyai.createbigcannons.index.CBCBlockEntities;

@Mod(CBCPeripheral.MODID)
public class CBCPeripheral {
    public static final String MODID = "cbcperipheral";

    public CBCPeripheral(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.addListener(this::registerCapabilities);
    }

    public void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(
                PeripheralCapability.get(),
                CBCBlockEntities.CANNON_MOUNT.get(),
                (blockEntity, side) -> new CannonMountPeripheral(blockEntity)
        );

        event.registerBlockEntity(
                PeripheralCapability.get(),
                CBCBlockEntities.FIXED_CANNON_MOUNT.get(),
                (blockEntity, side) -> new FixedCannonMountPeripheral(blockEntity)
        );

    }
}
