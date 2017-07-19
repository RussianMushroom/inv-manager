package io.github.russianmushroom;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

import io.github.russianmushroom.listener.PlayerListener;
import io.github.russianmushroom.player.PlayerManager;
import io.github.russianmushroom.player.SavePlayerData;

/**
 * Separates the inventories as well as data of players from one another through worlds 
 * (Survival, Creative, Adventure and Spectator)
 * @author RussianMushroom
 *
 */
public class MultiInventoryManager extends JavaPlugin {
	
	private final Logger log = getServer().getLogger();
	private final PluginDescriptionFile pdfFile = this.getDescription();
	
	@Override
	public void onEnable() {
		this.log.info(pdfFile.getName() +" v" + pdfFile.getVersion() + " has been enabled!");
		getServer().getPluginManager().registerEvents(new PlayerListener(), this);
		
		super.onEnable();
	}
	
	@Override
	public void onDisable() {
		log.log(Level.INFO, String.format("%s: Saving all player's data...", pdfFile.getName()));
		
		// Save everyone's data
		getServer().getOnlinePlayers()
			.parallelStream()
			.forEach(player -> {
				try {
					SavePlayerData.save(new PlayerManager(player), player.getGameMode());
				} catch (IOException e) {
					
					e.printStackTrace();
				}
			});

		this.log.info(pdfFile.getName() +" v" + pdfFile.getVersion() + " has been disabled!");
		
		super.onDisable();
	}

}
     