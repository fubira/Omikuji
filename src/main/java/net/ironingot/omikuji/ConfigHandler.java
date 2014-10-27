package net.ironingot.omikuji;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

public class ConfigHandler {
    private File configFile;
    private YamlConfiguration config;

    public ConfigHandler(File configFile) {
        this.configFile = configFile;
        load();
    }

    public boolean load() {
        try {
            this.config = YamlConfiguration.loadConfiguration(configFile);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean save() {
        try {
            config.save(configFile);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public String getFormat() {
        return config.getString("format", "%player%::%value%");
    }

    public Map<String, Object> getElementsMap() {
        return config.getConfigurationSection("elements").getValues(false);
    }
}
