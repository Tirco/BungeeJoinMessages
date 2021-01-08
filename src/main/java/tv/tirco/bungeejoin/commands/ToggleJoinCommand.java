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

public class ToggleJoinCommand extends Command implements TabExecutor{

    public ToggleJoinCommand() {
        super("togglejoinmessage","bungeejoinmessages.togglemessage","bjointoggle");
   }
    
    List<String> commandArguments = ImmutableList.of("join","leave","quit","switch","all");
    
    @Override
    public void execute(CommandSender commandSender, String[] args) {
        if (commandSender instanceof ProxiedPlayer) {
            ProxiedPlayer player = (ProxiedPlayer) commandSender;
            if(!player.hasPermission("bungeejoinmessages.fakemessage")) {
            	return;
            }
            if(args.length < 1) {
            	String msg =             			
            			"&6Please specify which messages you would like to disable/enable.\n"
            			+ "&6Valid arguments are:&f join, leave, switch, all";
            	TextComponent message = new TextComponent(ChatColor.translateAlternateColorCodes('&', msg));
            	player.sendMessage(message);
            	return;
            }
            
            if(args.length < 3) {
            	String msg =             			
            			"&6Please specify which state you would like to set the message to.\n"
            			+ "&6Valid arguments are: &aon &7/ &coff &for &atrue &7/ &cfalse&f.";
            	TextComponent message = new TextComponent(ChatColor.translateAlternateColorCodes('&', msg));
            	player.sendMessage(message);
            } else {
            	Boolean state = false;
            	if(args[1].equalsIgnoreCase("on") || args[1].equalsIgnoreCase("true")) {
            		state = true;
            	}
            	if(commandArguments.contains(args[0].toLowerCase())) {
            		Storage.getInstance().setSendMessageState(args[0], player.getUniqueId(), state);
                	String msg =             			
                			"&6Receive messages for &f" + args[0] + " &6 has been set to &f" + state
                			+ "\n&6This will last until the network reboots.";
                	TextComponent message = new TextComponent(ChatColor.translateAlternateColorCodes('&', msg));
                	player.sendMessage(message);
                	return;
            	} else {
                	String msg =             			
                			"&6Please specify which state you would like to set the message to.\n"
                			+ "&6Valid arguments are:&f join, leave, switch, all";
                	TextComponent message = new TextComponent(ChatColor.translateAlternateColorCodes('&', msg));
                	player.sendMessage(message);
                	return;
            	}
            }
        }
    }

	@Override
	public Iterable<String> onTabComplete(CommandSender sender, String[] args) {
		switch (args.length) {
		case 1:
			return commandArguments;
		case 2:
			return ImmutableList.of("on","off");
		default:
			return ImmutableList.of("No more arguments needed.");
    	}
	}
}
