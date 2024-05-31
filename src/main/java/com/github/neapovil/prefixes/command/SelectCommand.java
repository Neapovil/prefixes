package com.github.neapovil.prefixes.command;

import org.bukkit.persistence.PersistentDataContainer;

import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.ArgumentSuggestions;
import dev.jorel.commandapi.arguments.LiteralArgument;
import dev.jorel.commandapi.arguments.StringArgument;

public final class SelectCommand extends AbstractCommand
{
    @Override
    public void register()
    {
        new CommandAPICommand("prefixes")
                .withPermission("prefixes.command.select")
                .withArguments(new LiteralArgument("select"))
                .withArguments(new StringArgument("name").replaceSuggestions(ArgumentSuggestions.strings(info -> {
                    return plugin.prefixesResource.commandSuggestions();
                })))
                .executesPlayer((player, args) -> {
                    final String name = (String) args.get("name");

                    if (plugin.prefixesResource.get(name) == null)
                    {
                        throw CommandAPI.failWithString("Prefix not found");
                    }

                    final PersistentDataContainer pdc = player.getPersistentDataContainer();

                    if (pdc.has(plugin.currentPrefix) && pdc.get(plugin.currentPrefix, plugin.currentPrefixType).equalsIgnoreCase(name))
                    {
                        throw CommandAPI.failWithString("You already have this prefix active");
                    }

                    pdc.set(plugin.currentPrefix, plugin.currentPrefixType, name);

                    plugin.updatePlayerPrefix(player);

                    player.sendMessage("Own prefix changed to: " + name);
                })
                .register();
    }
}
