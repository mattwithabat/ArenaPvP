# ArenaPvP

A lightweight 1v1 arena system for Spigot/Paper servers with Elo ratings and kit management.

## Features

- 1v1 arena matchmaking
- Elo rating system
- Kit management
- Queue system
- Win/loss tracking

## Installation

1. Download the latest `.jar` file
2. Place it in your server's `plugins` folder
3. Restart your server

## Commands

| Command | Description |
|---------|-------------|
| `/arena join` | Join the arena queue |
| `/arena leave` | Leave the arena |
| `/arena stats [player]` | View Elo stats |
| `/kit create <name>` | Create a kit from your inventory |
| `/duel <player>` | Challenge a player |

## Configuration

Edit `plugins/ArenaPvP/config.yml` to customize:
- Arena spawn locations
- Elo starting rating and K-factor
- Kit behavior

## License

MIT License
