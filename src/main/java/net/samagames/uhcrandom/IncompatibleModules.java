package net.samagames.uhcrandom;

import net.samagames.survivalapi.modules.AbstractSurvivalModule;

public class IncompatibleModules
{
    private final Class<? extends AbstractSurvivalModule>[] modules;

    @SafeVarargs
    public IncompatibleModules(Class<? extends AbstractSurvivalModule>... modules)
    {
        this.modules = modules;
    }

    public boolean areIncompatibles(Class<? extends AbstractSurvivalModule> first, Class<? extends AbstractSurvivalModule> second)
    {
        boolean ok[] = {false, false};
        for (Class<? extends AbstractSurvivalModule> module : modules)
        {
            if (module.equals(first))
                ok[0] = true;
            if (module.equals(second))
                ok[1] = true;
        }
        return ok[0] && ok[1];
    }
}
