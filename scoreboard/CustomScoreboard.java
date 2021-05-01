package me.twentybytes.scoreboard;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Scoreboard;

/**
 * Created by TwentyBytes
 * in 30.04.2021
 */

@Getter
public class CustomScoreboard {

    @Setter
    private Scoreboard scoreboard;
    private final CustomObjective objective;
    private BukkitTask updateTask;

    public CustomScoreboard(Player owner) {
        this.scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        this.objective = new CustomObjective(this, owner);
    }

    public CustomScoreboard() {
        this(null);
    }

    public CustomScoreboard title(String title) {
        objective.title(title);
        return this;
    }

    public CustomScoreboard slot(DisplaySlot slot) {
        objective.slot(slot);
        return this;
    }

    public CustomScoreboard update() {
        objective.update();
        return this;
    }

    protected void reset() {
        objective.reset();
    }

    public CustomScoreboard setUpdateTask(Plugin plugin, boolean async, int interval) {
        removeUpdateTask();
        updateTask = async ? Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, this::update, interval, interval)
                : Bukkit.getScheduler().runTaskTimer(plugin, this::update, interval, interval);
        return this;
    }

    public CustomScoreboard removeUpdateTask() {
        if (updateTask != null)
            updateTask.cancel();
        updateTask = null;
        return this;
    }

}
