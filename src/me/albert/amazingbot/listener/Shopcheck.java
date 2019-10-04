package me.albert.amazingbot.listener;

import cc.moecraft.icq.event.events.message.EventGroupMessage;
import me.albert.amazingbot.AmazingBot;
import me.albert.amazingbot.Utils;
import me.albert.amazingbot.config.Data;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.Set;

public class Shopcheck {
    private static Set<String> items = Data.getData("shop.yml").getConfigurationSection("items").getKeys(false);
    private static int getEmptySlots(Player p) {
        PlayerInventory inventory = p.getInventory();
        int amount = 0;
        for (int i =0;i<36;i++) {
            ItemStack is = inventory.getItem(i);
            if (is == null || is.getType() == Material.AIR) {
                amount++;
            }
        }
        return amount;
    }

    private static void checkBuy(Player p, String[] args, EventGroupMessage e, ItemStack is, double price){
        if (Utils.notNumeric(args[1])){
            e.respond("请输入数字!");
            return;
        }
        String name = args[0];
        int amount = Integer.parseInt(args[1]);
        price = (price*amount);
        double balance = AmazingBot.getEconomy().getBalance(p);
        if (balance < price){
            e.respond("您的游戏币不足哦! 购买这些"+name+"一共需要 "+price+" 游戏币\n"+"您的账户余额为: "+balance+" 游戏币");
            return;
        }
        int slots = (int) Math.ceil(amount/is.getMaxStackSize());
        if (amount%is.getMaxStackSize() != 0){slots++;}
        if (slots == 0){slots = 1;}
        if (getEmptySlots(p) < slots){
            e.respond("您的背包空间只有: "+getEmptySlots(p)+" 格了\n"
                    +"购买这么多"+name+"至少需要 "+slots+" 格空间");
            return;
        }
        AmazingBot.getEconomy().withdrawPlayer(p,price);
        e.respond("你购买了 "+amount+"个"+name+",花费: "+price+" 游戏币\n"
                +"物品已经发放到您的背包,请查收");
        for (int i = 0;i<amount;i++){
            p.getInventory().addItem(is);
        }
        p.updateInventory();

    }

    private static void sendShopList(EventGroupMessage e){
       StringBuilder itemList = new StringBuilder();
       itemList.append("商品列表:\n");
       for (String s : items){
           String price = Data.getData("shop.yml").getString("items."+s + ".price");
           itemList.append(s).append("[").append(price).append("游戏币]\n");
       }
       e.respond(itemList.toString());
    }

    public static void buy(Player p,String[] args,EventGroupMessage e){
        if (args.length == 2) {
            if (items.contains(args[0])){
                String name = args[0];
                FileConfiguration shop = Data.getData("shop.yml");
                Material m = Material.getMaterial(shop.getString("items."+name+".material").toUpperCase());
                ItemStack is = new ItemStack(m);
                is.setDurability((short) shop.getInt("items."+name+".data"));
                double price = shop.getDouble("items."+name+".price");
                checkBuy(p,args,e,is,price);
                return;
            }
            sendShopList(e);
            return;
        } e.respond("正确用法: 购买 物品名称 数量");

    }
}
