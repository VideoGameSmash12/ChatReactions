/*    */ package me.clip.chatreaction.tasks;
/*    */ 
/*    */ import me.clip.chatreaction.ChatReaction;
/*    */ import me.clip.chatreaction.reactionplayer.ReactionPlayer;
/*    */ 
/*    */ public class LoadPlayerTask
/*    */   implements Runnable {
/*    */   private ChatReaction plugin;
/*    */   private String uuid;
/*    */   private String name;
/*    */   
/*    */   public LoadPlayerTask(ChatReaction instance, String uuid, String name) {
/* 13 */     this.plugin = instance;
/* 14 */     this.uuid = uuid;
/* 15 */     this.name = name;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void run() {
/* 21 */     if (!ChatReaction.hasData(this.uuid)) {
/*    */       
/* 23 */       ReactionPlayer pl = this.plugin.loadPlayer(this.uuid);
/*    */       
/* 25 */       if (pl == null) {
/* 26 */         pl = new ReactionPlayer(this.uuid, this.name, 0);
/* 27 */         ChatReaction.reactionPlayers.put(this.uuid, pl);
/*    */       } else {
/*    */         
/* 30 */         pl.setName(this.name);
/* 31 */         ChatReaction.reactionPlayers.put(this.uuid, pl);
/*    */       } 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              E:\Downloads\ChatReaction.jar!\me\clip\chatreaction\tasks\LoadPlayerTask.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */