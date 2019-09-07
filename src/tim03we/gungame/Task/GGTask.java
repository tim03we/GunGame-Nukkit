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

import cn.nukkit.Player;
import cn.nukkit.scheduler.Task;
import tim03we.gungame.Main;

public class GGTask extends Task {

    Main plugin;

    public GGTask(Main plugin)
    {

        this.plugin = plugin;
    }

    @Override
    public void onRun(int currentTicks)
    {
        for (Player player : this.plugin.getServer().getOnlinePlayers().values()) {
            int needLevel = this.plugin.needLevel.get(player.getName());
            if (needLevel == 1) {
                this.plugin.levelChange(player);
            }
        }
    }
}