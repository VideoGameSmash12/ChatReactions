package me.clip.chatreaction;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.bukkit.configuration.file.FileConfiguration;

public class ReactionConfig
{
    private ChatReaction plugin;
    
    public ReactionConfig(ChatReaction i)
    {
        this.plugin = i;
    }

    public void loadCfg()
    {
        FileConfiguration c = this.plugin.getConfig();

        c.options().header("ChatReaction version " + this.plugin.getDescription().getVersion() + " configuration file");
        c.addDefault("debug", false);
        c.addDefault("reaction_stats.track_stats", false);
        c.addDefault("reaction_stats.top_players_size", 100);
        c.addDefault("reaction_stats.hostname", "localhost");
        c.addDefault("reaction_stats.port", 3306);
        c.addDefault("reaction_stats.database", "chatreaction");
        c.addDefault("reaction_stats.prefix", "");
        c.addDefault("reaction_stats.username", "root");
        c.addDefault("reaction_stats.password", "");
        c.addDefault("reaction_stats.ssl", false);
        c.addDefault("reaction.interval", 500);
        c.addDefault("reaction.time_limit", 30);
        c.addDefault("reaction.players_needed_to_start", 4);
        c.addDefault("reaction.word_character_length", 10);
        c.addDefault("reaction.ignore_case", false);
        c.addDefault("reaction.use_custom_words", true);
        c.addDefault("reaction.split_words_by_line", true);
        c.addDefault("reaction.scramble_custom_words", true);
        c.addDefault("reaction.scramble_at_random", true);
        c.addDefault("reaction.scramble_spaces", false);
        c.addDefault("reaction.disabled_worlds", Arrays.asList("world_nether", "world_the_end"));
        c.addDefault("reaction.reward_amount", 2);
        c.addDefault("reaction.rewards", Arrays.asList("eco give @p 500", "give @p diamondblock 100"));
        c.addDefault("messages.reaction_start", Collections.singletonList("&8[&e&lReaction&8] &bHover for the word to type!"));
        c.addDefault("messages.reaction_start_tooltip", "&f%word%");
        c.addDefault("messages.scramble_start", Collections.singletonList("&8[&e&lReaction&8] &bHover for the word to unscramble!"));
        c.addDefault("messages.scramble_start_tooltip", "&f%word%");
        c.addDefault("messages.reaction_end", Collections.singletonList("&8[&e&lReaction&8] &cNobody got the word in time &4:("));
        c.addDefault("messages.scramble_end", Arrays.asList("&8[&e&lReaction&8] &cNobody got the word in time &4:(", "&cThe word was &f%word%"));
        c.addDefault("messages.reaction_win", Collections.singletonList("&8[&e&lReaction&8] &f%player% &awon in &f%time% &aseconds!"));
        c.addDefault("messages.scramble_win", Collections.singletonList("&8[&e&lReaction&8] &f%player% &aunscrambled the word &f%word% &ain &f%time% &aseconds!"));
        c.addDefault("hooks.qaplugin.enabled", true);
        c.addDefault("hooks.qaplugin.hint.charge", true);
        c.addDefault("hooks.qaplugin.hint.charge_amount", 100.0D);

        c.options().copyDefaults(true);

        this.plugin.saveConfig();
        this.plugin.reloadConfig();
    }

    public boolean hookQA()
    {
        return this.plugin.getConfig().getBoolean("hooks.qaplugin.enabled", true);
    }

    public boolean qaCharge()
    {
        return this.plugin.getConfig().getBoolean("hooks.qaplugin.hint.charge", true);
    }

    public double qaChargeAmt()
    {
        return this.plugin.getConfig().getDouble("hooks.qaplugin.hint.charge_amount", 0.0D);
    }

    public String startTooltip()
    {
        return this.plugin.getConfig().getString("messages.reaction_start_tooltip");
    }

    public String scrambleStartTooltip()
    {
        return this.plugin.getConfig().getString("messages.scramble_start_tooltip");
    }

    public List<String> getDisabledWorlds()
    {
        return this.plugin.getConfig().getStringList("reaction.disabled_worlds");
    }

    public boolean splitWordsByLine()
    {
        return this.plugin.getConfig().getBoolean("reaction.split_words_by_line");
    }

    public boolean randomScramble()
    {
        return this.plugin.getConfig().getBoolean("reaction.scramble_at_random");
    }

    public boolean debug()
    {
        return this.plugin.getConfig().getBoolean("debug");
    }

    public int topPlayersSize()
    {
        return this.plugin.getConfig().getInt("reaction_stats.top_players_size");
    }

    public boolean trackStats()
    {
        return this.plugin.getConfig().getBoolean("reaction_stats.track_stats");
    }

    public boolean scrambleSpaces()
    {
        return this.plugin.getConfig().getBoolean("reaction.scramble_spaces");
    }

    public boolean scramble()
    {
        return this.plugin.getConfig().getBoolean("reaction.scramble_custom_words");
    }

    public boolean ignoreCase()
    {
        return this.plugin.getConfig().getBoolean("reaction.ignore_case");
    }

    public boolean useCustomWords()
    {
        return this.plugin.getConfig().getBoolean("reaction.use_custom_words");
    }

    public int reactionInterval()
    {
        return this.plugin.getConfig().getInt("reaction.interval");
    }

    public int timeLimit()
    {
        return this.plugin.getConfig().getInt("reaction.time_limit");
    }

    public int playersNeeded()
    {
        return this.plugin.getConfig().getInt("reaction.players_needed_to_start");
    }

    public int wordLength()
    {
        return this.plugin.getConfig().getInt("reaction.word_character_length");
    }

    public int rewardAmt()
    {
        return this.plugin.getConfig().getInt("reaction.reward_amount");
    }

    public List<String> rewards()
    {
        return this.plugin.getConfig().getStringList("reaction.rewards");
    }

    public List<String> startMsg()
    {
        return this.plugin.getConfig().getStringList("messages.reaction_start");
    }

    public List<String> endMsg()
    {
        return this.plugin.getConfig().getStringList("messages.reaction_end");
    }

    public List<String> winMsg()
    {
        return this.plugin.getConfig().getStringList("messages.reaction_win");
    }

    public List<String> scrambleStartMsg()
    {
        return this.plugin.getConfig().getStringList("messages.scramble_start");
    }

    public List<String> scrambleEndMsg()
    {
        return this.plugin.getConfig().getStringList("messages.scramble_end");
    }

    public List<String> scrambleWinMsg()
    {
        return this.plugin.getConfig().getStringList("messages.scramble_win");
    }
}