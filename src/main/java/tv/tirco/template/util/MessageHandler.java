package tv.tirco.template.util;

import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;

public class MessageHandler {

	private static MessageHandler instance;
	public String prefix = ChatColor.BLUE +"" +  ChatColor.BOLD + "Plugin" + ChatColor.GRAY;
	
	private boolean debug = false;
	private boolean debugToAdmins = false;
	
	public void updatePrefix(String prefix) {
		this.prefix = ChatColor.translateAlternateColorCodes('&', prefix);
	}
	
	public static MessageHandler getInstance() {
		if (instance == null) {
			instance = new MessageHandler();
		}
		return instance;
	}
	
	/*
	 * Debug messages are only sendt if enabled in config.
	 */
	public void debug(String msg) {
		if(debug) {
			Bukkit.getLogger().log(Level.INFO, prefix + msg);
		}
		if(debugToAdmins) {
			for(Player p : Bukkit.getOnlinePlayers()) {
				if(p.hasPermission("lottery.admin")) {
					p.sendMessage(prefix + msg);
				}
			}
		}
		
	}

	/*
	 * Log messages are always sendt to console.
	 */
	public void log(Level lvl, String msg) {
		Bukkit.getLogger().log(lvl, prefix + msg);
		if(debugToAdmins) {
			for(Player p : Bukkit.getOnlinePlayers()) {
				if(p.hasPermission("lottery.admin")) {
					p.sendMessage(prefix + msg);
				}
			}
		}
	}
	
	public void log(String msg) {
		log(Level.INFO, msg);
	}

	
	
	//Config states
	public void setDebugState(boolean b) {
		this.debug = b;
	}
	
	public void setDebugToAdminState(boolean b) {
		this.debugToAdmins = b;
	}
	
	public boolean getDebugState() {
		return this.debug;
	}
	
	public boolean getDebugToAdminState() {
		return this.debugToAdmins;
	}




}
