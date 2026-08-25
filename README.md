# NameMask

[![Build](https://github.com/tiraaamisuuu/paper-nickname-plugin/actions/workflows/build.yml/badge.svg)](https://github.com/tiraaamisuuu/paper-nickname-plugin/actions/workflows/build.yml)
[![Latest release](https://img.shields.io/github/v/release/tiraaamisuuu/paper-nickname-plugin)](https://github.com/tiraaamisuuu/paper-nickname-plugin/releases/latest)

NameMask is a lightweight Paper plugin for persistent player nicknames. Players can set a nickname with `/nickname <name>` and restore their Minecraft username with `/nickname reset`.

## Compatibility

- Paper **1.16.1 and newer**, tested through Paper 26.2 build 119.
- One plugin jar works across all supported versions.
- The jar targets Java 8 bytecode. Run the Java version required by your Paper server; see [Paper's version table](https://docs.papermc.io/paper/getting-started/#requirements).
- Spigot and forks other than Paper are not supported.

NameMask builds its release jar against Paper 1.16.1 and continuously compiles the same source against the latest stable Paper API. The plugin declares `api-version: 1.16`, allowing newer servers to load the same jar.

## Downloads

- [GitHub Releases](https://github.com/tiraaamisuuu/paper-nickname-plugin/releases/latest)
- [Modrinth](https://modrinth.com/plugin/name-mask)

Do not download the automatically generated GitHub source archives; download the `NameMask-<version>.jar` release asset.

## Features

- Changes the player's profile, display, and tab-list names.
- Restores the original Minecraft username with `/nickname reset`.
- Saves nicknames by UUID in `plugins/NameMask/nicknames.yml`.
- Re-applies saved nicknames when players join and after plugin reloads.
- Rejects invalid or already tracked names.
- Has no bundled libraries or runtime dependencies.

Chat plugins can choose their own name renderer. If chat does not show the nickname, configure that plugin to use the player's display name.

## Installation

1. Download the latest `NameMask-<version>.jar` from GitHub Releases or Modrinth.
2. Place it in the server's `plugins` folder.
3. Start or restart the Paper server.
4. Run `/nickname <name>` in game.

Existing `nicknames.yml` data is kept when upgrading.

## Commands

| Command | Description |
| --- | --- |
| `/nickname <name>` | Set a 1-16 character nickname containing letters, numbers, or underscores. |
| `/nickname reset` | Restore your original Minecraft username. |
| `/nick ...` | Short alias for `/nickname`. |

`clear` and `off` are also accepted as reset aliases.

## Permissions

| Permission | Default | Description |
| --- | --- | --- |
| `namemask.nickname` | Everyone | Set or reset your own nickname. |

## Building from source

NameMask uses the included Gradle wrapper and compiles to Java 8 bytecode. Building requires JDK 17 or newer:

```bash
./gradlew clean build
```

Windows:

```powershell
.\gradlew.bat clean build
```

The jar is written to `build/libs/`. To build an exact release version locally:

```bash
./gradlew clean build -PpluginVersion=1.1.0
```

See [RELEASING.md](RELEASING.md) for the tag-based release process and [CHANGELOG.md](CHANGELOG.md) for notable changes.

## Data storage

Nicknames are stored in `plugins/NameMask/nicknames.yml` and associated with player UUIDs. Back up this file with the rest of the server's plugin data.

## License

NameMask is licensed under the [MIT License](LICENSE).
