/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package me.kolorafa.premiumgroupchanger;

import java.util.logging.Level;
import java.util.logging.Logger;
import me.kolorafa.premiumproxy.PremiumStatusEvent;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author kolorafa
 */
public class premiumgroupPlugin extends JavaPlugin implements Listener {
    
    public static Permission perms = null;
    
    public final Logger logger = Logger.getLogger("Minecraft");
    PluginDescriptionFile pdffile;
    
    @EventHandler(priority = EventPriority.NORMAL)
    public void playergroupchange(PremiumStatusEvent event) {
        if(event.hasPremium()){
            String group = perms.getPrimaryGroup(getServer().getPlayerExact(event.getPlayerName()));
            if(group.equalsIgnoreCase(getConfig().getString("NonPremiumGroupName"))){
                perms.playerAddGroup((String)null, event.getPlayerName(), getConfig().getString("PremiumGroupName"));
                log("Adding group "+getConfig().getString("PremiumGroupName")+" to "+event.getPlayerName());
                return;
            }
            log("Didn't add "+getConfig().getString("PremiumGroupName")+" to "+event.getPlayerName()+" because it doesn't have "+getConfig().getString("NonPremiumGroupName")+" group or already has it");
            return;
        }
        log("Don't do anything, user "+event.getPlayerName()+" is not premium");
    }
    
    public void log(String text) {
        if (getConfig().getBoolean("debug")) {
            logger.log(Level.INFO, "[" + pdffile.getName() + "] DEBUG: " + text);
        }
    }

    @Override
    public void onDisable() {
        logger.log(Level.INFO, "[" + pdffile.getName() + "] is disabled.");
    }

    @Override
    public void onEnable() {
        loadConfiguration();
        pdffile = this.getDescription();
        logger.log(Level.INFO, "[" + pdffile.getName() + "] is enabled.");
        RegisteredServiceProvider<Permission> rsp = getServer().getServicesManager().getRegistration(Permission.class);
        perms = rsp.getProvider();
        getServer().getPluginManager().registerEvents(this, this);
    }

    private void loadConfiguration() {
        getConfig().options().copyDefaults(true);
        saveConfig();
    }
}
