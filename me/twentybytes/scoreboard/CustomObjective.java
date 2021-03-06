package me.twentybytes.scoreboard;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Team;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

/**
 * Created by TwentyBytes
 * in 30.04.2021
 */

@Getter
public class CustomObjective {

    private static final Random random = new Random();

    private final CustomScoreboard scoreboard;
    private Objective objective;
    private Player owner;
    private int initial = 99;
    private String blank = "";
    private final Map<Team, IChangeableText<String>> records = new HashMap<>();

    public CustomObjective(CustomScoreboard scoreboard) {
        this(scoreboard, null);
    }

    public CustomObjective(CustomScoreboard scoreboard, Player owner) {
        this.scoreboard = scoreboard;
        this.objective = scoreboard.getScoreboard().registerNewObjective("board", "dummy");
        this.owner = owner;
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        setScoreboard();
    }

    public CustomObjective title(String title) {
        objective.setDisplayName(title);
        return this;
    }

    public CustomObjective slot(DisplaySlot slot) {
        objective.setDisplaySlot(slot);
        return this;
    }

    public CustomObjective blank() {
        blank = blank.concat(" ");
        objective.getScore(blank).setScore(initial--);
        return this;
    }

    public CustomObjective record(IChangeableText<String> text) {
        Team team = scoreboard.getScoreboard().registerNewTeam(String.valueOf(random.nextInt()));
        blank();
        team.addEntry(blank);
        objective.getScore(blank).setScore(initial--);
        records.put(team, text);
        update();
        return this;
    }

    public CustomObjective record(String text) {
        objective.getScore(text).setScore(initial--);
        return this;
    }

    public CustomObjective reset() {
        initial = 99;
        records.clear();
        objective.unregister();
        scoreboard.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
        objective = scoreboard.getScoreboard().registerNewObjective("board", "dummy");
        setScoreboard();
        return this;
    }

    private void setScoreboard() {
        if (owner == null) {
            Bukkit.getOnlinePlayers().forEach(player -> player.setScoreboard(scoreboard.getScoreboard()));
            return;
        }
        owner.setScoreboard(scoreboard.getScoreboard());
    }

    public CustomObjective update() {
        for (Map.Entry<Team, IChangeableText<String>> entry : records.entrySet())
            entry.getKey().setPrefix(entry.getValue().getText());
        setScoreboard();
        return this;
    }

}
