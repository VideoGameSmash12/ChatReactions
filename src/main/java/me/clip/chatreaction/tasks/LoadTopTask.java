/*    */ package me.clip.chatreaction.tasks;
/*    */ 
/*    */ import me.clip.chatreaction.ChatReaction;
/*    */ 
/*    */ public class LoadTopTask
/*    */   implements Runnable {
/*    */   private ChatReaction plugin;
/*    */   
/*    */   public LoadTopTask(ChatReaction instance) {
/* 10 */     this.plugin = instance;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void run() {
/* 16 */     this.plugin.updateTopWins();
/*    */   }
/*    */ }


/* Location:              E:\Downloads\ChatReaction.jar!\me\clip\chatreaction\tasks\LoadTopTask.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */