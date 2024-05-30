package com.github.neapovil.prefixes;

import org.bukkit.plugin.java.JavaPlugin;

public final class Prefixes extends JavaPlugin
{
    private static Prefixes instance;

    @Override
    public void onEnable()
    {
        instance = this;
    }

    public static Prefixes instance()
    {
        return instance;
    }
}
