package net.darqy.bukkit.NetTag;

import java.io.File;
import java.util.HashMap;
import java.util.logging.Logger;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class NewTag extends JavaPlugin {
    
    public static final Logger log = Logger.getLogger("Minecraft");
    public static String prefix;
    
    public static String tag_prefix_color;
    public static int max_tag_length;
    public static boolean enforce_max_tag_length;
    public static HashMap<String, String> tags = new HashMap<String, String>();

    YamlConfiguration config;
    ConfigurationSection config_tags;
    
    @Override
    public void onEnable() {
        config = (YamlConfiguration) getConfig();
        prefix = "[" + getDescription().getName() + "] ";
        PluginManager pm = getServer().getPluginManager();
        loadConf();
        getCommand("newtag").setExecutor(new Commands(this));
        pm.registerEvents(new PlayerListener(), this);
        log.info(prefix + getDescription().getVersion() + " enabled!");
    }
    
    @Override
    public void onDisable() {
        log.info(prefix + getDescription().getVersion() + " disabled!");
    }
    
    public void loadConf() {
        
        if (!new File(getDataFolder(), "config.yml").exists()) {
            config.options().copyDefaults(true);
            saveConfig();
        }
        
        config.set("tag_prefix_color", tag_prefix_color = config.getString("tag_prefix_color", "a"));
        config.set("enforce_max_tag_length", enforce_max_tag_length = config.getBoolean("enforce_max_tag_length", true));
        config.set("max_tag_length", max_tag_length = config.getInt("max_tag_length"));
        config_tags = config.getConfigurationSection("tags");
        for (String name : config_tags.getKeys(false)) {
            tags.put(name, config_tags.getString(name));
        }
        saveConfig();
    }
    
    public boolean hasPerm(CommandSender s, String node) {
        if (!(s instanceof Player)) return true;
        return s.hasPermission(node);
    }

}
