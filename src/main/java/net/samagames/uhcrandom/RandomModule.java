package net.samagames.uhcrandom;

import net.samagames.survivalapi.modules.AbstractSurvivalModule;
import net.samagames.survivalapi.modules.gameplay.GenerationModule;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.Map;

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
public class RandomModule
{
    private Class<? extends AbstractSurvivalModule> moduleclass;
    private Map<String, Object> config;
    private String name;
    private String description;
    private ItemStack item;
    private boolean run;

    public RandomModule(Class<? extends AbstractSurvivalModule> moduleclass, Map<String, Object> config, String description, ItemStack item)
    {
        this(moduleclass, config, description, item, true);
    }

    public RandomModule(Class<? extends AbstractSurvivalModule> moduleclass, Map<String, Object> config, String description, ItemStack item, boolean run)
    {
        this.moduleclass = moduleclass;
        this.config = config;
        this.name = this.moduleclass.getCanonicalName().substring(
                this.moduleclass.getCanonicalName().lastIndexOf('.') + 1,
                this.moduleclass.getCanonicalName().indexOf("Module"));
        this.description = description;
        this.item = item;

        ItemMeta itemMeta = this.item.getItemMeta();
        itemMeta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS, ItemFlag.HIDE_ATTRIBUTES);
        this.item.setItemMeta(itemMeta);
        this.run = run;
    }

    public RandomModule(Class<GenerationModule> moduleclass, String name, String description, ItemStack item)
    {
        this.moduleclass = moduleclass;
        this.config = new HashMap<>();
        this.config.put("mapname", name);
        this.name = this.moduleclass.getCanonicalName().substring(
                this.moduleclass.getCanonicalName().lastIndexOf('.') + 1,
                this.moduleclass.getCanonicalName().indexOf("Module"));
        this.description = description;
        this.item = item;

        ItemMeta itemMeta = this.item.getItemMeta();
        itemMeta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS, ItemFlag.HIDE_ATTRIBUTES);
        this.item.setItemMeta(itemMeta);
    }

    public Class<? extends AbstractSurvivalModule> getModuleClass()
    {
        return this.moduleclass;
    }

    public Map<String, Object> getConfig()
    {
        return this.config;
    }

    public String getName()
    {
        return this.name;
    }

    public String getDescription()
    {
        return this.description;
    }

    public ItemStack getItem()
    {
        return this.item;
    }

    public boolean isRunModule()
    {
        return run;
    }
}
