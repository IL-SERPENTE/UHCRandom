package net.samagames.uhcrandom;

import net.samagames.api.SamaGamesAPI;
import net.samagames.api.gui.AbstractGui;
import net.samagames.api.gui.IGuiManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.concurrent.SynchronousQueue;

public class RandomGUI extends AbstractGui
{
    private List<RandomModule> modules;
    private Runnable callback;
    private UHCRandom plugin;
    private int enabled;

    private int[] delays = new int[]{1, 1, 1, 1, 1, 1, 1, 2, 3, 4, 6, 8, 12, 14, 18, 22, 26, 30, 35, 40, 40};
    private int index;

    public RandomGUI(UHCRandom plugin, Collection<RandomModule> allmodules, Collection<RandomModule> enabledModules, Runnable callback)
    {
        this.plugin = plugin;
        this.modules = new ArrayList<>();
        this.enabled = enabledModules.size();
        Iterator<RandomModule> it = allmodules.iterator();
        for (int i = 0; i < this.delays.length; i++)
        {
            if (!it.hasNext())
                it = allmodules.iterator();
            if (it.hasNext())
                this.modules.add(it.next());
        }
        this.modules.addAll(enabledModules);
        this.callback = callback;
        this.index = 0;
        this.next();
    }

    public void display(Player player)
    {
        player.closeInventory();
        player.openInventory(this.inventory);
    }

    private void next()
    {
        Random random = new Random();
        this.inventory = Bukkit.createInventory(null, 27, "UHCRandom");
        for (int i = 0; i < 27; i++)
        {
            if (i < 13 - (this.enabled / 2) || i > 13 + (this.enabled / 2))
                this.setSlotData(" ", new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte)random.nextInt(16)), i, null, "");
            else
            {
                RandomModule module = this.modules.get(0);
                this.setSlotData(module.getName(), module.getItem(), i, new String[]{module.getDescription()}, "");
                this.modules.add(module);
            }
        }
        IGuiManager manager = SamaGamesAPI.get().getGuiManager();
        if (manager != null)
            for (Player player : plugin.getServer().getOnlinePlayers())
                manager.openGui(player, this);
        if (this.index < this.delays.length)
            plugin.getServer().getScheduler().runTaskLater(plugin, this::next, this.delays[this.index]);
        else
            callback.run();
        this.index++;
    }
}
