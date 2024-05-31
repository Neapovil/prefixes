package com.github.neapovil.prefixes.command;

import org.bukkit.persistence.PersistentDataContainer;

import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.LiteralArgument;

public final class ToggleCommand extends AbstractCommand
{
    @Override
    public void register()
    {
        new CommandAPICommand("prefixes")
                .withPermission("prefixes.command.toggleOff")
                .withArguments(new LiteralArgument("toggleOff"))
                .executesPlayer((player, args) -> {
                    final PersistentDataContainer pdc = player.getPersistentDataContainer();
                    final String prefixname = pdc.get(plugin.currentPrefix, plugin.currentPrefixType);

                    if (prefixname == null)
                    {
                        throw CommandAPI.failWithString("No prefix found");
                    }

                    pdc.remove(plugin.currentPrefix);
                    player.getScoreboard().getTeam(prefixname).removeEntity(player);

                    player.sendMessage("Prefix toggled off");
                })
                .register();
    }
}
