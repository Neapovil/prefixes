package com.github.neapovil.prefixes.command;

import org.bukkit.scoreboard.Team;

import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.ArgumentSuggestions;
import dev.jorel.commandapi.arguments.LiteralArgument;
import dev.jorel.commandapi.arguments.StringArgument;

public final class DeleteCommand extends AbstractCommand
{
    @Override
    public void register()
    {
        new CommandAPICommand("prefixes")
                .withPermission("prefixes.command.delete")
                .withArguments(new LiteralArgument("delete"))
                .withArguments(new StringArgument("name").replaceSuggestions(ArgumentSuggestions.strings(info -> {
                    return plugin.prefixesResource.commandSuggestions();
                })))
                .executesPlayer((player, args) -> {
                    final String name = (String) args.get("name");

                    final boolean removed = plugin.prefixesResource.prefixes.removeIf(i -> i.name.equalsIgnoreCase(name));

                    if (!removed)
                    {
                        throw CommandAPI.failWithString("Prefix not found");
                    }

                    final Team team = plugin.getServer().getScoreboardManager().getMainScoreboard().getTeam(name);

                    if (team != null)
                    {
                        team.unregister();
                    }

                    plugin.save().whenCompleteAsync((result, ex) -> {
                        if (ex == null)
                        {
                            player.sendMessage("Prefix deleted");
                        }
                    });
                })
                .register();
    }
}
