# NameMask

A lightweight Paper plugin that lets players set persistent nicknames using `/nickname <name>` and reset back to their original username with `/nickname reset`.

## Downloads

NameMask is available on **Modrinth**:

https://modrinth.com/plugin/name-mask

## Features

* Change your visible in-game name with `/nickname <name>`.
* Reset your nickname at any time with `/nickname reset`.
* Updates the player's visible Paper profile name and tab-list name.
* Stores nicknames by player UUID in `plugins/NameMask/nicknames.yml`.
* Automatically re-applies saved nicknames when players rejoin.
* Nicknames persist across server restarts.
* Lightweight with no additional dependencies.

## Installation

1. Download the latest NameMask `.jar` from [Modrinth](https://modrinth.com/plugin/name-mask) or the GitHub Releases page.
2. Place the `.jar` in your Paper server's `plugins` folder.
3. Start or restart the server.
4. Use `/nickname <name>` in-game.

## Requirements

* **Paper**
* **Java 21**

Check the latest release for supported Minecraft versions.

## Commands

| Command            | Description                                |
| ------------------ | ------------------------------------------ |
| `/nickname <name>` | Sets your nickname.                        |
| `/nickname reset`  | Restores your original Minecraft username. |

## Permissions

| Permission          | Description                                       |
| ------------------- | ------------------------------------------------- |
| `namemask.nickname` | Allows the player to set or reset their nickname. |

## Building from Source

NameMask uses Gradle and targets Java 21.

```bash
./gradlew build
```

The compiled plugin `.jar` will be generated in:

```text
build/libs/
```

## Data Storage

Player nicknames are stored locally in:

```text
plugins/NameMask/nicknames.yml
```

Nicknames are associated with UUIDs rather than usernames, so they remain associated with the correct player if their Minecraft username changes.

## License

NameMask is licensed under the [MIT License](LICENSE).
