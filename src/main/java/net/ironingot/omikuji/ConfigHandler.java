package net.ironingot.omikuji;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
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
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(configFile), "UTF-8"));
            this.config = YamlConfiguration.loadConfiguration(reader);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return false;
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
