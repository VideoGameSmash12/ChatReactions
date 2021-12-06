/*    */ package me.clip.chatreaction.tasks;
/*    */ 
/*    */ import java.util.List;
/*    */ import me.clip.chatreaction.ChatReaction;
/*    */ import me.clip.chatreaction.events.ReactionFailEvent;
/*    */ import org.bukkit.Bukkit;
/*    */ import org.bukkit.event.Event;
/*    */ 
/*    */ public class EndTask
/*    */   implements Runnable
/*    */ {
/*    */   private ChatReaction plugin;
/*    */   
/*    */   public EndTask(ChatReaction plugin) {
/* 15 */     this.plugin = plugin;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void run() {
/* 21 */     if (!ChatReaction.isRunning()) {
/*    */       return;
/*    */     }
/*    */     
/* 25 */     List<String> msg = null;
/*    */     
/* 27 */     if (ChatReaction.isScrambled()) {
/* 28 */       msg = this.plugin.getOptions().scrambleEndMsg();
/*    */     } else {
/* 30 */       msg = this.plugin.getOptions().endMsg();
/*    */     } 
/*    */     
/* 33 */     ReactionFailEvent event = new ReactionFailEvent(ChatReaction.getCurrentWord(), ChatReaction.getDisplayWord(), ChatReaction.isScrambled(), msg);
/*    */     
/* 35 */     Bukkit.getPluginManager().callEvent((Event)event);
/*    */     
/* 37 */     if (event.showFailMessage() && event.getFailMessage() != null) {
/* 38 */       for (String line : event.getFailMessage()) {
/* 39 */         this.plugin.sendMsg(line.replace("%word%", ChatReaction.getCurrentWord()));
/*    */       }
/*    */     }
/*    */     
/* 43 */     ChatReaction.setRunning(false);
/*    */     
/* 45 */     ChatReaction.setScrambled(false);
/*    */     
/* 47 */     ChatReaction.setStartTime(0L);
/*    */     
/* 49 */     ChatReaction.setCurrentWord(null);
/*    */     
/* 51 */     ChatReaction.setDisplayWord(null);
/*    */   }
/*    */ }


/* Location:              E:\Downloads\ChatReaction.jar!\me\clip\chatreaction\tasks\EndTask.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */