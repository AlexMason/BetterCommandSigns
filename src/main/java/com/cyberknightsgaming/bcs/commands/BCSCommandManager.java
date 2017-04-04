package com.cyberknightsgaming.bcs.commands;

import java.util.HashMap;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.text.Text;

import com.cyberknightsgaming.bcs.BCS;

public class BCSCommandManager {
	

	public BCSCommandManager() {
	}

	public void registerCmds() {
		
		
		HashMap<String, String> subCommandMap = new HashMap<String, String>();
		subCommandMap.put("add", "add");
		subCommandMap.put("remove", "remove");
		subCommandMap.put("permission", "permission");
		subCommandMap.put("cost", "cost");
		
		CommandSpec bcsCommandSpec = CommandSpec.builder()
				.description(Text.of("BetterSignCommands Base"))
				.permission("bcs.base")
				.arguments(
							GenericArguments.remainingJoinedStrings(Text.of("cmdArgs"))
						)
				.executor(new BCSCommand())
				.build();
		
		Sponge.getCommandManager().register(BCS.getInstance().getMyPlugin(), bcsCommandSpec, "bettersigncommands", "bcs");
	}
	
}
