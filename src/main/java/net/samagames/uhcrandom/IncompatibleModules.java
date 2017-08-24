package net.samagames.uhcrandom;

import net.samagames.survivalapi.modules.AbstractSurvivalModule;

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
        boolean[] ok = {false, false};
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
