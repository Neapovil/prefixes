package com.github.neapovil.prefixes.command;

import com.github.neapovil.prefixes.resource.PrefixesResource;

import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.ArgumentSuggestions;
import dev.jorel.commandapi.arguments.LiteralArgument;
import dev.jorel.commandapi.arguments.StringArgument;
import dev.jorel.commandapi.arguments.TextArgument;

public final class ModifyCommand extends AbstractCommand
{
    @Override
    public void register()
    {
        new CommandAPICommand("prefixes")
                .withPermission("prefixes.command.modify")
                .withArguments(new LiteralArgument("modify"))
                .withArguments(new StringArgument("name").replaceSuggestions(ArgumentSuggestions.strings(info -> {
                    return plugin.prefixesResource.commandSuggestions();
                })))
                .withArguments(new LiteralArgument("prefix"))
                .withArguments(new TextArgument("value"))
                .executesPlayer((player, args) -> {
                    final String prefixname = (String) args.get("name");
                    final String value = (String) args.get("value");

                    final PrefixesResource.PrefixItem prefix = plugin.prefixesResource.get(prefixname);

                    if (plugin.prefixesResource.get(prefixname) == null)
                    {
                        throw CommandAPI.failWithString("Prefix not found");
                    }

                    prefix.prefix = value;
                    plugin.makeTeams();

                    plugin.save().whenCompleteAsync((result, ex) -> {
                        if (ex == null)
                        {
                            player.sendMessage("Prefix " + prefixname + " modified");
                        }
                    });
                })
                .register();
    }
}
