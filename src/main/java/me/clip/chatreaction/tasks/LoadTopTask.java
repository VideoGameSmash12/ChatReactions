package me.clip.chatreaction.tasks;

import me.clip.chatreaction.ChatReaction;

public class LoadTopTask implements Runnable
{
    private ChatReaction plugin;

    public LoadTopTask(ChatReaction instance)
    {
        this.plugin = instance;
    }

    public void run()
    {
        this.plugin.updateTopWins();
    }
}