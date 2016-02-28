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
        this.nextEvent = new TimedEvent(0, 3, "Sélection des modules", ChatColor.GREEN, true,
                () -> ((UHCRandom)this.plugin).displayModulesGUI(() ->
                {
                    this.nextEvent = new TimedEvent(0, 10, "Suppression des cages", ChatColor.GREEN, true, () ->
                    {
                        this.game.removeWaitingBlocks();
                        this.createDamageEvent();
                    });
                })
        );
    }
}
