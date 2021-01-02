package tv.tirco.bungeejoin.commands;

import java.util.List;

import com.google.common.collect.ImmutableList;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;
import tv.tirco.bungeejoin.BungeeJoinMessages.Storage;
import tv.tirco.bungeejoin.util.MessageHandler;

public class FakeCommand extends Command implements TabExecutor{

    public FakeCommand() {
        super("fakemessage","bungeejoinmessages.fakemessage","fm");
   }
    
    @Override
    public void execute(CommandSender commandSender, String[] args) {
        if (commandSender instanceof ProxiedPlayer) {
            ProxiedPlayer player = (ProxiedPlayer) commandSender;
            if(!player.hasPermission("bungeejoinmessages.fakemessage")) {
            	return;
            }
            if(args.length < 1) {
            	String msg =             			
            			"&6Arguments:\n"
            			+ "- &cfakejoin\n"
            			+ "- &cfakequit\n"
            			+ "- &cfakeswitch&6 (to) (from)\n";
            	TextComponent message = new TextComponent(ChatColor.translateAlternateColorCodes('&', msg));
            	player.sendMessage(message);
            	return;
            } else {
            	if(args[0].equalsIgnoreCase("fakejoin") || args[0].equalsIgnoreCase("fj") ) {
            		String message = MessageHandler.getInstance().formatJoinMessage(player);
            		MessageHandler.getInstance().broadcastMessage(ChatColor.translateAlternateColorCodes('&', message));
            		return;
            		
            	} else if(args[0].equalsIgnoreCase("fakequit")  || args[0].equalsIgnoreCase("fq")) {
            		String message = MessageHandler.getInstance().formatQuitMessage(player);
            		MessageHandler.getInstance().broadcastMessage(ChatColor.translateAlternateColorCodes('&', message));
            		return;
            		
            	} else if(args[0].equalsIgnoreCase("fakeswitch")  || args[0].equalsIgnoreCase("fs")) {
            		if(args.length < 3) {
                    	String msg =             			
                    			"&6Arguments:\n"
                    			+ "- &cfakejoin\n"
                    			+ "- &cfakequit\n"
                    			+ "- &cfakeswitch&6 (to) (from)\n"
                    			+ "&4Error: Please specify &cTO&4 and &cFROM\n";
                    	TextComponent message = new TextComponent(ChatColor.translateAlternateColorCodes('&', msg));
                    	player.sendMessage(message);
                    	return;
            		} else {
            			String fromName = args[1];
            			String toName = args[2];
            			
            			String message = MessageHandler.getInstance().formatSwitchMessage(player, fromName, toName);

                		MessageHandler.getInstance().broadcastMessage(ChatColor.translateAlternateColorCodes('&', message));
                		return;
            		}
            	} else if(args[0].equalsIgnoreCase("toggle")) {
            		String msg = "";
            		if(!player.hasPermission("bungeejoinmessages.silent")){
            			msg = "&cYou do not have the permission to join the server silently.";
            			TextComponent message = new TextComponent(ChatColor.translateAlternateColorCodes('&', msg));
                    	player.sendMessage(message);
                    	return;
            		} else {
            			Boolean state = !Storage.getInstance().getAdminMessageState(player);
            			msg = "&eYour SilentMode has now been set to &6" + state;
            			TextComponent message = new TextComponent(ChatColor.translateAlternateColorCodes('&', msg));
                    	player.sendMessage(message);
            			Storage.getInstance().setAdminMessageState(player,state);
            			return;
            		}
            	}
            }
        }
    }

	@Override
	public Iterable<String> onTabComplete(CommandSender sender, String[] args) {
		List<String> commandArguments = ImmutableList.of("fakejoin","fakequit","fakeswitch","fj","fq","fs","toggle");
		switch (args.length) {
		case 1:
			return commandArguments;
		case 2: 
			if(args[0].equalsIgnoreCase("fs") || args[0].equalsIgnoreCase("fakeswitch")) {
				return MessageHandler.getInstance().getServerNames();
			}
		case 3:
			if(args[0].equalsIgnoreCase("fs") || args[0].equalsIgnoreCase("fakeswitch")) {
				return MessageHandler.getInstance().getServerNames();
			}
		default:
			return ImmutableList.of("No more arguments needed.");
    	}
	}

}
