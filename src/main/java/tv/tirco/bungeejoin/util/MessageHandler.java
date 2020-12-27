package tv.tirco.bungeejoin.util;

import java.util.HashMap;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.config.Configuration;
import tv.tirco.bungeejoin.BungeeJoinMessages.Main;

public class MessageHandler {

	private static MessageHandler instance;
	
	public static MessageHandler getInstance() {
		if (instance == null) {
			instance = new MessageHandler();
		}
		return instance;
	}

	String SwapServerMessage = "";
	String JoinNetworkMessage = "";
	String LeaveNetworkMessage = "";
	HashMap <String, String> serverNames;
	//String FirstTimeJoinMessage = "";

	public void setupConfigMessages() {
		Configuration config = Main.getInstance().getConfig();
		SwapServerMessage = config.getString("Messages.SwapServerMessage","&6&l%player%&r  &7[%from%&7] -> [%to%&7]");
		JoinNetworkMessage = config.getString("Messages.JoinNetworkMessage","&6%player% &6has connected to the network!");
		LeaveNetworkMessage = config.getString("Messages.LeaveNetworkMessage","&6%player% &6has disconnected from the network!");
		
		HashMap<String, String> serverNames = new HashMap<String, String>();
		
		for(String server : config.getSection("Servers").getKeys()) {
			//Main.getInstance().getLogger().info("Looping: " + server);
			serverNames.put(server.toLowerCase(), config.getString("Servers." + server, server));
			//Main.getInstance().getLogger().info("Put: " + server.toLowerCase() + " as " + config.getString("Servers." + server, server));
		}
		
		this.serverNames = serverNames;
		
		
	}
	
	public String getServerName(String key) {
		String name = key;
		if(serverNames != null) {
			//Main.getInstance().getLogger().info("ServerNames is not null");
			if(serverNames.containsKey(key.toLowerCase())) {
				//Main.getInstance().getLogger().info("serverNames contains the key");
				name = serverNames.get(key.toLowerCase());
			}
		}
		//Main.getInstance().getLogger().info("Fetched " + key + " got " + name);
		return name;
	}


	public void broadcastMessage(String text) {
		TextComponent msg = new TextComponent();
				msg.setText(text);
				//You could also use a StringBuilder here to get the arguments.
				ProxyServer.getInstance().broadcast(msg);
	}

	public String getJoinNetworkMessage() {
		return JoinNetworkMessage;
	}
	
	public String getLeaveNetworkMessage() {
		return LeaveNetworkMessage;
	}
	
	public String getSwapServerMessage() {
		return SwapServerMessage;
	}

	public Iterable<String> getServerNames() {
		if(serverNames != null) {
			return serverNames.keySet();
		}
		return null;
	}


}
