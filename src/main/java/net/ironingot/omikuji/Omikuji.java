package net.ironingot.omikuji;

import java.io.File;
import java.util.logging.Logger;

import org.bukkit.plugin.java.JavaPlugin;

public class Omikuji extends JavaPlugin {
    public static Omikuji plugin = null;
    
    public static final Logger logger = Logger.getLogger("Minecraft");
    private ConfigHandler configHandler;

    @Override
    public void onEnable() {
        if (plugin != null) {
            return;
        }

        plugin = this;
        setupConfig();
        getCommand("omikuji").setExecutor(new OmikujiCommand(this));
    }

    @Override
    public void onDisable() {
        if (plugin == null) {
            return;
        }

        plugin = null;
    }

    public ConfigHandler getConfigHandler() {
        return configHandler;
    }

    private void setupConfig() {
        File configFile = new File(getDataFolder(), "config.yml");

        if (!configFile.exists()) {
            saveDefaultConfig();
        }

        configHandler = new ConfigHandler(configFile);
    }
}
