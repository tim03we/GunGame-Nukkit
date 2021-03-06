<h1>GunGame<img src="http://imgur.com/tCTL2QD.png" height="64" width="64" align="left"></img></h1>
<br />

GunGame is a nice MiniGame that hasn't been seen often in the Minecraft Bedrock Edition scene before. That's why I want to give you one. This MiniGame also includes a self-configurable Config that allows you to customize the game to your needs.

# Features
- Edit the game news
- Activate or deactivate certain events
- Change the chat format, as well as your nametag in the game
# Setup
- Download the plugin as Jar
- Pack the plugin into the plugins folder
- Set the game mode in server.properties to Adventure (value: 2)
- Start / restart the server
- You can play with your friends now

# Config
```
# At which level should you no longer be able to receive kits?
Maximum-Level: 3

# Specify a percentage of the chance that you will lose many levels.
# Default = 0.60
Chance: 0.60

# Set the default game mode.
# Default = 2
gamemode: 2

# Activate or deactivate if you want to get your old level kit back after a rejoin
save-level: false

# Set the PvP radius in which PvP should be allowed.
pvp-radius: 10

# Allow other items to be added. You can manage them in "level.yml" under "other-items".
use-other-items: false

# Here you can change the chat and nametag format.
format:
  chat: "§8[§c{level}§8] §7{player} §8> §f{msg}"
  nametag: "§8[§c{level}§8] §7{player}"

events:
  load:
    login: true
    join: true
    quit: true
    break: true
    place: true
    damage: true
    death: true
    drop: true
    hunger: true
    inv-move: true
    move: true
    respawn: true
    chat: true
    fight: true

# Here you can change the messages.
# {player} = The player is you
# {level} = Your current level
# {msg} = Your message
# {killer} = Who killed you
messages:
  join: "{player} has entered the GunGame server."
  quit: "{player} has left the GunGame server."
  death: "{player} died!"
  kill: "{player} was killed by {killer}."
  reload: "Your levels were reset by a server reload."
```
----------------
# Discord

If problems arise, create an issue or write us on Discord.

| The Programmers |
| :---: |
[![Discord](https://img.shields.io/discord/427472879072968714.svg?style=flat-square&label=discord&colorB=7289da)](https://discord.gg/Ce2aY25) |