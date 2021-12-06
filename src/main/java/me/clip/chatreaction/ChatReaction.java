/*     */ package me.clip.chatreaction;
/*     */ 
/*     */ import java.lang.reflect.Method;
/*     */ import java.sql.PreparedStatement;
/*     */ import java.sql.ResultSet;
/*     */ import java.sql.SQLException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.ThreadLocalRandom;
/*     */ import java.util.logging.Level;
/*     */ import me.clip.chatreaction.compatibility.SpigotChat;
/*     */ import me.clip.chatreaction.database.Database;
/*     */ import me.clip.chatreaction.database.MySQL;
/*     */ import me.clip.chatreaction.events.ReactionFailEvent;
/*     */ import me.clip.chatreaction.hooks.VaultHook;
/*     */ import me.clip.chatreaction.reactionplayer.ReactionPlayer;
/*     */ import me.clip.chatreaction.tasks.IntervalTask;
/*     */ import me.clip.chatreaction.tasks.LoadTopTask;
/*     */ import me.clip.chatreaction.updater.SpigotUpdater;
/*     */ import org.bukkit.Bukkit;
/*     */ import org.bukkit.ChatColor;
/*     */ import org.bukkit.World;
/*     */ import org.bukkit.command.CommandSender;
/*     */ import org.bukkit.entity.Player;
/*     */ import org.bukkit.event.Event;
/*     */ import org.bukkit.plugin.Plugin;
/*     */ import org.bukkit.plugin.java.JavaPlugin;
/*     */ import org.bukkit.scheduler.BukkitTask;
/*     */ 
/*     */ public class ChatReaction
/*     */   extends JavaPlugin
/*     */ {
/*  37 */   public ReactionConfig config = new ReactionConfig(this);
/*     */   
/*  39 */   public WordFile wordFile = new WordFile(this);
/*     */   
/*     */   private ReactionOptions options;
/*     */   
/*  43 */   final String CHARS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
/*     */   
/*     */   private static boolean isRunning;
/*     */   
/*     */   private static boolean scrambled;
/*     */   
/*  49 */   private static String currentWord = null;
/*     */   
/*  51 */   private static String displayWord = null;
/*     */   
/*     */   private static long startTime;
/*     */   
/*  55 */   private static BukkitTask iTask = null;
/*     */   
/*  57 */   private static BukkitTask endTask = null;
/*     */   
/*  59 */   protected static Database database = null;
/*     */   
/*  61 */   protected SpigotUpdater updater = null;
/*     */   
/*  63 */   public static Map<String, ReactionPlayer> reactionPlayers = new HashMap<>();
/*     */   
/*  65 */   public static List<ReactionPlayer> topPlayers = new ArrayList<>();
/*     */ 
/*     */   
/*     */   private SpigotChat chat;
/*     */   
/*     */   private static boolean useSpigotChat = false;
/*     */   
/*     */   private VaultHook vaultHook;
/*     */ 
/*     */   
/*     */   public void onEnable() {
/*  76 */     if (checkSpigot()) {
/*  77 */       getLogger().info("You are using Spigot! Tooltips in reaction start messages are enabled!");
/*     */     } else {
/*  79 */       getLogger().warning("You are not using Spigot! Tooltips in chat will not work with your server software!");
/*     */     } 
/*     */     
/*  82 */     this.config.loadCfg();
/*     */     
/*  84 */     this.options = new ReactionOptions(this);
/*     */     
/*  86 */     Bukkit.getPluginManager().registerEvents(new ChatReactionListener(this), (Plugin)this);
/*     */     
/*  88 */     getCommand("reaction").setExecutor(new ReactionCommands(this));
/*     */     
/*  90 */     start();
/*     */     
/*  92 */     if (startDatabase()) {
/*  93 */       Bukkit.getScheduler().runTaskLaterAsynchronously((Plugin)this, (Runnable)new LoadTopTask(this), 20L);
/*     */     }
/*     */ 
/*     */     
/*  97 */     if (getConfig().getBoolean("check_updates")) {
/*     */       
/*  99 */       this.updater = new SpigotUpdater(this);
/*     */       
/* 101 */       this.updater.checkUpdates();
/*     */       
/* 103 */       if (SpigotUpdater.updateAvailable()) {
/*     */         
/* 105 */         System.out.println("---------------------------");
/* 106 */         System.out.println("     ChatReaction Updater");
/* 107 */         System.out.println(" ");
/* 108 */         System.out.println("An update for ChatReaction has been found!");
/* 109 */         System.out.println("ChatReaction " + SpigotUpdater.getHighest());
/* 110 */         System.out.println("You are running " + getDescription().getVersion());
/* 111 */         System.out.println(" ");
/* 112 */         System.out.println("Download at http://www.spigotmc.org/resources/chatreaction.3748/");
/* 113 */         System.out.println("---------------------------");
/*     */       } else {
/*     */         
/* 116 */         System.out.println("---------------------------");
/* 117 */         System.out.println("     ChatReaction Updater");
/* 118 */         System.out.println(" ");
/* 119 */         System.out.println("You are running " + getDescription().getVersion());
/* 120 */         System.out.println("The latest version");
/* 121 */         System.out.println("of ChatReaction!");
/* 122 */         System.out.println(" ");
/* 123 */         System.out.println("---------------------------");
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 128 */     setupHooks();
/*     */   }
/*     */ 
/*     */   
/*     */   private void setupHooks() {
/* 133 */     if (Bukkit.getPluginManager().getPlugin("Vault") != null) {
/* 134 */       this.vaultHook = new VaultHook();
/* 135 */       if (!this.vaultHook.setup()) {
/* 136 */         this.vaultHook = null;
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   private boolean checkSpigot() {
/*     */     try {
/* 143 */       Class.forName("org.spigotmc.SpigotConfig");
/* 144 */       this.chat = new SpigotChat(this);
/* 145 */       useSpigotChat = true;
/* 146 */     } catch (Exception exception) {
/* 147 */       useSpigotChat = false;
/*     */     } 
/* 149 */     return (useSpigotChat && this.chat != null);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void onDisable() {
/* 155 */     if (getOptions().trackStats() && database != null) {
/* 156 */       database.close();
/* 157 */       database = null;
/*     */     } 
/*     */     
/* 160 */     stop();
/*     */     
/* 162 */     Bukkit.getScheduler().cancelTasks((Plugin)this);
/* 163 */     this.options = null;
/* 164 */     setCurrentWord((String)null);
/* 165 */     setDisplayWord((String)null);
/* 166 */     reactionPlayers = null;
/* 167 */     topPlayers = null;
/*     */   }
/*     */   
/*     */   public void debug(Level lv, String msg) {
/* 171 */     if (getOptions().debug()) {
/* 172 */       System.out.println("[RDebug " + lv.getName() + "] " + msg);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean startDatabase() {
/* 178 */     if (!getOptions().trackStats()) {
/* 179 */       debug(Level.INFO, "track stats was set to false in the config!");
/* 180 */       getLogger().info("Reaction stats are disabled!!");
/* 181 */       return false;
/*     */     } 
/*     */     
/*     */     try {
/* 185 */       getLogger().info("Creating MySQL connection ...");
/* 186 */       database = (Database)new MySQL(getConfig().getString("reaction_stats.prefix"), 
/* 187 */           getConfig().getString("reaction_stats.hostname"), (new StringBuilder(
/* 188 */             String.valueOf(getConfig().getInt("reaction_stats.port")))).toString(), getConfig()
/* 189 */           .getString("reaction_stats.database"), getConfig()
/* 190 */           .getString("reaction_stats.username"), getConfig()
/* 191 */           .getString("reaction_stats.password"), 
/* 192 */           getConfig().getBoolean("reaction_stats.ssl", false));
/* 193 */       database.open();
/*     */       
/* 195 */       if (!database.checkTable(String.valueOf(database.getTablePrefix()) + "reactionstats")) {
/*     */         
/* 197 */         getLogger().info("Creating MySQL table ...");
/* 198 */         database.createTable("CREATE TABLE IF NOT EXISTS `" + 
/* 199 */             database.getTablePrefix() + "reactionstats` (" + 
/* 200 */             "  `uuid` varchar(50) NOT NULL," + 
/* 201 */             "  `name` varchar(50) NOT NULL," + 
/* 202 */             "  `wins` integer NOT NULL," + 
/* 203 */             "  PRIMARY KEY (`uuid`)" + 
/* 204 */             ") ENGINE=InnoDB DEFAULT CHARSET=latin1;");
/*     */       } 
/* 206 */       return true;
/* 207 */     } catch (Exception ex) {
/* 208 */       ex.printStackTrace();
/* 209 */       database = null;
/* 210 */       getOptions().setTrackStats(false);
/* 211 */       getLogger().warning("Could not connect to database! Reaction stats will be disabled!!");
/* 212 */       return false;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void savePlayer(ReactionPlayer pl) {
/* 219 */     if (pl == null || pl.getUuid() == null || pl.getName() == null) {
/*     */       return;
/*     */     }
/*     */     
/* 223 */     if (getOptions().trackStats() && database != null) {
/*     */       
/* 225 */       debug(Level.INFO, "Initializing save for player: " + pl.getName());
/*     */       
/* 227 */       if (database.getConnection() == null) {
/* 228 */         getLogger().warning("Could not connect to database! Failed to save data for player: " + pl.getName() + "!");
/*     */         
/*     */         return;
/*     */       } 
/*     */       
/*     */       try {
/* 234 */         String query = "SELECT wins,name FROM `" + database.getTablePrefix() + "reactionstats` WHERE uuid=?";
/* 235 */         PreparedStatement statement = database.prepare(query);
/* 236 */         statement.setString(1, pl.getUuid());
/* 237 */         ResultSet result = statement.executeQuery();
/*     */         
/* 239 */         if (result.next()) {
/* 240 */           debug(Level.INFO, "Player has stats stored, updating stats.");
/* 241 */           query = "UPDATE `" + database.getTablePrefix() + "reactionstats` SET wins=?,name=? WHERE uuid=?";
/* 242 */           PreparedStatement updateStatement = database.prepare(query);
/* 243 */           updateStatement.setInt(1, pl.getWins());
/* 244 */           updateStatement.setString(2, pl.getName());
/* 245 */           updateStatement.setString(3, pl.getUuid());
/* 246 */           updateStatement.execute();
/* 247 */           updateStatement.close();
/*     */         } else {
/* 249 */           debug(Level.INFO, "Player does not have any stats stored, inserting stats.");
/* 250 */           query = "INSERT INTO `" + database.getTablePrefix() + "reactionstats` (uuid,name,wins) VALUES (?,?,?)";
/* 251 */           PreparedStatement insertStatement = database.prepare(query);
/* 252 */           insertStatement.setString(1, pl.getUuid());
/* 253 */           insertStatement.setString(2, pl.getName());
/* 254 */           insertStatement.setInt(3, pl.getWins());
/* 255 */           insertStatement.execute();
/* 256 */           insertStatement.close();
/*     */         } 
/*     */         
/* 259 */         statement.close();
/* 260 */         result.close();
/*     */       }
/* 262 */       catch (Exception ex) {
/* 263 */         ex.printStackTrace();
/*     */       } 
/*     */       
/* 266 */       database.close();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void updateTopWins() {
/* 276 */     if (database == null || !getOptions().trackStats()) {
/*     */       return;
/*     */     }
/*     */     
/* 280 */     debug(Level.INFO, "Updating top players....");
/*     */     
/* 282 */     if (database.getConnection() == null) {
/* 283 */       getLogger().warning("Could not connect to database! Failed to update top wins!");
/*     */       
/*     */       return;
/*     */     } 
/* 287 */     List<ReactionPlayer> top = new ArrayList<>();
/*     */ 
/*     */     
/*     */     try {
/* 291 */       String query = "SELECT uuid,name,wins FROM `" + database.getTablePrefix() + "reactionstats` ORDER BY wins DESC LIMIT " + getOptions().topPlayersSize() + ";";
/* 292 */       PreparedStatement statement = database.prepare(query);
/* 293 */       ResultSet result = statement.executeQuery();
/*     */       
/* 295 */       while (result.next()) {
/* 296 */         String uuid = result.getString(1);
/* 297 */         String name = result.getString(2);
/* 298 */         int wins = result.getInt(3);
/* 299 */         top.add(new ReactionPlayer(uuid, name, wins));
/*     */       } 
/*     */       
/* 302 */       if (top != null && !top.isEmpty()) {
/* 303 */         debug(Level.INFO, String.valueOf(top.size()) + " top wins loaded!");
/*     */       } else {
/* 305 */         debug(Level.INFO, "No top wins loaded!");
/*     */       } 
/*     */       
/* 308 */       topPlayers = top;
/* 309 */       statement.close();
/* 310 */       result.close();
/*     */     }
/* 312 */     catch (Exception ex) {
/* 313 */       ex.printStackTrace();
/*     */     } 
/*     */     
/* 316 */     database.close();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ReactionPlayer loadPlayer(String uuid) {
/* 327 */     if (!getOptions().trackStats() || database == null) {
/* 328 */       return null;
/*     */     }
/*     */     
/* 331 */     debug(Level.INFO, "Loading wins for uuid: " + uuid);
/*     */     
/* 333 */     if (database.getConnection() == null) {
/* 334 */       getLogger().warning("Could not connect to database! Failed to load data for uuid: " + uuid + "!");
/* 335 */       return null;
/*     */     } 
/*     */     
/*     */     try {
/* 339 */       if (database.getConnection().isClosed()) {
/* 340 */         database.open();
/*     */       }
/* 342 */     } catch (SQLException e) {
/* 343 */       e.printStackTrace();
/* 344 */       return null;
/*     */     } 
/*     */     
/*     */     try {
/* 348 */       String query = "SELECT * FROM `" + database.getTablePrefix() + "reactionstats` WHERE uuid=?";
/* 349 */       PreparedStatement statement = database.getConnection().prepareStatement(query);
/* 350 */       statement.setString(1, uuid);
/* 351 */       ResultSet result = statement.executeQuery();
/*     */       
/* 353 */       if (result.next()) {
/* 354 */         debug(Level.INFO, "Player data found! Loading data...");
/* 355 */         String uid = result.getString(1);
/* 356 */         String name = result.getString(2);
/* 357 */         int wins = result.getInt(3);
/* 358 */         statement.close();
/* 359 */         result.close();
/* 360 */         database.close();
/* 361 */         return new ReactionPlayer(uid, name, wins);
/*     */       } 
/*     */       
/* 364 */       statement.close();
/* 365 */       result.close();
/*     */     }
/* 367 */     catch (Exception ex) {
/* 368 */       ex.printStackTrace();
/*     */     } 
/*     */     
/* 371 */     debug(Level.INFO, "Player data was not found for uuid " + uuid);
/* 372 */     database.close();
/* 373 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ReactionPlayer loadPlayerByName(String playerName) {
/* 384 */     if (!getOptions().trackStats() || database == null) {
/* 385 */       return null;
/*     */     }
/*     */     
/* 388 */     debug(Level.INFO, "Loading wins for player: " + playerName);
/*     */     
/* 390 */     if (database.getConnection() == null) {
/* 391 */       getLogger().warning("Could not connect to database! Failed to load data for player: " + playerName + "!");
/* 392 */       return null;
/*     */     } 
/*     */     
/*     */     try {
/* 396 */       String query = "SELECT * FROM `" + database.getTablePrefix() + "reactionstats` WHERE name=?";
/* 397 */       PreparedStatement statement = database.prepare(query);
/* 398 */       statement.setString(1, playerName);
/* 399 */       ResultSet result = statement.executeQuery();
/*     */       
/* 401 */       if (result.next()) {
/* 402 */         debug(Level.INFO, "Player data found! Loading data...");
/* 403 */         String uid = result.getString(1);
/* 404 */         String name = result.getString(2);
/* 405 */         int wins = result.getInt(3);
/* 406 */         statement.close();
/* 407 */         result.close();
/* 408 */         database.close();
/* 409 */         return new ReactionPlayer(uid, name, wins);
/*     */       } 
/*     */       
/* 412 */       statement.close();
/* 413 */       result.close();
/*     */     }
/* 415 */     catch (Exception ex) {
/* 416 */       ex.printStackTrace();
/*     */     } 
/* 418 */     debug(Level.INFO, "Player data was not found for player " + playerName);
/* 419 */     database.close();
/* 420 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void reload() {
/* 426 */     if (isRunning()) {
/* 427 */       Bukkit.getScheduler().runTaskAsynchronously((Plugin)this, new Runnable()
/*     */           {
/*     */             public void run() {
/* 430 */               Bukkit.getPluginManager().callEvent((Event)new ReactionFailEvent(ChatReaction.getCurrentWord(), ChatReaction.getDisplayWord(), ChatReaction.isScrambled(), null));
/*     */             }
/*     */           });
/*     */     }
/*     */     
/* 435 */     stop();
/* 436 */     Bukkit.getScheduler().cancelTasks((Plugin)this);
/* 437 */     reloadConfig();
/* 438 */     saveConfig();
/* 439 */     this.options = new ReactionOptions(this);
/* 440 */     start();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void start() {
/* 445 */     if (endTask != null) {
/* 446 */       debug(Level.INFO, "Cancelling previous reaction end task");
/* 447 */       endTask.cancel();
/* 448 */       endTask = null;
/*     */     } 
/*     */     
/* 451 */     if (iTask == null) {
/* 452 */       debug(Level.INFO, "Starting interval task!");
/* 453 */       iTask = Bukkit.getScheduler().runTaskTimerAsynchronously((Plugin)this, 
/* 454 */           (Runnable)new IntervalTask(this), (getOptions().interval() * 20), (
/* 455 */           getOptions().interval() * 20));
/*     */     } else {
/* 457 */       debug(Level.INFO, "Stopping old interval task!");
/* 458 */       iTask.cancel();
/* 459 */       iTask = null;
/* 460 */       debug(Level.INFO, "Starting new interval task!");
/* 461 */       iTask = Bukkit.getScheduler().runTaskTimerAsynchronously((Plugin)this, 
/* 462 */           (Runnable)new IntervalTask(this), (getOptions().interval() * 20), (
/* 463 */           getOptions().interval() * 20));
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void stop() {
/* 469 */     if (endTask != null) {
/* 470 */       debug(Level.INFO, "Stopping reaction end task!");
/* 471 */       endTask.cancel();
/* 472 */       endTask = null;
/*     */     } 
/* 474 */     if (iTask != null) {
/* 475 */       debug(Level.INFO, "Stopping reaction interval task!");
/* 476 */       iTask.cancel();
/* 477 */       iTask = null;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public String scramble(String input) {
/* 483 */     if (!input.contains(" ")) {
/*     */       
/* 485 */       List<Character> characters = new ArrayList<>(); byte b1; int j;
/*     */       char[] arrayOfChar;
/* 487 */       for (j = (arrayOfChar = input.toCharArray()).length, b1 = 0; b1 < j; ) { char c = arrayOfChar[b1];
/* 488 */         characters.add(Character.valueOf(c));
/*     */         b1++; }
/*     */       
/* 491 */       StringBuilder output = new StringBuilder(input.length());
/*     */       
/* 493 */       while (characters.size() > 0) {
/* 494 */         int bound = (characters.size() - 1 > 0) ? (characters.size() - 1) : characters.size();
/* 495 */         int rndm = ThreadLocalRandom.current().nextInt(bound);
/* 496 */         output.append(characters.remove(rndm));
/*     */       } 
/*     */       
/* 499 */       return output.toString();
/*     */     } 
/*     */ 
/*     */     
/* 503 */     String out = ""; byte b; int i;
/*     */     String[] arrayOfString;
/* 505 */     for (i = (arrayOfString = input.split(" ")).length, b = 0; b < i; ) { String part = arrayOfString[b];
/*     */       
/* 507 */       List<Character> characters = new ArrayList<>(); byte b1; int j;
/*     */       char[] arrayOfChar;
/* 509 */       for (j = (arrayOfChar = part.toCharArray()).length, b1 = 0; b1 < j; ) { char c = arrayOfChar[b1];
/* 510 */         characters.add(Character.valueOf(c));
/*     */         b1++; }
/*     */       
/* 513 */       StringBuilder output = new StringBuilder(part.length());
/*     */       
/* 515 */       while (characters.size() > 0) {
/* 516 */         int bound = (characters.size() - 1 > 0) ? (characters.size() - 1) : characters.size();
/* 517 */         int rndm = ThreadLocalRandom.current().nextInt(bound);
/* 518 */         output.append(characters.remove(rndm));
/*     */       } 
/* 520 */       out = String.valueOf(out) + output.toString() + " ";
/*     */       b++; }
/*     */     
/* 523 */     return out.trim();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String pickWord() {
/* 529 */     String s = "";
/*     */     
/* 531 */     if (getOptions().useCustomWords() && getOptions().customWords() != null && !getOptions().customWords().isEmpty()) {
/*     */       
/* 533 */       s = getOptions().customWords().get(ThreadLocalRandom.current().nextInt(getOptions().customWords().size()));
/*     */     }
/*     */     else {
/*     */       
/* 537 */       for (int i = 0; i < getOptions().charLength(); i++)
/*     */       {
/* 539 */         s = String.valueOf(s) + "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".charAt(ThreadLocalRandom.current().nextInt("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".length()));
/*     */       }
/*     */     } 
/* 542 */     return s;
/*     */   }
/*     */ 
/*     */   
/*     */   public ReactionOptions getOptions() {
/* 547 */     if (this.options == null) {
/* 548 */       this.options = new ReactionOptions(this);
/*     */     }
/* 550 */     return this.options;
/*     */   }
/*     */ 
/*     */   
/*     */   public void giveRewards(String playerName) {
/* 555 */     List<String> rewards = getOptions().rewards();
/*     */     
/* 557 */     if (rewards == null || rewards.isEmpty()) {
/*     */       return;
/*     */     }
/*     */     
/* 561 */     List<String> give = new ArrayList<>();
/*     */     
/* 563 */     for (int i = 0; i < getOptions().rewardAmt(); i++) {
/* 564 */       String g = rewards.get(ThreadLocalRandom.current().nextInt(rewards.size()));
/*     */       
/* 566 */       if (give.contains(g)) {
/* 567 */         g = rewards.get(ThreadLocalRandom.current().nextInt(rewards.size()));
/*     */         
/* 569 */         if (give.contains(g)) {
/*     */           continue;
/*     */         }
/*     */       } 
/* 573 */       give.add(g);
/*     */       continue;
/*     */     } 
/* 576 */     if (give.isEmpty()) {
/*     */       return;
/*     */     }
/*     */     
/* 580 */     for (String command : give) {
/* 581 */       Bukkit.dispatchCommand((CommandSender)Bukkit.getConsoleSender(), command.replace("@p", playerName));
/*     */     }
/*     */   }
/*     */   
/*     */   public void sendMsg(final String message) {
/* 586 */     Bukkit.getScheduler().runTask((Plugin)this, new Runnable()
/*     */         {
/*     */           public void run()
/*     */           {
/* 590 */             List<String> disabled = ChatReaction.this.getOptions().disabledWorlds();
/*     */             
/* 592 */             if (disabled == null || disabled.isEmpty()) {
/* 593 */               Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', message));
/*     */               
/*     */               return;
/*     */             } 
/* 597 */             for (World w : Bukkit.getWorlds()) {
/*     */               
/* 599 */               if (disabled.contains(w.getName())) {
/*     */                 continue;
/*     */               }
/*     */               
/* 603 */               if (w.getPlayers() == null || w.getPlayers().isEmpty()) {
/*     */                 continue;
/*     */               }
/*     */               
/* 607 */               for (Player p : w.getPlayers()) {
/* 608 */                 p.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
/*     */               }
/*     */             } 
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   public void sendMsg(final String message, final String tooltip) {
/* 617 */     if (tooltip == null || this.chat == null || !useSpigotChat) {
/* 618 */       sendMsg(message);
/*     */       
/*     */       return;
/*     */     } 
/* 622 */     Bukkit.getScheduler().runTask((Plugin)this, new Runnable()
/*     */         {
/*     */           public void run()
/*     */           {
/* 626 */             ChatReaction.this.chat.sendMessage(ChatColor.translateAlternateColorCodes('&', message), ChatColor.translateAlternateColorCodes('&', tooltip));
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static int getOnline() {
/*     */     try {
/* 635 */       Method method = Bukkit.class.getMethod("getOnlinePlayers", new Class[0]);
/* 636 */       Object players = method.invoke(null, new Object[0]);
/*     */       
/* 638 */       if (players instanceof Player[]) {
/*     */         
/* 640 */         Player[] oldPlayers = (Player[])players;
/* 641 */         return oldPlayers.length;
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/* 646 */       Collection<Player> newPlayers = (Collection<Player>)players;
/* 647 */       return newPlayers.size();
/*     */     
/*     */     }
/* 650 */     catch (NoSuchMethodException|IllegalAccessException|java.lang.reflect.InvocationTargetException e) {
/* 651 */       e.printStackTrace();
/*     */ 
/*     */       
/* 654 */       return 0;
/*     */     } 
/*     */   }
/*     */   
/*     */   public String getTime(long time) {
/* 659 */     if (startTime <= 0L) {
/* 660 */       return "0";
/*     */     }
/*     */     
/* 663 */     double s = (time - startTime) / 1000.0D;
/* 664 */     return String.format(Locale.US, "%.2f", new Object[] { Double.valueOf(s) });
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean hasData(String uuid) {
/* 669 */     if (reactionPlayers == null || reactionPlayers.isEmpty()) {
/* 670 */       return false;
/*     */     }
/*     */     
/* 673 */     return (reactionPlayers.keySet().contains(uuid) && reactionPlayers.get(uuid) != null);
/*     */   }
/*     */   
/*     */   public static boolean isRunning() {
/* 677 */     return isRunning;
/*     */   }
/*     */   
/*     */   public static void setRunning(boolean isRunning) {
/* 681 */     ChatReaction.isRunning = isRunning;
/*     */   }
/*     */   
/*     */   public static String getCurrentWord() {
/* 685 */     return currentWord;
/*     */   }
/*     */   
/*     */   public static void setCurrentWord(String currentWord) {
/* 689 */     ChatReaction.currentWord = currentWord;
/*     */   }
/*     */   
/*     */   public static String getDisplayWord() {
/* 693 */     return displayWord;
/*     */   }
/*     */   
/*     */   public static void setDisplayWord(String displayWord) {
/* 697 */     ChatReaction.displayWord = displayWord;
/*     */   }
/*     */   
/*     */   public static long getStartTime() {
/* 701 */     return startTime;
/*     */   }
/*     */   
/*     */   public static void setStartTime(long time) {
/* 705 */     startTime = time;
/*     */   }
/*     */   
/*     */   public static BukkitTask getIntervalTask() {
/* 709 */     return iTask;
/*     */   }
/*     */   
/*     */   public static void setIntervalTask(BukkitTask task) {
/* 713 */     iTask = task;
/*     */   }
/*     */   
/*     */   public static BukkitTask getEndTask() {
/* 717 */     return endTask;
/*     */   }
/*     */   
/*     */   public static void setEndTask(BukkitTask task) {
/* 721 */     endTask = task;
/*     */   }
/*     */   
/*     */   public static boolean isScrambled() {
/* 725 */     return scrambled;
/*     */   }
/*     */   
/*     */   public static void setScrambled(boolean scramble) {
/* 729 */     scrambled = scramble;
/*     */   }
/*     */   
/*     */   public VaultHook getVaultHook() {
/* 733 */     return this.vaultHook;
/*     */   }
/*     */   
/*     */   public void setVaultHook(VaultHook vaultHook) {
/* 737 */     this.vaultHook = vaultHook;
/*     */   }
/*     */ }


/* Location:              E:\Downloads\ChatReaction.jar!\me\clip\chatreaction\ChatReaction.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */