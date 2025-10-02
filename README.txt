# AA_Mbvd - Ace Attorney Mod for Minecraft 1.16.5

A Minecraft mod that brings elements from the Ace Attorney series into Minecraft, featuring badges, evidence system, courtroom mechanics, and iconic characters.

## Features

### Core Gameplay Elements
- **Court Records System**: A 54-slot container for storing and managing evidence
- **Evidence System**: Collect and present evidence during gameplay
- **Badge Mechanics**: Attorney badges and other iconic items from the series
- **Sound Effects**: Authentic audio clips from Ace Attorney games

### Items & Equipment
- Attorney's Badge
- Prosecutor's Badge
- Metal Detector
- Various evidence items
- Curios integration with charm slots for badges and magatamas

### Entities & Rendering
- Phoenix Wright character model with animations
- Badge entity rendering
- Custom block rendering for silver ore and blocks

### Capabilities & Data Management
- Player capability system for evidence data
- Court records tile entity management
- Custom data holders for gameplay state

## Installation

1. Download and install Minecraft 1.16.5
2. Install Forge 1.16.5
3. Download the latest release of this mod
4. Place the mod file in your [mods](file://net\minecraftforge\client\event\InputEvent.java#L64-L64) folder
5. Launch Minecraft with the Forge profile

## Dependencies

- Minecraft 1.16.5
- Forge 1.16.5
- Curios API
- GeckoLib

## Building from Source

This mod uses Gradle for building:

##Clone the repository
git clone https://github.com/your-username/aa-mbvd.git

##Navigate to the project directory
cd aa-mbvd

##Build the mod
./gradlew build

The built mod file will be located in `build/libs/`.

## Key Classes and Systems

### Main Mod Class
- [AA_MbvdMod](file://D:\MODS\1_16_5\aa-mbvd\src\main\java\com\cookiewyq\aa_mbvd\AA_MbvdMod.java#L49-L166): Main mod class handling initialization and event registration

### Core Systems
- [CourtRecordsContainer](file://D:\MODS\1_16_5\aa-mbvd\src\main\java\com\cookiewyq\aa_mbvd\container\CourtRecordsContainer.java#L19-L171): 54-slot evidence container system
- [CourtRecordsGUI](file://D:\MODS\1_16_5\aa-mbvd\src\main\java\com\cookiewyq\aa_mbvd\screen\CourtRecordsGUI.java#L21-L70): GUI interface for the court records
- [IShowingEvidenceData](file://D:\MODS\1_16_5\aa-mbvd\src\main\java\com\cookiewyq\aa_mbvd\capability\IShowingEvidenceData.java#L15-L71): Player capability for evidence management
- [MinecraftEvidences](file://D:\MODS\1_16_5\aa-mbvd\src\main\java\com\cookiewyq\aa_mbvd\items\MinecraftEvidences.java#L5-L9): Evidence registration and management

### Rendering & Animation
- [PhoenixWrightRenderer](file://D:\MODS\1_16_5\aa-mbvd\src\main\java\com\cookiewyq\aa_mbvd\renderers\PhoenixWrightRenderer.java#L15-L30): Character model renderer
- Animation files in `assets/aa_mbvd/animations/`

## Configuration

The mod includes configurable settings in [ModConfigs.java](file://D:\MODS\1_16_5\aa-mbvd\src\main\java\com\cookiewyq\aa_mbvd\configs\ModConfigs.java) for adjusting gameplay parameters.

## Known Issues

- Large file warnings may appear during Git operations (historical large files have been removed)
- Some animations may require optimization

## Contributing

1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request

## License

This mod is provided as-is for educational and entertainment purposes. All rights to Ace Attorney belong to Capcom.

## Credits

- Inspired by the Ace Attorney series by Capcom
- Uses GeckoLib for animations
- Integrates with Curios API for equipment slots
