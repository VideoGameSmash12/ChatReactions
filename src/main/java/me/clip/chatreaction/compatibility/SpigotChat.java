/*    */ package me.clip.chatreaction.compatibility;
/*    */ 
/*    */ import java.util.List;
/*    */ import me.clip.chatreaction.ChatReaction;
/*    */ import net.md_5.bungee.api.chat.BaseComponent;
/*    */ import net.md_5.bungee.api.chat.HoverEvent;
/*    */ import net.md_5.bungee.api.chat.TextComponent;
/*    */ import org.bukkit.Bukkit;
/*    */ import org.bukkit.World;
/*    */ import org.bukkit.entity.Player;
/*    */ 
/*    */ 
/*    */ public class SpigotChat
/*    */ {
/*    */   private ChatReaction plugin;
/*    */   
/*    */   public SpigotChat(ChatReaction instance) {
/* 18 */     this.plugin = instance;
/*    */   }
/*    */ 
/*    */   
/*    */   public void sendMessage(String message, String tooltip) {
/* 23 */     if (tooltip == null) {
/* 24 */       this.plugin.sendMsg(message);
/*    */       
/*    */       return;
/*    */     } 
/* 28 */     TextComponent com = new TextComponent(TextComponent.fromLegacyText(message));
/*    */     
/* 30 */     com.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText(tooltip)));
/*    */     
/* 32 */     List<String> disabled = this.plugin.getOptions().disabledWorlds();
/*    */     
/* 34 */     if (disabled == null || disabled.isEmpty()) {
/* 35 */       for (World w : Bukkit.getWorlds()) {
/*    */         
/* 37 */         if (w.getPlayers() == null || w.getPlayers().isEmpty()) {
/*    */           continue;
/*    */         }
/*    */         
/* 41 */         for (Player p : w.getPlayers()) {
/* 42 */           p.spigot().sendMessage((BaseComponent)com);
/*    */         }
/*    */       } 
/*    */       
/*    */       return;
/*    */     } 
/* 48 */     for (World w : Bukkit.getWorlds()) {
/*    */       
/* 50 */       if (disabled.contains(w.getName())) {
/*    */         continue;
/*    */       }
/*    */       
/* 54 */       if (w.getPlayers() == null || w.getPlayers().isEmpty()) {
/*    */         continue;
/*    */       }
/*    */       
/* 58 */       for (Player p : w.getPlayers())
/* 59 */         p.spigot().sendMessage((BaseComponent)com); 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              E:\Downloads\ChatReaction.jar!\me\clip\chatreaction\compatibility\SpigotChat.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */