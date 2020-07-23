package tim03we.gungame.listener;

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
import cn.nukkit.entity.Entity;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.EventPriority;
import cn.nukkit.event.Listener;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.event.player.PlayerDeathEvent;
import tim03we.gungame.GunGame;

public class DeathListener implements Listener {

    private GunGame plugin;

    public DeathListener(GunGame plugin)
    {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDeath(PlayerDeathEvent event)
    {
        event.setDrops(null);
        Player player = event.getEntity();
        event.setDeathMessage(this.plugin.getConfig().getString("messages.death").replace("{player}", player.getName()));
        this.plugin.levelDown(player);
        EntityDamageEvent cause = player.getLastDamageCause();
        if(cause instanceof EntityDamageByEntityEvent) {
            Entity damager = ((EntityDamageByEntityEvent) cause).getDamager();
            this.plugin.levelUp((Player)damager);
            event.setDeathMessage(this.plugin.getConfig().getString("messages.kill").replace("{player}", player.getName()).replace("{killer}", damager.getName()));
        }
        if(cause.getCause() == EntityDamageEvent.DamageCause.CUSTOM) {
            event.setDeathMessage("");
        }
    }
}
