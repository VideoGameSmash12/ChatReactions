package me.clip.chatreaction.tasks;

import me.clip.chatreaction.ChatReaction;
import me.clip.chatreaction.reactionplayer.ReactionPlayer;

public class LoadPlayerTask implements Runnable
{
    private ChatReaction plugin;
    private String uuid;
    private String name;

    public LoadPlayerTask(ChatReaction instance, String uuid, String name)
    {
        this.plugin = instance;
        this.uuid = uuid;
        this.name = name;
    }

    public void run()
    {
        if (!ChatReaction.hasData(this.uuid))
        {
            ReactionPlayer pl = this.plugin.loadPlayer(this.uuid);

            if (pl == null)
            {
                pl = new ReactionPlayer(this.uuid, this.name, 0);
                ChatReaction.reactionPlayers.put(this.uuid, pl);
            }
            else
            {

                pl.setName(this.name);
                ChatReaction.reactionPlayers.put(this.uuid, pl);
            }
        }
    }
}