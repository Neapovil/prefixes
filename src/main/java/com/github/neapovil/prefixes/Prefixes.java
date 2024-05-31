package com.github.neapovil.prefixes;

import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;

import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import com.github.neapovil.core.Core;
import com.github.neapovil.prefixes.command.CreateCommand;
import com.github.neapovil.prefixes.command.DeleteCommand;
import com.github.neapovil.prefixes.command.SelectCommand;
import com.github.neapovil.prefixes.command.ToggleCommand;
import com.github.neapovil.prefixes.listener.PrefixesListener;
import com.github.neapovil.prefixes.resource.PrefixesResource;
import com.github.neapovil.prefixes.resource.PrefixesResource.PrefixItem;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import io.papermc.paper.util.MCUtil;
import net.kyori.adventure.text.minimessage.MiniMessage;

public final class Prefixes extends JavaPlugin
{
    private static Prefixes instance;
    public static final MiniMessage MINI_MESSAGE = MiniMessage.miniMessage();
    public final Path prefixesPath = this.getDataFolder().toPath().resolve("prefixes.json");
    public PrefixesResource prefixesResource;
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    public final NamespacedKey currentPrefix = new NamespacedKey(this, "current-prefix");
    public final PersistentDataType<String, String> currentPrefixType = PersistentDataType.STRING;

    @Override
    public void onEnable()
    {
        instance = this;

        this.load().whenCompleteAsync((result, ex) -> {
            new SelectCommand().register();
            new ToggleCommand().register();
            new CreateCommand().register();
            new DeleteCommand().register();

            this.getServer().getPluginManager().registerEvents(new PrefixesListener(), this);

            this.makeTeams();
        }, MCUtil.MAIN_EXECUTOR);
    }

    public static Prefixes instance()
    {
        return instance;
    }

    public CompletableFuture<String> load()
    {
        this.saveResource("prefixes.json", false);

        final Core core = Core.instance();
        return core.loadResource(this, this.prefixesPath).whenCompleteAsync((result, ex) -> {
            if (ex == null)
            {
                this.prefixesResource = this.gson.fromJson(result, PrefixesResource.class);
            }
        }, MCUtil.MAIN_EXECUTOR);
    }

    public CompletableFuture<Void> save()
    {
        final Core core = Core.instance();
        final String json = this.gson.toJson(this.prefixesResource);
        return core.saveResource(this.prefixesPath, json);
    }

    public void updatePlayerPrefix(Player player)
    {
        final String prefixname = player.getPersistentDataContainer().get(this.currentPrefix, this.currentPrefixType);
        final PrefixesResource.PrefixItem prefixitem = this.prefixesResource.get(prefixname);

        if (prefixitem != null)
        {
            final Team team = this.getServer().getScoreboardManager().getMainScoreboard().getTeam(prefixname);

            if (team != null)
            {
                team.addEntity(player);
            }
        }
    }

    public void makeTeams()
    {
        final Scoreboard scoreboard = this.getServer().getScoreboardManager().getMainScoreboard();

        for (PrefixItem i : this.prefixesResource.prefixes)
        {
            Team team = scoreboard.getTeam(i.name);

            if (team == null)
            {
                team = scoreboard.registerNewTeam(i.name);
            }

            team.prefix(i.prefix().appendSpace());
        }
    }
}
