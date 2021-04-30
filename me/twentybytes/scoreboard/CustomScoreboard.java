package me.twentybytes.scoreboard;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
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

    protected void reset() {
        objective.reset();
    }

    public void update() {
        objective.update();
    }

}
