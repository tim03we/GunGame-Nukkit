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
import cn.nukkit.block.Block;
import cn.nukkit.entity.Entity;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.EventPriority;
import cn.nukkit.event.Listener;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.event.player.PlayerMoveEvent;
import cn.nukkit.level.Level;
import tim03we.gungame.GunGame;

public class MoveListener implements Listener {

    private GunGame plugin;

    public MoveListener(GunGame plugin)
    {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onMove(PlayerMoveEvent event)
    {
        Player player = event.getPlayer();
        int x = Math.round((int) player.getX());
        int y = Math.round((int) player.getY());
        int z = Math.round((int) player.getZ());
        Level level = event.getPlayer().getLevel();
        int ground = level.getBlockIdAt(x, y, z);
        if(ground == Block.WATER || ground == Block.WATER_LILY || ground == Block.STILL_WATER) {
            EntityDamageEvent cause = player.getLastDamageCause();
            if(cause instanceof EntityDamageByEntityEvent) {
                Entity damager = ((EntityDamageByEntityEvent) cause).getDamager();
                if(damager instanceof Player) {
                    this.plugin.levelUp((Player)damager);
                    player.attack(new EntityDamageEvent(player, EntityDamageEvent.DamageCause.CUSTOM, 1000));
                    this.plugin.getServer().broadcastMessage(this.plugin.getConfig().getString("messages.kill").replace("{player}", player.getName()).replace("{killer}", damager.getName()));
                }
            } else {
                player.attack(new EntityDamageEvent(player, EntityDamageEvent.DamageCause.DROWNING, 1000));
            }
        }

    }
}
