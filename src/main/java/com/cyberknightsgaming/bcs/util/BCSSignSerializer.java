package com.cyberknightsgaming.bcs.util;

import java.util.ArrayList;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import com.cyberknightsgaming.bcs.BCSSign;
import com.google.common.reflect.TypeToken;

import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializer;

public class BCSSignSerializer implements TypeSerializer<BCSSign> {

	@Override
	public BCSSign deserialize(TypeToken<?> type, ConfigurationNode value) throws ObjectMappingException {
        
		ArrayList<String> commands = new ArrayList<String>();
		String[] cmdsString = value.getNode("commands").getString().split("%");
		for (String cmd : cmdsString) {
			commands.add(cmd);
		}
		int cost = value.getNode("cost").getInt();
		String permission = value.getNode("permission").getString();
		
		World world = Sponge.getServer().getWorld("world").get();
		
		Location<World> location = new Location<World>(world, value.getNode("x").getInt(),
				value.getNode("y").getInt(),
				value.getNode("z").getInt());
		
		return new BCSSign(commands, cost, permission, location);
	}

	@Override
	public void serialize(TypeToken<?> type, BCSSign bcsSign, ConfigurationNode value) throws ObjectMappingException {
		String cmdsString = "";
		for (String str : bcsSign.commands) {
			cmdsString = cmdsString + "%" + str;
		}
		value.getNode("commands").setValue(cmdsString.substring(1));
		value.getNode("cost").setValue(bcsSign.cost);
		value.getNode("permission").setValue(bcsSign.permission);
		value.getNode("x").setValue(bcsSign.location.getBlockX());
		value.getNode("y").setValue(bcsSign.location.getBlockY());
		value.getNode("z").setValue(bcsSign.location.getBlockZ());
	}


}
