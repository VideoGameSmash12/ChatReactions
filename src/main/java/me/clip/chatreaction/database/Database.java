/*    */ package me.clip.chatreaction.database;
/*    */ 
/*    */ import java.sql.Connection;
/*    */ import java.sql.PreparedStatement;
/*    */ import java.sql.ResultSet;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class Database
/*    */ {
/*    */   protected final String PREFIX;
/*    */   protected boolean connected;
/*    */   protected Connection connection;
/*    */   
/*    */   protected enum Statements
/*    */   {
/* 21 */     SELECT, INSERT, UPDATE, DELETE, DO, REPLACE, LOAD, HANDLER, CALL, CREATE, ALTER, DROP, TRUNCATE, RENAME;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Database(String prefix) {
/* 31 */     this.PREFIX = prefix;
/* 32 */     this.connected = false;
/* 33 */     this.connection = null;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getTablePrefix() {
/* 42 */     return this.PREFIX;
/*    */   }
/*    */   
/*    */   abstract boolean initialize();
/*    */   
/*    */   public abstract Connection open();
/*    */   
/*    */   public abstract void close();
/*    */   
/*    */   public abstract Connection getConnection();
/*    */   
/*    */   public abstract boolean checkConnection();
/*    */   
/*    */   public abstract ResultSet query(String paramString);
/*    */   
/*    */   public abstract PreparedStatement prepare(String paramString);
/*    */   
/*    */   protected Statements getStatement(String query) {
/* 60 */     String trimmedQuery = query.trim();
/* 61 */     if (trimmedQuery.substring(0, 6).equalsIgnoreCase("SELECT"))
/* 62 */       return Statements.SELECT; 
/* 63 */     if (trimmedQuery.substring(0, 6).equalsIgnoreCase("INSERT"))
/* 64 */       return Statements.INSERT; 
/* 65 */     if (trimmedQuery.substring(0, 6).equalsIgnoreCase("UPDATE"))
/* 66 */       return Statements.UPDATE; 
/* 67 */     if (trimmedQuery.substring(0, 6).equalsIgnoreCase("DELETE"))
/* 68 */       return Statements.DELETE; 
/* 69 */     if (trimmedQuery.substring(0, 6).equalsIgnoreCase("CREATE"))
/* 70 */       return Statements.CREATE; 
/* 71 */     if (trimmedQuery.substring(0, 5).equalsIgnoreCase("ALTER"))
/* 72 */       return Statements.ALTER; 
/* 73 */     if (trimmedQuery.substring(0, 4).equalsIgnoreCase("DROP"))
/* 74 */       return Statements.DROP; 
/* 75 */     if (trimmedQuery.substring(0, 8).equalsIgnoreCase("TRUNCATE"))
/* 76 */       return Statements.TRUNCATE; 
/* 77 */     if (trimmedQuery.substring(0, 6).equalsIgnoreCase("RENAME"))
/* 78 */       return Statements.RENAME; 
/* 79 */     if (trimmedQuery.substring(0, 2).equalsIgnoreCase("DO"))
/* 80 */       return Statements.DO; 
/* 81 */     if (trimmedQuery.substring(0, 7).equalsIgnoreCase("REPLACE"))
/* 82 */       return Statements.REPLACE; 
/* 83 */     if (trimmedQuery.substring(0, 4).equalsIgnoreCase("LOAD"))
/* 84 */       return Statements.LOAD; 
/* 85 */     if (trimmedQuery.substring(0, 7).equalsIgnoreCase("HANDLER"))
/* 86 */       return Statements.HANDLER; 
/* 87 */     if (trimmedQuery.substring(0, 4).equalsIgnoreCase("CALL")) {
/* 88 */       return Statements.CALL;
/*    */     }
/* 90 */     return Statements.SELECT;
/*    */   }
/*    */   
/*    */   public abstract boolean createTable(String paramString);
/*    */   
/*    */   public abstract boolean checkTable(String paramString);
/*    */   
/*    */   public abstract boolean wipeTable(String paramString);
/*    */   
/*    */   public abstract String getCreateStatement(String paramString);
/*    */ }


/* Location:              E:\Downloads\ChatReaction.jar!\me\clip\chatreaction\database\Database.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */