# NameMask

A Paper 1.21.11 plugin that lets players rename themselves with `/nickname <name>` and reset with `/nickname reset`.

## Features

- Updates the player's visible Paper profile name, which also updates the tab list.
- Saves nicknames by UUID in `plugins/NameMask/nicknames.yml`.
- Re-applies saved nicknames when players join again.

## Installation

1. Download the latest jar from `build/libs/` or your GitHub/Modrinth release.
2. Place it in your server's `plugins` folder.
3. Start or restart your Paper 1.21.11 server.

## Build

This project targets Java 21.

```bash
./gradlew build
```

The built plugin jar will be in `build/libs/`.

## Commands

- `/nickname <name>`
- `/nickname reset`

## Permission

- `namemask.nickname`
