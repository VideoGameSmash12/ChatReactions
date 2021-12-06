package me.clip.chatreaction.reactionplayer;

public class ReactionPlayer
{
    private String uuid;
    private String name;
    private int wins;

    public ReactionPlayer(String uuid, String name, int wins)
    {
        setUuid(uuid);
        setName(name);
        setWins(wins);
    }

    public String getUuid()
    {
        return this.uuid;
    }

    public void setUuid(String uuid)
    {
        this.uuid = uuid;
    }

    public String getName()
    {
        return this.name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public int getWins()
    {
        return this.wins;
    }

    public void setWins(int wins)
    {
        this.wins = wins;
    }
}