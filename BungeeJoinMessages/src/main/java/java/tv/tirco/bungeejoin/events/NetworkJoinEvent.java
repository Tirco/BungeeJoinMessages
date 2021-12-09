package tv.tirco.bungeejoin.events;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Event;

public class NetworkJoinEvent extends Event{

	/**
	 * Ignore this, it's a test for the sake of being compatible with Discord Bots.
	 */
	
	private ProxiedPlayer player;
	private String serverJoined;
	private boolean isSilenced;
	private String message;
	
	public NetworkJoinEvent(ProxiedPlayer player, String serverJoined, boolean isSilenced, String message) {
		this.player = player;
		this.serverJoined = serverJoined;
		this.isSilenced = isSilenced;
		this.message = message;
	}
	
	public ProxiedPlayer getPlayer() {
		return player;
	}
	
	public String getServerJoined() {
		return serverJoined;
	}
	
	public boolean isSilenced() {
		return isSilenced;
	}
	
	public String getMessage() {
		return message;
	}
}
