package me.clip.chatreaction.events;

import java.util.List;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ReactionFailEvent extends Event
{
    private static final HandlerList handlers = new HandlerList();
    private String actualWord;
    private String displayWord;
    private boolean scrambled;
    private List<String> failMessage;
    private boolean showFailMessage;

    public ReactionFailEvent(String actualWord, String displayWord, boolean scrambled, List<String> failMessage)
    {
        super(true);
        this.actualWord = actualWord;
        this.displayWord = displayWord;
        this.scrambled = scrambled;
        setFailMessage(failMessage);
        setShowFailMessage((failMessage != null && !failMessage.isEmpty()));
    }

    public HandlerList getHandlers()
    {
        return handlers;
    }

    public static HandlerList getHandlerList()
    {
        return handlers;
    }

    public String getActualWord()
    {
        return this.actualWord;
    }

    public String getDisplayWord()
    {
        return this.displayWord;
    }

    public boolean isScrambled()
    {
        return this.scrambled;
    }

    public List<String> getFailMessage()
    {
        return this.failMessage;
    }

    public void setFailMessage(List<String> failMessage)
    {
        this.failMessage = failMessage;
    }

    public boolean showFailMessage()
    {
        return this.showFailMessage;
    }

    public void setShowFailMessage(boolean showFailMessage)
    {
        this.showFailMessage = showFailMessage;
    }
}