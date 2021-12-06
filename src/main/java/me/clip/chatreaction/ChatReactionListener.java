/*     */ package me.clip.chatreaction;
/*     */ 
/*     */ import java.util.List;
/*     */ import me.clip.chatreaction.events.ReactionWinEvent;
/*     */ import me.clip.chatreaction.reactionplayer.ReactionPlayer;
/*     */ import me.clip.chatreaction.tasks.AddWinTask;
/*     */ import me.clip.chatreaction.tasks.LoadPlayerTask;
/*     */ import me.clip.chatreaction.updater.SpigotUpdater;
/*     */ import org.bukkit.Bukkit;
/*     */ import org.bukkit.ChatColor;
/*     */ import org.bukkit.entity.Player;
/*     */ import org.bukkit.event.Event;
/*     */ import org.bukkit.event.EventHandler;
/*     */ import org.bukkit.event.EventPriority;
/*     */ import org.bukkit.event.Listener;
/*     */ import org.bukkit.event.player.AsyncPlayerChatEvent;
/*     */ import org.bukkit.event.player.PlayerJoinEvent;
/*     */ import org.bukkit.event.player.PlayerQuitEvent;
/*     */ import org.bukkit.plugin.Plugin;
/*     */ import org.bukkit.scheduler.BukkitTask;
/*     */ 
/*     */ public class ChatReactionListener implements Listener {
/*     */   private ChatReaction plugin;
/*     */   
/*     */   public ChatReactionListener(ChatReaction i) {
/*  26 */     this.plugin = i;
/*     */   }
/*     */ 
/*     */   
/*     */   @EventHandler
/*     */   public void onJoin(PlayerJoinEvent e) {
/*  32 */     if ((e.getPlayer().hasPermission("chatreaction.admin") || e.getPlayer().isOp()) && 
/*  33 */       this.plugin.updater != null && SpigotUpdater.updateAvailable()) {
/*  34 */       e.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', "&8&l» &7&oAn Update for &aChatReaction &7&ohas been found!"));
/*  35 */       e.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', "&8&l» &7Your Version: [&c" + this.plugin.getDescription().getVersion() + "&7] &8&l- &7New Version: [&c" + SpigotUpdater.getHighest() + "&7]"));
/*  36 */       e.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', "&8&l» &7&oDownload at &fhttp://www.spigotmc.org/resources/chatreaction.3748/"));
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/*  41 */     if (this.plugin.getOptions().trackStats()) {
/*  42 */       Bukkit.getScheduler().runTaskAsynchronously((Plugin)this.plugin, (Runnable)new LoadPlayerTask(this.plugin, e.getPlayer().getUniqueId().toString(), e.getPlayer().getName()));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @EventHandler
/*     */   public void onQuit(PlayerQuitEvent e) {
/*  50 */     if (this.plugin.getOptions().trackStats()) {
/*     */       
/*  52 */       String uuid = e.getPlayer().getUniqueId().toString();
/*     */       
/*  54 */       if (ChatReaction.hasData(uuid))
/*     */       {
/*  56 */         ChatReaction.reactionPlayers.remove(uuid);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   synchronized boolean handleReaction(Player p, String word) {
/*  63 */     if (ChatReaction.getCurrentWord() == null) {
/*  64 */       ChatReaction.setRunning(false);
/*  65 */       return false;
/*     */     } 
/*     */     
/*  68 */     word = ChatColor.translateAlternateColorCodes('&', word);
/*     */     
/*  70 */     word = ChatColor.stripColor(word);
/*     */     
/*  72 */     if (word.endsWith(".")) {
/*  73 */       word = word.substring(0, word.length() - 1);
/*     */     }
/*     */     
/*  76 */     if (this.plugin.getOptions().ignoreCase()) {
/*     */       
/*  78 */       if (!word.equalsIgnoreCase(ChatReaction.getCurrentWord())) {
/*  79 */         return false;
/*     */       
/*     */       }
/*     */     }
/*  83 */     else if (!word.equals(ChatReaction.getCurrentWord())) {
/*  84 */       return false;
/*     */     } 
/*     */ 
/*     */     
/*  88 */     if (ChatReaction.getEndTask() != null) {
/*  89 */       ChatReaction.getEndTask().cancel();
/*  90 */       ChatReaction.setEndTask((BukkitTask)null);
/*     */     } 
/*     */     
/*  93 */     String time = this.plugin.getTime(System.currentTimeMillis());
/*     */     
/*  95 */     int wins = 1;
/*     */     
/*  97 */     boolean trackWins = false;
/*     */     
/*  99 */     if (this.plugin.getOptions().trackStats()) {
/*     */       
/* 101 */       trackWins = true;
/*     */       
/* 103 */       if (ChatReaction.reactionPlayers != null && ChatReaction.reactionPlayers.containsKey(p.getUniqueId().toString())) {
/* 104 */         ReactionPlayer pl = ChatReaction.reactionPlayers.get(p.getUniqueId().toString());
/*     */         
/* 106 */         if (pl != null) {
/* 107 */           wins = pl.getWins() + 1;
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 112 */     List<String> winMsg = null;
/*     */     
/* 114 */     if (ChatReaction.isScrambled()) {
/* 115 */       winMsg = this.plugin.getOptions().scrambleWinMsg();
/*     */     } else {
/* 117 */       winMsg = this.plugin.getOptions().winMsg();
/*     */     } 
/*     */     
/* 120 */     ReactionWinEvent event = new ReactionWinEvent(p, word, Double.parseDouble(time), trackWins, wins, winMsg);
/*     */     
/* 122 */     Bukkit.getPluginManager().callEvent((Event)event);
/*     */     
/* 124 */     if (event.showWinMessage() && event.getWinMessage() != null) {
/* 125 */       for (String line : event.getWinMessage()) {
/* 126 */         this.plugin.sendMsg(line.replace("%player%", p.getName()).replace("%word%", word).replace("%time%", time));
/*     */       }
/*     */     }
/*     */     
/* 130 */     ChatReaction.setRunning(false);
/*     */     
/* 132 */     ChatReaction.setCurrentWord((String)null);
/*     */     
/* 134 */     ChatReaction.setDisplayWord((String)null);
/*     */     
/* 136 */     ChatReaction.setScrambled(false);
/*     */     
/* 138 */     ChatReaction.setStartTime(0L);
/*     */     
/* 140 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
/*     */   public void onChat(AsyncPlayerChatEvent e) {
/* 146 */     if (!ChatReaction.isRunning()) {
/*     */       return;
/*     */     }
/*     */     
/* 150 */     if (this.plugin.getOptions().disabledWorlds() != null && !this.plugin.getOptions().disabledWorlds().isEmpty())
/*     */     {
/* 152 */       if (this.plugin.getOptions().disabledWorlds().contains(e.getPlayer().getWorld().getName())) {
/*     */         return;
/*     */       }
/*     */     }
/*     */     
/* 157 */     if (handleReaction(e.getPlayer(), e.getMessage())) {
/*     */       
/* 159 */       final String name = e.getPlayer().getName();
/*     */       
/* 161 */       Bukkit.getScheduler().runTask((Plugin)this.plugin, new Runnable()
/*     */           {
/*     */             public void run()
/*     */             {
/* 165 */               ChatReactionListener.this.plugin.giveRewards(name);
/*     */             }
/*     */           });
/*     */ 
/*     */       
/* 170 */       if (this.plugin.getOptions().trackStats())
/*     */       {
/* 172 */         Bukkit.getScheduler().runTaskAsynchronously((Plugin)this.plugin, (Runnable)new AddWinTask(this.plugin, e.getPlayer().getUniqueId().toString(), e.getPlayer().getName()));
/*     */       }
/*     */       
/* 175 */       e.setCancelled(true);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              E:\Downloads\ChatReaction.jar!\me\clip\chatreaction\ChatReactionListener.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */