package com.github.neapovil.prefixes.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.persistence.PersistentDataContainer;

import com.github.neapovil.prefixes.Prefixes;

public final class PrefixesListener implements Listener
{
    private Prefixes plugin = Prefixes.instance();

    @EventHandler
    private void onPlayerJoin(PlayerJoinEvent event)
    {
        final PersistentDataContainer pdc = event.getPlayer().getPersistentDataContainer();
        final String prefixname = pdc.get(plugin.currentPrefix, plugin.currentPrefixType);

        if (prefixname == null)
        {
            return;
        }

        if (plugin.prefixesResource.get(prefixname) == null)
        {
            pdc.remove(plugin.currentPrefix);
        }
        else
        {
            plugin.updatePlayerPrefix(event.getPlayer());
        }
    }
}
