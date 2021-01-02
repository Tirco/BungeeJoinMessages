package tv.tirco.bungeejoin.BungeeJoinMessages;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import net.md_5.bungee.api.connection.ProxiedPlayer;

public class Storage {

	private static Storage instance;
	
	HashMap<ProxiedPlayer,String> previousServer;
	HashMap<UUID,Boolean> messageState;
	List<UUID> onlinePlayers;
	
	boolean SwapServerMessageEnabled = true;
	boolean JoinNetworkMessageEnabled = true;
	boolean LeaveNetworkMessageEnabled = true;
	boolean NotifyAdminsOnSilentMove = true;
	
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
	}
	
	public void setUpDefaultValuesFromConfig() {
		this.SwapServerMessageEnabled = Main.getInstance().getConfig().getBoolean("Settings.SwapServerMessageEnabled", true);
		this.JoinNetworkMessageEnabled = Main.getInstance().getConfig().getBoolean("Settings.JoinNetworkMessageEnabled", true);
		this.LeaveNetworkMessageEnabled = Main.getInstance().getConfig().getBoolean("Settings.LeaveNetworkMessageEnabled", true);
		this.NotifyAdminsOnSilentMove = Main.getInstance().getConfig().getBoolean("Settings.NotifyAdminsOnSilentMove", true);
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




	
}
