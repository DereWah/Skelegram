package org.derewah.skelegram;

import lombok.Getter;
import org.bstats.bukkit.Metrics;
import ch.njol.skript.Skript;
import ch.njol.skript.SkriptAddon;
import org.bstats.charts.SimplePie;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.derewah.skelegram.telegram.TelegramSessions;

import java.io.IOException;

public class Skelegram extends JavaPlugin {

    static Skelegram instance;
    private SkriptAddon addon;
    @Getter
    private TelegramSessions telegramSessions;

    public void onEnable(){
        instance = this;
        addon = Skript.registerAddon(this);
        try {
            addon.loadClasses("org.derewah.skelegram");
        } catch(IOException e){
            e.printStackTrace();
        }
        telegramSessions = new TelegramSessions();
        // Register Metrics
        Metrics metrics = new Metrics(this, 18364);
        metrics.addCustomChart(new SimplePie("skript_version", () ->
                Bukkit.getServer().getPluginManager().getPlugin("Skript").getDescription().getVersion()));
        metrics.addCustomChart(new SimplePie("skelegram_version", () ->
                this.getDescription().getVersion()));
        getCommand("skelegram").setExecutor(new Commands(this));
        Bukkit.getLogger().info("[Skelegram] has been enabled!");
    }

    @Override
    public void onDisable(){telegramSessions.clearAllSessions();}
    public static Skelegram getInstance(){
        return instance;
    }

    public SkriptAddon getAddonInstance(){
        return addon;
    }
}
