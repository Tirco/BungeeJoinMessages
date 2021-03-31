package tv.tirco.bungeejoin.BungeeJoinMessages;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import tv.tirco.bungeejoin.util.MessageHandler;

public class Storage {

	private static Storage instance;
	
	HashMap<ProxiedPlayer,String> previousServer;
	HashMap<UUID,Boolean> messageState;
	List<UUID> onlinePlayers;
	List<UUID> noJoinMessage;
	List<UUID> noLeaveMessage;
	List<UUID> noSwitchMessage;
	
	boolean SwapServerMessageEnabled = true;
	boolean JoinNetworkMessageEnabled = true;
	boolean LeaveNetworkMessageEnabled = true;
	boolean NotifyAdminsOnSilentMove = true;
	
	boolean SwapViewableByJoined = true;
	boolean SwapViewableByLeft = true;
	boolean SwapViewableByOther = true;
	
	//BlackList settings
	List<String> BlacklistedServers;
	boolean useBlacklistAsWhitelist;
	String SwapServerMessageRequires = "ANY";
	
	public static Storage getInstance() {
		if (instance == null) {
			instance = new Storage();
		}

		return instance;
	}
	
	public Storage() {
		this.previousServer = new HashMap<ProxiedPlayer,String>();
		this.messageState = new HashMap<UUID,Boolean>();
		this.onlinePlayers = new ArrayList<UUID>();
		this.noJoinMessage = new ArrayList<UUID>();
		this.noLeaveMessage = new ArrayList<UUID>();
		this.noSwitchMessage = new ArrayList<UUID>();
	}
	
	public void setUpDefaultValuesFromConfig() {
		this.SwapServerMessageEnabled = Main.getInstance().getConfig().getBoolean("Settings.SwapServerMessageEnabled", true);
		this.JoinNetworkMessageEnabled = Main.getInstance().getConfig().getBoolean("Settings.JoinNetworkMessageEnabled", true);
		this.LeaveNetworkMessageEnabled = Main.getInstance().getConfig().getBoolean("Settings.LeaveNetworkMessageEnabled", true);
		this.NotifyAdminsOnSilentMove = Main.getInstance().getConfig().getBoolean("Settings.NotifyAdminsOnSilentMove", true);
	
		this.SwapViewableByJoined = Main.getInstance().getConfig().getBoolean("Settings.SwapServerMessageViewableBy.ServerJoined", true);
		this.SwapViewableByLeft = Main.getInstance().getConfig().getBoolean("Settings.SwapServerMessageViewableBy.ServerLeft", true);
		this.SwapViewableByOther = Main.getInstance().getConfig().getBoolean("Settings.SwapServerMessageViewableBy.OtherServer", true);
	
		//Blacklist
		this.BlacklistedServers = Main.getInstance().getConfig().getStringList("Settings.ServerBlacklist");
		if(this.BlacklistedServers == null) { //Not sure if needed. Better safe than sorry.
			this.BlacklistedServers = new ArrayList<String>();
		}
		this.useBlacklistAsWhitelist = Main.getInstance().getConfig().getBoolean("Settings.UseBlacklistAsWhitelist", false);
		this.SwapServerMessageRequires = Main.getInstance().getConfig().getString("Settings.SwapServerMessageRequires", "ANY").toUpperCase();
		
		//Verify Swap Server Message
		switch(SwapServerMessageRequires) {
		case "JOINED":
		case "LEFT":
		case "BOTH":
		case "ANY":
			break;
		default:
			MessageHandler.getInstance().log("Setting error: Settings.SwapServerMessageRequires "
					+ "only allows JOINED LEFT BOTH or ANY. Got " + SwapServerMessageRequires +
					"Defaulting to ANY.");
			this.SwapServerMessageRequires = "ANY";
		}
		
	}
	
	
	
	public boolean isSwapServerMessageEnabled() {
		return SwapServerMessageEnabled;
	}
	public boolean isJoinNetworlMessageEnabled() {
		return JoinNetworkMessageEnabled;
	}
	public boolean isLeaveNetworlMessageEnabled() {
		return LeaveNetworkMessageEnabled;
	}
	public boolean notifyAdminsOnSilentMove() {
		return NotifyAdminsOnSilentMove;
	}
	
	public boolean getAdminMessageState(ProxiedPlayer p) {
		if(p.hasPermission("bungeejoinmessages.silent")) {
			if(messageState.containsKey(p.getUniqueId())) {
				return messageState.get(p.getUniqueId());
			} else {
				boolean state = Main.getInstance().getConfig().getBoolean("Settings.SilentJoinDefaultState", true);
				messageState.put(p.getUniqueId(), state);
				return state;
			}
		} else {
			return false; //Is not silent by default as they don't have silent perm..
		}
	}
	
	public void setAdminMessageState(ProxiedPlayer player, Boolean state) {
		messageState.put(player.getUniqueId(), state);
	}
	
	public Boolean isConnected(ProxiedPlayer p) {
		return onlinePlayers.contains(p.getUniqueId());
	}
	
	public void setConnected(ProxiedPlayer p, Boolean state) {
		if(state) {
			if(!isConnected(p)) {
				onlinePlayers.add(p.getUniqueId());
			}
		} else {
			if(isConnected(p)) {
				onlinePlayers.remove(p.getUniqueId());
			}
		}
	}

	public String getFrom(ProxiedPlayer p) {
		if(previousServer.containsKey(p)) {
			return previousServer.get(p);
		} else {
			return p.getServer().getInfo().getName();
		}
	}
	
	public void setFrom(ProxiedPlayer p, String name) {
		previousServer.put(p, name);
	}

	
	public boolean isElsewhere(ProxiedPlayer player) {
		return previousServer.containsKey(player);
	}

	public void clearPlayer(ProxiedPlayer player) {
		previousServer.remove(player);
		
	}
	
	private void setJoinState(UUID id, boolean state) {
		if(state) {
			noJoinMessage.remove(id);
		} else {
			if(!noJoinMessage.contains(id)) { //Prevent duplicates.
				noJoinMessage.add(id);
			}
		}
	}
	private void setLeaveState(UUID id, boolean state) {
		if(state) {
			noLeaveMessage.remove(id);
		} else {
			if(!noLeaveMessage.contains(id)) { //Prevent duplicates.
				noLeaveMessage.add(id);
			}
		}
	}
	private void setSwitchState(UUID id, boolean state) {
		if(state) {
			noSwitchMessage.remove(id);
		} else {
			if(!noSwitchMessage.contains(id)) { //Prevent duplicates.
				noSwitchMessage.add(id);
			}
		}
	}
	
	public void setSendMessageState(String list, UUID id, boolean state) {
		switch(list) {
			case "all":
				setSwitchState(id, state);
				setJoinState(id, state);
				setLeaveState(id, state);
				return;
			case "join":
				setJoinState(id, state);
				return;
			case "leave":
			case "quit":
				setLeaveState(id, state);
				return;
			case "switch":
				setSwitchState(id, state);
				return;
			default:
				return;
		}
	}

	public List<UUID> getIgnorePlayers(String type) {
		switch(type) {
		case "join":
			return noJoinMessage;
		case "leave":
			return noLeaveMessage;
		case "switch":
			return noSwitchMessage;
		default:
			return new ArrayList<UUID>();
		}
	}

	public List<ProxiedPlayer> getSwitchMessageReceivers(String to, String from) {
		List<ProxiedPlayer> receivers = new ArrayList<ProxiedPlayer>();
		//If all are true, add all players:
		if(SwapViewableByJoined && SwapViewableByLeft && SwapViewableByOther) {
			receivers.addAll(ProxyServer.getInstance().getPlayers());
			return receivers;
		} 
		
		//Other server is true, but atleast one of the to or from are set to false:
		else if(SwapViewableByOther){
			receivers.addAll(ProxyServer.getInstance().getPlayers());
			//Players on the connected server is not allowed to see. Remove them all.
			if(!SwapViewableByJoined) {
				receivers.removeAll(getServerPlayers(to));
			}
			
			if(!SwapViewableByLeft) {
				receivers.removeAll(getServerPlayers(from));
			}
			return receivers;
		} 
		
		//OtherServer is false.
		else {
			if(SwapViewableByJoined) {
				receivers.addAll(getServerPlayers(to));
			}
			
			if(SwapViewableByLeft) {
				receivers.addAll(getServerPlayers(from));
			}
			return receivers;
		}
	}
	
	public List<ProxiedPlayer> getServerPlayers(String serverName){
		ServerInfo info = Main.getInstance().getProxy().getServers().get(serverName);
		if(info != null) {
			return new ArrayList<ProxiedPlayer>(info.getPlayers());
		} else {
			return new ArrayList<ProxiedPlayer>();
		}
	}

	
	public boolean blacklistCheck(ProxiedPlayer player) {
		String server = player.getServer().getInfo().getName();
		boolean listed = BlacklistedServers.contains(server);
		if(useBlacklistAsWhitelist) {
		//WHITELIST
			return !listed;
			//Returns TRUE if it's NOT in the list (Deny MessagE)
			//FALSE if it is in the list. (Allow Message)

		} else {
		//BLACKLIST
			return listed;
			//Returns TRUE if it is listed. (Allow Message)
			//FALSE if it's not listed. (Deny Message)

		}
		//Returning FALSE allows the message to go further, TRUE will stop it.
	}

	public boolean blacklistCheck(String from, String to) {
		Boolean fromListed = BlacklistedServers.contains(from);
		Boolean toListed = BlacklistedServers.contains(to);
		Boolean result = true;
		switch(SwapServerMessageRequires.toUpperCase()) {
		case "JOINED":
			result = toListed;
			//MessageHandler.getInstance().log("Joined - toListed = " + toListed);
			break;
		case "LEFT":
			result = fromListed;
			//MessageHandler.getInstance().log("Left - fromListed = " + fromListed);
			break;
		case "ANY":
			result = fromListed || toListed;
			//MessageHandler.getInstance().log("ANY - fromListed = " + fromListed + "toListed = " + toListed + " result = " + result);
			break;
		case "BOTH":
			result = fromListed && toListed;
			//MessageHandler.getInstance().log("BOTH - fromListed = " + fromListed + "toListed = " + toListed + " result = " + result);
			break;
		default:
			//MessageHandler.getInstance().log("Warning: Default action triggered under blacklist check. Is it correctly configured?");
			break;
		}
		//Returning FALSE allows the message to go further, TRUE will stop it.
		if(useBlacklistAsWhitelist) {
			//WHITELIST
				return !result;
			} else {
			//BLACKLIST
				return result;
			}
	}
}
