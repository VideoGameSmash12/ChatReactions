/*    */ package me.clip.chatreaction.hooks;
/*    */ 
/*    */ import net.milkbowl.vault.economy.Economy;
/*    */ import org.bukkit.Bukkit;
/*    */ import org.bukkit.plugin.RegisteredServiceProvider;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class VaultHook
/*    */ {
/*    */   private Economy econ;
/*    */   
/*    */   public boolean setup() {
/* 17 */     RegisteredServiceProvider<Economy> rsp = Bukkit.getServer()
/* 18 */       .getServicesManager().getRegistration(Economy.class);
/*    */     
/* 20 */     if (rsp == null) {
/* 21 */       return false;
/*    */     }
/*    */     
/* 24 */     this.econ = (Economy)rsp.getProvider();
/*    */     
/* 26 */     return (this.econ != null);
/*    */   }
/*    */   
/*    */   public boolean isAvailable() {
/* 30 */     return (this.econ != null);
/*    */   }
/*    */   
/*    */   public Economy getEcon() {
/* 34 */     return this.econ;
/*    */   }
/*    */ }


/* Location:              E:\Downloads\ChatReaction.jar!\me\clip\chatreaction\hooks\VaultHook.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */