<h1 style="display: flex; align-items: center; justify-content: space-around;"><img height="100em" src="./src/main/resources/assets/cobblores_title.png" alt="icon"></h1>

![Modrinth Downloads](https://img.shields.io/modrinth/dt/ADDHERE?style=for-the-badge&logo=Modrinth&label=Modrinth%20Downloads&color=00af5c&link=https%3A%2F%2Fmodrinth.com%2Fmod%2Fcobblores) 
![CurseForge Downloads](https://img.shields.io/curseforge/dt/ADDHERE?style=for-the-badge&logo=curseforge&label=CurseForge%20Downloads)
 ![GitHub Release](https://img.shields.io/github/v/release/Adrien5902/Cobblores?style=for-the-badge&label=Latest%20Released%20Version)

A simple server-sided mod (can be configured when on client or in the server config file) to make cobblestone generators generate more blocks such as ores.

Required mods : [Fabric API](https://modrinth.com/mod/fabric-api)

| Mod Version | Supported Minecraft Version |
| ----------- | --------------------------- |
| 1.0.0       | 1.20.1                      |

## How to use
Configure the generators rates in the `config/cobblores.json` file, make a cobblestone generator and that's it.

Example config :

`config/cobblores.json`
```json
{
	"blocks": {
		"minecraft:cobblestone": 5, // The numbers represent the weight of the block
		"minecraft:diamond_ore": 1, // You can only use ints
		"create:zinc_ore": 3 // You can put modded ores to
	}
}
```
