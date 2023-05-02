package org.derewah.skelegram;

import org.bstats.bukkit.Metrics;
import ch.njol.skript.Skript;
import ch.njol.skript.SkriptAddon;
import org.bstats.charts.SimplePie;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.derewah.skelegram.telegram.TelegramSessions;
import org.telegram.telegrambots.meta.generics.BotSession;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.io.IOException;
import java.util.List;

public class Skelegram extends JavaPlugin {

    static Skelegram instance;
    SkriptAddon addon;


    public void onEnable(){
        instance = this;
        addon = Skript.registerAddon(this);
        try {
            addon.loadClasses("org.derewah.skelegram");
        } catch(IOException e){
            e.printStackTrace();
        }


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
    public void onDisable(){
        for(BotSession sess : TelegramSessions.sessions.values()){
            sess.stop();
        }
    }



    public static Skelegram getInstance(){

        return instance;
    }

    public SkriptAddon getAddonInstance(){
        return addon;
    }




}
