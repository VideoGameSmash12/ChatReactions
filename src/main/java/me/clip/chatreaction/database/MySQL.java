/*     */ package me.clip.chatreaction.database;
/*     */ 
/*     */ import java.sql.Connection;
/*     */ import java.sql.DriverManager;
/*     */ import java.sql.PreparedStatement;
/*     */ import java.sql.ResultSet;
/*     */ import java.sql.SQLException;
/*     */ import java.sql.Statement;
/*     */ import org.bukkit.Bukkit;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MySQL
/*     */   extends Database
/*     */ {
/*  19 */   private String hostname = "localhost";
/*  20 */   private String portnmbr = "3306";
/*  21 */   private String username = "minecraft";
/*  22 */   private String password = "";
/*  23 */   private String database = "minecraft";
/*     */   
/*     */   private boolean ssl = false;
/*     */   
/*     */   public MySQL(String prefix, String hostname, String portnmbr, String database, String username, String password, boolean ssl) {
/*  28 */     super(prefix);
/*  29 */     this.hostname = hostname;
/*  30 */     this.portnmbr = portnmbr;
/*  31 */     this.database = database;
/*  32 */     this.username = username;
/*  33 */     this.password = password;
/*  34 */     this.ssl = ssl;
/*     */   }
/*     */   
/*     */   protected boolean initialize() {
/*     */     try {
/*  39 */       Class.forName("com.mysql.jdbc.Driver");
/*  40 */       return true;
/*  41 */     } catch (ClassNotFoundException e) {
/*  42 */       Bukkit.getLogger().severe("[ChatReaction] Class Not Found Exception: " + e.getMessage() + ".");
/*  43 */       return false;
/*     */     } 
/*     */   }
/*     */   
/*     */   public Connection open() {
/*  48 */     open(true);
/*  49 */     return this.connection;
/*     */   }
/*     */   
/*     */   public Connection open(boolean showError) {
/*  53 */     if (initialize()) {
/*  54 */       String url = "";
/*     */       try {
/*  56 */         url = "jdbc:mysql://" + this.hostname + ":" + this.portnmbr + 
/*  57 */           "/" + this.database + "?allowReconnect=true" + (this.ssl ? "&useSSL=true" : "");
/*  58 */         this.connection = DriverManager.getConnection(url, 
/*  59 */             this.username, this.password);
/*  60 */         if (checkConnection())
/*  61 */           this.connected = true; 
/*  62 */         return this.connection;
/*  63 */       } catch (SQLException e) {
/*  64 */         if (showError) {
/*  65 */           Bukkit.getLogger().severe("[ChatReaction] " + url);
/*  66 */           Bukkit.getLogger()
/*  67 */             .severe("[ChatReaction] Could not be resolved because of an SQL Exception: " + 
/*  68 */               e.getMessage() + ".");
/*     */         } 
/*  70 */       } catch (Exception e) {
/*  71 */         e.printStackTrace();
/*     */       } 
/*     */     } 
/*  74 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public void close() {
/*     */     try {
/*  80 */       if (this.connection != null)
/*  81 */         this.connection.close(); 
/*  82 */     } catch (Exception e) {
/*  83 */       Bukkit.getLogger().severe(
/*  84 */           "[ChatReaction] Failed to close database connection: " + 
/*  85 */           e.getMessage());
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public Connection getConnection() {
/*  91 */     if (this.connection == null)
/*  92 */       return open(); 
/*     */     try {
/*  94 */       if (this.connection.isClosed()) {
/*  95 */         return open();
/*     */       }
/*  97 */     } catch (SQLException e) {
/*     */       
/*  99 */       e.printStackTrace();
/*     */     } 
/* 101 */     return this.connection;
/*     */   }
/*     */   
/*     */   public boolean checkConnection() {
/* 105 */     if (this.connection != null)
/* 106 */       return true; 
/* 107 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public ResultSet query(String query) {
/* 112 */     Statement statement = null;
/* 113 */     ResultSet result = null;
/*     */ 
/*     */     
/*     */     try {
/* 117 */       for (int counter = 0; counter < 5 && result == null; counter++) {
/*     */         try {
/* 119 */           statement = this.connection.createStatement();
/* 120 */           result = statement.executeQuery("SELECT CURTIME()");
/* 121 */         } catch (SQLException e) {
/* 122 */           if (counter == 4) {
/* 123 */             throw e;
/*     */           }
/* 125 */           if (e.getMessage().contains("connection closed")) {
/* 126 */             Bukkit.getLogger()
/* 127 */               .severe("[ChatReaction] Error in SQL query. Attempting to reestablish connection. Attempt #" + 
/*     */                 
/* 129 */                 Integer.toString(counter + 1) + 
/* 130 */                 "!");
/* 131 */             open(false);
/*     */           } else {
/* 133 */             throw e;
/*     */           } 
/*     */         } 
/*     */       } 
/*     */ 
/*     */       
/* 139 */       switch (getStatement(query))
/*     */       { case SELECT:
/* 141 */           result = statement.executeQuery(query);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 147 */           return result; }  statement.executeUpdate(query); return result;
/* 148 */     } catch (SQLException e) {
/* 149 */       Bukkit.getLogger().severe(
/* 150 */           "[ChatReaction] Error in SQL query: " + e.getMessage());
/*     */       
/* 152 */       return result;
/*     */     } 
/*     */   }
/*     */   public PreparedStatement prepare(String query) {
/* 156 */     PreparedStatement ps = null;
/*     */     try {
/* 158 */       ps = this.connection.prepareStatement(query);
/* 159 */       return ps;
/* 160 */     } catch (SQLException e) {
/* 161 */       if (!e.toString().contains("not return ResultSet")) {
/* 162 */         Bukkit.getLogger().severe(
/* 163 */             "[ChatReaction] Error in SQL prepare() query: " + 
/* 164 */             e.getMessage());
/*     */       }
/* 166 */       return ps;
/*     */     } 
/*     */   }
/*     */   public boolean createTable(String query) {
/* 170 */     Statement statement = null;
/*     */     try {
/* 172 */       if (query.equals("") || query == null) {
/* 173 */         Bukkit.getLogger().severe(
/* 174 */             "[ChatReaction] SQL query empty: createTable(" + query + 
/* 175 */             ")");
/* 176 */         return false;
/*     */       } 
/*     */       
/* 179 */       statement = this.connection.createStatement();
/* 180 */       statement.execute(query);
/* 181 */       return true;
/* 182 */     } catch (SQLException e) {
/* 183 */       Bukkit.getLogger().severe("[ChatReaction] " + e.getMessage());
/* 184 */       return false;
/* 185 */     } catch (Exception e) {
/* 186 */       Bukkit.getLogger().severe("[ChatReaction] " + e.getMessage());
/* 187 */       return false;
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean checkTable(String table) {
/*     */     try {
/* 193 */       Statement statement = getConnection().createStatement();
/*     */       
/* 195 */       ResultSet result = statement.executeQuery("SELECT * FROM " + table);
/*     */       
/* 197 */       if (result == null)
/* 198 */         return false; 
/* 199 */       if (result != null)
/* 200 */         return true; 
/* 201 */     } catch (SQLException e) {
/* 202 */       if (e.getMessage().contains("exist")) {
/* 203 */         return false;
/*     */       }
/* 205 */       Bukkit.getLogger()
/* 206 */         .severe("[ChatReaction] Error in SQL query: " + 
/* 207 */           e.getMessage());
/*     */     } 
/*     */ 
/*     */     
/* 211 */     if (query("SELECT * FROM " + table) == null)
/* 212 */       return true; 
/* 213 */     return false;
/*     */   }
/*     */   
/*     */   public boolean wipeTable(String table) {
/* 217 */     Statement statement = null;
/* 218 */     String query = null;
/*     */     try {
/* 220 */       if (!checkTable(table)) {
/* 221 */         Bukkit.getLogger().severe(
/* 222 */             "[ChatReaction] Error wiping table: \"" + table + 
/* 223 */             "\" does not exist.");
/* 224 */         return false;
/*     */       } 
/* 226 */       statement = getConnection().createStatement();
/* 227 */       query = "DELETE FROM " + table + ";";
/* 228 */       statement.executeUpdate(query);
/*     */       
/* 230 */       return true;
/* 231 */     } catch (SQLException e) {
/* 232 */       if (!e.toString().contains("not return ResultSet")) {
/* 233 */         return false;
/*     */       }
/* 235 */       return false;
/*     */     } 
/*     */   }
/*     */   
/*     */   public String getCreateStatement(String table) {
/* 240 */     if (checkTable(table)) {
/*     */       try {
/* 242 */         ResultSet result = query("SHOW CREATE TABLE " + table);
/* 243 */         result.next();
/* 244 */         return result.getString(2);
/* 245 */       } catch (Exception exception) {}
/*     */     }
/*     */ 
/*     */     
/* 249 */     return "";
/*     */   }
/*     */ }


/* Location:              E:\Downloads\ChatReaction.jar!\me\clip\chatreaction\database\MySQL.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */