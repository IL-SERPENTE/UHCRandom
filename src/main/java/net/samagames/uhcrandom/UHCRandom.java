package net.samagames.uhcrandom;

import com.google.gson.JsonPrimitive;
import net.samagames.api.SamaGamesAPI;
import net.samagames.survivalapi.SurvivalAPI;
import net.samagames.survivalapi.game.SurvivalGame;
import net.samagames.survivalapi.game.types.SurvivalSoloGame;
import net.samagames.survivalapi.game.types.SurvivalTeamGame;
import net.samagames.survivalapi.modules.AbstractSurvivalModule;
import net.samagames.survivalapi.modules.block.DiamondFlowerModule;
import net.samagames.survivalapi.modules.block.HardObsidianModule;
import net.samagames.survivalapi.modules.block.ParanoidModule;
import net.samagames.survivalapi.modules.block.RandomChestModule;
import org.apache.commons.lang3.tuple.Triple;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public class UHCRandom extends JavaPlugin
{
    private List<Triple<Class<? extends AbstractSurvivalModule>, Map<String, Object>, String>> enabledModules;

    @Override
    public void onEnable()
    {
        SurvivalAPI api = SurvivalAPI.get();
        List<Triple<Class<? extends AbstractSurvivalModule>, Map<String, Object>, String>> modules = new ArrayList<>();
        int modulesNumber = SamaGamesAPI.get().getGameManager().getGameProperties().getConfig("modulesNumber", new JsonPrimitive(5)).getAsInt();
        Random random = new Random();

        modules.add(Triple.of(DiamondFlowerModule.class, null, "Les fleurs peuvent donner du diamant."));
        modules.add(Triple.of(HardObsidianModule.class, null, "L'obsidienne est plus dure à casser (pioche en diamant uniquement)."));
        modules.add(Triple.of(ParanoidModule.class, null, "Tout minage de diamant est annoncé aux autres joueurs."));

        enabledModules = new ArrayList<>();
        modulesNumber = Math.min(modulesNumber, modules.size());
        getLogger().info("Selecting " + modulesNumber + " modules.");
        for (int i = 0; i < modulesNumber; i++)
        {
            int rand = random.nextInt() % modules.size();
            Triple<Class<? extends AbstractSurvivalModule>, Map<String, Object>, String> last = null;
            for (Triple<Class<? extends AbstractSurvivalModule>, Map<String, Object>, String> entry : modules)
            {
                if (rand == 0)
                {
                    api.loadModule(entry.getLeft(), entry.getMiddle());
                    enabledModules.add(entry);
                    last = entry;
                    break ;
                }
                rand--;
            }
            if (last != null)
                modules.remove(last);
        }

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

    public void displayModules(Runnable callback)
    {
        getServer().broadcastMessage("Modules activés :");
        for (Triple<Class<? extends AbstractSurvivalModule>, Map<String, Object>, String> mod : enabledModules)
            getServer().broadcastMessage(" - " + mod.getRight());
        callback.run();
    }
}
