/*    */ package me.clip.chatreaction.reactionplayer;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ReactionPlayer
/*    */ {
/*    */   private String uuid;
/*    */   private String name;
/*    */   private int wins;
/*    */   
/*    */   public ReactionPlayer(String uuid, String name, int wins) {
/* 12 */     setUuid(uuid);
/* 13 */     setName(name);
/* 14 */     setWins(wins);
/*    */   }
/*    */   
/*    */   public String getUuid() {
/* 18 */     return this.uuid;
/*    */   }
/*    */   
/*    */   public void setUuid(String uuid) {
/* 22 */     this.uuid = uuid;
/*    */   }
/*    */   
/*    */   public String getName() {
/* 26 */     return this.name;
/*    */   }
/*    */   
/*    */   public void setName(String name) {
/* 30 */     this.name = name;
/*    */   }
/*    */   
/*    */   public int getWins() {
/* 34 */     return this.wins;
/*    */   }
/*    */   
/*    */   public void setWins(int wins) {
/* 38 */     this.wins = wins;
/*    */   }
/*    */ }


/* Location:              E:\Downloads\ChatReaction.jar!\me\clip\chatreaction\reactionplayer\ReactionPlayer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */