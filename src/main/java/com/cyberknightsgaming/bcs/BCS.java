package com.cyberknightsgaming.bcs;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;

import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandManager;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.config.DefaultConfig;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.GameReloadEvent;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.event.service.ChangeServiceProviderEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.service.economy.EconomyService;
import org.spongepowered.api.text.Text;

import com.cyberknightsgaming.bcs.commands.BCSCommandManager;
import com.cyberknightsgaming.bcs.events.SignInteractListener;
import com.cyberknightsgaming.bcs.util.BCSSignSerializer;
import com.google.common.reflect.TypeToken;
import com.google.inject.Inject;

import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializers;

@Plugin(id = "better-command-signs", name = "Better Command Signs", version = "0.0.1")
public class BCS {

	//@Inject
	//private Game game;
	
	@Inject
	private PluginContainer myPlugin;
	
	@Inject
	@ConfigDir(sharedRoot=true)
	private Path configDir;

	@Inject
	@DefaultConfig(sharedRoot = true)
	private ConfigurationLoader<CommentedConfigurationNode> loader;
	
	@Inject
	private Logger logger;
	
	private BCSCommandManager bcsCmdManager;
	private BCSData bcsData;
	private BCSConfig bcsConfig;
	
	private static BCS instance;
	
	private static EconomyService economyService;
	
	private CommandManager cmdManager;
	
	@Listener
	public void reload(GameReloadEvent event) {
		// Do reload stuff
		BCSConfig.serverMessages = new HashMap<String, Text>();
		instance.getBCSData().commandSigns = new ArrayList<BCSSign>();
		
		instance.bcsConfig.load();
	}
	
	@Listener
	public void onChangeServiceProvider(ChangeServiceProviderEvent event) {
	        if (event.getService().equals(EconomyService.class)) {
	                economyService = (EconomyService) event.getNewProviderRegistration().getProvider();
	        }
	}
	
	@Listener
	public void onServerStart(GameStartedServerEvent event) {
		TypeSerializers.getDefaultSerializers().registerType(TypeToken.of(BCSSign.class), new BCSSignSerializer());
		
		instance = this;
		
		bcsCmdManager = new BCSCommandManager();
		bcsData = new BCSData();
		bcsConfig = new BCSConfig();
		
		try {
			bcsConfig.load();
			bcsConfig.save();
		} catch (Exception e) {
			e.printStackTrace();
		}
		bcsCmdManager.registerCmds();
		
		Sponge.getEventManager().registerListeners(this, new SignInteractListener());
		cmdManager = Sponge.getCommandManager();

		logger.info("Loaded BetterCommandSigns");
		logger.info("by Tantrex | github.com/AlexMason");
	}
	
	public PluginContainer getMyPlugin() {
		return myPlugin;
	}
	
	public Path getConfigDir() {
		return configDir;
	}

	public Logger getLogger() {
		return logger;
	}
	
	public ConfigurationLoader<CommentedConfigurationNode> getLoader() {
		return loader;
	}

	public BCSData getBCSData() {
		return bcsData;
	}
	
	public static BCS getInstance() {
		return instance;
	}

	public BCSConfig getBCSConfig() {
		return bcsConfig;
	}
	
	public EconomyService getEconomyService() {
		return economyService;
	}
	
	public CommandManager getCommandManager() {
		return cmdManager;
	}
	
}
