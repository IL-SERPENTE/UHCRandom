package net.samagames.uhcrandom;

import net.samagames.survivalapi.modules.AbstractSurvivalModule;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public class RandomModule
{
    private Class<? extends AbstractSurvivalModule> moduleclass;
    private Map<String, Object> config;
    private String name;
    private String description;
    private ItemStack item;

    public RandomModule(Class<? extends AbstractSurvivalModule> moduleclass, Map<String, Object> config, String description, ItemStack item)
    {
        this.moduleclass = moduleclass;
        this.config = config;
        this.name = this.moduleclass.getCanonicalName().substring(
                this.moduleclass.getCanonicalName().lastIndexOf('.') + 1,
                this.moduleclass.getCanonicalName().indexOf("Module"));
        this.description = description;
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
}
