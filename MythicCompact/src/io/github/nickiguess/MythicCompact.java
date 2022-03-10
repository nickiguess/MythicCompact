package io.github.nickiguess;

import org.bukkit.plugin.java.JavaPlugin;

public class MythicCompact extends JavaPlugin {
	// Fired when plugin is enabled 
	@Override
	public void onEnable() {
	    getServer().getPluginManager().registerEvents(new EventListener(), this);
	}
	
	//Fired when plugin is disabled
	@Override
	public void onDisable() {
		
	}
}
