## BetterCommandSigns
This is a Command Signs plugin that adds support for multiple commands on one sign.  Signs support permissions, and economy.  All messages sent to players are customizable in the config file.

### Install
Download one of the releases, and put it into your plugins/mods folder.
After loading the plugin for the first time, a default config will be generated.  
Edit the configuration to your liking, and restart the server.

### How to use
Place a sign wherever you would like, and then use the commands below.  After a command, you right click the sign you want to apply it too.

In commands you can use the {player} placeholder to target the player who clicked on the sign.

### Commands
- /bcs add <command To Add> - This is how you create signs, and add multiple commands to signs.
- /bcs permission <permission Node> - This is the permission node required to use the sign.
- /bcs cost <cost> - This is the cost for economy. (Uses servers default currency)
- /bcs remove - You can use this to remove a command sign, without have to actually destroy the sign.
- /bcs info - Use this to get information about a sign, shows the cost, permissions, and any commands attached.

#### Permissions
- bcs.admin - for the bcs command.
- bcs.destroy - allows the removal of command signs