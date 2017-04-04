package com.cyberknightsgaming.bcs;

import java.util.ArrayList;
import java.util.HashMap;

import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

public class BCSData {
	
	public ArrayList<BCSSign> commandSigns = new ArrayList<BCSSign>();
	public HashMap<String, String> editingPlayers = new HashMap<String, String>();
	
	public BCSData() {
	}
	
	public void addOrUpdateEditingPlayer(String player, String task) {
		editingPlayers.put(player, task);
	}
	
	public void removeEditingPlayer(String player) {
		editingPlayers.remove(player);
	}
	
	public boolean isEditing(String player) {
		if (editingPlayers.containsKey(player)) {
			return true;
		}
		return false;
	}

	public boolean isCommandSign(Location<World> loc) {
		for (BCSSign sign : commandSigns) {
			if (loc.getBlockX() == sign.location.getBlockX() && 
					loc.getBlockY() == sign.location.getBlockY() && 
					loc.getBlockZ() == sign.location.getBlockZ()) {
				return true; 
			}
		}
		return false;
	}

	public BCSSign getCommandSign(Location<World> loc) {
		for (BCSSign sign : commandSigns) {
			if (loc.getBlockX() == sign.location.getBlockX() && 
					loc.getBlockY() == sign.location.getBlockY() && 
					loc.getBlockZ() == sign.location.getBlockZ()) {
				return sign;
			}
		}
		BCSSign newSign = new BCSSign(loc);
		commandSigns.add(newSign);
		return newSign;
	}
	
	public void destroySign(BCSSign sign) {
		commandSigns.remove(sign);
	}
}
