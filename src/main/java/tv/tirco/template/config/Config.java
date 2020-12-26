package tv.tirco.template.config;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.md_5.bungee.api.ChatColor;
import tv.tirco.template.util.MessageHandler;

public class Config extends AutoUpdateConfigLoader {
	private static Config instance;

	private Config() {
		super("config.yml");
		validate();
	}

	public static Config getInstance() {
		if (instance == null) {
			instance = new Config();
		}

		return instance;
	}

	@Override
	protected void loadKeys() {

	}

	@Override
	protected boolean validateKeys() {
		// Validate all the settings!
		List<String> reason = new ArrayList<String>();

		if (getDebug()) {
			MessageHandler.getInstance().setDebugState(true);
			MessageHandler.getInstance().debug("Debugging has been enabled.");
		}
		if (getDebugToAdmins()) {
			MessageHandler.getInstance().setDebugState(true);
			MessageHandler.getInstance().debug("Debug loggint to admins has been enabled.");
		}

		// If the reason list is empty, keys are valid.
		return noErrorsInConfig(reason);
	}

	@SuppressWarnings("unused")
	private String getStringIncludingInts(String key) {
		String str = config.getString(key);

		if (str == null) {
			str = String.valueOf(config.getInt(key));
		}

		if (str.equals("0")) {
			str = "No value set for '" + key + "'";
		}
		return str;
	}

	// Config Getters
	/* General Settings */
	public String getLocale() {
		return config.getString("General.Locale", "en_us");
	}

	public boolean getDebug() {
		return config.getBoolean("setting.debug", false);
	}
	
	public boolean getDebugToAdmins() {
		return config.getBoolean("setting.debugtoadmins", false);
	}

}
