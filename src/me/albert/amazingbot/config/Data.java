package me.albert.amazingbot.config;

import me.albert.amazingbot.AmazingBot;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class Data {
    private static AmazingBot instance =  AmazingBot.getInstance();
    public static FileConfiguration getData(String file){
        return create(file);
    }
    public static void set(String file,String a,Object b){
        FileConfiguration config = create(file);
        config.set(a,b);
        try {
            config.save(new File(instance.getDataFolder(), file));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private static FileConfiguration create(String file) {
        File ConfigFile = new File(instance.getDataFolder(), file);
        if (!ConfigFile.exists()) {
            ConfigFile.getParentFile().mkdirs();
            instance.saveResource(file, false);
        }
        FileConfiguration Config= new YamlConfiguration();
        try {
            Config.load(ConfigFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
        return Config;
    }

}