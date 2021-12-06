package me.clip.chatreaction.events;

import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ReactionWinEvent extends Event
{
    private static final HandlerList handlers = new HandlerList();
    private Player winner;
    private String word;
    private double time;
    private int wins;
    private boolean trackingWins;
    private boolean showWinMessage;
    private List<String> winMessage;

    public ReactionWinEvent(Player winner, String word, double time, boolean trackingWins, int wins, List<String> winMessage)
    {
        super(true);
        this.winner = winner;
        this.word = word;
        this.time = time;
        this.trackingWins = trackingWins;
        this.wins = wins;
        setWinMessage(winMessage);
        setShowWinMessage((winMessage != null && !winMessage.isEmpty()));
    }

    public HandlerList getHandlers()
    {
        return handlers;
    }

    public static HandlerList getHandlerList()
    {
        return handlers;
    }

    public Player getWinner()
    {
        return this.winner;
    }

    public String getWord()
    {
        return this.word;
    }

    public double getTime()
    {
        return this.time;
    }

    public int getWins()
    {
        return this.wins;
    }

    public boolean isWinTrackingEnabled()
    {
        return this.trackingWins;
    }

    public boolean showWinMessage()
    {
        return this.showWinMessage;
    }

    public void setShowWinMessage(boolean showWinMessage)
    {
        this.showWinMessage = showWinMessage;
    }

    public List<String> getWinMessage()
    {
        return this.winMessage;
    }

    public void setWinMessage(List<String> winMessage)
    {
        this.winMessage = winMessage;
    }
}