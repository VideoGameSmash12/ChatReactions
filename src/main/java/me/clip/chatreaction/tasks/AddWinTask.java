package me.clip.chatreaction.tasks;

import me.clip.chatreaction.ChatReaction;
import me.clip.chatreaction.reactionplayer.ReactionPlayer;

public class AddWinTask implements Runnable
{
    private ChatReaction plugin;
    private String uuid;
    private String name;

    public AddWinTask(ChatReaction instance, String uuid, String name)
    {
        this.plugin = instance;
        this.uuid = uuid;
        this.name = name;
    }

    public void run()
    {
        if (ChatReaction.hasData(this.uuid))
        {
            ReactionPlayer pl = ChatReaction.reactionPlayers.get(this.uuid);
            pl.setName(this.name);
            pl.setWins(pl.getWins() + 1);
            this.plugin.savePlayer(pl);
        }
        else
        {
            ReactionPlayer pl = this.plugin.loadPlayer(this.uuid);

            if (pl == null)
            {
                pl = new ReactionPlayer(this.uuid, this.name, 1);
                ChatReaction.reactionPlayers.put(this.uuid, pl);
                this.plugin.savePlayer(pl);
            }
            else
            {
                pl.setWins(pl.getWins() + 1);
                pl.setName(this.name);
                ChatReaction.reactionPlayers.put(this.uuid, pl);
                this.plugin.savePlayer(pl);
            }
        }

        this.plugin.updateTopWins();
    }
}