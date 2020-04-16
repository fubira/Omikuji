package net.ironingot.omikuji;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Calendar;
import java.util.Map;
import java.util.HashMap;
import java.text.SimpleDateFormat;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.nio.ByteBuffer;

public class OmikujiCommand implements CommandExecutor {
    private Omikuji plugin;
    private OmikujiBox omikujiBox;
    private String pluginName;
    private String pluginVersion;

    public OmikujiCommand(Omikuji plugin){
        this.plugin = plugin;
        this.pluginName = plugin.getDescription().getName();
        this.pluginVersion = plugin.getDescription().getVersion();

        loadOmikujiBox();
    }

    private boolean loadOmikujiBox() {
        try {
            OmikujiBox newOmikujiBox = 
                    new OmikujiBox(plugin.getConfigHandler().getElementsMap());
            omikujiBox = newOmikujiBox;

            plugin.logger.info("OmikujiBox loaded.");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        plugin.logger.warning("OmikujiBox load failed.");
        return false;
    }

    
    private long generateDailySeed(String strKey) {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        String strDate = format.format(Calendar.getInstance().getTime());

        long seed = 0;

        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            md.reset();
            md.update(strKey.getBytes());
            md.update(strDate.getBytes());
            ByteBuffer byteBuffer = ByteBuffer.wrap(md.digest());
            seed = byteBuffer.getLong();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return seed;
    }

    private String buildText(Map<String, String> keywords) {
        String text = plugin.getConfigHandler().getFormat();

        for (Map.Entry<String, String> keyword: keywords.entrySet()) {
            String key = "%" + keyword.getKey() + "%";
            text = text.replace(key, keyword.getValue());
        }

        for (ChatColor color: ChatColor.values()) {
            String key = "&" + color.name();
            text = text.replace(key, color.toString());
            text = text.replace(key.toLowerCase(), color.toString());
        }

        return text;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        Player senderPlayer = sender instanceof Player ? (Player) sender : null;
        String arg = "draw";
        String drawer = sender.getName();
        String target = sender.getName();

        
        if (args.length >= 1) {
            arg = args[0];
        }
        if (args.length >= 2) {
            if (senderPlayer != null && (senderPlayer.hasPermission("omikuji.command.draw.others") || senderPlayer.isOp())) {
                target = args[1];
            } else {
                sender.sendMessage(ChatColor.RED + "* You don't have permission.");
                return false;
            }
        }

        if (arg.equals("draw")) {
            String result = omikujiBox.draw(generateDailySeed(target));

            HashMap<String, String> keywords = new HashMap<String, String>();
            keywords.put("player", target);
            keywords.put("result", result);
            if (target != drawer) {
                keywords.put("drawby", "by " + drawer);
            } else {
                keywords.put("drawby", "");
            }

            plugin.getServer().broadcastMessage(buildText(keywords));
            return true;
        } 

        if (arg.equals("reload")) {
            if (!sender.isOp()) {
                sender.sendMessage(ChatColor.RED + "* You don't have permission.");
                return false;
            }

            if (plugin.getConfigHandler().load()) {
                sender.sendMessage(ChatColor.GOLD + "Config Reloaded.");
                loadOmikujiBox();
            } else {
                sender.sendMessage(ChatColor.RED + "* Failed to load config.xml.");
            }
            return true;
        }

        sender.sendMessage(ChatColor.GOLD + "Unknown command: '" + arg + "'");
        return true;
    }
}
