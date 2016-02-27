package net.samagames.uhcrandom;

import net.samagames.survivalapi.modules.AbstractSurvivalModule;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public class RandomModule
{
    private Class<? extends AbstractSurvivalModule> moduleclass;
    private Map<String, Object> config;
    private String name;
    private ItemStack item;

    public RandomModule(Class<? extends AbstractSurvivalModule> moduleclass, Map<String, Object> config, String name, ItemStack item)
    {
        this.moduleclass = moduleclass;
        this.config = config;
        this.name = name;
        this.item = item;
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
        return this.moduleclass.getName().substring(0, this.moduleclass.getName().indexOf("Module"));
    }

    public String getDescription()
    {
        return this.name;
    }

    public ItemStack getItem()
    {
        return this.item;
    }
}
