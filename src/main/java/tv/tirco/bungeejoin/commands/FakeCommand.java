package tv.tirco.bungeejoin.commands;

import java.util.List;

import com.google.common.collect.ImmutableList;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;
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
            		String message = MessageHandler.getInstance().getJoinNetworkMessage();
            		message = message.replace("%player%", player.getName());
            		MessageHandler.getInstance().broadcastMessage(ChatColor.translateAlternateColorCodes('&', message));
            		return;
            		
            	} else if(args[0].equalsIgnoreCase("fakequit")  || args[0].equalsIgnoreCase("fq")) {
            		String message = MessageHandler.getInstance().getLeaveNetworkMessage();
            		message = message.replace("%player%", player.getName());
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
            			
            			String from = MessageHandler.getInstance().getServerName(fromName);
            			String to = MessageHandler.getInstance().getServerName(toName);
            			
                		String message = MessageHandler.getInstance().getSwapServerMessage();
                		message = message.replace("%player%", player.getName());
                		message = message.replace("%to%", to);
                		message = message.replace("%from%", from);
                		MessageHandler.getInstance().broadcastMessage(ChatColor.translateAlternateColorCodes('&', message));
                		return;
            		}
            	} else if(args[0].equalsIgnoreCase("toggle")) {
            		
            	}
            }
        }
    }

	@Override
	public Iterable<String> onTabComplete(CommandSender sender, String[] args) {
		List<String> commandArguments = ImmutableList.of("fakejoin","fakequit","fakeswitch","fj","fq","fs");
		switch (args.length) {
		case 1:
			return commandArguments;
		case 2: return MessageHandler.getInstance().getServerNames();
		case 3: return MessageHandler.getInstance().getServerNames();
		default:
			return null;
    	}
	}

}
