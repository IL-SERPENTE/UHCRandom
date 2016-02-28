package net.samagames.uhcrandom;

import net.samagames.api.SamaGamesAPI;
import net.samagames.api.gui.AbstractGui;
import net.samagames.api.gui.IGuiManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class RandomGUI extends AbstractGui
{
    public static String INV_NAME = "UHCRandom";

    private List<RandomModule> modules;
    private Runnable callback;
    private UHCRandom plugin;
    private int enabled;

    private int[] delays = new int[]{3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 4, 4, 5, 5, 6, 6, 7, 8, 9, 10, 12, 14, 16, 18, 20, 25, 10};
    private int index;

    public RandomGUI(UHCRandom plugin, Collection<RandomModule> allModules, Collection<RandomModule> enabledModules, Runnable callback)
    {
        this.plugin = plugin;
        this.modules = new ArrayList<>();
        this.enabled = enabledModules.size();
        Iterator<RandomModule> it = allModules.iterator();
        for (int i = 0; i < this.delays.length; i++)
        {
            if (!it.hasNext())
                it = allModules.iterator();
            if (it.hasNext())
                this.modules.add(it.next());
        }
        this.modules.addAll(enabledModules);
        this.callback = callback;
        this.index = 0;
        this.next();
    }

    /**
     * Open inventory if not opened (first time or after close).
     * @param player The player holding the inventory.
     */
    public void display(Player player)
    {
        player.closeInventory();
        player.openInventory(this.inventory);
    }

    /**
     * Generates next inventory, with random glass colors (without light grey), and modules icons.
     */
    private void next()
    {
        Random random = new Random();
        this.inventory = Bukkit.createInventory(null, 27, INV_NAME);
        int j = 0;
        for (int i = 0; i < 27; i++)
        {
            if (i < 13 - (this.enabled / 2) || i > 13 + (this.enabled / 2))
                this.setSlotData(" ", new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte)random.nextInt(16)), i, null, "");
            else
            {
                RandomModule module = this.modules.get(j);
                this.setSlotData(ChatColor.BOLD + "" + ChatColor.AQUA + module.getName(), module.getItem(), i, new String[]{ChatColor.GRAY + module.getDescription()}, "");
                j++;
            }
        }
        this.modules.remove(0);
        IGuiManager manager = SamaGamesAPI.get().getGuiManager();
        if (manager != null)
            for (Player player : plugin.getServer().getOnlinePlayers())
            {
                player.playSound(player.getLocation(), this.index < this.delays.length ? Sound.ORB_PICKUP : Sound.LEVEL_UP, 1F, 1F);
                InventoryView view = player.getOpenInventory();
                if (view == null || view.getTopInventory() == null || !view.getTopInventory().getName().equals(INV_NAME))
                    manager.openGui(player, this);
                else
                    view.getTopInventory().setContents(this.inventory.getContents());
            }
        if (this.index < this.delays.length)
            this.plugin.getServer().getScheduler().runTaskLater(this.plugin, this::next, this.delays[this.index]);
        else
            this.plugin.getServer().getScheduler().runTaskLater(this.plugin, () -> {
                this.plugin.getServer().getOnlinePlayers().forEach(Player::closeInventory);
                this.callback.run();
            }, 60);
        this.index++;
    }
}
