package tv.tirco.bungeejoin.BungeeJoinMessages;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;
import tv.tirco.bungeejoin.Listener.PlayerListener;
import tv.tirco.bungeejoin.commands.FakeCommand;
import tv.tirco.bungeejoin.commands.ReloadCommand;
import tv.tirco.bungeejoin.util.MessageHandler;

public class Main extends Plugin {
	
	//TODO
	/*
	 * - Config option: Always displayJoinMessage for admins
	 * - Config option: Always displayLeaveMessage for admins
	 * - Config options to enable the different messages.
	 * - Make fakeswitch command auto use current servers.
	 * - Make arguments have shorter versions: fakeswitch -> fs - Fakejoin -> fj - Fakequit - fq
	 * - Admin toggle options.
	 * - List of all registered UUIDs to see if a player is new.
	 */
	
	private static Main instance;
    //private File file;
    private Configuration configuration;
    
    private Plugin mainPlugin;

	@Override
    public void onEnable() {
		getLogger().info("is loading...");
		setInstance(this);
		this.mainPlugin = this;
        // Don't log enabling, Spigot does that for you automatically!

		loadConfig();
        // Commands enabled with following method must have entries in plugin.yml
        //getCommand("example").setExecutor(new ExampleCommand(this));
		MessageHandler.getInstance().setupConfigMessages();
		getProxy().getPluginManager().registerListener(this, new PlayerListener());
		
		ProxyServer.getInstance().getPluginManager().registerCommand(this, new FakeCommand());
		ProxyServer.getInstance().getPluginManager().registerCommand(this, new ReloadCommand());
		getLogger().info("has loaded!");
    }
    
    private void loadConfig() {
        if (!getDataFolder().exists()) getDataFolder().mkdir(); //Make folder
        		File file = new File(getDataFolder(), "config.yml");
          try {
              if (!file.exists()) {
                  try (InputStream in = getResourceAsStream("config.yml")) {
                      Files.copy(in, file.toPath());
                  } catch (IOException e) {
                      e.printStackTrace();
                  }
              }
              configuration = ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);
              ConfigurationProvider.getProvider(YamlConfiguration.class).save(configuration, file);
          } catch (IOException e) {
              e.printStackTrace();
          }
		
	}
    
    public Configuration getConfig() {
		return configuration;
    }

	@Override
    public void onDisable() {
        // Don't log disabling, Spigot does that for you automatically!
    }


	public static Main getInstance() {
		return instance;
	}

	public static void setInstance(Main instance) {
		Main.instance = instance;
	}

	public Plugin getPlugin() {
		return mainPlugin;
	}

	
	public void reloadConfig() {
		loadConfig();
		MessageHandler.getInstance().setupConfigMessages();
	}
}
