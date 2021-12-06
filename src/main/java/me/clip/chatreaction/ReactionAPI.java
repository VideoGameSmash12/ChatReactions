package me.clip.chatreaction;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import me.clip.chatreaction.reactionplayer.ReactionPlayer;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class ReactionAPI
{
    public static boolean isStarted()
    {
        return ChatReaction.isRunning();
    }

    public static String getReactionWord()
    {
        return ChatReaction.getCurrentWord();
    }

    public static String getDisplayWord()
    {
        return ChatReaction.getDisplayWord();
    }

    public static long getStartTime()
    {
        return ChatReaction.getStartTime();
    }

    public static int getWins(Player p)
    {
        String uuid = p.getUniqueId().toString();

        if (ChatReaction.hasData(uuid))
        {
            return ChatReaction.reactionPlayers.get(uuid).getWins();
        }

        return 0;
    }

    public static int getWins(OfflinePlayer p)
    {
        String uuid = p.getUniqueId().toString();

        if (ChatReaction.hasData(uuid))
        {
            return ChatReaction.reactionPlayers.get(uuid).getWins();
        }

        return 0;
    }

    public List<ReactionPlayer> getTopWinners()
    {
        if (ChatReaction.topPlayers == null || ChatReaction.topPlayers.isEmpty())
        {
            return null;
        }

        return new ArrayList<>(ChatReaction.topPlayers);
    }

    public static int getTopRank(Player p)
    {
        if (ChatReaction.topPlayers == null || ChatReaction.topPlayers.isEmpty())
        {
            return -1;
        }

        Iterator<ReactionPlayer> check = ChatReaction.topPlayers.iterator();

        while (check.hasNext())
        {
            ReactionPlayer localPlayer = check.next();

            if (localPlayer.getUuid().equals(p.getUniqueId().toString()))
            {
                return localPlayer.getWins();
            }
        }
        return -1;
    }

    public static int getTopRank(OfflinePlayer p)
    {
        if (ChatReaction.topPlayers == null || ChatReaction.topPlayers.isEmpty())
        {
            return -1;

        }
        Iterator<ReactionPlayer> check = ChatReaction.topPlayers.iterator();
        while (check.hasNext())
        {
            ReactionPlayer localPlayer = check.next();

            if (localPlayer.getUuid().equals(p.getUniqueId().toString()))
            {
                return localPlayer.getWins();
            }
        }

        return -1;
    }
}