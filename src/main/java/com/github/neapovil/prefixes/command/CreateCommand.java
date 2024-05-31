package com.github.neapovil.prefixes.command;

import com.github.neapovil.prefixes.resource.PrefixesResource;

import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.LiteralArgument;
import dev.jorel.commandapi.arguments.StringArgument;

public final class CreateCommand extends AbstractCommand
{
    @Override
    public void register()
    {
        new CommandAPICommand("prefixes")
                .withPermission("prefixes.command.create")
                .withArguments(new LiteralArgument("create"))
                .withArguments(new StringArgument("name"))
                .withArguments(new StringArgument("prefix"))
                .executesPlayer((player, args) -> {
                    final String name = (String) args.get("name");
                    final String prefix = (String) args.get("prefix");

                    if (plugin.prefixesResource.get(name) != null)
                    {
                        throw CommandAPI.failWithString("A prefix with this name already exists");
                    }

                    plugin.prefixesResource.prefixes.add(new PrefixesResource.PrefixItem(name, prefix));
                    plugin.makeTeams();

                    plugin.save().whenCompleteAsync((result, ex) -> {
                        if (ex == null)
                        {
                            player.sendMessage("New prefix created");
                        }
                    });
                })
                .register();
    }
}
