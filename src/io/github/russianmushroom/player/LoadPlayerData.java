package io.github.russianmushroom.player;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.inventory.ItemStack;

import io.github.russianmushroom.item.Stack;
import io.github.russianmushroom.yaml.BaseYAML;
/**
 * Load player data from file and apply it.
 * @author RussianMushroom
 *
 */
public class LoadPlayerData {
	
	private static Map<String, Object> playerInventory = Collections.synchronizedMap(new HashMap<>());

	public synchronized static void load(PlayerManager pManager, GameMode gMode) throws IOException {
		loadPlayerInventory(pManager, gMode);
		
	}
	
	private static void loadPlayerInventory(PlayerManager pManager, GameMode gMode) throws IOException {
		playerInventory = BaseYAML.getAllData(new File(
				BaseYAML.getPlayerFolder() 
				+ File.separator 
				+ pManager.getPlayerUUID().toString() 
				+ ".yml"))
				.get(gMode.toString());
		
		// Set data to player
		setPlayerData(pManager);
		setPlayerInventory(pManager);
	}
	
	private static void setPlayerData(PlayerManager pManager) {
		// Set player data
		pManager.getPlayer().setHealth(
				Double.parseDouble(playerInventory.get("playerHealth").toString()));
		pManager.getPlayer().setExp(
				Float.parseFloat(playerInventory.get("playerXP").toString()));
		pManager.getPlayer().setLevel(
				Integer.parseInt(playerInventory.get("playerLvl").toString()));
		
	}
	
	/**
	 * Split the saved inventory into it's components
	 * <ul>
	 * <li><code>.split("#")</code>: to get individual items</li>
	 * <li><code>.split(":")</code>: to get individual components, like Durability, MetaData etc.</li>
	 * <li>get MetaTags (Last in list) and decompress it</li>
	 * @param pManager
	 */
	private static void setPlayerInventory(PlayerManager pManager) {
		String playerInv = playerInventory.get("playerInventory").toString();
		List<String> individualItems = Arrays.asList(playerInv.split("#"));
		
		individualItems.parallelStream()
			.forEach(rawItem -> {
				Stack stack = new Stack(rawItem);
				ItemStack iStack = stack.toItemStack();
			});
		
	}
	
}
