/*    */ package me.clip.chatreaction.events;
/*    */ 
/*    */ import java.util.List;
/*    */ import org.bukkit.entity.Player;
/*    */ import org.bukkit.event.Event;
/*    */ import org.bukkit.event.HandlerList;
/*    */ 
/*    */ public class ReactionWinEvent
/*    */   extends Event
/*    */ {
/* 11 */   private static final HandlerList handlers = new HandlerList();
/*    */   
/*    */   private Player winner;
/*    */   
/*    */   private String word;
/*    */   
/*    */   private double time;
/*    */   
/*    */   private int wins;
/*    */   
/*    */   private boolean trackingWins;
/*    */   
/*    */   private boolean showWinMessage;
/*    */   
/*    */   private List<String> winMessage;
/*    */   
/*    */   public ReactionWinEvent(Player winner, String word, double time, boolean trackingWins, int wins, List<String> winMessage) {
/* 28 */     super(true);
/* 29 */     this.winner = winner;
/* 30 */     this.word = word;
/* 31 */     this.time = time;
/* 32 */     this.trackingWins = trackingWins;
/* 33 */     this.wins = wins;
/* 34 */     setWinMessage(winMessage);
/* 35 */     setShowWinMessage((winMessage != null && !winMessage.isEmpty()));
/*    */   }
/*    */ 
/*    */   
/*    */   public HandlerList getHandlers() {
/* 40 */     return handlers;
/*    */   }
/*    */   
/*    */   public static HandlerList getHandlerList() {
/* 44 */     return handlers;
/*    */   }
/*    */   
/*    */   public Player getWinner() {
/* 48 */     return this.winner;
/*    */   }
/*    */   
/*    */   public String getWord() {
/* 52 */     return this.word;
/*    */   }
/*    */   
/*    */   public double getTime() {
/* 56 */     return this.time;
/*    */   }
/*    */   
/*    */   public int getWins() {
/* 60 */     return this.wins;
/*    */   }
/*    */   
/*    */   public boolean isWinTrackingEnabled() {
/* 64 */     return this.trackingWins;
/*    */   }
/*    */   
/*    */   public boolean showWinMessage() {
/* 68 */     return this.showWinMessage;
/*    */   }
/*    */   
/*    */   public void setShowWinMessage(boolean showWinMessage) {
/* 72 */     this.showWinMessage = showWinMessage;
/*    */   }
/*    */   
/*    */   public List<String> getWinMessage() {
/* 76 */     return this.winMessage;
/*    */   }
/*    */   
/*    */   public void setWinMessage(List<String> winMessage) {
/* 80 */     this.winMessage = winMessage;
/*    */   }
/*    */ }


/* Location:              E:\Downloads\ChatReaction.jar!\me\clip\chatreaction\events\ReactionWinEvent.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */