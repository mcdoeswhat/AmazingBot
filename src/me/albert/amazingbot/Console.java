package me.albert.amazingbot;

import cc.moecraft.icq.event.events.message.EventGroupMessage;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.conversations.Conversation;
import org.bukkit.conversations.ConversationAbandonedEvent;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.Optional;
import java.util.Set;

public class Console implements ConsoleCommandSender {
    private static int taskid;
    private final Server server;
    private final EventGroupMessage e;
    private final ArrayList<String> output = new ArrayList<>();

    public Console(Server server, EventGroupMessage e) {
        this.server = server;
        this.e = e;
    }
    private void send(){
        Bukkit.getScheduler().cancelTask(taskid);
        taskid = Bukkit.getScheduler().runTaskLater(AmazingBot.getInstance(), () -> {
            StringBuilder output = new StringBuilder();
            for (String s : this.output){
                output.append(s.replaceAll("§\\S", "")).append("\n");
            }
            e.respond(output.toString());
            this.output.clear();
        },4).getTaskId();
    }


    private Optional<ConsoleCommandSender> get() {
        return Optional.ofNullable(this.server.getConsoleSender());
    }

    @Override
    public Server getServer() {
        return this.server;
    }

    @Override
    public String getName() {
        return "CONSOLE";
    }

    @Override
    public void sendMessage(String message) {
        this.output.add(message);
        send();
    }

    @Override
    public void sendMessage(String[] messages) {
        for (String msg : messages) {
            sendMessage(msg);
        }
    }

    @Override
    public boolean isPermissionSet(String s) {
        return get().map(c -> c.isPermissionSet(s)).orElse(true);
    }

    @Override
    public boolean isPermissionSet(Permission permission) {
        return get().map(c -> c.isPermissionSet(permission)).orElse(true);
    }

    @Override
    public boolean hasPermission(String s) {
        return get().map(c -> c.hasPermission(s)).orElse(true);
    }

    @Override
    public boolean hasPermission(Permission permission) {
        return get().map(c -> c.hasPermission(permission)).orElse(true);
    }

    @Override
    public boolean isOp() {
        return true;
    }

    // just throw UnsupportedOperationException - we never use any of these methods
    @Override public Spigot spigot() { throw new UnsupportedOperationException(); }
    @Override public boolean isConversing() { throw new UnsupportedOperationException(); }
    @Override public void acceptConversationInput(String s) {}
    @Override public boolean beginConversation(Conversation conversation) { throw new UnsupportedOperationException(); }
    @Override public void abandonConversation(Conversation conversation) { throw new UnsupportedOperationException(); }
    @Override public void abandonConversation(Conversation conversation, ConversationAbandonedEvent conversationAbandonedEvent) { throw new UnsupportedOperationException(); }
    @Override public void sendRawMessage(String s) { }
    @Override public PermissionAttachment addAttachment(Plugin plugin, String s, boolean b) { throw new UnsupportedOperationException(); }
    @Override public PermissionAttachment addAttachment(Plugin plugin) { throw new UnsupportedOperationException(); }
    @Override public PermissionAttachment addAttachment(Plugin plugin, String s, boolean b, int i) { throw new UnsupportedOperationException(); }
    @Override public PermissionAttachment addAttachment(Plugin plugin, int i) { throw new UnsupportedOperationException(); }
    @Override public void removeAttachment(PermissionAttachment permissionAttachment) { throw new UnsupportedOperationException(); }
    @Override public void recalculatePermissions() { throw new UnsupportedOperationException(); }
    @Override public Set<PermissionAttachmentInfo> getEffectivePermissions() { throw new UnsupportedOperationException(); }
    @Override public void setOp(boolean b) { throw new UnsupportedOperationException(); }
}