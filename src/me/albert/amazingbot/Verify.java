package me.albert.amazingbot;

import me.albert.amazingbot.config.Data;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.HashMap;
import java.util.UUID;

public class Verify implements Listener {
    private static HashMap<UUID,Long> codes = new HashMap<>();
    @EventHandler
    public void onChat(AsyncPlayerChatEvent e){
        if (!codes.containsKey(e.getPlayer().getUniqueId())){
            return;
        }
        Player p = e.getPlayer();
        if (e.getMessage().startsWith("confirm ") && e.getMessage().replace("confirm ","")
        .equalsIgnoreCase(String.valueOf(codes.get(p.getUniqueId())))){
            Data.set("data.yml",String.valueOf(codes.get(p.getUniqueId())),p.getUniqueId().toString());
            codes.remove(p.getUniqueId());
            p.sendMessage("§a验证成功!");
            e.setCancelled(true);
        }
    }

    public static void start(Player p, long qq){
        codes.put(p.getUniqueId(),qq);
        p.sendMessage("§7Q群内的: "+qq+" 用户正在绑定此账号进行对接");
        p.sendMessage("§7请再聊天栏输入 confirm "+qq+" 以完成绑定");
        p.sendMessage("§7如果非本人操作请忽略此消息!");
    }

}
