/*     */ package me.clip.chatreaction;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import me.clip.chatreaction.reactionplayer.ReactionPlayer;
/*     */ import org.bukkit.OfflinePlayer;
/*     */ import org.bukkit.entity.Player;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ReactionAPI
/*     */ {
/*     */   public static boolean isStarted() {
/*  19 */     return ChatReaction.isRunning();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String getReactionWord() {
/*  27 */     return ChatReaction.getCurrentWord();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String getDisplayWord() {
/*  35 */     return ChatReaction.getDisplayWord();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static long getStartTime() {
/*  43 */     return ChatReaction.getStartTime();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int getWins(Player p) {
/*  53 */     String uuid = p.getUniqueId().toString();
/*     */     
/*  55 */     if (ChatReaction.hasData(uuid)) {
/*  56 */       return ((ReactionPlayer)ChatReaction.reactionPlayers.get(uuid)).getWins();
/*     */     }
/*     */     
/*  59 */     return 0;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int getWins(OfflinePlayer p) {
/*  69 */     String uuid = p.getUniqueId().toString();
/*     */     
/*  71 */     if (ChatReaction.hasData(uuid)) {
/*  72 */       return ((ReactionPlayer)ChatReaction.reactionPlayers.get(uuid)).getWins();
/*     */     }
/*     */     
/*  75 */     return 0;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<ReactionPlayer> getTopWinners() {
/*  83 */     if (ChatReaction.topPlayers == null || ChatReaction.topPlayers.isEmpty()) {
/*  84 */       return null;
/*     */     }
/*  86 */     return new ArrayList<>(ChatReaction.topPlayers);
/*     */   }
/*     */   
/*     */   public static int getTopRank(Player p) {
/*  90 */     if (ChatReaction.topPlayers == null || ChatReaction.topPlayers.isEmpty()) {
/*  91 */       return -1;
/*     */     }
/*  93 */     Iterator<ReactionPlayer> check = ChatReaction.topPlayers.iterator();
/*  94 */     while (check.hasNext()) {
/*  95 */       ReactionPlayer localPlayer = check.next();
/*     */       
/*  97 */       if (localPlayer.getUuid().equals(p.getUniqueId().toString())) {
/*  98 */         return localPlayer.getWins();
/*     */       }
/*     */     } 
/* 101 */     return -1;
/*     */   }
/*     */   
/*     */   public static int getTopRank(OfflinePlayer p) {
/* 105 */     if (ChatReaction.topPlayers == null || ChatReaction.topPlayers.isEmpty()) {
/* 106 */       return -1;
/*     */     }
/* 108 */     Iterator<ReactionPlayer> check = ChatReaction.topPlayers.iterator();
/* 109 */     while (check.hasNext()) {
/* 110 */       ReactionPlayer localPlayer = check.next();
/*     */       
/* 112 */       if (localPlayer.getUuid().equals(p.getUniqueId().toString())) {
/* 113 */         return localPlayer.getWins();
/*     */       }
/*     */     } 
/* 116 */     return -1;
/*     */   }
/*     */ }


/* Location:              E:\Downloads\ChatReaction.jar!\me\clip\chatreaction\ReactionAPI.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */