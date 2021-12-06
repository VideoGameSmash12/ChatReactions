/*     */ package me.clip.chatreaction.tasks;
/*     */ 
/*     */ import java.util.List;
/*     */ import java.util.Random;
/*     */ import me.clip.chatreaction.ChatReaction;
/*     */ import me.clip.chatreaction.events.ReactionStartEvent;
/*     */ import org.bukkit.Bukkit;
/*     */ import org.bukkit.event.Event;
/*     */ import org.bukkit.plugin.Plugin;
/*     */ 
/*     */ public class IntervalTask
/*     */   implements Runnable
/*     */ {
/*     */   ChatReaction plugin;
/*     */   Random r;
/*     */   
/*     */   public IntervalTask(ChatReaction instance) {
/*  18 */     this.plugin = instance;
/*  19 */     this.r = new Random();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void run() {
/*  25 */     if (ChatReaction.getOnline() < this.plugin.getOptions().playersNeeded()) {
/*     */       return;
/*     */     }
/*     */     
/*  29 */     if (ChatReaction.isRunning()) {
/*     */       
/*  31 */       ChatReaction.setRunning(false);
/*  32 */       ChatReaction.setScrambled(false);
/*  33 */       ChatReaction.setCurrentWord(null);
/*  34 */       ChatReaction.setDisplayWord(null);
/*     */       
/*  36 */       if (ChatReaction.getEndTask() != null) {
/*  37 */         ChatReaction.getEndTask().cancel();
/*  38 */         ChatReaction.setEndTask(null);
/*     */       } 
/*     */     } 
/*     */     
/*  42 */     String actual = this.plugin.pickWord();
/*     */     
/*  44 */     String show = actual;
/*     */     
/*  46 */     boolean scrambled = false;
/*     */     
/*  48 */     if (this.plugin.getOptions().useCustomWords())
/*     */     {
/*  50 */       if (this.plugin.getOptions().randomScramble()) {
/*     */         
/*  52 */         if (this.r.nextBoolean()) {
/*  53 */           scrambled = true;
/*  54 */           show = this.plugin.scramble(actual);
/*     */         }
/*     */       
/*  57 */       } else if (this.plugin.getOptions().scramble()) {
/*     */         
/*  59 */         scrambled = true;
/*  60 */         show = this.plugin.scramble(actual);
/*     */       } 
/*     */     }
/*     */     
/*  64 */     ReactionStartEvent event = new ReactionStartEvent(this.plugin.getOptions().timeLimit(), actual, show, scrambled);
/*     */     
/*  66 */     Bukkit.getPluginManager().callEvent((Event)event);
/*     */     
/*  68 */     if (event.isCancelled()) {
/*     */       return;
/*     */     }
/*     */     
/*  72 */     actual = event.getActualWord();
/*     */     
/*  74 */     show = event.getDisplayWord();
/*     */     
/*  76 */     scrambled = event.isScrambled();
/*     */     
/*  78 */     List<String> msg = null;
/*     */     
/*  80 */     String tip = null;
/*     */     
/*  82 */     if (event.showMessage()) {
/*     */       
/*  84 */       if (scrambled) {
/*  85 */         msg = this.plugin.getOptions().scrambleStartMsg();
/*  86 */         tip = this.plugin.getOptions().scrambleTooltip();
/*     */       } else {
/*  88 */         msg = this.plugin.getOptions().startMsg();
/*  89 */         tip = this.plugin.getOptions().startTooltip();
/*     */       } 
/*     */       
/*  92 */       if (tip == null || tip.isEmpty()) {
/*  93 */         tip = null;
/*     */       } else {
/*  95 */         tip = tip.replace("%word%", show).replace("%time%", (new StringBuilder(String.valueOf(this.plugin.getOptions().timeLimit()))).toString());
/*     */       } 
/*     */     } 
/*     */     
/*  99 */     ChatReaction.setScrambled(scrambled);
/* 100 */     ChatReaction.setCurrentWord(actual);
/* 101 */     ChatReaction.setDisplayWord(show);
/* 102 */     long time = System.currentTimeMillis();
/* 103 */     ChatReaction.setRunning(true);
/* 104 */     ChatReaction.setStartTime(time);
/*     */     
/* 106 */     if (msg != null && !msg.isEmpty()) {
/* 107 */       for (String line : msg) {
/* 108 */         this.plugin.sendMsg(line.replace("%word%", show).replace("%time%", (new StringBuilder(String.valueOf(this.plugin.getOptions().timeLimit()))).toString()), tip);
/*     */       }
/*     */     }
/*     */     
/* 112 */     ChatReaction.setEndTask(Bukkit.getScheduler().runTaskLaterAsynchronously((Plugin)this.plugin, new EndTask(this.plugin), (this.plugin.getOptions().timeLimit() * 20)));
/*     */   }
/*     */ }


/* Location:              E:\Downloads\ChatReaction.jar!\me\clip\chatreaction\tasks\IntervalTask.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */