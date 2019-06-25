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

import java.util.HashMap;

public class Main extends PluginBase {

    public HashMap<String, Integer> levels = new HashMap<String, Integer>();
    public HashMap<String, Integer> needLevel = new HashMap<String, Integer>();

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
        this.register();
        this.getServer().getScheduler().scheduleRepeatingTask(new GGTask(this), 20);
        this.saveResource("level.yml");
        this.saveResource("config.yml");

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

    public void levelChange(Player player, int level)
    {
        Config lcfg = new Config(this.getDataFolder() + "/level.yml", Config.YAML);
        this.invClear(player);
        this.needLevel.put(player.getName(), 0);
        int currLevel = this.levels.get(player.getName());
        player.setHealth(20);
        player.setNameTag(String.valueOf(this.getConfig().get("format.nametag")).replace("{player}", player.getName()).replace("{level}", String.valueOf(currLevel)));
        if(this.getConfig().getInt("Maximum-Level") < currLevel) {
            player.sendMessage(this.getConfig().getString("messages.max"));
            Item helmet = Item.get(lcfg.getInt("L" + this.getConfig().getString("Maximum-Level") + ".helmet.id"));
            Item chestplate = Item.get(lcfg.getInt("L" + this.getConfig().getString("Maximum-Level") + ".chestplate.id"));
            Item leggings = Item.get(lcfg.getInt("L" + this.getConfig().getString("Maximum-Level") + ".leggings.id"));
            Item boots = Item.get(lcfg.getInt("L" + this.getConfig().getString("Maximum-Level") + ".boots.id"));
            Item weapon = Item.get(lcfg.getInt("L" + this.getConfig().getString("Maximum-Level") + ".weapon.id"));
            player.getInventory().setHelmet(helmet);
            player.getInventory().setChestplate(chestplate);
            player.getInventory().setLeggings(leggings);
            player.getInventory().setBoots(boots);
            player.getInventory().setItem(0, weapon);
        } else {
            Item helmet = Item.get(lcfg.getInt("L" + currLevel + ".helmet.id"));
            Item chestplate = Item.get(lcfg.getInt("L" + currLevel + ".chestplate.id"));
            Item leggings = Item.get(lcfg.getInt("L" + currLevel + ".leggings.id"));
            Item boots = Item.get(lcfg.getInt("L" + currLevel + ".boots.id"));
            Item weapon = Item.get(lcfg.getInt("L" + currLevel + ".weapon.id"));
            player.getInventory().setHelmet(helmet);
            player.getInventory().setChestplate(chestplate);
            player.getInventory().setLeggings(leggings);
            player.getInventory().setBoots(boots);
            player.getInventory().setItem(0, weapon);
        }
    }

    public void register()
    {
        this.getLogger().notice("Events registered.");
        this.getServer().getPluginManager().registerEvents(new ChatListener(this), this);
        this.getServer().getPluginManager().registerEvents(new HungerListener(this), this);
        this.getServer().getPluginManager().registerEvents(new LogListener(this), this);
        this.getServer().getPluginManager().registerEvents(new BlockListener(this), this);
        this.getServer().getPluginManager().registerEvents(new EntityListener(this), this);
        this.getServer().getPluginManager().registerEvents(new InventoryListener(this), this);
        this.getServer().getPluginManager().registerEvents(new RespawnListener(this), this);
        this.getServer().getPluginManager().registerEvents(new DropListener(this), this);
    }

    public void levelUp(Player player)
    {
        this.levels.put(player.getName(), this.levels.get(player.getName()) + 1);
        this.needLevel.put(player.getName(), 1);
    }

    public void levelDown(Player player)
    {
        int cL = this.levels.get(player.getName());
        int nL = Math.round(cL * this.getConfig().getInt("Chance"));
        this.levels.put(player.getName(), nL);
    }
}