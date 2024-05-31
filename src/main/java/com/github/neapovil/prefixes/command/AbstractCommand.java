package com.github.neapovil.prefixes.command;

import com.github.neapovil.prefixes.Prefixes;

public abstract class AbstractCommand
{
    protected Prefixes plugin = Prefixes.instance();

    public abstract void register();
}
