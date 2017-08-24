package net.samagames.uhcrandom;

import net.samagames.survivalapi.game.SurvivalGame;
import net.samagames.survivalapi.game.types.run.RunBasedGameLoop;
import net.samagames.survivalapi.utils.TimedEvent;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.plugin.java.JavaPlugin;

/*
 * This file is part of UHCRandom.
 *
 * UHCRandom is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * UHCRandom is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with UHCRandom.  If not, see <http://www.gnu.org/licenses/>.
 */
public class RandomRunGameLoop extends RunBasedGameLoop
{
    public RandomRunGameLoop(JavaPlugin plugin, Server server, SurvivalGame game)
    {
        super(plugin, server, game);
    }

    @Override
    public void createWaitingBlockRemovingEvent()
    {
        this.nextEvent = new TimedEvent(0, 5, "Sélection des modules", ChatColor.GREEN, true, () -> {
            this.nextEvent = null;
            ((UHCRandom)this.plugin).displayModulesGUI(super::createWaitingBlockRemovingEvent);
        }
        );
    }
}
