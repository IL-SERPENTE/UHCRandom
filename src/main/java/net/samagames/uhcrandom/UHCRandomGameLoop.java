package net.samagames.uhcrandom;

import net.samagames.survivalapi.game.SurvivalGame;
import net.samagames.survivalapi.game.SurvivalGameLoop;
import net.samagames.survivalapi.utils.TimedEvent;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.plugin.java.JavaPlugin;

public class UHCRandomGameLoop extends SurvivalGameLoop
{
    public UHCRandomGameLoop(JavaPlugin plugin, Server server, SurvivalGame game)
    {
        super(plugin, server, game);
        this.episodeEnabled = true;
    }

    @Override
    public void createWaitingBlockRemovingEvent()
    {
        ((UHCRandom)this.plugin).displayModulesGUI(() -> {
            this.nextEvent = new TimedEvent(0, 10, "Suppression des cages", ChatColor.GREEN, true, () ->
            {
                this.game.removeWaitingBlocks();
                this.createDamageEvent();
            });
        });
    }

    /**
     * Will contain function to open Moodules GUI, after teleport.
     * Waiting for BlueSlime work about it.
     */
}
