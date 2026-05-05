# Create Big Cannons: Peripheral
<p align="center">
  <img src="brand/icon.png" width="256" style="border-radius: 50%"/>
</p>
> This mod adds CC: Tweaked peripherals to Create Big Cannons

## TL;DR
> This mod adds CC: Tweaked peripherals to Create Big Cannons.

- `assemble()` / `disassemble()` - assembles and disassembles the cannon, respectively.
- `fire()` - fires the cannon; throws an error if the cannon is disassembled.
- `getYaw()` / `getPitch()` - returns the cannon's current yaw and pitch.
- `getX()` / `getY()` / `getZ()` - returns the cannon mount block position.
- `isRunning()` - returns the cannon's assembly status.
- `setYaw(yaw)` / `setPitch(pitch)` - sets the cannon angles; not available on `cbc_fixed_cannon_mount`.

## Downloading
Mod is available at Modrinth and GitHub releases.

- Modrinth (still under review): https://modrinth.com/project/cbcperipheral
- GitHub Releases: https://github.com/nosqd/cbcperipheral/releases

Also nightly builds available as GitHub Actions artifacts: https://github.com/nosqd/cbcperipheral/actions

## API Documentation
API documentation is available here: https://github.com/nosqd/cbcperipheral/wiki/Peripherals

## Developing
The mod uses nothing more complicated than Gradle, so you can open it in IntelliJ IDEA and start writing code.

## Contributing
All contributions are welcome. The mod's code and all of its assets are licensed under the MIT License.

## History
This mod is based on code from VS: Addition, but I wanted to make it physics engine agnostic.