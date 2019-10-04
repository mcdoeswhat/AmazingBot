package me.albert.amazingbot;


import cc.moecraft.icq.PicqBotX;
import cc.moecraft.icq.PicqConfig;
import cc.moecraft.icq.sender.IcqHttpApi;
import me.albert.amazingbot.listener.ABListener;
import org.bukkit.Bukkit;



class Bot {

    private static AmazingBot instance = AmazingBot.getInstance();
    private static PicqBotX bot;
    public static IcqHttpApi api;

    static void start(){
        PicqConfig config = new PicqConfig(instance.getConfig().getInt("listen_port"));
        config.setDebug(false);
        config.setLogPath("");
        bot = new PicqBotX(config);
        Bukkit.getLogger().info(bot.getAccountManager().getAccounts().toString());
        bot.getAccountManager().getAccounts().clear();
        bot.addAccount("Bot01", instance.getConfig().getString("post_url"),instance.getConfig().getInt("post_port"));
        bot.getEventManager().registerListeners(
                new ABListener()
        );
        api = bot.getAccountManager().getNonAccountSpecifiedApi();
                bot.startBot();
    }
    static void stop() {
        bot.getHttpServer().stop();
    }

}
