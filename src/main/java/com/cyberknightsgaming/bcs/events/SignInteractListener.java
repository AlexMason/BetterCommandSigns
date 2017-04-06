package com.cyberknightsgaming.bcs.events;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Optional;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.Order;
import org.spongepowered.api.event.block.InteractBlockEvent;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.service.economy.account.UniqueAccount;
import org.spongepowered.api.service.pagination.PaginationList;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import com.cyberknightsgaming.bcs.BCS;
import com.cyberknightsgaming.bcs.BCSConfig;
import com.cyberknightsgaming.bcs.BCSSign;

public class SignInteractListener {

	@Listener(order=Order.EARLY)
	public void leftClickBlockEvent(InteractBlockEvent.Primary event, @First Player player) {
		Optional<Location<World>> optLoc = event.getTargetBlock().getLocation();
		if (!optLoc.isPresent()) {
			return;
		}
		Location<World> location = (Location<World>) optLoc.get();
		BlockType blockType = location.getBlockType();
		
		if (!blockType.equals(BlockTypes.WALL_SIGN)) {
			if (BCS.getInstance().getBCSData().isEditing(player.getName())) {
				player.sendMessage(BCSConfig.getMessageTemplate("CommandSignEditingCancelled"));
				BCS.getInstance().getBCSData().removeEditingPlayer(player.getName());
			}
			return;
		}
		
		if (BCS.getInstance().getBCSData().isCommandSign(location)) {
			BCSSign sign = BCS.getInstance().getBCSData().getCommandSign(location);
			if (!player.hasPermission("bcs.destroy")) {
				event.setCancelled(true);
				player.sendMessage(BCSConfig.getMessageTemplate("CannotDestroySigns"));
			} else {
				player.sendMessage(BCSConfig.getMessageTemplate("CommandSignDestroyed"));
				BCS.getInstance().getBCSData().destroySign(sign);
			}
		}
	}

	@Listener(order=Order.EARLY)
	public void rightClickBlockEvent(InteractBlockEvent.Secondary event, @First Player player) {
		Optional<Location<World>> optLoc = event.getTargetBlock().getLocation();
		if (!optLoc.isPresent()) {
			return;
		}
		Location<World> location = (Location<World>) optLoc.get();
		BlockType blockType = location.getBlockType();
		
		if (!blockType.equals(BlockTypes.WALL_SIGN)) {
			if (BCS.getInstance().getBCSData().isEditing(player.getName())) {
				player.sendMessage(BCSConfig.getMessageTemplate("CommandSignEditingCancelled"));
				BCS.getInstance().getBCSData().removeEditingPlayer(player.getName());
			}
			return;
		}
		
		if (BCS.getInstance().getBCSData().isEditing(player.getName())) {
			String editingInfo = BCS.getInstance().getBCSData().editingPlayers.get(player.getName());

			String editingType = editingInfo.split("~")[0];
			String editingData = editingInfo.split("~")[1];
			
			BCSSign sign;
			
			switch (editingType) {
				case "add":
					sign = BCS.getInstance().getBCSData().getCommandSign(location);
					if (sign.commands.isEmpty()) {
						player.sendMessage(BCSConfig.getMessageTemplate("CommandSignCreated"));

					}
					sign.commands.add(editingData);
					player.sendMessage(BCSConfig.getMessageTemplate("SignAdminCommandAdded"));
					break;
				case "remove":
					if (BCS.getInstance().getBCSData().isCommandSign(location)) {
						sign = BCS.getInstance().getBCSData().getCommandSign(location);
						BCS.getInstance().getBCSData().destroySign(sign);
						player.sendMessage(BCSConfig.getMessageTemplate("SignAdminRemove"));
					} else {
						player.sendMessage(BCSConfig.getMessageTemplate("CommandSignDoesNotExist"));
					}
					break;
				case "permission":
					if (BCS.getInstance().getBCSData().isCommandSign(location)) {
						sign = BCS.getInstance().getBCSData().getCommandSign(location);
						sign.permission = editingData;
						player.sendMessage(BCSConfig.getMessageTemplate("SignAdminPermissionAdded"));
					} else {
						player.sendMessage(BCSConfig.getMessageTemplate("CommandSignDoesNotExist"));
					}
					break;
				case "cost":
					if (BCS.getInstance().getBCSData().isCommandSign(location)) {
						sign = BCS.getInstance().getBCSData().getCommandSign(location);
						sign.cost = Integer.parseInt(editingData);
						player.sendMessage(BCSConfig.getMessageTemplate("SignAdminCostAdded"));
					} else {
						player.sendMessage(BCSConfig.getMessageTemplate("CommandSignDoesNotExist"));
					}
					break;
				case "info":
					if (BCS.getInstance().getBCSData().isCommandSign(location)) {
						sign = BCS.getInstance().getBCSData().getCommandSign(location);
						ArrayList<Text> cmds = new ArrayList<Text>();
						sign.commands.forEach((v) -> 
							cmds.add(Text.of(v))
						);
						
						PaginationList.Builder builder = PaginationList.builder();
						builder.title(Text.of("Sign Information"))
						.contents(cmds)
						.header(Text.of("Cost: "+ sign.cost + " | Permission: "+ sign.permission))
					    .padding(Text.of("#"))
						.build().sendTo(player);
						
					} else {
						player.sendMessage(BCSConfig.getMessageTemplate("CommandSignDoesNotExist"));
					}
					break;
				default:
					player.sendMessage(Text.of(editingType +" : "+ editingData));
					break;
			}
			
			BCS.getInstance().getBCSData().removeEditingPlayer(player.getName());
			
			try {
				BCS.getInstance().getBCSConfig().save();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else { //USER LOGIC HERE
			if (BCS.getInstance().getBCSData().isCommandSign(location)) {
				BCSSign sign = BCS.getInstance().getBCSData().getCommandSign(location);
				
				if (player.hasPermission(sign.getPermission()) || sign.getPermission().equals("")) {
					Optional<UniqueAccount> uOpt = BCS.getInstance().getEconomyService().getOrCreateAccount(player.getUniqueId());
					
					if (uOpt.isPresent()) {
					    UniqueAccount acc = uOpt.get();
					    BigDecimal balance = acc.getBalance(BCS.getInstance().getEconomyService().getDefaultCurrency());
					    
					    if (balance.intValue() >= sign.cost || sign.getCost() == 0) {
					    	acc.withdraw(BCS.getInstance().getEconomyService().getDefaultCurrency(), BigDecimal.valueOf(sign.cost), Cause.source(this).build());
					    	if (BCSConfig.showMsgOnSignUse) {
					    		player.sendMessage(BCSConfig.getMessageTemplate("SignUseTransactionComplete"));
					    	}
					    	processCommands(player.getName(), sign.commands);
					    } else {
					    	player.sendMessage(BCSConfig.getMessageTemplate("SignUseNotEnoughMoney"));
					    }
					} else if (sign.getCost() == 0) {
				    	if (BCSConfig.showMsgOnSignUse) {
				    		player.sendMessage(BCSConfig.getMessageTemplate("SignUseTransactionComplete"));
				    	}
				    	processCommands(player.getName(), sign.commands);
					}
				} else {
					player.sendMessage(BCSConfig.getMessageTemplate("SignUseNoPermissions"));
				}
			}
		}
	}
	
	private void processCommands(String player, ArrayList<String> commands) {
		for (String str : commands) {
				BCS.getInstance().getCommandManager().process(Sponge.getServer().getConsole(), str.replace("{player}", player));
		}
	}
	
}
