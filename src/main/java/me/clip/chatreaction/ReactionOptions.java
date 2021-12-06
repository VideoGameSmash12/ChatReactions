package me.clip.chatreaction;

import java.util.Collections;
import java.util.List;

public class ReactionOptions
{
    private int interval;
    private int playersNeeded;
    private int timeLimit;
    private int charLength;
    private boolean ignoreCase;
    private int rewardAmt;
    private List<String> rewards;
    private List<String> startMsg;
    private String startTooltip;
    private List<String> endMsg;
    private List<String> winMsg;
    private List<String> scrambleStartMsg;
    private String scrambleStartTooltip;
    private List<String> scrambleEndMsg;
    private List<String> scrambleWinMsg;
    private boolean useCustomWords;
    private boolean scramble;
    private boolean scrambleSpaces;
    private boolean trackStats;
    private int topPlayersSize;
    private boolean debug;
    private boolean randomScramble;
    private List<String> customWords;
    private List<String> disabledWorlds;

    public ReactionOptions(ChatReaction plugin)
    {
        this.interval = plugin.config.reactionInterval();
        this.timeLimit = plugin.config.timeLimit();
        this.playersNeeded = plugin.config.playersNeeded();
        this.charLength = plugin.config.wordLength();
        this.ignoreCase = plugin.config.ignoreCase();
        this.rewardAmt = plugin.config.rewardAmt();
        this.rewards = plugin.config.rewards();
        this.useCustomWords = plugin.config.useCustomWords();

        if (this.useCustomWords)
        {
            List<String> words = plugin.wordFile.loadWords(plugin.config.splitWordsByLine());

            if (words == null || words.isEmpty())
            {
                this.useCustomWords = false;
            }
            else
            {
                this.customWords = Collections.unmodifiableList(words);
            }
        }

        this.startMsg = plugin.config.startMsg();
        this.startTooltip = plugin.config.startTooltip();
        this.endMsg = plugin.config.endMsg();
        this.winMsg = plugin.config.winMsg();
        this.scramble = plugin.config.scramble();
        this.scrambleSpaces = plugin.config.scrambleSpaces();
        this.trackStats = plugin.config.trackStats();
        this.topPlayersSize = plugin.config.topPlayersSize();
        this.debug = plugin.config.debug();
        this.scrambleStartMsg = plugin.config.scrambleStartMsg();
        this.scrambleStartTooltip = plugin.config.scrambleStartTooltip();
        this.scrambleEndMsg = plugin.config.scrambleEndMsg();
        this.scrambleWinMsg = plugin.config.scrambleWinMsg();
        this.randomScramble = plugin.config.randomScramble();
        this.disabledWorlds = plugin.config.getDisabledWorlds();
    }

    public String startTooltip()
    {
        return this.startTooltip;
    }

    public String scrambleTooltip()
    {
        return this.scrambleStartTooltip;
    }

    public List<String> disabledWorlds()
    {
        return this.disabledWorlds;
    }

    public boolean randomScramble()
    {
        return this.randomScramble;
    }

    public List<String> scrambleStartMsg()
    {
        return this.scrambleStartMsg;
    }

    public List<String> scrambleEndMsg()
    {
        return this.scrambleEndMsg;
    }

    public List<String> scrambleWinMsg()
    {
        return this.scrambleWinMsg;
    }

    public boolean debug()
    {
        return this.debug;
    }

    public void setTrackStats(boolean b)
    {
        this.trackStats = b;
    }

    public boolean trackStats()
    {
        return this.trackStats;
    }

    public int topPlayersSize()
    {
        return this.topPlayersSize;
    }

    public boolean scrambleSpaces()
    {
        return this.scrambleSpaces;
    }

    public boolean scramble()
    {
        return this.scramble;
    }

    public boolean ignoreCase()
    {
        return this.ignoreCase;
    }

    public boolean useCustomWords()
    {
        return this.useCustomWords;
    }

    public List<String> customWords()
    {
        return this.customWords;
    }

    public int interval()
    {
        return this.interval;
    }

    public int playersNeeded()
    {
        return this.playersNeeded;
    }

    public int timeLimit()
    {
        return this.timeLimit;
    }

    public int charLength()
    {
        return this.charLength;
    }

    public int rewardAmt()
    {
        return this.rewardAmt;
    }

    public List<String> rewards()
    {
        return this.rewards;
    }

    public List<String> startMsg()
    {
        return this.startMsg;
    }

    public List<String> endMsg()
    {
        return this.endMsg;
    }

    public List<String> winMsg()
    {
        return this.winMsg;
    }
}