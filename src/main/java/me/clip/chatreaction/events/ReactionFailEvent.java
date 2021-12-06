/*    */ package me.clip.chatreaction.events;
/*    */ 
/*    */ import java.util.List;
/*    */ import org.bukkit.event.Event;
/*    */ import org.bukkit.event.HandlerList;
/*    */ 
/*    */ public class ReactionFailEvent
/*    */   extends Event
/*    */ {
/* 10 */   private static final HandlerList handlers = new HandlerList();
/*    */   
/*    */   private String actualWord;
/*    */   
/*    */   private String displayWord;
/*    */   
/*    */   private boolean scrambled;
/*    */   
/*    */   private List<String> failMessage;
/*    */   
/*    */   private boolean showFailMessage;
/*    */   
/*    */   public ReactionFailEvent(String actualWord, String displayWord, boolean scrambled, List<String> failMessage) {
/* 23 */     super(true);
/* 24 */     this.actualWord = actualWord;
/* 25 */     this.displayWord = displayWord;
/* 26 */     this.scrambled = scrambled;
/* 27 */     setFailMessage(failMessage);
/* 28 */     setShowFailMessage((failMessage != null && !failMessage.isEmpty()));
/*    */   }
/*    */ 
/*    */   
/*    */   public HandlerList getHandlers() {
/* 33 */     return handlers;
/*    */   }
/*    */   
/*    */   public static HandlerList getHandlerList() {
/* 37 */     return handlers;
/*    */   }
/*    */   
/*    */   public String getActualWord() {
/* 41 */     return this.actualWord;
/*    */   }
/*    */   
/*    */   public String getDisplayWord() {
/* 45 */     return this.displayWord;
/*    */   }
/*    */   
/*    */   public boolean isScrambled() {
/* 49 */     return this.scrambled;
/*    */   }
/*    */   
/*    */   public List<String> getFailMessage() {
/* 53 */     return this.failMessage;
/*    */   }
/*    */   
/*    */   public void setFailMessage(List<String> failMessage) {
/* 57 */     this.failMessage = failMessage;
/*    */   }
/*    */   
/*    */   public boolean showFailMessage() {
/* 61 */     return this.showFailMessage;
/*    */   }
/*    */   
/*    */   public void setShowFailMessage(boolean showFailMessage) {
/* 65 */     this.showFailMessage = showFailMessage;
/*    */   }
/*    */ }


/* Location:              E:\Downloads\ChatReaction.jar!\me\clip\chatreaction\events\ReactionFailEvent.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */