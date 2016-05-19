package net.samagames.uhcrandom;

import net.samagames.survivalapi.modules.AbstractSurvivalModule;
import net.samagames.survivalapi.modules.gameplay.GenerationModule;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
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

        ItemMeta itemMeta = this.item.getItemMeta();
        itemMeta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS, ItemFlag.HIDE_ATTRIBUTES);
        this.item.setItemMeta(itemMeta);
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
}
