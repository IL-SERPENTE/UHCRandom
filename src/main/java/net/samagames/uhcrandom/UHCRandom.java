package net.samagames.uhcrandom;

import com.google.gson.JsonPrimitive;
import net.samagames.api.SamaGamesAPI;
import net.samagames.survivalapi.SurvivalAPI;
import net.samagames.survivalapi.game.SurvivalGame;
import net.samagames.survivalapi.game.types.SurvivalSoloGame;
import net.samagames.survivalapi.game.types.SurvivalTeamGame;
import net.samagames.survivalapi.modules.block.*;
import net.samagames.survivalapi.modules.combat.*;
import net.samagames.survivalapi.modules.craft.*;
import net.samagames.survivalapi.modules.entity.EntityDropModule;
import net.samagames.survivalapi.modules.entity.InfestationModule;
import net.samagames.survivalapi.modules.gameplay.*;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffectType;

import java.util.*;

public class UHCRandom extends JavaPlugin
{
    private List<RandomModule> modules;
    private List<RandomModule> enabledModules;

    @Override
    public void onEnable()
    {
        SurvivalAPI api = SurvivalAPI.get();
        this.modules = new ArrayList<>();
        Random random = new Random();

        /** Modules list */
        this.modules.add(new RandomModule(DiamondFlowerModule.class, null, "Les fleurs peuvent donner du diamant.", new ItemStack(Material.RED_ROSE)));
        this.modules.add(new RandomModule(HardObsidianModule.class, null, "L'obsidienne ne peut être cassée qu'avec une pioche en diamant.", new ItemStack(Material.OBSIDIAN)));
        this.modules.add(new RandomModule(ParanoidModule.class, null, "Tout minage de diamant est annoncé aux autres joueurs.", new ItemStack(Material.DIAMOND_ORE)));
        this.modules.add(new RandomModule(RapidOresModule.class, new RapidOresModule.ConfigurationBuilder().addDefaults().build(), "Le minage vous rapporte deux fois plus de ressources.", new ItemStack(Material.GOLD_PICKAXE)));
        this.modules.add(new RandomModule(TorchThanCoalModule.class, new TorchThanCoalModule.ConfigurationBuilder().build(), "Le charbon se transforme en torches.", new ItemStack(Material.TORCH)));
        this.modules.add(new RandomModule(AutomaticTNTModule.class, null, "La TNT s'active automatiquement lorsqu'elle est posée.", new ItemStack(Material.TNT)));
        this.modules.add(new RandomModule(BombersModule.class, null, "Ramassez de la TNT sur les cadavres et explosez vos adversaires !", new ItemStack(Material.FLINT_AND_STEEL)));
        this.modules.add(new RandomModule(DropMyEffectsModule.class, new DropMyEffectsModule.ConfigurationBuilder().blacklistPotionEffect(PotionEffectType.SPEED).build(), "Les effets se transforment en potions à votre mort.", new ItemStack(Material.POTION, 1, (short)8201)));
        this.modules.add(new RandomModule(KillForEnchantmentModule.class, null, "Les tables d'enchantement ne peuvent s'obtenir qu'en tuant vos ennemis.", new ItemStack(Material.ENCHANTMENT_TABLE)));
        this.modules.add(new RandomModule(KillToToggleTimeModule.class, null, "A chaque mort le temps change.", new ItemStack(Material.WATCH)));
        this.modules.add(new RandomModule(OneShootPassiveModule.class, null, "Les animaux meurent en un coup.", new ItemStack(Material.WOOD_SWORD)));
        this.modules.add(new RandomModule(ThreeArrowModule.class, null, "Vous tirez 3 flèches au lieu d'une.", new ItemStack(Material.BOW)));
        this.modules.add(new RandomModule(DisableFlintAndSteelModule.class, null, "Vous ne pouvez plus fabriquer de briquet.", new ItemStack(Material.FLINT)));
        this.modules.add(new RandomModule(DisableLevelTwoPotionModule.class, null, "Les potions de niveau 2 sont désactivées.", new ItemStack(Material.POTION, 1, (short)8262)));
        this.modules.add(new RandomModule(DisableNotchAppleModule.class, null, "Les pommes de Notch ne peuvent pas être obtenues.", new ItemStack(Material.GOLDEN_APPLE, 1, (short)1)));
        this.modules.add(new RandomModule(DisableSpeckedMelonModule.class, null, "Le melon scintillant est infabricable.", new ItemStack(Material.SPECKLED_MELON)));
        this.modules.add(new RandomModule(OneWorkbenchModule.class, null, "Vous ne pouvez créer qu'une seule table de craft.", new ItemStack(Material.WORKBENCH)));
        this.modules.add(new RandomModule(RapidToolsModule.class, new RapidToolsModule.ConfigurationBuilder().setToolsMaterial(RapidToolsModule.ConfigurationBuilder.ToolMaterial.IRON).build(), "Vos outils sont plus puissants.", new ItemStack(Material.IRON_PICKAXE)));
        this.modules.add(new RandomModule(InfestationModule.class, null, "Chaque mob tué a 40% de chances de ré-apparaître.", new ItemStack(Material.MONSTER_EGG, 1, (short)54)));
        this.modules.add(new RandomModule(AutomaticLapisModule.class, null, "Les tables d'enchantement n'ont pas besoin de lapis.", new ItemStack(Material.INK_SACK, 1, (short)4)));
        this.modules.add(new RandomModule(BloodDiamondModule.class, new BloodDiamondModule.ConfigurationBuilder().build(), "Chaque diamant miné vous ôtera un demi-coeur.", new ItemStack(Material.DIAMOND)));
        this.modules.add(new RandomModule(CatsEyesModule.class, null, "Vous voyez dans l'obscurité tel un chat.", new ItemStack(Material.EYE_OF_ENDER)));
        this.modules.add(new RandomModule(ConstantPotionModule.class, new ConstantPotionModule.ConfigurationBuilder().addPotionEffect(PotionEffectType.SPEED, 1).build(), "Votre vitesse est augmentée.", new ItemStack(Material.POTION, 1, (short)8194)));
        this.modules.add(new RandomModule(DoubleHealthModule.class, null, "Votre vie est doublée.", new ItemStack(Material.POTION, 1, (short)8229)));
        this.modules.add(new RandomModule(FastTreeModule.class, null, "Les arbres se cassent en un coup.", new ItemStack(Material.DIAMOND_AXE)));
        this.modules.add(new RandomModule(NineSlotsModule.class, null, "Votre inventaire n'a que 9 cases.", new ItemStack(Material.BARRIER)));
        this.modules.add(new RandomModule(PersonalBlocksModule.class, null, "Vos blocs seront protégés des autres joueurs.", new ItemStack(Material.CHEST)));
        this.modules.add(new RandomModule(RapidFoodModule.class, new RapidFoodModule.ConfigurationBuilder().addDefaults().build(), "Vous obtenez de la nourriture cuite sur les animaux.", new ItemStack(Material.COOKED_BEEF)));
        this.modules.add(new RandomModule(RapidUsefullModule.class, new RapidUsefullModule.ConfigurationBuilder().addDefaults().build(), "Vous obtenez des éléments utiles sur certains blocs.", new ItemStack(Material.GRAVEL)));
        this.modules.add(new RandomModule(RemoveItemOnUseModule.class, null, "Les bols disparaissent une fois bus.", new ItemStack(Material.MUSHROOM_SOUP)));
        this.modules.add(new RandomModule(RottenPotionsModule.class, null, "Manger de la chair de zombie vous donne un effet aléatoire.", new ItemStack(Material.ROTTEN_FLESH)));
        this.modules.add(new RandomModule(EntityDropModule.class, new EntityDropModule.ConfigurationBuilder().addCustomDrops(EntityType.ZOMBIE, new ItemStack(Material.FEATHER)).build(), "Les zombies donnent des plumes à leur mort.", new ItemStack(Material.FEATHER)));

        //StackableItemModule > Need explanations
        //RapidStackingModule > How to describe ?
        //DropMyEffectsModule > Config ?

        /** Random modules selector */
        Collections.shuffle(this.modules);
        this.enabledModules = new ArrayList<>();
        int modulesNumber = SamaGamesAPI.get().getGameManager().getGameProperties().getConfig("modulesNumber", new JsonPrimitive(5)).getAsInt();
        modulesNumber = Math.min(modulesNumber, this.modules.size());
        modulesNumber = Math.min(modulesNumber, 7); //GUI does not support more than 7 modules actually.
        getLogger().info("Selecting " + modulesNumber + " modules out of " + this.modules.size() + ".");
        for (int i = 0; i < modulesNumber; i++)
        {
            int rand = random.nextInt(this.modules.size());
            RandomModule entry = this.modules.get(rand);
            api.loadModule(entry.getModuleClass(), entry.getConfig());
            this.enabledModules.add(entry);
            this.modules.remove(entry);
        }
        getLogger().info("Random modules selected");

        /** Solo or team game, depending on config, and overriding of startGame function to display modules (will me moved to GameLoop later) */
        int nb = SamaGamesAPI.get().getGameManager().getGameProperties().getOption("playersPerTeam", new JsonPrimitive(1)).getAsInt();
        SurvivalGame game;
        if (nb > 1)
            game = new SurvivalTeamGame<>(this, "uhcrandom", "UHCRandom", "La chance sera-t-elle avec vous ?", "", UHCRandomGameLoop.class, nb);
        else
            game = new SurvivalSoloGame<>(this, "uhcrandom", "UHCRandom", "La chance sera-t-elle avec vous ?", "", UHCRandomGameLoop.class);

        api.unloadModule(RandomChestModule.class);

        SamaGamesAPI.get().getGameManager().setMaxReconnectTime(10);
        SamaGamesAPI.get().getGameManager().registerGame(game);
    }

    /**
     * Show modules GUI to players.
     * @param callback callback which be called after display's end
     */
    public void displayModulesGUI(Runnable callback)
    {
        new RandomGUI(this, this.modules, this.enabledModules, () -> {
            getServer().getOnlinePlayers().forEach(this::displayModules);
            callback.run();
        });
    }

    /**
     * Show modules in chat.
     * @param player the player to display modules.
     */
    public void displayModules(CommandSender player)
    {
        player.sendMessage(ChatColor.GOLD + "Liste des modifications de cette partie :");
        for (RandomModule mod : this.enabledModules)
            player.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + " + " + ChatColor.WHITE + mod.getDescription());
    }

    /**
     * Handle /modules
     * @param sender the one who send the command.
     * @param command the command class.
     * @param label the command name, here should always be "modules".
     * @param args the arguments of the command, ignored here.
     * @return In this case, always return true.
     */
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if (label.equals("modules"))
            displayModules(sender);
        return true;
    }
}
