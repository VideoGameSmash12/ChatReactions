/*     */ package me.clip.chatreaction;
/*     */ 
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import org.bukkit.configuration.file.FileConfiguration;
/*     */ 
/*     */ 
/*     */ public class ReactionConfig
/*     */ {
/*     */   private ChatReaction plugin;
/*     */   
/*     */   public ReactionConfig(ChatReaction i) {
/*  13 */     this.plugin = i;
/*     */   }
/*     */ 
/*     */   
/*     */   public void loadCfg() {
/*  18 */     FileConfiguration c = this.plugin.getConfig();
/*  19 */     c.options().header("ChatReaction version " + this.plugin.getDescription().getVersion() + " configuration file");
/*  20 */     c.addDefault("debug", Boolean.valueOf(false));
/*  21 */     c.addDefault("check_updates", Boolean.valueOf(true));
/*     */     
/*  23 */     c.addDefault("reaction_stats.track_stats", Boolean.valueOf(false));
/*  24 */     c.addDefault("reaction_stats.top_players_size", Integer.valueOf(100));
/*  25 */     c.addDefault("reaction_stats.hostname", "localhost");
/*  26 */     c.addDefault("reaction_stats.port", Integer.valueOf(3306));
/*  27 */     c.addDefault("reaction_stats.database", "chatreaction");
/*  28 */     c.addDefault("reaction_stats.prefix", "");
/*  29 */     c.addDefault("reaction_stats.username", "root");
/*  30 */     c.addDefault("reaction_stats.password", "");
/*  31 */     c.addDefault("reaction_stats.ssl", Boolean.valueOf(false));
/*  32 */     c.addDefault("reaction.interval", Integer.valueOf(500));
/*  33 */     c.addDefault("reaction.time_limit", Integer.valueOf(30));
/*  34 */     c.addDefault("reaction.players_needed_to_start", Integer.valueOf(4));
/*  35 */     c.addDefault("reaction.word_character_length", Integer.valueOf(10));
/*  36 */     c.addDefault("reaction.ignore_case", Boolean.valueOf(false));
/*  37 */     c.addDefault("reaction.use_custom_words", Boolean.valueOf(true));
/*     */     
/*  39 */     c.addDefault("reaction.split_words_by_line", Boolean.valueOf(true));
/*  40 */     c.addDefault("reaction.scramble_custom_words", Boolean.valueOf(true));
/*  41 */     c.addDefault("reaction.scramble_at_random", Boolean.valueOf(true));
/*  42 */     c.addDefault("reaction.scramble_spaces", Boolean.valueOf(false));
/*     */ 
/*     */     
/*  45 */     c.addDefault("reaction.disabled_worlds", Arrays.asList(new String[] {
/*  46 */             "world_nether", "world_the_end"
/*     */           }));
/*     */     
/*  49 */     c.addDefault("reaction.reward_amount", Integer.valueOf(2));
/*  50 */     c.addDefault("reaction.rewards", Arrays.asList(new String[] {
/*  51 */             "eco give @p 500", "give @p diamondblock 100"
/*     */           }));
/*     */     
/*  54 */     c.addDefault("messages.reaction_start", Arrays.asList(new String[] {
/*  55 */             "&8[&e&lReaction&8] &bHover for the word to type!"
/*     */           }));
/*  57 */     c.addDefault("messages.reaction_start_tooltip", "&f%word%");
/*  58 */     c.addDefault("messages.scramble_start", Arrays.asList(new String[] {
/*  59 */             "&8[&e&lReaction&8] &bHover for the word to unscramble!"
/*     */           }));
/*  61 */     c.addDefault("messages.scramble_start_tooltip", "&f%word%");
/*  62 */     c.addDefault("messages.reaction_end", Arrays.asList(new String[] {
/*  63 */             "&8[&e&lReaction&8] &cNobody got the word in time &4:("
/*     */           }));
/*  65 */     c.addDefault("messages.scramble_end", Arrays.asList(new String[] {
/*  66 */             "&8[&e&lReaction&8] &cNobody got the word in time &4:(", "&cThe word was &f%word%"
/*     */           }));
/*  68 */     c.addDefault("messages.reaction_win", Arrays.asList(new String[] {
/*  69 */             "&8[&e&lReaction&8] &f%player% &awon in &f%time% &aseconds!"
/*     */           }));
/*  71 */     c.addDefault("messages.scramble_win", Arrays.asList(new String[] {
/*  72 */             "&8[&e&lReaction&8] &f%player% &aunscrambled the word &f%word% &ain &f%time% &aseconds!"
/*     */           }));
/*  74 */     c.addDefault("hooks.qaplugin.enabled", Boolean.valueOf(true));
/*  75 */     c.addDefault("hooks.qaplugin.hint.charge", Boolean.valueOf(true));
/*  76 */     c.addDefault("hooks.qaplugin.hint.charge_amount", Double.valueOf(100.0D));
/*  77 */     c.options().copyDefaults(true);
/*  78 */     this.plugin.saveConfig();
/*  79 */     this.plugin.reloadConfig();
/*     */   }
/*     */   
/*     */   public boolean hookQA() {
/*  83 */     return this.plugin.getConfig().getBoolean("hooks.qaplugin.enabled", true);
/*     */   }
/*     */   
/*     */   public boolean qaCharge() {
/*  87 */     return this.plugin.getConfig().getBoolean("hooks.qaplugin.hint.charge", true);
/*     */   }
/*     */   
/*     */   public double qaChargeAmt() {
/*  91 */     return this.plugin.getConfig().getDouble("hooks.qaplugin.hint.charge_amount", 0.0D);
/*     */   }
/*     */   
/*     */   public String startTooltip() {
/*  95 */     return this.plugin.getConfig().getString("messages.reaction_start_tooltip");
/*     */   }
/*     */   
/*     */   public String scrambleStartTooltip() {
/*  99 */     return this.plugin.getConfig().getString("messages.scramble_start_tooltip");
/*     */   }
/*     */   
/*     */   public List<String> getDisabledWorlds() {
/* 103 */     return this.plugin.getConfig().getStringList("reaction.disabled_worlds");
/*     */   }
/*     */   
/*     */   public boolean splitWordsByLine() {
/* 107 */     return this.plugin.getConfig().getBoolean("reaction.split_words_by_line");
/*     */   }
/*     */   
/*     */   public boolean randomScramble() {
/* 111 */     return this.plugin.getConfig().getBoolean("reaction.scramble_at_random");
/*     */   }
/*     */   
/*     */   public boolean debug() {
/* 115 */     return this.plugin.getConfig().getBoolean("debug");
/*     */   }
/*     */   
/*     */   public int topPlayersSize() {
/* 119 */     return this.plugin.getConfig().getInt("reaction_stats.top_players_size");
/*     */   }
/*     */   
/*     */   public boolean trackStats() {
/* 123 */     return this.plugin.getConfig().getBoolean("reaction_stats.track_stats");
/*     */   }
/*     */   
/*     */   public boolean scrambleSpaces() {
/* 127 */     return this.plugin.getConfig().getBoolean("reaction.scramble_spaces");
/*     */   }
/*     */   
/*     */   public boolean scramble() {
/* 131 */     return this.plugin.getConfig().getBoolean("reaction.scramble_custom_words");
/*     */   }
/*     */   
/*     */   public boolean ignoreCase() {
/* 135 */     return this.plugin.getConfig().getBoolean("reaction.ignore_case");
/*     */   }
/*     */   
/*     */   public boolean useCustomWords() {
/* 139 */     return this.plugin.getConfig().getBoolean("reaction.use_custom_words");
/*     */   }
/*     */   
/*     */   public int reactionInterval() {
/* 143 */     return this.plugin.getConfig().getInt("reaction.interval");
/*     */   }
/*     */   
/*     */   public int timeLimit() {
/* 147 */     return this.plugin.getConfig().getInt("reaction.time_limit");
/*     */   }
/*     */   
/*     */   public int playersNeeded() {
/* 151 */     return this.plugin.getConfig().getInt("reaction.players_needed_to_start");
/*     */   }
/*     */   
/*     */   public int wordLength() {
/* 155 */     return this.plugin.getConfig().getInt("reaction.word_character_length");
/*     */   }
/*     */   
/*     */   public int rewardAmt() {
/* 159 */     return this.plugin.getConfig().getInt("reaction.reward_amount");
/*     */   }
/*     */   
/*     */   public List<String> rewards() {
/* 163 */     return this.plugin.getConfig().getStringList("reaction.rewards");
/*     */   }
/*     */   
/*     */   public List<String> startMsg() {
/* 167 */     return this.plugin.getConfig().getStringList("messages.reaction_start");
/*     */   }
/*     */   
/*     */   public List<String> endMsg() {
/* 171 */     return this.plugin.getConfig().getStringList("messages.reaction_end");
/*     */   }
/*     */   
/*     */   public List<String> winMsg() {
/* 175 */     return this.plugin.getConfig().getStringList("messages.reaction_win");
/*     */   }
/*     */   
/*     */   public List<String> scrambleStartMsg() {
/* 179 */     return this.plugin.getConfig().getStringList("messages.scramble_start");
/*     */   }
/*     */   
/*     */   public List<String> scrambleEndMsg() {
/* 183 */     return this.plugin.getConfig().getStringList("messages.scramble_end");
/*     */   }
/*     */   
/*     */   public List<String> scrambleWinMsg() {
/* 187 */     return this.plugin.getConfig().getStringList("messages.scramble_win");
/*     */   }
/*     */ }


/* Location:              E:\Downloads\ChatReaction.jar!\me\clip\chatreaction\ReactionConfig.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */