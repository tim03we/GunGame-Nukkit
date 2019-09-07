package tim03we.gungame.Task;

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

import cn.nukkit.scheduler.Task;
import cn.nukkit.utils.Config;
import tim03we.gungame.Main;

public class LoadKits extends Task {

    private Main plugin;

    public LoadKits(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public void onRun(int i) {
        Config lcfg = new Config(this.plugin.getDataFolder() + "/level.yml", Config.YAML);
        plugin.kits.helmet.put(plugin.change_max, lcfg.getInt("L" + plugin.change_max + ".helmet.id"));
        plugin.kits.chestplate.put(plugin.change_max, lcfg.getInt("L" + plugin.change_max + ".chestplate.id"));
        plugin.kits.leggings.put(plugin.change_max, lcfg.getInt("L" + plugin.change_max + ".leggings.id"));
        plugin.kits.boots.put(plugin.change_max, lcfg.getInt("L" + plugin.change_max + ".boots.id"));
        plugin.kits.weapons.put(plugin.change_max, lcfg.getInt("L" + plugin.change_max + ".weapon.id"));
        plugin.getServer().getLogger().notice("Kit " + plugin.change_max + " loading...");
        if(plugin.change_max > 0) {
            plugin.change_max--;
        } else {
            plugin.getServer().getLogger().notice("All kits loaded!");
            getHandler().cancel();
        }
    }
}
