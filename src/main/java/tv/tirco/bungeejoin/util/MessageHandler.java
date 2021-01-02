package tv.tirco.bungeejoin.util;

import java.util.Collection;
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
		return getServerPlayerCount(player.getServer().getInfo(), leaving, player);
	}
	
	public String getServerPlayerCount(String serverName, boolean leaving, ProxiedPlayer player) {
		return getServerPlayerCount(Main.getInstance().getProxy().getServers().get(serverName), leaving, player);
	}
	
	public String getServerPlayerCount(ServerInfo serverInfo, boolean leaving, ProxiedPlayer player) {
		String serverPlayerCount = "?";
		if(serverInfo != null) {
			int count = 0;
			Collection<ProxiedPlayer> players = serverInfo.getPlayers();
			if(leaving && player != null) {
				if(players.contains(player)) {
					count = players.size() - 1;
				}
			} else if(player != null){
				if(!players.contains(player)) {
					count = players.size() + 1;
				} else {
					count = players.size();
				}
			}
			serverPlayerCount = String.valueOf(count);
		}
		return serverPlayerCount;
	}
	
	public String getNetworkPlayerCount(ProxiedPlayer player, Boolean leaving) {
		Collection<ProxiedPlayer> players = Main.getInstance().getProxy().getPlayers();
		if(leaving && player != null) {
			if(players.contains(player)) {
				return String.valueOf(players.size() - 1);
			} else {
				return String.valueOf(players.size());
			}
		} else if (player != null){
			if(players.contains(player)) {
				return String.valueOf(players.size());
			} else {
				return String.valueOf(players.size() + 1);
			}
		}
		return String.valueOf(players.size());
	}

	public String formatSwitchMessage(ProxiedPlayer player, String fromName, String toName) {
		String from = getServerName(fromName);
		String to = getServerName(toName);
		
		String messageFormat = getSwapServerMessage();
		messageFormat = messageFormat.replace("%player%", player.getName());
		messageFormat = messageFormat.replace("%to%", to);
		messageFormat = messageFormat.replace("%from%", from);
		if(messageFormat.contains("%playercount_from%")) {
			messageFormat = messageFormat.replace("%playercount_from%", getServerPlayerCount(fromName, true, player));
		}
		if(messageFormat.contains("%playercount_to%")) {
			messageFormat = messageFormat.replace("%playercount_to%", getServerPlayerCount(toName, false, player));
		}
		if(messageFormat.contains("%playercount_network%")) {
			messageFormat = messageFormat.replace("%playercount_network%", getNetworkPlayerCount(player, false));
		}
		
		return messageFormat;
	}
	
	public String formatJoinMessage(ProxiedPlayer player) {
		String messageFormat = getJoinNetworkMessage();
		messageFormat = messageFormat.replace("%player%", player.getName());
		if(messageFormat.contains("%playercount_server%")) {
			messageFormat = messageFormat.replace("%playercount_server%", getServerPlayerCount(player, false));
		}
		if(messageFormat.contains("%playercount_network%")) {
			messageFormat = messageFormat.replace("%playercount_network%", getNetworkPlayerCount(player, false));
		}
		
		return messageFormat;
	}
	
	public String formatQuitMessage(ProxiedPlayer player) {
		String messageFormat = getLeaveNetworkMessage();
		messageFormat = messageFormat.replace("%player%", player.getName());
		if(messageFormat.contains("%playercount_server%")) {
			messageFormat = messageFormat.replace("%playercount_server%", getServerPlayerCount(player, true));
		}
		if(messageFormat.contains("%playercount_network%")) {
			messageFormat = messageFormat.replace("%playercount_network%", getNetworkPlayerCount(player, true));
		}
		
		return messageFormat;
	}


}
