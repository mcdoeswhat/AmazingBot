package me.albert.amazingbot;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class AmazingBot extends JavaPlugin {
    private static AmazingBot instance;
    private static Economy econ;

    @Override
    public void onEnable(){
        if (!setupEconomy()) {
            this.getLogger().severe("Vault not found!");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }
        instance = this;
        saveDefaultConfig();
        Bot.start();
        getCommand("amazingbot").setExecutor(new ABCommands());
        Bukkit.getServer().getPluginManager().registerEvents(new Verify(),this);
        Bukkit.getLogger().info("§a[AmazingBot] Enabled..");

    }
    public static Economy getEconomy() {return econ;}



    @Override
    public void onDisable(){
        Bot.stop();
    }

    private boolean setupEconomy() {
        if (Bukkit.getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }

    public static AmazingBot getInstance(){return instance;}
}
