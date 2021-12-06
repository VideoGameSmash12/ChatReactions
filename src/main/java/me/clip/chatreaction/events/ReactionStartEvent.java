/*    */ package me.clip.chatreaction.events;
/*    */ 
/*    */ import org.bukkit.event.Cancellable;
/*    */ import org.bukkit.event.Event;
/*    */ import org.bukkit.event.HandlerList;
/*    */ 
/*    */ public class ReactionStartEvent
/*    */   extends Event implements Cancellable {
/*  9 */   private static final HandlerList handlers = new HandlerList();
/*    */   
/*    */   private boolean cancelled;
/*    */   
/*    */   private long timeLimit;
/*    */   
/*    */   private String actualWord;
/*    */   
/*    */   private String displayWord;
/*    */   
/*    */   private boolean scrambled;
/*    */   
/*    */   private boolean showMessage;
/*    */   
/*    */   public ReactionStartEvent(long timeLimit, String actualWord, String displayWord, boolean scrambled) {
/* 24 */     super(true);
/* 25 */     setTimeLimit(timeLimit);
/* 26 */     setActualWord(actualWord);
/* 27 */     setDisplayWord(displayWord);
/* 28 */     setScrambled(scrambled);
/* 29 */     setShowMessage(true);
/*    */   }
/*    */ 
/*    */   
/*    */   public HandlerList getHandlers() {
/* 34 */     return handlers;
/*    */   }
/*    */   
/*    */   public static HandlerList getHandlerList() {
/* 38 */     return handlers;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isCancelled() {
/* 43 */     return this.cancelled;
/*    */   }
/*    */ 
/*    */   
/*    */   public void setCancelled(boolean cancelled) {
/* 48 */     this.cancelled = cancelled;
/*    */   }
/*    */   
/*    */   public String getActualWord() {
/* 52 */     return this.actualWord;
/*    */   }
/*    */   
/*    */   public void setActualWord(String actualWord) {
/* 56 */     this.actualWord = actualWord;
/*    */   }
/*    */   
/*    */   public String getDisplayWord() {
/* 60 */     return this.displayWord;
/*    */   }
/*    */   
/*    */   public void setDisplayWord(String displayWord) {
/* 64 */     this.displayWord = displayWord;
/*    */   }
/*    */   
/*    */   public boolean isScrambled() {
/* 68 */     return this.scrambled;
/*    */   }
/*    */   
/*    */   public void setScrambled(boolean scrambled) {
/* 72 */     this.scrambled = scrambled;
/*    */   }
/*    */   
/*    */   public boolean showMessage() {
/* 76 */     return this.showMessage;
/*    */   }
/*    */   
/*    */   public void setShowMessage(boolean showMessage) {
/* 80 */     this.showMessage = showMessage;
/*    */   }
/*    */   
/*    */   public long getTimeLimit() {
/* 84 */     return this.timeLimit;
/*    */   }
/*    */   
/*    */   public void setTimeLimit(long timeLimit) {
/* 88 */     this.timeLimit = timeLimit;
/*    */   }
/*    */ }


/* Location:              E:\Downloads\ChatReaction.jar!\me\clip\chatreaction\events\ReactionStartEvent.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */