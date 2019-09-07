package tim03we.gungame;

/*
 * Copyright (c) 2019 tim03we  < https://github.com/tim03we >
 * Discord: tim03we | TP#9129
 *
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
import cn.nukkit.item.Item;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.Config;
import tim03we.gungame.Events.*;
import tim03we.gungame.Task.GGTask;
import tim03we.gungame.Task.LoadKits;

import java.util.HashMap;

public class Main extends PluginBase {

    public HashMap<String, Integer> levels = new HashMap<String, Integer>();
    public HashMap<String, Integer> needLevel = new HashMap<String, Integer>();

    public Settings settings = new Settings();
    public Kits kits = new Kits();
    public Integer change_max = settings.MAX_KITS;

    @Override
    public void onLoad()
    {
        for (Player player: this.getServer().getOnlinePlayers().values()) {
            this.levels.put(player.getName(), 0);
            this.needLevel.put(player.getName(), 0);
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

    public void levelChange(Player player)
    {
        this.invClear(player);
        this.needLevel.put(player.getName(), 0);
        int currLevel = this.levels.get(player.getName());
        player.setHealth(20);
        player.setNameTag(settings.NAMETAG_FORMAT.replace("{player}", player.getName()).replace("{level}", String.valueOf(currLevel)));
        if(settings.MAX_KITS < currLevel) {
            Item helmet = Item.get(kits.helmet.get(settings.MAX_KITS));
            Item chestplate = Item.get(kits.chestplate.get(settings.MAX_KITS));
            Item leggings = Item.get(kits.leggings.get(settings.MAX_KITS));
            Item boots = Item.get(kits.boots.get(settings.MAX_KITS));
            Item weapon = Item.get(kits.weapons.get(settings.MAX_KITS));
            player.getInventory().setHelmet(helmet);
            player.getInventory().setChestplate(chestplate);
            player.getInventory().setLeggings(leggings);
            player.getInventory().setBoots(boots);
            player.getInventory().setItem(0, weapon);
        } else {
            Item helmet = Item.get(kits.helmet.get(currLevel));
            Item chestplate = Item.get(kits.chestplate.get(currLevel));
            Item leggings = Item.get(kits.leggings.get(currLevel));
            Item boots = Item.get(kits.boots.get(currLevel));
            Item weapon = Item.get(kits.weapons.get(currLevel));
            player.getInventory().setHelmet(helmet);
            player.getInventory().setChestplate(chestplate);
            player.getInventory().setLeggings(leggings);
            player.getInventory().setBoots(boots);
            player.getInventory().setItem(0, weapon);
        }
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
            this.getServer().getPluginManager().registerEvents(new QuitListener(this), this);
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
        for (String list : new Config(this.getDataFolder() + "/level.yml", Config.YAML).getStringList("other-items")) {
            settings.OTHER_ITEMS.add(list);
        }
        settings.GAMEMODE = this.getConfig().getInt("gamemode");
        settings.CHAT_FORMAT = this.getConfig().getString("format.chat");
        settings.NAMETAG_FORMAT = this.getConfig().getString("format.nametag");
        settings.PVP_RADIUS = this.getConfig().getInt("pvp-radius");
        settings.MAX_KITS = this.getConfig().getInt("Maximum-Level");
        settings.SAVE_INVENTORY = this.getConfig().getBoolean("save-level");
        settings.USE_OTHER_ITEMS = this.getConfig().getBoolean("use-other-items");
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
            this.levels.put(player.getName(), 0);
        } else {
            this.levels.put(player.getName(), nL);
        }
    }
}