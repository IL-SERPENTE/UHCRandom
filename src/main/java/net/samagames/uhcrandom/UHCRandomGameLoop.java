package net.samagames.uhcrandom;

import net.samagames.survivalapi.game.SurvivalGame;
import net.samagames.survivalapi.game.SurvivalGameLoop;
import org.bukkit.Server;
import org.bukkit.plugin.java.JavaPlugin;

public class UHCRandomGameLoop extends SurvivalGameLoop
{
    public UHCRandomGameLoop(JavaPlugin plugin, Server server, SurvivalGame game)
    {
        super(plugin, server, game);
    }

    /**
     * Will contain function to open Moodules GUI, after teleport.
     * Waiting for BlueSlime work about it.
     */
}
