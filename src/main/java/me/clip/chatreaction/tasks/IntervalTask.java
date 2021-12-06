package me.clip.chatreaction.tasks;

import java.util.List;
import java.util.Random;

import me.clip.chatreaction.ChatReaction;
import me.clip.chatreaction.events.ReactionStartEvent;
import org.bukkit.Bukkit;

public class IntervalTask implements Runnable
{
    ChatReaction plugin;
    Random r;

    public IntervalTask(ChatReaction instance)
    {
        this.plugin = instance;
        this.r = new Random();
    }

    public void run()
    {
        if (ChatReaction.getOnline() < this.plugin.getOptions().playersNeeded())
        {
            return;
        }

        if (ChatReaction.isRunning())
        {
            ChatReaction.setRunning(false);
            ChatReaction.setScrambled(false);
            ChatReaction.setCurrentWord(null);
            ChatReaction.setDisplayWord(null);

            if (ChatReaction.getEndTask() != null)
            {
                ChatReaction.getEndTask().cancel();
                ChatReaction.setEndTask(null);
            }
        }

        String actual = this.plugin.pickWord();
        String show = actual;
        boolean scrambled = false;

        if (this.plugin.getOptions().useCustomWords())
        {
            if (this.plugin.getOptions().randomScramble())
            {
                if (this.r.nextBoolean())
                {
                    scrambled = true;
                    show = this.plugin.scramble(actual);
                }
            }
            else if (this.plugin.getOptions().scramble())
            {
                scrambled = true;
                show = this.plugin.scramble(actual);
            }
        }

        ReactionStartEvent event = new ReactionStartEvent(this.plugin.getOptions().timeLimit(), actual, show, scrambled);
        Bukkit.getPluginManager().callEvent(event);

        if (event.isCancelled())
        {
            return;
        }

        actual = event.getActualWord();
        show = event.getDisplayWord();
        scrambled = event.isScrambled();

        List<String> msg = null;
        String tip = null;

        if (event.showMessage())
        {
            if (scrambled)
            {
                msg = this.plugin.getOptions().scrambleStartMsg();
                tip = this.plugin.getOptions().scrambleTooltip();
            }
            else
            {
                msg = this.plugin.getOptions().startMsg();
                tip = this.plugin.getOptions().startTooltip();
            }

            if (tip == null || tip.isEmpty())
            {
                tip = null;
            }
            else
            {
                tip = tip.replace("%word%", show).replace("%time%", String.valueOf(this.plugin.getOptions().timeLimit()));
            }
        }

        ChatReaction.setScrambled(scrambled);
        ChatReaction.setCurrentWord(actual);
        ChatReaction.setDisplayWord(show);
        long time = System.currentTimeMillis();
        ChatReaction.setRunning(true);
        ChatReaction.setStartTime(time);

        if (msg != null && !msg.isEmpty())
        {
            for (String line : msg)
            {
                this.plugin.sendMsg(line.replace("%word%", show).replace("%time%", String.valueOf(this.plugin.getOptions().timeLimit())), tip);
            }
        }

        ChatReaction.setEndTask(Bukkit.getScheduler().runTaskLaterAsynchronously(this.plugin, new EndTask(this.plugin), (this.plugin.getOptions().timeLimit() * 20)));
    }
}