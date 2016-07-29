package net.samagames.uhcrandom;

import net.samagames.api.SamaGamesAPI;
import net.samagames.survivalapi.game.SurvivalGameStatisticsHelper;

import java.util.UUID;

/**
 *                )\._.,--....,'``.
 * .b--.        /;   _.. \   _\  (`._ ,.
 * `=,-,-'~~~   `----(,_..'--(,_..'`-.;.'
 *
 * This file is issued of the project UHCRandom
 * Created by Jérémy L. (BlueSlime) on 29/07/16
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