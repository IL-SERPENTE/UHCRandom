package net.samagames.uhcrandom;

import net.samagames.survivalapi.modules.AbstractSurvivalModule;

public class IncompatibleModulePair
{
    public final Class<? extends AbstractSurvivalModule> first;
    public final Class<? extends AbstractSurvivalModule> second;

    public IncompatibleModulePair(Class<? extends AbstractSurvivalModule> first, Class<? extends AbstractSurvivalModule> second)
    {
        this.first = first;
        this.second = second;
    }
}
