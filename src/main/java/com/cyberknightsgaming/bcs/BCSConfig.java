package com.cyberknightsgaming.bcs;

import java.io.IOException;
import java.util.HashMap;

import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.serializer.TextSerializers;

import com.google.common.reflect.TypeToken;

import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.ConfigurationOptions;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;

public class BCSConfig {
	
	private ConfigurationNode rootNode;
	
	public static Text serverMsgFormat = Text.of("&7[&bBetterSignCommands&7]");
	
	public static boolean showMsgOnSignUse;
	
	public static HashMap<String, Text> serverMessages = new HashMap<String, Text>();
	
	public BCSConfig() {
		try {
			rootNode = BCS.getInstance().getLoader().load(ConfigurationOptions.defaults());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void load() {
			for(ConfigurationNode node : rootNode.getNode("signs").getChildrenList()) {
				try {
					BCS.getInstance().getBCSData().commandSigns.add(node.getValue(TypeToken.of(BCSSign.class)));
				} catch (ObjectMappingException e) {
					e.printStackTrace();
				}
			}

			serverMessages.put("CannotDestroySigns", TextSerializers.FORMATTING_CODE.deserialize(rootNode.getNode("default", "messages", "CannotDestroySigns").getString("You do not have permission to destroy command signs.")));
			serverMessages.put("CommandCannotUse", TextSerializers.FORMATTING_CODE.deserialize(rootNode.getNode("default", "messages", "CommandCannotUse").getString("You do not have permission to use this command.")));
			serverMessages.put("CommandMustBePlayer", TextSerializers.FORMATTING_CODE.deserialize(rootNode.getNode("default", "messages", "CommandMustBePlayer").getString("You can only use this command as a player.")));
			serverMessages.put("CommandSignCreated", TextSerializers.FORMATTING_CODE.deserialize(rootNode.getNode("default", "messages", "CommandSignCreated").getString("New command sign created.")));
			serverMessages.put("CommandSignDestroyed", TextSerializers.FORMATTING_CODE.deserialize(rootNode.getNode("default", "messages", "CommandSignDestroyed").getString("You have destroyed the command sign.")));
			serverMessages.put("CommandSignDoesNotExist", TextSerializers.FORMATTING_CODE.deserialize(rootNode.getNode("default", "messages", "CommandSignDoesNotExist").getString("A command sign does not exist here, use '/bcs add' to create one.")));
			serverMessages.put("CommandSignEditingCancelled", TextSerializers.FORMATTING_CODE.deserialize(rootNode.getNode("default", "messages", "CommandSignEditingCancelled").getString("Command sign editing cancelled.")));
			serverMessages.put("CommandsAdd", TextSerializers.FORMATTING_CODE.deserialize(rootNode.getNode("default", "messages", "CommandsAdd").getString("Right click a sign to add the command to it.  Right click any other block to cancel.")));
			serverMessages.put("CommandsInfo", TextSerializers.FORMATTING_CODE.deserialize(rootNode.getNode("default", "messages", "CommandsInfo").getString("Right click a sign to get information about it.  Right click any other block to cancel.")));
			serverMessages.put("CommandsCost", TextSerializers.FORMATTING_CODE.deserialize(rootNode.getNode("default", "messages", "CommandsCost").getString("Right click a sign to set the cost on it.  Right click any other block to cancel.")));
			serverMessages.put("CommandsPermission", TextSerializers.FORMATTING_CODE.deserialize(rootNode.getNode("default", "messages", "CommandsPermission").getString("Right click a sign to add the permission to it.  Right click any other block to cancel.")));
			serverMessages.put("CommandsReload", TextSerializers.FORMATTING_CODE.deserialize(rootNode.getNode("default", "messages", "CommandsReload").getString("Config Reloaded.")));
			serverMessages.put("CommandsRemove", TextSerializers.FORMATTING_CODE.deserialize(rootNode.getNode("default", "messages", "CommandsRemove").getString("Right click a sign to remove all commands.  Right click any other block to cancel.")));
			serverMessages.put("SignAdminCommandAdded", TextSerializers.FORMATTING_CODE.deserialize(rootNode.getNode("default", "messages", "SignAdminCommandAdded").getString("You have successfully added a command to the sign.")));
			serverMessages.put("SignAdminCostAdded", TextSerializers.FORMATTING_CODE.deserialize(rootNode.getNode("default", "messages", "SignAdminCostAdded").getString("You have successfully added a cost to this sign.")));
			serverMessages.put("SignAdminPermissionAdded", TextSerializers.FORMATTING_CODE.deserialize(rootNode.getNode("default", "messages", "SignAdminPermissionAdded").getString("You have successfully added a permission to this sign.")));
			serverMessages.put("SignAdminRemove", TextSerializers.FORMATTING_CODE.deserialize(rootNode.getNode("default", "messages", "SignAdminRemove").getString("You have successfully removed this sign.")));
			serverMessages.put("SignUseNoPermissions", TextSerializers.FORMATTING_CODE.deserialize(rootNode.getNode("default", "messages", "SignUseNoPermissions").getString("You do not have permissions to use this sign.")));
			serverMessages.put("SignUseNotEnoughMoney", TextSerializers.FORMATTING_CODE.deserialize(rootNode.getNode("default", "messages", "SignUseNotEnoughMoney").getString("You do not have enough money to use this sign.")));
			serverMessages.put("SignUseTransactionComplete", TextSerializers.FORMATTING_CODE.deserialize(rootNode.getNode("default", "messages", "SignUseTransactionComplete").getString("You have successfully used the sign.")));

			showMsgOnSignUse = rootNode.getNode("default", "showMessageOnSignUse").getBoolean(false);
			
			serverMsgFormat = TextSerializers.FORMATTING_CODE.deserialize(rootNode.getNode("default", "serverMsgFormat").getString("&7[&bBetterSignCommands&7]"));
	}
	
	public void save() throws Exception {
			int i = 0;
			for (BCSSign sign : BCS.getInstance().getBCSData().commandSigns) {
				rootNode.getNode("signs", i).setValue(TypeToken.of(BCSSign.class), sign);
				i++;
			}
			
			
			rootNode.getNode("default", "serverMsgFormat").setValue(TextSerializers.FORMATTING_CODE.serialize(serverMsgFormat));
			
			serverMessages.forEach((k,v) -> 
				rootNode.getNode("default", "messages", k).setValue(TextSerializers.FORMATTING_CODE.serialize(v))
			);
			
			BCS.getInstance().getLoader().save(rootNode);
	}
	
	public static Text getMessageTemplate(String template) {
		if (serverMessages.containsKey(template)) {
			Text tmp = Text.builder().append(BCSConfig.serverMsgFormat).append(Text.of(" "))
			.append(serverMessages.get(template)).build();

			return tmp;
		} else {
			return Text.of("Could not find a message template for: "+template);
		}
	}
	
}
