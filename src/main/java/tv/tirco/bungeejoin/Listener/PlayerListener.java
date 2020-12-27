package tv.tirco.bungeejoin.Listener;

import java.util.concurrent.TimeUnit;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.connection.Server;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.event.ServerConnectEvent.Reason;
import net.md_5.bungee.api.event.ServerConnectedEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import tv.tirco.bungeejoin.BungeeJoinMessages.Main;
import tv.tirco.bungeejoin.BungeeJoinMessages.Storage;
import tv.tirco.bungeejoin.util.MessageHandler;

public class PlayerListener implements Listener{

	@EventHandler
	public void prePlayerSwitchServer(ServerConnectEvent e) {
		ProxiedPlayer player = e.getPlayer();
		if(player == null) {
			return;
		}

		if(e.getReason() != null) {
			if(e.getReason().equals(Reason.COMMAND)
			  || e.getReason().equals(Reason.JOIN_PROXY)
			  || e.getReason().equals(Reason.PLUGIN)
			  || e.getReason().equals(Reason.PLUGIN_MESSAGE)) {
				//Normal connection reason. All is okay,
			} else {
				//Remove player from OldServer list, so that their movement is not notified.
				Storage.getInstance().clearPlayer(player);
			}
		}
		
		Server server = player.getServer();
		if(server != null ) {
			String serverName = server.getInfo().getName();
			if(serverName != null) {
				Storage.getInstance().setFrom(player, server.getInfo().getName());
			}
		}
	}
	@EventHandler
	public void onPlayerSwitchServer(ServerConnectedEvent e) {
		ProxiedPlayer player = e.getPlayer();
		Server server = e.getServer();

		String to = MessageHandler.getInstance().getServerName(server.getInfo().getName());
		String from = "???";
		if(Storage.getInstance().isElsewhere(player)) {
			from = MessageHandler.getInstance().getServerName(Storage.getInstance().getFrom(player));
		} else {
			return; //Event was not a To-From event, so we send no message.
		}
		
    	if(!player.hasPermission("bungeejoinmessages.silent")) {
    		String message = MessageHandler.getInstance().getSwapServerMessage();
    		message = message.replace("%player%", player.getName());
    		message = message.replace("%to%", to);
    		message = message.replace("%from%", from);
    		MessageHandler.getInstance().broadcastMessage(ChatColor.translateAlternateColorCodes('&', message));
    	} else {
    		Main.getInstance().getLogger().info(ChatColor.BLUE + ChatColor.translateAlternateColorCodes('&', 
    				"Move Event was silenced. " + player.getName() + ": "+ from + " -> " + to));
    	}

	}
	
	
    @EventHandler
    public void onPostLogin(PostLoginEvent event) {
    	ProxiedPlayer player = event.getPlayer();
    	if(player == null) {
    		return;
    	}
    	
    	
   	 ProxyServer.getInstance().getScheduler().schedule(Main.getInstance().getPlugin(), new Runnable() {
		 public void run()
		 {
			 if(player.isConnected()) {
			    	if(!player.hasPermission("bungeejoinmessages.silent")) {
			    		String message = MessageHandler.getInstance().getJoinNetworkMessage();
			    		message = message.replace("%player%", player.getName());
			    		MessageHandler.getInstance().broadcastMessage(ChatColor.translateAlternateColorCodes('&', message));
			    	} else {
			    		Main.getInstance().getLogger().info(ChatColor.GOLD + "Move Event was silenced. " + player.getName() + " Joined the network.");
			    	}
			    	Storage.getInstance().setConnected(player, true);
			 }


		 }
	 }, 3, TimeUnit.SECONDS);

//        for (ProxiedPlayer player : ProxyServer.getInstance().getPlayers()) {
//        	
//        }
    }
    
    @EventHandler
    public void onPostQuit(PlayerDisconnectEvent event) {
    	ProxiedPlayer player = event.getPlayer();
    	if(player == null) {
    		return;
    	}
    	
    	if(!Storage.getInstance().isConnected(player)) {
    		return;
    	}
    	
    	if(!player.hasPermission("bungeejoinmessages.silent")) {
    		String message = MessageHandler.getInstance().getLeaveNetworkMessage();
    		message = message.replace("%player%", player.getName());
    		MessageHandler.getInstance().broadcastMessage(ChatColor.translateAlternateColorCodes('&', message));
    	} else {
    		Main.getInstance().getLogger().info(ChatColor.RED + "Move Event was silenced. " + player.getName() + " left the network.");
    	}
    	
    	//Set them as not connected, as they have left the server.
    	Storage.getInstance().setConnected(player, false);
//        for (ProxiedPlayer player : ProxyServer.getInstance().getPlayers()) {
//        	
//        }
    }
}
