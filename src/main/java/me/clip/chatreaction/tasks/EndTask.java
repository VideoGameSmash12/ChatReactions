package me.clip.chatreaction.tasks;

import java.util.List;

import me.clip.chatreaction.ChatReaction;
import me.clip.chatreaction.events.ReactionFailEvent;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;

public class EndTask implements Runnable
{
    private ChatReaction plugin;

    public EndTask(ChatReaction plugin)
    {
        this.plugin = plugin;
    }

    public void run()
    {
        if (!ChatReaction.isRunning())
        {
            return;
        }

        List<String> msg;

        if (ChatReaction.isScrambled())
        {
            msg = this.plugin.getOptions().scrambleEndMsg();
        }
        else
        {
            msg = this.plugin.getOptions().endMsg();
        }

        ReactionFailEvent event = new ReactionFailEvent(ChatReaction.getCurrentWord(), ChatReaction.getDisplayWord(), ChatReaction.isScrambled(), msg);

        Bukkit.getPluginManager().callEvent(event);

        if (event.showFailMessage() && event.getFailMessage() != null)
        {
            for (String line : event.getFailMessage())
            {
                this.plugin.sendMsg(line.replace("%word%", ChatReaction.getCurrentWord()));
            }
        }

        ChatReaction.setRunning(false);
        ChatReaction.setScrambled(false);
        ChatReaction.setStartTime(0L);
        ChatReaction.setCurrentWord(null);
        ChatReaction.setDisplayWord(null);
    }
}