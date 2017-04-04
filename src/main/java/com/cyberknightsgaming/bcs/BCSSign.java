package com.cyberknightsgaming.bcs;

import java.util.ArrayList;

import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

public class BCSSign {

	public ArrayList<String> commands = new ArrayList<String>();
	
	public int cost = 0;
	
	public String permission = "";
	
	public Location<World> location;

	public BCSSign(ArrayList<String> testCmds, int cost, String permission, Location<World> location) {
		this.commands = testCmds;
		this.cost = cost;
		this.permission = permission;
		this.location = location;
	}

	public BCSSign(Location<World> location) {
		this.location = location;
	}
	
	public ArrayList<String> getCommands() {
		return commands;
	}
	
	public int getCost() {
		return cost;
	}
	
	public String getPermission() {
		return permission;
	}
	
	public Location<World> getLocation() {
		return location;
	}
	
}
