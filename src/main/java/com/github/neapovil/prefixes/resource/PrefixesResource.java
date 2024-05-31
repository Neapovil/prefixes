package com.github.neapovil.prefixes.resource;

import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.Nullable;

import com.github.neapovil.prefixes.Prefixes;

import net.kyori.adventure.text.Component;

public class PrefixesResource
{
    public final List<PrefixItem> prefixes = new ArrayList<>();

    @Nullable
    public PrefixItem get(String name)
    {
        return this.prefixes.stream().filter(i -> i.name.equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    public String[] commandSuggestions()
    {
        return this.prefixes.stream().map(i -> i.name).toArray(String[]::new);
    }

    public static class PrefixItem
    {
        public String name;
        public String prefix;

        public PrefixItem(String name, String prefix)
        {
            this.name = name;
            this.prefix = prefix;
        }

        public Component prefix()
        {
            return Prefixes.MINI_MESSAGE.deserialize(this.prefix);
        }
    }
}
