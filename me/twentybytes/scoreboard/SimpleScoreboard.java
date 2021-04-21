package me.twentybytes.scoreboard;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.ArrayList;
import java.util.List;

/**
 * Created in 21.04.2021
 * by TwentyBytes.
 */

public class SimpleScoreboard {

    private int initial;
    private int index;
    private String blank = "";
    private boolean finished = false;

    private final Player owner;
    private final Scoreboard scoreboard;
    private Objective objective;
    private String title;

    private final List<IRow.Switchable> switchables = new ArrayList<>();

    public SimpleScoreboard(Player owner) {
        this(20, "Scoreboard", owner);
    }

    public SimpleScoreboard(int initialIndex, Player owner) {
        this(initialIndex, "Scoreboard", owner);
    }

    public SimpleScoreboard(String title, Player owner) {
        this(20, title, owner);
    }

    public SimpleScoreboard(int initialIndex, String title, Player owner) {
        initial = initialIndex;
        index = initialIndex;
        this.title = title;
        this.owner = owner;
        scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        objective = scoreboard.registerNewObjective("board", "dummy");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        objective.setDisplayName(title);
    }

    public SimpleScoreboard setTitle(String title) {
        this.title = title;
        objective.setDisplayName(title);
        return this;
    }

    public SimpleScoreboard setInitialIndex(int initial) {
        this.initial = initial;
        return this;
    }

    public SimpleScoreboard addRows(IRow... rows) {
        if (finished)
            throw new RuntimeException("You can add rows if board not finished.");
        for (IRow row : rows) {
            if (row instanceof IRow.Constant) {
                objective.getScore(((IRow.Constant) row).text).setScore(index--);
                continue;
            }
            IRow.Switchable switchable = (IRow.Switchable) row;
            switchables.add(switchable);
            Team team = scoreboard.registerNewTeam(switchable.id);
            team.addEntry(blank());
            objective.getScore(blank).setScore(index--);
        }
        return this;
    }

    public SimpleScoreboard resetRows(IRow... rows) {
        finished = false;
        if (objective != null)
            objective.unregister();
        objective = scoreboard.registerNewObjective("board", "dummy");
        objective.setDisplayName(title);
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        switchables.clear();
        index = initial;
        addRows(rows);
        finish();
        return this;
    }

    public SimpleScoreboard finish() {
        if (finished)
            throw new RuntimeException("Board already finished.");
        finished = true;
        update();
        owner.setScoreboard(scoreboard);
        return this;
    }

    public SimpleScoreboard update() {
        for (IRow.Switchable switchable : switchables) {
            Team team = scoreboard.getTeam(switchable.id);
            team.setPrefix(switchable.text.getText());
        }
        return this;
    }

    public SimpleScoreboard reset() {
        return update();
    }

    public String blank() {
        return (blank = blank.concat(" "));
    }

    public interface IRow {

        class Constant implements IRow {

            public String text;

            public Constant(String text) {
                this.text = text;
            }

        }

        class Switchable implements IRow  {

            public String id;
            public ISwitchableText text;

            public Switchable(String id, ISwitchableText text) {
                this.id = id;
                this.text = text;
            }

            public interface ISwitchableText {
                String getText();
            }

        }

    }

}
