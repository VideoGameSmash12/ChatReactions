/*     */ package me.clip.chatreaction;
/*     */ 
/*     */ import me.clip.chatreaction.reactionplayer.ReactionPlayer;
/*     */ import org.bukkit.Bukkit;
/*     */ import org.bukkit.ChatColor;
/*     */ import org.bukkit.command.Command;
/*     */ import org.bukkit.command.CommandExecutor;
/*     */ import org.bukkit.command.CommandSender;
/*     */ import org.bukkit.entity.Player;
/*     */ import org.bukkit.plugin.Plugin;
/*     */ 
/*     */ public class ReactionCommands
/*     */   implements CommandExecutor {
/*     */   private ChatReaction plugin;
/*     */   
/*     */   public ReactionCommands(ChatReaction i) {
/*  17 */     this.plugin = i;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean onCommand(CommandSender s, Command c, String label, String[] args) {
/*  24 */     if (args.length == 0) {
/*  25 */       sms(s, "&8&m+----------------+");
/*  26 */       sms(s, "&e&lChatReaction &f&o" + 
/*  27 */           this.plugin.getDescription().getVersion());
/*  28 */       sms(s, "&7Created by &f&oextended_clip");
/*  29 */       sms(s, "&8&m+----------------+");
/*  30 */       return true;
/*     */     } 
/*     */     
/*  33 */     if (!(s instanceof Player)) {
/*     */       
/*  35 */       if (args.length > 0 && args[0].equalsIgnoreCase("help")) {
/*     */         
/*  37 */         sms(s, "&8&m+----------------+");
/*  38 */         sms(s, "&e&lChatReaction &f&oHelp");
/*  39 */         sms(s, " ");
/*  40 */         sms(s, "&e/reaction");
/*  41 */         sms(s, "&f&oView plugin information");
/*  42 */         sms(s, "&e/reaction status");
/*  43 */         sms(s, "&f&oView current status");
/*  44 */         sms(s, "&e/reaction start");
/*  45 */         sms(s, "&f&oStart ChatReaction");
/*  46 */         sms(s, "&e/reaction stop");
/*  47 */         sms(s, "&f&oStop ChatReaction");
/*  48 */         sms(s, "&e/reaction reload");
/*  49 */         sms(s, "&f&oReload the config file");
/*  50 */         sms(s, "&8&m+----------------+");
/*  51 */         return true;
/*     */       } 
/*  53 */       if (args.length > 0 && args[0].equalsIgnoreCase("reload")) {
/*     */         
/*  55 */         this.plugin.reload();
/*  56 */         sms(s, "&8&m+----------------+");
/*  57 */         sms(s, "&e&lChatReaction &f&oReloaded");
/*  58 */         sms(s, "&8&m+----------------+");
/*  59 */         return true;
/*     */       } 
/*  61 */       if (args.length > 0 && args[0].equalsIgnoreCase("status")) {
/*     */         
/*  63 */         sms(s, "&8&m+----------------+");
/*  64 */         sms(s, "&e&lChatReaction &f&ostatus");
/*  65 */         sms(s, " ");
/*  66 */         if (ChatReaction.getIntervalTask() != null) {
/*     */           
/*  68 */           sms(s, "&fInterval task: &arunning");
/*     */           
/*  70 */           if (ChatReaction.isRunning() && ChatReaction.getCurrentWord() != null) {
/*  71 */             sms(s, "&fCurrent word: &a" + ChatReaction.getCurrentWord());
/*     */           }
/*     */           
/*  74 */           if (this.plugin.getOptions().useCustomWords()) {
/*     */ 
/*     */             
/*  77 */             sms(s, "&fUse custom words: &ayes");
/*     */             
/*  79 */             if (this.plugin.getOptions().customWords() != null) {
/*  80 */               sms(s, "&fWords loaded: &f" + this.plugin.getOptions().customWords().size());
/*     */             } else {
/*  82 */               sms(s, "&fWords loaded: &cnone");
/*     */             } 
/*     */           } else {
/*  85 */             sms(s, "&fUse custom words: &cno");
/*     */           } 
/*     */           
/*  88 */           sms(s, "&fPlayers needed to start: &7(&f" + 
/*  89 */               ChatReaction.getOnline() + "&7/&a" + 
/*  90 */               this.plugin.getOptions().playersNeeded() + "&7)");
/*     */         }
/*     */         else {
/*     */           
/*  94 */           sms(s, "&fInterval task: &cstopped");
/*     */         } 
/*  96 */         sms(s, "&8&m+----------------+");
/*  97 */         return true;
/*     */       } 
/*  99 */       if (args.length > 0 && args[0].equalsIgnoreCase("start")) {
/*     */         
/* 101 */         if (ChatReaction.getIntervalTask() == null) {
/*     */           
/* 103 */           this.plugin.start();
/*     */           
/* 105 */           sms(s, "&8&m+----------------+");
/* 106 */           sms(s, "&e&lChatReaction &f&ostarted");
/* 107 */           sms(s, "&8&m+----------------+");
/*     */         }
/*     */         else {
/*     */           
/* 111 */           sms(s, "&8&m+----------------+");
/* 112 */           sms(s, "&e&lChatReaction &f&ois already started");
/* 113 */           sms(s, "&8&m+----------------+");
/*     */         } 
/*     */ 
/*     */         
/* 117 */         return true;
/* 118 */       }  if (args.length > 0 && args[0].equalsIgnoreCase("stop")) {
/*     */         
/* 120 */         if (ChatReaction.getIntervalTask() != null) {
/*     */           
/* 122 */           this.plugin.stop();
/*     */           
/* 124 */           sms(s, "&8&m+----------------+");
/* 125 */           sms(s, "&e&lChatReaction &f&ostopped");
/* 126 */           sms(s, "&8&m+----------------+");
/*     */         }
/*     */         else {
/*     */           
/* 130 */           sms(s, "&8&m+----------------+");
/* 131 */           sms(s, "&e&lChatReaction &f&ois already stopped");
/* 132 */           sms(s, "&8&m+----------------+");
/*     */         } 
/*     */ 
/*     */         
/* 136 */         return true;
/*     */       } 
/*     */       
/* 139 */       sms(s, "&8&m+----------------+");
/* 140 */       sms(s, "&c&oIncorrect usage! Use &e/reaction help");
/* 141 */       sms(s, "&8&m+----------------+");
/* 142 */       return true;
/*     */     } 
/*     */ 
/*     */     
/* 146 */     Player p = (Player)s;
/*     */     
/* 148 */     if (args.length > 0 && args[0].equalsIgnoreCase("help")) {
/*     */       
/* 150 */       sms(s, "&8&m+----------------+");
/* 151 */       sms(s, "&e&lChatReaction &f&oHelp");
/* 152 */       sms(s, " ");
/* 153 */       sms(s, "&e/reaction");
/* 154 */       sms(s, "&f&oView plugin information");
/* 155 */       if (this.plugin.getOptions().trackStats()) {
/* 156 */         sms(s, "&e/reaction wins (player)");
/* 157 */         sms(s, "&f&oView your/others wins");
/* 158 */         sms(s, "&e/reaction top (page)");
/* 159 */         sms(s, "&f&oView the top winners");
/*     */       } 
/*     */       
/* 162 */       if (p.hasPermission("chatreaction.admin")) {
/* 163 */         sms(s, "&e/reaction status");
/* 164 */         sms(s, "&f&oView current status");
/* 165 */         sms(s, "&e/reaction start");
/* 166 */         sms(s, "&f&oStart ChatReaction");
/* 167 */         sms(s, "&e/reaction stop");
/* 168 */         sms(s, "&f&oStop ChatReaction");
/* 169 */         sms(s, "&e/reaction reload");
/* 170 */         sms(s, "&f&oReload the config file");
/*     */       } 
/*     */       
/* 173 */       sms(s, "&8&m+----------------+");
/* 174 */       return true;
/*     */     } 
/* 176 */     if (args.length > 0 && args[0].equalsIgnoreCase("wins")) {
/*     */       
/* 178 */       if (!this.plugin.getOptions().trackStats()) {
/* 179 */         sms(s, "&7Stats are currently disabled!");
/* 180 */         return true;
/*     */       } 
/*     */       
/* 183 */       if (args.length == 1) {
/*     */         
/* 185 */         if (!p.hasPermission("chatreaction.wins")) {
/* 186 */           sms(s, "&c&oYou don't have permission to do that!");
/* 187 */           return true;
/*     */         } 
/*     */         
/* 190 */         sms(s, "&8&m+----------------+");
/*     */         
/* 192 */         int i = 0;
/*     */         
/* 194 */         if (ChatReaction.hasData(p.getUniqueId().toString()))
/*     */         {
/* 196 */           i = ((ReactionPlayer)ChatReaction.reactionPlayers.get(
/* 197 */               p.getUniqueId().toString())).getWins();
/*     */         }
/*     */         
/* 200 */         sms(s, "&aYour reaction wins: &f" + i);
/*     */         
/* 202 */         sms(s, "&8&m+----------------+");
/* 203 */         return true;
/*     */       } 
/*     */       
/* 206 */       if (!p.hasPermission("chatreaction.wins.others")) {
/* 207 */         sms(s, "&c&oYou don't have permission to do that!");
/* 208 */         return true;
/*     */       } 
/*     */       
/* 211 */       final String playerName = args[1];
/*     */       
/* 213 */       Player target = Bukkit.getPlayer(playerName);
/*     */       
/* 215 */       if (target == null) {
/*     */         
/* 217 */         sms(s, String.valueOf(playerName) + 
/* 218 */             " &cis not online! Fetching from database!");
/*     */         
/* 220 */         if (ChatReaction.topPlayers != null && 
/* 221 */           !ChatReaction.topPlayers.isEmpty()) {
/*     */           
/* 223 */           for (ReactionPlayer pl : ChatReaction.topPlayers) {
/* 224 */             if (pl.getName().equalsIgnoreCase(playerName)) {
/* 225 */               sms(s, "&8&m+----------------+");
/* 226 */               sms(s, 
/* 227 */                   "&f" + pl.getName() + 
/* 228 */                   "'s &areaction wins: &f" + 
/* 229 */                   pl.getWins());
/* 230 */               sms(s, "&8&m+----------------+");
/* 231 */               return true;
/*     */             } 
/*     */           } 
/*     */           
/* 235 */           final String sender = s.getName();
/*     */           
/* 237 */           Bukkit.getScheduler().runTaskAsynchronously((Plugin)this.plugin, 
/* 238 */               new Runnable()
/*     */               {
/*     */                 
/*     */                 public void run()
/*     */                 {
/* 243 */                   final ReactionPlayer pl = ReactionCommands.this.plugin
/* 244 */                     .loadPlayerByName(playerName);
/*     */                   
/* 246 */                   Bukkit.getScheduler().runTask((Plugin)ReactionCommands.this.plugin, 
/* 247 */                       new Runnable()
/*     */                       {
/*     */                         
/*     */                         public void run()
/*     */                         {
/* 252 */                           Player waiting = 
/* 253 */                             Bukkit.getServer()
/* 254 */                             .getPlayer(
/* 255 */                               sender);
/*     */                           
/* 257 */                           if (waiting != null)
/*     */                           {
/* 259 */                             if (pl != null) {
/* 260 */                               ReactionCommands.null.access$0(ReactionCommands.null.this).sms((CommandSender)waiting, 
/* 261 */                                   "&8&m+----------------+");
/* 262 */                               ReactionCommands.null.access$0(ReactionCommands.null.this).sms((CommandSender)waiting, 
/* 263 */                                   "&f" + 
/* 264 */                                   pl.getName() + 
/* 265 */                                   "'s &areaction wins: &f" + 
/* 266 */                                   pl.getWins());
/* 267 */                               ReactionCommands.null.access$0(ReactionCommands.null.this).sms((CommandSender)waiting, 
/* 268 */                                   "&8&m+----------------+");
/*     */                             } else {
/* 270 */                               ReactionCommands.null.access$0(ReactionCommands.null.this).sms((CommandSender)waiting, 
/* 271 */                                   "&8&m+----------------+");
/* 272 */                               ReactionCommands.null.access$0(ReactionCommands.null.this).sms((CommandSender)waiting, 
/* 273 */                                   "&cNo win data saved for &f" + 
/* 274 */                                   playerName);
/* 275 */                               ReactionCommands.null.access$0(ReactionCommands.null.this).sms((CommandSender)waiting, 
/* 276 */                                   "&8&m+----------------+");
/*     */                             } 
/*     */                           }
/*     */                         }
/*     */                       });
/*     */                 }
/*     */               });
/*     */         } 
/* 284 */         return true;
/*     */       } 
/*     */       
/* 287 */       sms(s, "&8&m+----------------+");
/*     */       
/* 289 */       int wins = 0;
/*     */       
/* 291 */       if (ChatReaction.hasData(target.getUniqueId().toString()))
/*     */       {
/* 293 */         wins = ((ReactionPlayer)ChatReaction.reactionPlayers.get(
/* 294 */             target.getUniqueId().toString())).getWins();
/*     */       }
/*     */       
/* 297 */       sms(s, "&f" + target.getName() + "'s &areaction wins: &f" + 
/* 298 */           wins);
/*     */       
/* 300 */       sms(s, "&8&m+----------------+");
/*     */       
/* 302 */       return true;
/*     */     } 
/*     */     
/* 305 */     if (args.length > 0 && args[0].equalsIgnoreCase("top")) {
/*     */       
/* 307 */       if (!this.plugin.getOptions().trackStats()) {
/* 308 */         sms(s, "&7Stats are currently disabled!");
/* 309 */         return true;
/*     */       } 
/*     */       
/* 312 */       if (!p.hasPermission("chatreaction.top")) {
/* 313 */         sms(s, "&c&oYou don't have permission to do that!");
/* 314 */         return true;
/*     */       } 
/*     */       
/* 317 */       sms(s, "&8&m+----------------+");
/*     */       
/* 319 */       if (ChatReaction.topPlayers == null || 
/* 320 */         ChatReaction.topPlayers.isEmpty()) {
/* 321 */         sms(s, 
/* 322 */             "&fThere are no top players loaded at the moment. Try back later!");
/* 323 */         sms(s, "&8&m+----------------+");
/* 324 */         return true;
/*     */       } 
/*     */       
/* 327 */       int page = 1;
/*     */       
/* 329 */       if (args.length > 1) {
/*     */         
/*     */         try {
/* 332 */           page = Integer.parseInt(args[1]);
/* 333 */         } catch (Exception e) {
/*     */           
/* 335 */           sms(s, "&cPage &f" + args[1] + " &cis invalid!");
/*     */           
/* 337 */           sms(s, "&8&m+----------------+");
/* 338 */           return true;
/*     */         } 
/*     */       }
/*     */       
/* 342 */       int pages = ChatReaction.topPlayers.size() / 10;
/*     */       
/* 344 */       if (ChatReaction.topPlayers.size() % 10 > 0) {
/* 345 */         pages++;
/*     */       }
/*     */       
/* 348 */       if (pages < page) {
/* 349 */         sms(s, "&cIncorrect page. Max pages: &f" + pages);
/* 350 */         sms(s, "&8&m+----------------+");
/* 351 */         return true;
/*     */       } 
/*     */       
/* 354 */       int max = page * 10;
/* 355 */       int min = max - 10;
/*     */       
/* 357 */       int rank = 1;
/*     */       
/* 359 */       for (ReactionPlayer top : ChatReaction.topPlayers) {
/*     */         
/* 361 */         if (rank < min) {
/* 362 */           rank++;
/*     */           
/*     */           continue;
/*     */         } 
/* 366 */         if (rank > max) {
/*     */           break;
/*     */         }
/*     */         
/* 370 */         sms(s, 
/* 371 */             String.valueOf(rank) + "&7: &f" + top.getName() + " &aWins&7: &f" + 
/* 372 */             top.getWins());
/* 373 */         rank++;
/*     */       } 
/*     */       
/* 376 */       sms(s, "&8&m+----------------+");
/* 377 */       return true;
/*     */     } 
/*     */     
/* 380 */     if (!p.hasPermission("chatreaction.admin")) {
/* 381 */       sms(s, "&c&oYou don't have permission to do that!");
/* 382 */       return true;
/*     */     } 
/*     */     
/* 385 */     if (args.length > 0 && args[0].equalsIgnoreCase("reload")) {
/*     */       
/* 387 */       this.plugin.reload();
/* 388 */       sms(s, "&8&m+----------------+");
/* 389 */       sms(s, "&e&lChatReaction &f&oReloaded");
/* 390 */       sms(s, "&8&m+----------------+");
/* 391 */       return true;
/*     */     } 
/* 393 */     if (args.length > 0 && args[0].equalsIgnoreCase("status")) {
/*     */       
/* 395 */       sms(s, "&8&m+----------------+");
/* 396 */       sms(s, "&e&lChatReaction &f&ostatus");
/* 397 */       sms(s, " ");
/* 398 */       if (ChatReaction.getIntervalTask() != null) {
/*     */         
/* 400 */         sms(s, "&fInterval task: &arunning");
/*     */         
/* 402 */         if (ChatReaction.isRunning() && ChatReaction.getCurrentWord() != null) {
/* 403 */           sms(s, "&fCurrent word: &a" + ChatReaction.getCurrentWord());
/*     */         }
/*     */         
/* 406 */         if (this.plugin.getOptions().useCustomWords()) {
/*     */ 
/*     */           
/* 409 */           sms(s, "&fUse custom words: &ayes");
/*     */           
/* 411 */           if (this.plugin.getOptions().customWords() != null) {
/* 412 */             sms(s, "&fWords loaded: &f" + this.plugin.getOptions().customWords().size());
/*     */           } else {
/* 414 */             sms(s, "&fWords loaded: &cnone");
/*     */           } 
/*     */         } else {
/* 417 */           sms(s, "&fUse custom words: &cno");
/*     */         } 
/*     */         
/* 420 */         sms(s, "&fPlayers needed to start: &7(&f" + 
/* 421 */             ChatReaction.getOnline() + "&7/&a" + 
/* 422 */             this.plugin.getOptions().playersNeeded() + "&7)");
/*     */       }
/*     */       else {
/*     */         
/* 426 */         sms(s, "&fInterval task: &cstopped");
/*     */       } 
/* 428 */       sms(s, "&8&m+----------------+");
/* 429 */       return true;
/*     */     } 
/* 431 */     if (args.length > 0 && args[0].equalsIgnoreCase("start")) {
/*     */       
/* 433 */       if (ChatReaction.getIntervalTask() == null) {
/*     */         
/* 435 */         this.plugin.start();
/*     */         
/* 437 */         sms(s, "&8&m+----------------+");
/* 438 */         sms(s, "&e&lChatReaction &f&ostarted");
/* 439 */         sms(s, "&8&m+----------------+");
/*     */       }
/*     */       else {
/*     */         
/* 443 */         sms(s, "&8&m+----------------+");
/* 444 */         sms(s, "&e&lChatReaction &f&ois already started");
/* 445 */         sms(s, "&8&m+----------------+");
/*     */       } 
/*     */ 
/*     */       
/* 449 */       return true;
/* 450 */     }  if (args.length > 0 && args[0].equalsIgnoreCase("stop")) {
/*     */       
/* 452 */       if (ChatReaction.getIntervalTask() != null) {
/*     */         
/* 454 */         this.plugin.stop();
/*     */         
/* 456 */         sms(s, "&8&m+----------------+");
/* 457 */         sms(s, "&e&lChatReaction &f&ostopped");
/* 458 */         sms(s, "&8&m+----------------+");
/*     */       }
/*     */       else {
/*     */         
/* 462 */         sms(s, "&8&m+----------------+");
/* 463 */         sms(s, "&e&lChatReaction &f&ois already stopped");
/* 464 */         sms(s, "&8&m+----------------+");
/*     */       } 
/*     */ 
/*     */       
/* 468 */       return true;
/*     */     } 
/*     */     
/* 471 */     sms(s, "&8&m+----------------+");
/* 472 */     sms(s, "&c&oIncorrect usage! Use &e/reaction help");
/* 473 */     sms(s, "&8&m+----------------+");
/* 474 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void sms(CommandSender s, String msg) {
/* 481 */     s.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
/*     */   }
/*     */ }


/* Location:              E:\Downloads\ChatReaction.jar!\me\clip\chatreaction\ReactionCommands.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */