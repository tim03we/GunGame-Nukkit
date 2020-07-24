package tim03we.gungame;

/*
 * This software is distributed under "GNU General Public License v3.0".
 * This license allows you to use it and/or modify it but you are not at
 * all allowed to sell this plugin at any cost. If found doing so the
 * necessary action required would be taken.
 *
 * GunGame is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License v3.0 for more details.
 *
 * You should have received a copy of the GNU General Public License v3.0
 * along with this program. If not, see
 * <https://opensource.org/licenses/GPL-3.0>.
 */

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.item.Item;
import cn.nukkit.level.GameRule;
import cn.nukkit.level.Location;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.Config;
import tim03we.gungame.listener.*;
import tim03we.gungame.task.GGTask;
import tim03we.gungame.task.LoadKits;

import java.util.HashMap;

public class GunGame extends PluginBase {

    public HashMap<String, Integer> levels = new HashMap<String, Integer>();
    public HashMap<String, Integer> needLevel = new HashMap<String, Integer>();

    public Settings settings = new Settings();
    public Kits kits = new Kits();
    public Integer change_max = settings.MAX_KITS;

    @Override
    public void onLoad()
    {
        for (Player player: this.getServer().getOnlinePlayers().values()) {
            this.levels.put(player.getName(), 1);
            this.needLevel.put(player.getName(), 1);
            player.sendMessage(this.getConfig().getString("messages.reload"));
        }
    }

    @Override
    public void onEnable()
    {
        this.saveResource("level.yml");
        this.saveResource("config.yml");
        this.loadSettings();
        this.loadEvents();
        change_max = settings.MAX_KITS;
        this.getServer().getScheduler().scheduleRepeatingTask(new GGTask(this), 10);
        this.getServer().getScheduler().scheduleRepeatingTask(new LoadKits(this), 1);

        this.getServer().getDefaultLevel().getGameRules().setGameRule(GameRule.DO_IMMEDIATE_RESPAWN, false);
    }

    public void invClear(Player player)
    {
        player.getInventory().setHelmet(Item.get(Item.AIR));
        player.getInventory().setChestplate(Item.get(Item.AIR));
        player.getInventory().setLeggings(Item.get(Item.AIR));
        player.getInventory().setBoots(Item.get(Item.AIR));
        player.getCursorInventory().clearAll();
        player.getInventory().clearAll();
    }

    public Location getSpawnPos() {
        return new Location(getConfig().getDouble("spawn.x"), getConfig().getDouble("spawn.y"), getConfig().getDouble("spawn.z"),
                Server.getInstance().getLevelByName(getConfig().getString("spawn.world")));
    }

    public void levelChange(Player player)
    {
        this.invClear(player);
        this.needLevel.put(player.getName(), 0);
        int currLevel = this.levels.get(player.getName());
        player.setHealth(20);
        player.setNameTag(settings.NAMETAG_FORMAT.replace("{player}", player.getName()).replace("{level}", String.valueOf(currLevel)));
        Item helmet;
        Item chestplate;
        Item leggings;
        Item boots;
        Item weapon;
        if(settings.MAX_KITS < currLevel) {
            helmet = Item.get(kits.helmet.get(settings.MAX_KITS));
            chestplate = Item.get(kits.chestplate.get(settings.MAX_KITS));
            leggings = Item.get(kits.leggings.get(settings.MAX_KITS));
            boots = Item.get(kits.boots.get(settings.MAX_KITS));
            weapon = Item.get(kits.weapons.get(settings.MAX_KITS));
        } else {
            helmet = Item.get(kits.helmet.get(currLevel));
            chestplate = Item.get(kits.chestplate.get(currLevel));
            leggings = Item.get(kits.leggings.get(currLevel));
            boots = Item.get(kits.boots.get(currLevel));
            weapon = Item.get(kits.weapons.get(currLevel));
        }
        player.getInventory().setHelmet(helmet);
        player.getInventory().setChestplate(chestplate);
        player.getInventory().setLeggings(leggings);
        player.getInventory().setBoots(boots);
        player.getInventory().setItem(0, weapon);
        if(settings.USE_OTHER_ITEMS) {
            for(String list : settings.OTHER_ITEMS) {
                String[] get = list.split(":");
                if(player.getInventory().canAddItem(Item.get(Integer.parseInt(get[0]), Integer.parseInt(get[1]), Integer.parseInt(get[2])))) {
                    player.getInventory().addItem(Item.get(Integer.parseInt(get[0]), Integer.parseInt(get[1]), Integer.parseInt(get[2])));
                }
            }
        }
    }

    private void loadEvents()
    {
        if(settings.CHAT_EVENT) {
            this.getServer().getLogger().notice("Chat Listener activated.");
            this.getServer().getPluginManager().registerEvents(new ChatListener(this), this);
        }
        if(settings.HUNGER_EVENT) {
            this.getServer().getLogger().notice("Hunger Listener activated.");
            this.getServer().getPluginManager().registerEvents(new HungerListener(this), this);
        }
        if(settings.BREAK_EVENT) {
            this.getServer().getLogger().notice("Break Listener activated.");
            this.getServer().getPluginManager().registerEvents(new BreakListener(this), this);
        }
        if(settings.PLACE_EVENT) {
            this.getServer().getLogger().notice("Place Listener activated.");
            this.getServer().getPluginManager().registerEvents(new PlaceListener(this), this);
        }
        if(settings.INV_MOVE_EVENT) {
            this.getServer().getLogger().notice("Inventory Move Listener activated.");
            this.getServer().getPluginManager().registerEvents(new InventoryListener(this), this);
        }
        if(settings.DROP_EVENT) {
            this.getServer().getLogger().notice("Drop Listener activated.");
            this.getServer().getPluginManager().registerEvents(new DropListener(this), this);
        }
        if(settings.RESPAWN_EVENT) {
            this.getServer().getLogger().notice("Respawn Listener activated.");
            this.getServer().getPluginManager().registerEvents(new RespawnListener(this), this);
        }
        if(settings.LOGIN_EVENT) {
            this.getServer().getLogger().notice("Login Listener activated.");
            this.getServer().getPluginManager().registerEvents(new LoginListener(this), this);
        }
        if(settings.JOIN_EVENT) {
            this.getServer().getLogger().notice("Join Listener activated.");
            this.getServer().getPluginManager().registerEvents(new JoinListener(this), this);
        }
        if(settings.QUIT_EVENT) {
            this.getServer().getLogger().notice("Quit Listener activated.");
            this.getServer().getPluginManager().registerEvents(new QuitListener(this), this);
        }
        if(settings.DEATH_EVENT) {
            this.getServer().getLogger().notice("Death Listener activated.");
            this.getServer().getPluginManager().registerEvents(new DeathListener(this), this);
        }
        if(settings.MOVE_EVENT) {
            this.getServer().getLogger().notice("Move Listener activated.");
            this.getServer().getPluginManager().registerEvents(new MoveListener(this), this);
        }
        if(settings.DAMAGE_EVENT) {
            this.getServer().getLogger().notice("Damage Listener activated.");
            this.getServer().getPluginManager().registerEvents(new DamageListener(this), this);
        }
        if(settings.FIGHT_EVENT) {
            this.getServer().getLogger().notice("Fight Listener activated.");
            this.getServer().getPluginManager().registerEvents(new FightListener(this), this);
        }
    }

    public void loadSettings() {
        settings.USE_OTHER_ITEMS = this.getConfig().getBoolean("use-other-items");
        if(settings.USE_OTHER_ITEMS) {
            settings.OTHER_ITEMS = new Config(this.getDataFolder() + "/level.yml", Config.YAML).getStringList("other-items");
        }
        settings.GAMEMODE = this.getConfig().getInt("gamemode");
        settings.CHAT_FORMAT = this.getConfig().getString("format.chat");
        settings.NAMETAG_FORMAT = this.getConfig().getString("format.nametag");
        settings.PVP_RADIUS = this.getConfig().getInt("pvp-radius");
        settings.MAX_KITS = this.getConfig().getInt("Maximum-Level");
        settings.SAVE_INVENTORY = this.getConfig().getBoolean("save-level");
        settings.BREAK_EVENT = this.getConfig().getBoolean("events.load.break");
        settings.CHAT_EVENT = this.getConfig().getBoolean("events.load.chat");
        settings.DAMAGE_EVENT = this.getConfig().getBoolean("events.load.damage");
        settings.DEATH_EVENT = this.getConfig().getBoolean("events.load.death");
        settings.DROP_EVENT = this.getConfig().getBoolean("events.load.drop");
        settings.HUNGER_EVENT = this.getConfig().getBoolean("events.load.hunger");
        settings.INV_MOVE_EVENT = this.getConfig().getBoolean("events.load.inv-move");
        settings.JOIN_EVENT = this.getConfig().getBoolean("events.load.join");
        settings.LOGIN_EVENT = this.getConfig().getBoolean("events.load.login");
        settings.MOVE_EVENT = this.getConfig().getBoolean("events.load.move");
        settings.PLACE_EVENT = this.getConfig().getBoolean("events.load.place");
        settings.QUIT_EVENT = this.getConfig().getBoolean("events.load.quit");
        settings.RESPAWN_EVENT = this.getConfig().getBoolean("events.load.respawn");
        settings.FIGHT_EVENT = this.getConfig().getBoolean("events.load.fight");
        this.getLogger().notice(" ");
        this.getLogger().notice("Settings:");
        this.getLogger().notice("Use other items: " + settings.USE_OTHER_ITEMS);
        this.getLogger().notice("Gamemode: " + settings.GAMEMODE);
        this.getLogger().notice("PvP Radius Protection: " + settings.PVP_RADIUS);
        this.getLogger().notice("Max Level: " + settings.MAX_KITS);
        this.getLogger().notice("Save Inventory: " + settings.SAVE_INVENTORY);
        this.getLogger().notice("Break Event: " + settings.BREAK_EVENT);
        this.getLogger().notice("Chat Event: " + settings.CHAT_EVENT);
        this.getLogger().notice("Damage Event: " + settings.DAMAGE_EVENT);
        this.getLogger().notice("Death Event: " + settings.DEATH_EVENT);
        this.getLogger().notice("Drop Event: " + settings.DROP_EVENT);
        this.getLogger().notice("Hunger Event: " + settings.HUNGER_EVENT);
        this.getLogger().notice("Inv Move Event: " + settings.INV_MOVE_EVENT);
        this.getLogger().notice("Join Event: " + settings.JOIN_EVENT);
        this.getLogger().notice("Login Event: " + settings.LOGIN_EVENT);
        this.getLogger().notice("Move Event: " + settings.MOVE_EVENT);
        this.getLogger().notice("Place Event: " + settings.PLACE_EVENT);
        this.getLogger().notice("Quit Event: " + settings.QUIT_EVENT);
        this.getLogger().notice("Respawn Event: " + settings.RESPAWN_EVENT);
        this.getLogger().notice("Fight Event: " + settings.FIGHT_EVENT);
        this.getLogger().notice(" ");
    }

    public void levelUp(Player player)
    {
        this.levels.put(player.getName(), this.levels.get(player.getName()) + 1);
        this.needLevel.put(player.getName(), 1);
    }

    public void levelDown(Player player)
    {
        int cL = this.levels.get(player.getName());
        int nL = (int) (levels.get(player.getName()) - Math.round(cL / 0.60 / 2 / 3));
        if(nL == 0 || nL < 0) {
            this.levels.put(player.getName(), 1);
        } else {
            this.levels.put(player.getName(), nL);
        }
    }
}
