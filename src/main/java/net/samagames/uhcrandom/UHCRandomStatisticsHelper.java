package net.samagames.uhcrandom;

import net.samagames.api.SamaGamesAPI;
import net.samagames.survivalapi.game.SurvivalGameStatisticsHelper;

import java.util.UUID;

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
public class UHCRandomStatisticsHelper implements SurvivalGameStatisticsHelper
{
    @Override
    public void increaseKills(UUID uuid)
    {
        SamaGamesAPI.get().getStatsManager().getPlayerStats(uuid).getUHCRandomStatistics().incrByKills(1);
    }

    @Override
    public void increaseDeaths(UUID uuid)
    {
        SamaGamesAPI.get().getStatsManager().getPlayerStats(uuid).getUHCRandomStatistics().incrByDeaths(1);
    }

    @Override
    public void increaseDamages(UUID uuid, double damages)
    {
        SamaGamesAPI.get().getStatsManager().getPlayerStats(uuid).getUHCRandomStatistics().incrByDamages((int) damages);
    }

    @Override
    public void increasePlayedTime(UUID uuid, long playedTime)
    {
        SamaGamesAPI.get().getStatsManager().getPlayerStats(uuid).getUHCRandomStatistics().incrByPlayedTime(playedTime);
    }

    @Override
    public void increasePlayedGames(UUID uuid)
    {
        SamaGamesAPI.get().getStatsManager().getPlayerStats(uuid).getUHCRandomStatistics().incrByPlayedGames(1);
    }

    @Override
    public void increaseWins(UUID uuid)
    {
        SamaGamesAPI.get().getStatsManager().getPlayerStats(uuid).getUHCRandomStatistics().incrByWins(1);
    }
}