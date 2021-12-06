/*    */ package me.clip.chatreaction.tasks;
/*    */ 
/*    */ import me.clip.chatreaction.ChatReaction;
/*    */ import me.clip.chatreaction.reactionplayer.ReactionPlayer;
/*    */ 
/*    */ public class AddWinTask
/*    */   implements Runnable {
/*    */   private ChatReaction plugin;
/*    */   private String uuid;
/*    */   private String name;
/*    */   
/*    */   public AddWinTask(ChatReaction instance, String uuid, String name) {
/* 13 */     this.plugin = instance;
/* 14 */     this.uuid = uuid;
/* 15 */     this.name = name;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void run() {
/* 21 */     if (ChatReaction.hasData(this.uuid)) {
/*    */       
/* 23 */       ReactionPlayer pl = (ReactionPlayer)ChatReaction.reactionPlayers.get(this.uuid);
/* 24 */       pl.setName(this.name);
/* 25 */       pl.setWins(pl.getWins() + 1);
/* 26 */       this.plugin.savePlayer(pl);
/*    */     }
/*    */     else {
/*    */       
/* 30 */       ReactionPlayer pl = this.plugin.loadPlayer(this.uuid);
/*    */       
/* 32 */       if (pl == null) {
/*    */         
/* 34 */         pl = new ReactionPlayer(this.uuid, this.name, 1);
/* 35 */         ChatReaction.reactionPlayers.put(this.uuid, pl);
/* 36 */         this.plugin.savePlayer(pl);
/*    */       }
/*    */       else {
/*    */         
/* 40 */         pl.setWins(pl.getWins() + 1);
/* 41 */         pl.setName(this.name);
/* 42 */         ChatReaction.reactionPlayers.put(this.uuid, pl);
/* 43 */         this.plugin.savePlayer(pl);
/*    */       } 
/*    */     } 
/*    */ 
/*    */     
/* 48 */     this.plugin.updateTopWins();
/*    */   }
/*    */ }


/* Location:              E:\Downloads\ChatReaction.jar!\me\clip\chatreaction\tasks\AddWinTask.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */