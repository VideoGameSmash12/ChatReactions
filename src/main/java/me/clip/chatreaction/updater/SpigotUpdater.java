/*    */ package me.clip.chatreaction.updater;
/*    */ 
/*    */ import java.io.BufferedReader;
/*    */ import java.io.InputStreamReader;
/*    */ import java.net.HttpURLConnection;
/*    */ import java.net.URL;
/*    */ import java.util.regex.Pattern;
/*    */ import me.clip.chatreaction.ChatReaction;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class SpigotUpdater
/*    */ {
/*    */   private ChatReaction plugin;
/* 17 */   final int resource = 3748;
/* 18 */   private static String latestVersion = "";
/*    */   private static boolean updateAvailable = false;
/*    */   
/*    */   public SpigotUpdater(ChatReaction i) {
/* 22 */     this.plugin = i;
/*    */   }
/*    */   
/*    */   private String getSpigotVersion() {
/*    */     try {
/* 27 */       HttpURLConnection con = (HttpURLConnection)(new URL(
/* 28 */           "http://www.spigotmc.org/api/general.php")).openConnection();
/* 29 */       con.setDoOutput(true);
/* 30 */       con.setRequestMethod("POST");
/* 31 */       con.getOutputStream()
/* 32 */         .write("key=98BE0FE67F88AB82B4C197FAF1DC3B69206EFDCC4D3B80FC83A00037510B99B4&resource=3748"
/* 33 */           .getBytes("UTF-8"));
/* 34 */       String version = (new BufferedReader(new InputStreamReader(
/* 35 */             con.getInputStream()))).readLine();
/* 36 */       if (version.length() <= 7) {
/* 37 */         return version;
/*    */       }
/* 39 */     } catch (Exception ex) {
/* 40 */       System.out.println("---------------------------");
/* 41 */       System.out.println("     ChatReaction Updater");
/* 42 */       System.out.println(" ");
/* 43 */       System.out.println("Could not connect to spigotmc.org");
/* 44 */       System.out.println("to check for updates! ");
/* 45 */       System.out.println(" ");
/* 46 */       System.out.println("---------------------------");
/*    */     } 
/* 48 */     return null;
/*    */   }
/*    */   
/*    */   private boolean checkHigher(String currentVersion, String newVersion) {
/* 52 */     String current = toReadable(currentVersion);
/* 53 */     String newVers = toReadable(newVersion);
/* 54 */     return (current.compareTo(newVers) < 0);
/*    */   }
/*    */   
/*    */   public boolean checkUpdates() {
/* 58 */     if (getHighest() != "") {
/* 59 */       return true;
/*    */     }
/* 61 */     String version = getSpigotVersion();
/* 62 */     if (version != null && 
/* 63 */       checkHigher(this.plugin.getDescription().getVersion(), version)) {
/* 64 */       latestVersion = version;
/* 65 */       updateAvailable = true;
/* 66 */       return true;
/*    */     } 
/*    */     
/* 69 */     return false;
/*    */   }
/*    */   
/*    */   public static boolean updateAvailable() {
/* 73 */     return updateAvailable;
/*    */   }
/*    */   
/*    */   public static String getHighest() {
/* 77 */     return latestVersion;
/*    */   }
/*    */   
/*    */   private String toReadable(String version) {
/* 81 */     String[] split = Pattern.compile(".", 16).split(
/* 82 */         version.replace("v", ""));
/* 83 */     version = ""; byte b; int i; String[] arrayOfString1;
/* 84 */     for (i = (arrayOfString1 = split).length, b = 0; b < i; ) { String s = arrayOfString1[b];
/* 85 */       version = String.valueOf(version) + String.format("%4s", new Object[] { s }); b++; }
/* 86 */      return version;
/*    */   }
/*    */ }


/* Location:              E:\Downloads\ChatReaction.jar!\me\clip\chatreactio\\updater\SpigotUpdater.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */