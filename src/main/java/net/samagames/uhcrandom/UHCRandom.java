package net.samagames.uhcrandom;

import com.google.gson.JsonPrimitive;
import net.samagames.api.SamaGamesAPI;
import net.samagames.survivalapi.SurvivalAPI;
import net.samagames.survivalapi.game.SurvivalGame;
import net.samagames.survivalapi.game.types.SurvivalSoloGame;
import net.samagames.survivalapi.game.types.SurvivalTeamGame;
import net.samagames.survivalapi.modules.block.DiamondFlowerModule;
import net.samagames.survivalapi.modules.block.HardObsidianModule;
import net.samagames.survivalapi.modules.block.ParanoidModule;
import net.samagames.survivalapi.modules.block.RandomChestModule;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public class UHCRandom extends JavaPlugin
{
    private List<RandomModule> enabledModules;

    @Override
    public void onEnable()
    {
        SurvivalAPI api = SurvivalAPI.get();
        List<RandomModule> modules = new ArrayList<>();
        int modulesNumber = SamaGamesAPI.get().getGameManager().getGameProperties().getConfig("modulesNumber", new JsonPrimitive(5)).getAsInt();
        Random random = new Random();

        /** Modules list */
        modules.add(new RandomModule(DiamondFlowerModule.class, null, "Les fleurs peuvent donner du diamant.", new ItemStack(Material.RED_ROSE)));
        modules.add(new RandomModule(HardObsidianModule.class, null, "L'obsidienne est plus dure à casser (pioche en diamant uniquement).", new ItemStack(Material.OBSIDIAN)));
        modules.add(new RandomModule(ParanoidModule.class, null, "Tout minage de diamant est annoncé aux autres joueurs.", new ItemStack(Material.DIAMOND)));

        /** Random modules selector */
        enabledModules = new ArrayList<>();
        modulesNumber = Math.min(modulesNumber, modules.size());
        getLogger().info("Selecting " + modulesNumber + " modules.");
        for (int i = 0; i < modulesNumber; i++)
        {
            int rand = random.nextInt() % modules.size();
            RandomModule last = null;
            for (RandomModule entry : modules)
            {
                if (rand == 0)
                {
                    api.loadModule(entry.getModuleClass(), entry.getConfig());
                    enabledModules.add(entry);
                    last = entry;
                    break ;
                }
                rand--;
            }
            if (last != null)
                modules.remove(last);
        }
        getLogger().info("Random modules selected");

        /** Solo or team game, depending on config, and overriding of startGame function to display modules (will me moved to GameLoop later) */
        int nb = SamaGamesAPI.get().getGameManager().getGameProperties().getOption("playersPerTeam", new JsonPrimitive(1)).getAsInt();
        SurvivalGame game;
        if (nb > 1)
            game = new SurvivalTeamGame<UHCRandomGameLoop>(this, "uhcrandom", "UHCRandom", "La chance sera-t-elle avec vous ?", "", UHCRandomGameLoop.class, nb){
                @Override
                public void startGame()
                {
                    displayModules(super::startGame);
                }
            };
        else
            game = new SurvivalSoloGame<UHCRandomGameLoop>(this, "uhcrandom", "UHCRandom", "La chance sera-t-elle avec vous ?", "", UHCRandomGameLoop.class){
                @Override
                public void startGame()
                {
                    displayModules(super::startGame);
                }
            };

        SurvivalAPI.get().unloadModule(RandomChestModule.class);

        SamaGamesAPI.get().getGameManager().setMaxReconnectTime(10);
        SamaGamesAPI.get().getGameManager().registerGame(game);
    }

    /**
     * Show modules GUI to players.
     * Work in progress.
     * @param callback
     */
    public void displayModules(Runnable callback)
    {
        getServer().broadcastMessage("Modules activés :");
        for (RandomModule mod : enabledModules)
            getServer().broadcastMessage(" - " + mod.getName());
        callback.run();
    }
}
