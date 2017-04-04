package com.cyberknightsgaming.bcs.commands;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;

import com.cyberknightsgaming.bcs.BCS;
import com.cyberknightsgaming.bcs.BCSConfig;

public class BCSCommand implements CommandExecutor {

	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
		if (!(src instanceof Player)) {
			src.sendMessage(BCSConfig.getMessageTemplate("CommandMustBePlayer"));
			return CommandResult.success();
		}
		Player player = (Player) src;
		if (!player.hasPermission("bcs.admin")) {
			player.sendMessage(BCSConfig.getMessageTemplate("CommandsCannotUse"));
		}
		String cmdArgsTmp = Text.of(args.getAll("cmdArgs")).toPlain().trim();
		cmdArgsTmp = cmdArgsTmp.substring(1, cmdArgsTmp.length()-1);
		String[] cmdArgs = cmdArgsTmp.split(" ");
		
		if (cmdArgs.length > 1) {
			if (cmdArgs[0].equalsIgnoreCase("add")) {
				player.sendMessage(BCSConfig.getMessageTemplate("CommandsAdd"));
				BCS.getInstance().getBCSData().addOrUpdateEditingPlayer(player.getName(), "add~"+cmdArgsTmp.substring(4));
			} else if (cmdArgs[0].equalsIgnoreCase("permission")) {
				player.sendMessage(BCSConfig.getMessageTemplate("CommandsPermission"));
				BCS.getInstance().getBCSData().addOrUpdateEditingPlayer(player.getName(), "permission~"+cmdArgs[1]);
			} else if (cmdArgs[0].equalsIgnoreCase("cost")) {
				player.sendMessage(BCSConfig.getMessageTemplate("CommandsCost"));
				BCS.getInstance().getBCSData().addOrUpdateEditingPlayer(player.getName(), "cost~"+cmdArgs[1]);
			}
			
			if (cmdArgs[0].equalsIgnoreCase("debug")) {
				if (cmdArgs[1].equalsIgnoreCase("signs")) {
					BCS.getInstance().getLogger().info(BCS.getInstance().getBCSData().commandSigns.toString());
				}
				if (cmdArgs[1].equalsIgnoreCase("reload")) {
					player.sendMessage(BCSConfig.getMessageTemplate("CommandReload"));
				}
				if (cmdArgs[1].equalsIgnoreCase("sm")) {
					BCS.getInstance().getLogger().info(BCSConfig.serverMessages.toString());
				}
			}
		} else if (cmdArgs.length == 1) {
			if (cmdArgs[0].equalsIgnoreCase("remove")) {
				player.sendMessage(BCSConfig.getMessageTemplate("CommandsRemove"));
				BCS.getInstance().getBCSData().addOrUpdateEditingPlayer(player.getName(), "remove~none");
			}
			
			//TODO: add usage command
		}
		
		return CommandResult.success();
	}

}
