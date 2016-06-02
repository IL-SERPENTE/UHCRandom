package net.samagames.uhcrandom;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import net.samagames.api.SamaGamesAPI;
import net.samagames.survivalapi.SurvivalAPI;
import net.samagames.survivalapi.game.SurvivalGame;
import net.samagames.survivalapi.game.types.SurvivalSoloGame;
import net.samagames.survivalapi.game.types.SurvivalTeamGame;
import net.samagames.survivalapi.game.types.run.RunBasedSoloGame;
import net.samagames.survivalapi.game.types.run.RunBasedTeamGame;
import net.samagames.survivalapi.modules.AbstractSurvivalModule;
import net.samagames.survivalapi.modules.block.*;
import net.samagames.survivalapi.modules.combat.*;
import net.samagames.survivalapi.modules.craft.*;
import net.samagames.survivalapi.modules.entity.*;
import net.samagames.survivalapi.modules.gameplay.*;
import net.samagames.tools.MojangShitUtils;
import net.samagames.tools.chat.fanciful.FancyMessage;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffectType;

import java.util.*;

public class UHCRandom extends JavaPlugin implements Listener
{
    private List<RandomModule> modules;
    private List<RandomModule> enabledModules;
    private List<IncompatibleModules> incompatibleModules;
    private boolean started;
    private boolean run;

    @Override
    public void onEnable()
    {
        SurvivalAPI api = SurvivalAPI.get();
        this.modules = new ArrayList<>();
        this.incompatibleModules = new ArrayList<>();
        List<RandomModule> generationModules = new ArrayList<>();
        this.started = false;
        Random random = new Random();

        /** Modules list */
        this.modules.add(new RandomModule(DiamondFlowerModule.class, null, "Les fleurs peuvent donner du diamant.", new ItemStack(Material.RED_ROSE)));
        this.modules.add(new RandomModule(HardObsidianModule.class, null, "L'obsidienne est plus dure à casser.", new ItemStack(Material.OBSIDIAN)));
        this.modules.add(new RandomModule(ParanoidModule.class, null, "Tout minage de diamant est annoncé aux autres joueurs.", new ItemStack(Material.DIAMOND_ORE)));
        this.modules.add(new RandomModule(RapidOresModule.class, new RapidOresModule.ConfigurationBuilder().addDefaults().build(), "Le minage vous rapporte plus de ressources.", new ItemStack(Material.GOLD_PICKAXE), false));
        this.modules.add(new RandomModule(TorchThanCoalModule.class, new TorchThanCoalModule.ConfigurationBuilder().build(), "Le charbon se transforme en torches.", new ItemStack(Material.TORCH), false));
        this.modules.add(new RandomModule(AutomaticTNTModule.class, null, "La TNT s'active automatiquement lorsqu'elle est posée.", new ItemStack(Material.TNT), false));
        this.modules.add(new RandomModule(BombersModule.class, null, "Ramassez de la TNT sur les cadavres et explosez vos adversaires !", new ItemStack(Material.FLINT_AND_STEEL)));
        this.modules.add(new RandomModule(DropMyEffectsModule.class, new DropMyEffectsModule.ConfigurationBuilder().blacklistPotionEffect(PotionEffectType.SPEED).build(), "Les effets se transforment en potions à votre mort.", new ItemStack(Material.POTION, 1, (short)8201), false));
        this.modules.add(new RandomModule(KillForEnchantmentModule.class, null, "Les tables d'enchantement ne peuvent s'obtenir qu'en tuant vos ennemis.", new ItemStack(Material.ENCHANTMENT_TABLE)));
        this.modules.add(new RandomModule(KillToToggleTimeModule.class, null, "A chaque mort l'heure change.", new ItemStack(Material.WATCH)));
        this.modules.add(new RandomModule(OneShootPassiveModule.class, null, "Les animaux meurent en un coup.", new ItemStack(Material.WOOD_SWORD)));
        this.modules.add(new RandomModule(DisableFlintAndSteelModule.class, null, "Vous ne pouvez plus fabriquer de briquet.", new ItemStack(Material.FLINT)));
        this.modules.add(new RandomModule(DisableSpeckedMelonModule.class, null, "Le melon scintillant est infabricable.", new ItemStack(Material.SPECKLED_MELON), false));
        this.modules.add(new RandomModule(OneWorkbenchModule.class, null, "Vous ne pouvez créer qu'une seule table de craft.", new ItemStack(Material.WORKBENCH)));
        this.modules.add(new RandomModule(RapidToolsModule.class, new RapidToolsModule.ConfigurationBuilder().setToolsMaterial(RapidToolsModule.ConfigurationBuilder.ToolMaterial.IRON).build(), "Vos outils sont plus puissants.", new ItemStack(Material.IRON_PICKAXE), false));
        this.modules.add(new RandomModule(InfestationModule.class, null, "Chaque mob tué a 40% de chances de ré-apparaître.", MojangShitUtils.getMonsterEgg(EntityType.SQUID)));
        this.modules.add(new RandomModule(AutomaticLapisModule.class, null, "Les tables d'enchantement n'ont pas besoin de lapis.", new ItemStack(Material.INK_SACK, 1, (short)4)));
        this.modules.add(new RandomModule(BloodDiamondModule.class, new BloodDiamondModule.ConfigurationBuilder().build(), "Chaque diamant miné vous ôtera un demi-coeur.", new ItemStack(Material.DIAMOND)));
        this.modules.add(new RandomModule(CatsEyesModule.class, null, "Vous voyez dans l'obscurité tel un chat.", new ItemStack(Material.EYE_OF_ENDER)));
        this.modules.add(new RandomModule(ConstantPotionModule.class, new ConstantPotionModule.ConfigurationBuilder().addPotionEffect(PotionEffectType.SPEED, 1).build(), "Votre vitesse est augmentée.", new ItemStack(Material.POTION, 1, (short)8194), false));
        this.modules.add(new RandomModule(DoubleHealthModule.class, null, "Votre vie est doublée.", new ItemStack(Material.POTION, 1, (short)8229)));
        this.modules.add(new RandomModule(FastTreeModule.class, null, "Les arbres se cassent en un coup.", new ItemStack(Material.DIAMOND_AXE), false));
        this.modules.add(new RandomModule(NineSlotsModule.class, null, "Votre inventaire n'a que 9 cases.", new ItemStack(Material.BARRIER)));
        this.modules.add(new RandomModule(PersonalBlocksModule.class, null, "Vos blocs seront protégés des autres joueurs.", new ItemStack(Material.CHEST), false));
        this.modules.add(new RandomModule(RapidFoodModule.class, new RapidFoodModule.ConfigurationBuilder().addDefaults().build(), "Les loots des animaux sont augmentés.", new ItemStack(Material.COOKED_BEEF), false));
        this.modules.add(new RandomModule(RapidUsefullModule.class, new RapidUsefullModule.ConfigurationBuilder().addDefaults().build(), "Vous obtenez des éléments utiles sur certains blocs.", new ItemStack(Material.STRING), false));
        this.modules.add(new RandomModule(RemoveItemOnUseModule.class, null, "Les bols disparaissent une fois bus.", new ItemStack(Material.MUSHROOM_SOUP), false));
        this.modules.add(new RandomModule(RottenPotionsModule.class, null, "Manger de la chair de zombie vous donne un effet aléatoire.", new ItemStack(Material.ROTTEN_FLESH)));
        this.modules.add(new RandomModule(EntityDropModule.class, new EntityDropModule.ConfigurationBuilder().addCustomDrops(EntityType.ZOMBIE, new ItemStack(Material.FEATHER)).build(), "Les zombies donnent des plumes à leur mort.", new ItemStack(Material.FEATHER)));
        this.modules.add(new RandomModule(LightsOutModule.class, null, "Vous ne pouvez pas poser de torches.", new ItemStack(Material.REDSTONE_TORCH_ON)));
        this.modules.add(new RandomModule(CocoaEffectsModule.class, null, "Vous avez 5 graines de cacao vous donnant des effets.", new ItemStack(Material.INK_SACK, 1, (short)3)));
        this.modules.add(new RandomModule(EveryRoseModule.class, null, "Vous obtenez un plastron en or Thorns III.", new ItemStack(Material.GOLD_CHESTPLATE)));
        this.modules.add(new RandomModule(GoneFishingModule.class, null, "Vous obtenez une canne à pêche très efficace, et vous enchantez à l'infini.", new ItemStack(Material.FISHING_ROD)));
        this.modules.add(new RandomModule(InfiniteEnchanterModule.class, null, "Vous pouvez enchanter à volonté.", new ItemStack(Material.EXP_BOTTLE)));
        this.modules.add(new RandomModule(NightmareModule.class, null, "Il fait toujours nuit.", new ItemStack(Material.STAINED_GLASS_PANE, 1, (short)15)));
        this.modules.add(new RandomModule(PainfullStonesModule.class, null, "Mettez des bottes, le gravier vous pique les pieds.", new ItemStack(Material.GRAVEL)));
        this.modules.add(new RandomModule(NoSwordModule.class, null, "Vous ne pouvez plus créer d'épée.", new ItemStack(Material.STONE_SWORD)));
        this.modules.add(new RandomModule(TheHobbitModule.class, null, "Utilisez la puissance de l'anneau pour vous rendre invisible.", new ItemStack(Material.GOLD_NUGGET)));
        this.modules.add(new RandomModule(PopeyeModule.class, null, "Mangez des épinards, c'est bon pour la santé.", new ItemStack(Material.INK_SACK, 1, (short)2)));
        this.modules.add(new RandomModule(PyroTechnicsModule.class, null, "Chaque dégats vous emflammera.", new ItemStack(Material.LAVA_BUCKET)));
        this.modules.add(new RandomModule(PuppyPowerModule.class, null, "Devenez dresseur de loups !", MojangShitUtils.getMonsterEgg(EntityType.WOLF)));
        this.modules.add(new RandomModule(ChickenModule.class, null, "Vous commencez avec 1 coeur et demi et une pomme de Notch.", new ItemStack(Material.RAW_CHICKEN)));
        this.modules.add(new RandomModule(GapZapModule.class, null, "Vous perdez votre regénération si vous prenez du dégat.", new ItemStack(Material.POTION, 1, (short)8193)));
        this.modules.add(new RandomModule(HighwayToHellModule.class, null, "Vous êtes équipé pour l'enfer.", new ItemStack(Material.NETHER_BRICK), false));
        this.modules.add(new RandomModule(SuperheroesModule.class, null, "Vous devenez un super-héros.", new ItemStack(Material.BANNER, 1, (short)1)));
        this.modules.add(new RandomModule(SuperheroesPlusModule.class, null, "Vous devenez plus fort qu'un super-héros.", new ItemStack(Material.BANNER, 1, (short)0)));
        this.modules.add(new RandomModule(SpeedSwapModule.class, null, "A chaque mort, un effet de vitesse ou de lenteur sera donné.", new ItemStack(Material.POTION, 1, (short)16450)));
        this.modules.add(new RandomModule(VengefulSpiritsModule.class, null, "Un ghast ou un blaze apparait à chaque mort de joueur.", new ItemStack(Material.GHAST_TEAR)));
        this.modules.add(new RandomModule(ZombiesModule.class, null, "Vous devenez un zombie à votre mort.", MojangShitUtils.getMonsterEgg(EntityType.ZOMBIE)));
        this.modules.add(new RandomModule(KillSwitchModule.class, null, "Quand vous tuez un joueur, vous prenez son inventaire.", new ItemStack(Material.TRAPPED_CHEST)));
        this.modules.add(new RandomModule(MobOresModule.class, null, "Chaque minerai miné peut faire apparaitre un monstre.", new ItemStack(Material.IRON_ORE)));
        this.modules.add(new RandomModule(OneHealModule.class, null, "Vous obtenez une houe vous permettant de vous soigner.", new ItemStack(Material.GOLD_HOE)));
        this.modules.add(new RandomModule(PotentialHeartsModule.class, null, "Vous avez 20 coeurs maximum, mais seulement 10 au départ.", new ItemStack(Material.POTION, 1, (short)8257)));
        this.modules.add(new RandomModule(SwitcherooModule.class, null, "Vous échangez votre place avec votre adversaire si vous le touchez à l'arc.", new ItemStack(Material.ARROW)));
        this.modules.add(new RandomModule(InventorsModule.class, null, "Chaque fabrication d'outil en diamant est annoncée.", new ItemStack(Material.STICK)));
        //this.modules.add(new RandomModule(NinjanautModule.class, null, "Un joueur est choisi pour être plus fort que les autres.", new ItemStack(Material.DIAMOND_CHESTPLATE)));
        this.modules.add(new RandomModule(ThreeArrowModule.class, null, "Vous tirez 3 flèches à la fois", new ItemStack(Material.ARROW, 3)));
        //this.modules.add(new RandomModule(RiskyRetrievalModule.class, null, "Chaque minerai miné est dupliqué dans un coffre au milieu du monde.", new ItemStack(Material.ENDER_CHEST)));
        this.modules.add(new RandomModule(StockupModule.class, null, "A chaque mort, vous gagnez un demi-coeur d'absorption.", new ItemStack(Material.IRON_CHESTPLATE)));
        this.modules.add(new RandomModule(MeleeFunModule.class, null, "Vous pouvez taper vos adversaires aussi vite que possible.", new ItemStack(Material.IRON_SWORD)));
        this.modules.add(new RandomModule(SpawnEggsModule.class, null, "Lancer un oeuf fait spawn un mob aléatoire.", new ItemStack(Material.EGG)));
        this.modules.add(new RandomModule(NoBowModule.class, null, "Vous ne pouvez plus fabriquer d'arc.", new ItemStack(Material.BOW)));
        this.modules.add(new RandomModule(ElytraModule.class, null, "Envolez vous vers la victoire !", new ItemStack(Material.ELYTRA)));

        generationModules.add(new RandomModule(GenerationModule.class, "bigcrack", "Une faille coupe le monde en deux", new ItemStack(Material.GRASS)));
        generationModules.add(new RandomModule(GenerationModule.class, "chunkapocalypse", "Chaque chunk a 30% de chance d'être remplacé par de l'air", new ItemStack(Material.GRASS)));

        /** Incompatibles modules list */
        this.incompatibleModules.add(new IncompatibleModules(ChickenModule.class, DoubleHealthModule.class, SuperheroesModule.class, SuperheroesPlusModule.class, PotentialHeartsModule.class, NinjanautModule.class, ConstantPotionModule.class));
        this.incompatibleModules.add(new IncompatibleModules(VengefulSpiritsModule.class, ZombiesModule.class));
        this.incompatibleModules.add(new IncompatibleModules(InfiniteEnchanterModule.class, KillForEnchantmentModule.class));
        this.incompatibleModules.add(new IncompatibleModules(ElytraModule.class, EveryRoseModule.class));
        this.incompatibleModules.add(new IncompatibleModules(SuperheroesModule.class, SuperheroesPlusModule.class, PyroTechnicsModule.class));
        this.incompatibleModules.add(new IncompatibleModules(GoneFishingModule.class, InfiniteEnchanterModule.class));
        this.incompatibleModules.add(new IncompatibleModules(NoBowModule.class, ThreeArrowModule.class));

        /** Always present modules */
        api.loadModule(DisableNotchAppleModule.class, null);
        api.loadModule(LoveMachineModule.class, null);
        api.loadModule(DisableLevelTwoPotionModule.class, null);
        api.loadModule(OneShieldModule.class, null);

        /** Random modules selector */
        JsonElement jsonArray = SamaGamesAPI.get().getGameManager().getGameProperties().getConfig("modules", null);
        if (jsonArray == null)
        {
            Collections.shuffle(this.modules);
            this.enabledModules = new ArrayList<>();
            int modulesNumber = SamaGamesAPI.get().getGameManager().getGameProperties().getConfig("modulesNumber", new JsonPrimitive(7)).getAsInt();
            this.run = SamaGamesAPI.get().getGameManager().getGameProperties().getConfig("run", new JsonPrimitive(false)).getAsBoolean();
            modulesNumber = Math.min(modulesNumber, this.modules.size());
            modulesNumber = Math.min(modulesNumber, 28); //GUI does not support more than 28 modules actually.
            getLogger().info("Selecting " + modulesNumber + " modules out of " + (this.modules.size() + generationModules.size()) + ".");
            int i = 0;
            while (i < modulesNumber) {
                int rand = random.nextInt(this.modules.size() + generationModules.size());
                RandomModule entry = (rand < this.modules.size() ? this.modules.get(rand) : generationModules.get(rand - this.modules.size()));
                if ((!this.run || entry.isRunModule()) && isModuleIncompatibleWithOther(entry.getModuleClass())) {
                    api.loadModule(entry.getModuleClass(), entry.getConfig());
                    this.enabledModules.add(entry);
                    if (rand < this.modules.size())
                        this.modules.remove(entry);
                    else
                        generationModules.clear();
                    i++;
                }
            }
            getLogger().info("Random modules selected");
        }
        else
            this.loadModulesFromConfig(api, jsonArray, generationModules);

        /** Solo or team game, depending on config */
        int nb = SamaGamesAPI.get().getGameManager().getGameProperties().getOption("playersPerTeam", new JsonPrimitive(1)).getAsInt();
        SurvivalGame game;
        if (run)
        {
            if (nb > 1)
                game = new RunBasedTeamGame<>(this, "randomrun", "RandomRun", "La chance sera-t-elle avec vous ?", "", RandomRunGameLoop.class, nb);
            else
                game = new RunBasedSoloGame<>(this, "randomrun", "RandomRun", "La chance sera-t-elle avec vous ?", "", RandomRunGameLoop.class);
        }
        else
        {
            if (nb > 1)
                game = new SurvivalTeamGame<>(this, "uhcrandom", "UHCRandom", "La chance sera-t-elle avec vous ?", "", UHCRandomGameLoop.class, nb);
            else
                game = new SurvivalSoloGame<>(this, "uhcrandom", "UHCRandom", "La chance sera-t-elle avec vous ?", "", UHCRandomGameLoop.class);
            api.unloadModule(RandomChestModule.class);
        }

        SamaGamesAPI.get().getGameManager().setMaxReconnectTime(10);
        SamaGamesAPI.get().getGameManager().registerGame(game);
        getServer().getPluginManager().registerEvents(this, this);
    }

    /**
     * Show modules GUI to players.
     * @param callback callback which be called after display's end
     */
    public void displayModulesGUI(Callback callback)
    {
        RandomGUI gui = new RandomGUI(this, this.modules, this.enabledModules, () -> {
            getServer().getOnlinePlayers().forEach(this::displayModules);
            this.started = true;
            callback.run();
        });
        gui.getClass();//FUCK SONAR.
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
        if (player instanceof Player)
            new FancyMessage("Tapez ").color(ChatColor.GOLD).then("/modules").command("/modules").color(ChatColor.AQUA).style(ChatColor.BOLD).then(" dans le chat pour revoir cette liste.").color(ChatColor.GOLD).send((Player)player);
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
        if ("modules".equals(label) || "module".equals(label))
        {
            if (this.started)
                displayModules(sender);
            else
                sender.sendMessage(ChatColor.RED + "La partie n'a pas encore démarré.");
        }
        if (!"enablemodule".equals(label) || !sender.hasPermission("uhcrandom.enablemodule") || args.length != 1)
            return true;
        if (this.started)
        {
            sender.sendMessage(ChatColor.RED + "La partie a déjà démarré.");
            return true;
        }
        handleEnableModuleCommand(sender, args);
        return true;
    }

    /**
     * Handle /enableModule, I put it here instead of making a CommandExecutor ...
     */
    private void handleEnableModuleCommand(CommandSender sender, String[] args)
    {
        for (Iterator<RandomModule> it = this.modules.iterator(); it.hasNext();)
        {
            RandomModule module = it.next();
            if (module.getName().equalsIgnoreCase(args[0]))
            {
                if (isModuleIncompatibleWithOther(module.getModuleClass()))
                {
                    SurvivalAPI.get().loadModule(module.getModuleClass(), module.getConfig());
                    this.enabledModules.add(module);
                    it.remove();
                    sender.sendMessage(ChatColor.GREEN + "Module " + module.getName() + " chargé.");
                }
                else
                    sender.sendMessage(ChatColor.RED + "Module incompatible avec un module déjà présent.");
                return ;
            }
        }
        sender.sendMessage(ChatColor.RED + "Module non trouvé ou déjà chargé.");

    }

    private boolean isModuleIncompatibleWithOther(Class<? extends AbstractSurvivalModule> moduleClass)
    {
        boolean ok = true;
        for (RandomModule module2 : this.enabledModules)
            for (IncompatibleModules incompatibleModule : this.incompatibleModules)
                ok = ok && !incompatibleModule.areIncompatibles(module2.getModuleClass(), moduleClass);
        return ok;
    }

    /**
     * Cancel click on RandomGUI
     * @param event The Event to be cancelled
     */
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event)
    {
        if (event.getClickedInventory() != null && (event.getClickedInventory().getName().equals(RandomGUI.INVNAME) || event.getClickedInventory().getName().equals(RandomGUI.INVNAME_RUN)))
            event.setCancelled(true);
    }

    /**
     * Get if this game is RandomRun
     * @return true if RandomRun, false otherwise
     */
    public boolean isRun()
    {
        return run;
    }

    private void loadModulesFromConfig(SurvivalAPI api, JsonElement element, List<RandomModule> generationModules)
    {
        List<RandomModule> list = new ArrayList<>(this.modules);
        list.addAll(generationModules);
        JsonArray array = element.getAsJsonArray();
        array.forEach(element2 ->
        {
            String moduleName = element2.getAsString();
            for (RandomModule entry : list)
            {
                if (entry.getName().equalsIgnoreCase(moduleName))
                {
                    if (!this.enabledModules.contains(entry) && (!this.run || entry.isRunModule()) && isModuleIncompatibleWithOther(entry.getModuleClass()))
                    {
                        api.loadModule(entry.getModuleClass(), entry.getConfig());
                        this.enabledModules.add(entry);
                        break ;
                    }
                }
            }
        });
    }
}
