package tv.tirco.bungeejoin.util;

import java.util.HashMap;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
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
	public String getServerPlayerCount(ProxiedPlayer player, boolean leaving) {
		return getServerPlayerCount(player.getServer().getInfo(), leaving);
	}
	
	public String getServerPlayerCount(String serverName, boolean leaving) {
		return getServerPlayerCount(Main.getInstance().getProxy().getServers().get(serverName), leaving);
	}
	
	public String getServerPlayerCount(ServerInfo serverInfo, boolean leaving) {
		String serverPlayerCount = "?";
		if(serverInfo != null) {
			int count = serverInfo.getPlayers().size();
			if(leaving && count > 0) {
				count += -1;
			}
			serverPlayerCount = String.valueOf(count);
		}
		return serverPlayerCount;
	}
	
	public String getNetworkPlayerCount() {
		return String.valueOf(Main.getInstance().getProxy().getPlayers().size());
	}

	public String formatSwitchMessage(ProxiedPlayer player, String fromName, String toName) {
		String from = getServerName(fromName);
		String to = getServerName(toName);
		
		String messageFormat = getSwapServerMessage();
		messageFormat = messageFormat.replace("%player%", player.getName());
		messageFormat = messageFormat.replace("%to%", to);
		messageFormat = messageFormat.replace("%from%", from);
		if(messageFormat.contains("%playercount_from%")) {
			messageFormat = messageFormat.replace("%playercount_from%", getServerPlayerCount(fromName, true));
		}
		if(messageFormat.contains("%playercount_to%")) {
			messageFormat = messageFormat.replace("%playercount_to%", getServerPlayerCount(toName, false));
		}
		if(messageFormat.contains("%playercount_network%")) {
			messageFormat = messageFormat.replace("%playercount_network%", getNetworkPlayerCount());
		}
		
		return messageFormat;
	}
	
	public String formatJoinMessage(ProxiedPlayer player) {
		String messageFormat = getJoinNetworkMessage();
		messageFormat = messageFormat.replace("%player%", player.getName());
		if(messageFormat.contains("%playercount_server%")) {
			messageFormat = messageFormat.replace("%playercount_server%", getServerPlayerCount(player.getServer().getInfo(), false));
		}
		if(messageFormat.contains("%playercount_network%")) {
			messageFormat = messageFormat.replace("%playercount_network%", getNetworkPlayerCount());
		}
		
		return messageFormat;
	}
	
	public String formatQuitMessage(ProxiedPlayer player) {
		String messageFormat = getLeaveNetworkMessage();
		messageFormat = messageFormat.replace("%player%", player.getName());
		if(messageFormat.contains("%playercount_server%")) {
			messageFormat = messageFormat.replace("%playercount_server%", getServerPlayerCount(player.getServer().getInfo(), true));
		}
		if(messageFormat.contains("%playercount_network%")) {
			messageFormat = messageFormat.replace("%playercount_network%", getNetworkPlayerCount());
		}
		
		return messageFormat;
	}


}
