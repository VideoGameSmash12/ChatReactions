package me.clip.chatreaction.events;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ReactionStartEvent extends Event implements Cancellable
{
    private static final HandlerList handlers = new HandlerList();
    private boolean cancelled;
    private long timeLimit;
    private String actualWord;
    private String displayWord;
    private boolean scrambled;
    private boolean showMessage;

    public ReactionStartEvent(long timeLimit, String actualWord, String displayWord, boolean scrambled)
    {
        super(true);
        setTimeLimit(timeLimit);
        setActualWord(actualWord);
        setDisplayWord(displayWord);
        setScrambled(scrambled);
        setShowMessage(true);
    }

    public HandlerList getHandlers()
    {
        return handlers;
    }

    public static HandlerList getHandlerList()
    {
        return handlers;
    }

    public boolean isCancelled()
    {
        return this.cancelled;
    }

    public void setCancelled(boolean cancelled)
    {
        this.cancelled = cancelled;
    }

    public String getActualWord()
    {
        return this.actualWord;
    }

    public void setActualWord(String actualWord)
    {
        this.actualWord = actualWord;
    }

    public String getDisplayWord()
    {
        return this.displayWord;
    }

    public void setDisplayWord(String displayWord)
    {
        this.displayWord = displayWord;
    }

    public boolean isScrambled()
    {
        return this.scrambled;
    }

    public void setScrambled(boolean scrambled)
    {
        this.scrambled = scrambled;
    }

    public boolean showMessage()
    {
        return this.showMessage;
    }

    public void setShowMessage(boolean showMessage)
    {
        this.showMessage = showMessage;
    }

    public long getTimeLimit()
    {
        return this.timeLimit;
    }

    public void setTimeLimit(long timeLimit)
    {
        this.timeLimit = timeLimit;
    }
}