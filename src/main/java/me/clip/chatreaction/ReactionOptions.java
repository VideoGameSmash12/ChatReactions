/*     */ package me.clip.chatreaction;
/*     */ 
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ReactionOptions
/*     */ {
/*     */   private int interval;
/*     */   private int playersNeeded;
/*     */   private int timeLimit;
/*     */   private int charLength;
/*     */   private boolean ignoreCase;
/*     */   private int rewardAmt;
/*     */   private List<String> rewards;
/*     */   private List<String> startMsg;
/*     */   private String startTooltip;
/*     */   private List<String> endMsg;
/*     */   private List<String> winMsg;
/*     */   private List<String> scrambleStartMsg;
/*     */   private String scrambleStartTooltip;
/*     */   private List<String> scrambleEndMsg;
/*     */   private List<String> scrambleWinMsg;
/*     */   private boolean useCustomWords;
/*     */   private boolean scramble;
/*     */   private boolean scrambleSpaces;
/*     */   private boolean trackStats;
/*     */   private int topPlayersSize;
/*     */   private boolean debug;
/*     */   private boolean randomScramble;
/*     */   private List<String> customWords;
/*     */   private List<String> disabledWorlds;
/*     */   
/*     */   public ReactionOptions(ChatReaction plugin) {
/*  57 */     this.interval = plugin.config.reactionInterval();
/*  58 */     this.timeLimit = plugin.config.timeLimit();
/*  59 */     this.playersNeeded = plugin.config.playersNeeded();
/*  60 */     this.charLength = plugin.config.wordLength();
/*  61 */     this.ignoreCase = plugin.config.ignoreCase();
/*  62 */     this.rewardAmt = plugin.config.rewardAmt();
/*  63 */     this.rewards = plugin.config.rewards();
/*  64 */     this.useCustomWords = plugin.config.useCustomWords();
/*     */     
/*  66 */     if (this.useCustomWords) {
/*     */       
/*  68 */       List<String> words = plugin.wordFile.loadWords(plugin.config.splitWordsByLine());
/*     */       
/*  70 */       if (words == null || words.isEmpty()) {
/*     */         
/*  72 */         this.useCustomWords = false;
/*     */       } else {
/*     */         
/*  75 */         this.customWords = Collections.unmodifiableList(words);
/*     */       } 
/*     */     } 
/*     */     
/*  79 */     this.startMsg = plugin.config.startMsg();
/*  80 */     this.startTooltip = plugin.config.startTooltip();
/*  81 */     this.endMsg = plugin.config.endMsg();
/*  82 */     this.winMsg = plugin.config.winMsg();
/*  83 */     this.scramble = plugin.config.scramble();
/*  84 */     this.scrambleSpaces = plugin.config.scrambleSpaces();
/*  85 */     this.trackStats = plugin.config.trackStats();
/*  86 */     this.topPlayersSize = plugin.config.topPlayersSize();
/*  87 */     this.debug = plugin.config.debug();
/*  88 */     this.scrambleStartMsg = plugin.config.scrambleStartMsg();
/*  89 */     this.scrambleStartTooltip = plugin.config.scrambleStartTooltip();
/*  90 */     this.scrambleEndMsg = plugin.config.scrambleEndMsg();
/*     */     
/*  92 */     this.scrambleWinMsg = plugin.config.scrambleWinMsg();
/*     */     
/*  94 */     this.randomScramble = plugin.config.randomScramble();
/*     */     
/*  96 */     this.disabledWorlds = plugin.config.getDisabledWorlds();
/*     */   }
/*     */   
/*     */   public String startTooltip() {
/* 100 */     return this.startTooltip;
/*     */   }
/*     */   
/*     */   public String scrambleTooltip() {
/* 104 */     return this.scrambleStartTooltip;
/*     */   }
/*     */   
/*     */   public List<String> disabledWorlds() {
/* 108 */     return this.disabledWorlds;
/*     */   }
/*     */   
/*     */   public boolean randomScramble() {
/* 112 */     return this.randomScramble;
/*     */   }
/*     */   
/*     */   public List<String> scrambleStartMsg() {
/* 116 */     return this.scrambleStartMsg;
/*     */   }
/*     */   
/*     */   public List<String> scrambleEndMsg() {
/* 120 */     return this.scrambleEndMsg;
/*     */   }
/*     */   
/*     */   public List<String> scrambleWinMsg() {
/* 124 */     return this.scrambleWinMsg;
/*     */   }
/*     */   
/*     */   public boolean debug() {
/* 128 */     return this.debug;
/*     */   }
/*     */   
/*     */   public void setTrackStats(boolean b) {
/* 132 */     this.trackStats = b;
/*     */   }
/*     */   
/*     */   public boolean trackStats() {
/* 136 */     return this.trackStats;
/*     */   }
/*     */   
/*     */   public int topPlayersSize() {
/* 140 */     return this.topPlayersSize;
/*     */   }
/*     */   
/*     */   public boolean scrambleSpaces() {
/* 144 */     return this.scrambleSpaces;
/*     */   }
/*     */   
/*     */   public boolean scramble() {
/* 148 */     return this.scramble;
/*     */   }
/*     */   
/*     */   public boolean ignoreCase() {
/* 152 */     return this.ignoreCase;
/*     */   }
/*     */   
/*     */   public boolean useCustomWords() {
/* 156 */     return this.useCustomWords;
/*     */   }
/*     */   
/*     */   public List<String> customWords() {
/* 160 */     return this.customWords;
/*     */   }
/*     */   
/*     */   public int interval() {
/* 164 */     return this.interval;
/*     */   }
/*     */   
/*     */   public int playersNeeded() {
/* 168 */     return this.playersNeeded;
/*     */   }
/*     */   
/*     */   public int timeLimit() {
/* 172 */     return this.timeLimit;
/*     */   }
/*     */   
/*     */   public int charLength() {
/* 176 */     return this.charLength;
/*     */   }
/*     */   
/*     */   public int rewardAmt() {
/* 180 */     return this.rewardAmt;
/*     */   }
/*     */   
/*     */   public List<String> rewards() {
/* 184 */     return this.rewards;
/*     */   }
/*     */   
/*     */   public List<String> startMsg() {
/* 188 */     return this.startMsg;
/*     */   }
/*     */   
/*     */   public List<String> endMsg() {
/* 192 */     return this.endMsg;
/*     */   }
/*     */   
/*     */   public List<String> winMsg() {
/* 196 */     return this.winMsg;
/*     */   }
/*     */ }


/* Location:              E:\Downloads\ChatReaction.jar!\me\clip\chatreaction\ReactionOptions.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */