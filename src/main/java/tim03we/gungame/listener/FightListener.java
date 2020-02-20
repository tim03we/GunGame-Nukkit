package tim03we.gungame.listener;

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
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.EventPriority;
import cn.nukkit.event.Listener;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import tim03we.gungame.GunGame;

public class FightListener implements Listener {

    private GunGame plugin;

    public FightListener(GunGame plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onFight(EntityDamageByEntityEvent event) {
        Player player = (Player) event.getEntity();
        int x = player.getFloorX();
        int y = player.getFloorY();
        int z = player.getFloorZ();
        int sx = player.getLevel().getSafeSpawn().getFloorX();
        int sy = player.getLevel().getSafeSpawn().getFloorY();
        int sz = player.getLevel().getSafeSpawn().getFloorZ();
        int cp = plugin.settings.PVP_RADIUS;
        if(Math.abs(sx - x) < cp && Math.abs(sy - y) < cp && Math.abs(sz - z) < cp) {
            event.setCancelled(true);
        }
    }
}
