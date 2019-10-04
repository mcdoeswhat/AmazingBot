package me.albert.amazingbot.listener;

import cc.moecraft.icq.event.EventHandler;
import cc.moecraft.icq.event.IcqListener;
import cc.moecraft.icq.event.events.message.EventGroupMessage;
import me.albert.amazingbot.AmazingBot;
import me.albert.amazingbot.Console;
import me.albert.amazingbot.Utils;
import me.albert.amazingbot.Verify;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.sql.Date;
import java.util.HashSet;
import java.util.concurrent.atomic.AtomicBoolean;

public class ABListener extends IcqListener {
    private static HashSet<Long> binds = new HashSet<>();

    private void adminCheck (EventGroupMessage e){
        if (AmazingBot.getInstance().getConfig().getStringList("owners").contains(String.valueOf(e.getSender().getId()))){
            if (e.getMessage().startsWith("sudo ")){
                e.respond("命令已提交");
                Bukkit.getScheduler().runTask(AmazingBot.getInstance(), () -> {
                Console sender = new Console(AmazingBot.getInstance().getServer(),e);
                String cmd = e.getMessage().substring(5);
                Bukkit.dispatchCommand(sender, cmd);
                Bukkit.getLogger().info("§aQQ用户: "+e.getSender().getId()+" 执行了 §c"+cmd+" §a命令");
                });
            }
        }

    }

    private void shopCheck(EventGroupMessage e){
        Bukkit.getScheduler().runTask(AmazingBot.getInstance(), () -> {
        if (e.getMessage().startsWith("/bd ")){
            String player = e.getMessage().substring(4);
            if (Bukkit.getPlayerExact(player) == null){
                e.respond("该玩家不在线!");
                return;
            }
            if (binds.contains(e.getSenderId())){
                e.respond("一天仅允许一次此操作");
                return;
            }
            Player p = Bukkit.getPlayerExact(player);
            Verify.start(p,e.getSenderId());
            e.respond("请在游戏内根据提示完成验证!");
            binds.add(e.getSenderId());
            return;
        }
        if (e.getMessage().startsWith("购买 ")){
            if (Utils.getPlayer(e.getSender().getId()) == null){
                e.respond("您尚未绑定游戏ID,输入/bd 玩家 进行绑定");
                return;
            }
            String[] args = e.getMessage().substring(3).split(" ");
            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(Utils.getPlayer(e.getSender().getId()));
            if (!offlinePlayer.isOnline()){
                e.respond("你所绑定的游戏用户: "+offlinePlayer.getName()+" 并不在线哦!");
                return;
            }
            Player p = offlinePlayer.getPlayer();
            Shopcheck.buy(p,args,e);
        }
        });

    }

    private void newPlayerCheck(EventGroupMessage e){
        if (e.getMessage().equalsIgnoreCase(AmazingBot.getInstance().getConfig()
        .getString("function.new_player"))){
            AtomicBoolean isRunning = new AtomicBoolean(false);
            if (isRunning.get()){
                e.respond("操作过于频繁,请稍后再试");
                return;
            }
            Bukkit.getScheduler().runTaskAsynchronously(AmazingBot.getInstance(), () -> {
                isRunning.set(true);
                int i =0;
                for (OfflinePlayer p : Bukkit.getOfflinePlayers()){
                    Date a = new Date(System.currentTimeMillis());
                    Date b = new Date(p.getFirstPlayed());
                    if (Utils.isSameDay(a,b)){
                        i++;
                    }
                }
                e.respond("今日新玩家数量： "+i);
                isRunning.set(false);
            });

        }

    }

    @EventHandler
    public void onMessage(final EventGroupMessage e) {
        if (!AmazingBot.getInstance().getConfig().getStringList("groups").contains(e.getGroupId().toString())){
            return;
        }
        e.getBot().getAccountManager().refreshCache();
        adminCheck(e);
        shopCheck(e);
        newPlayerCheck(e);
    }
}

