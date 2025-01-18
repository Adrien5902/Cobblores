![Cobblores](https://cdn.modrinth.com/data/cached_images/91a9e94c0c82b5c0ddb672b3db0ea0f5bc7ae7ef.png)

![Modrinth Downloads](https://img.shields.io/modrinth/dt/stxPAJHW?style=for-the-badge&logo=Modrinth&label=Modrinth%20Downloads&color=00af5c&link=https%3A%2F%2Fmodrinth.com%2Fmod%2Fcobblores) 
 ![GitHub Release](https://img.shields.io/github/v/release/Adrien5902/Cobblores?style=for-the-badge&label=Latest%20Released%20Version)

A simple server-sided mod (can be configured when on client or in the server config file) to make cobblestone generators generate more blocks such as ores.

Required mods : [Fabric API](https://modrinth.com/mod/fabric-api)

| Mod Version | Supported Minecraft Version |
| ----------- | --------------------------- |
| 1.1.0       | 1.20.1                      |

## How to use
 - If you've installed the mod on the client side and you have [ModMenu](https://modrinth.com/mod/modmenu) installed you can edit the rates in the config menu.
![Config menu screenshot](https://cdn.modrinth.com/data/stxPAJHW/images/e64eb71b92d1a70e91e34a9c01a850317d21b629.png)

- Alternatively you can configure the generators rates in the `config/cobblores.json` file, make a cobblestone generator and that's it.

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