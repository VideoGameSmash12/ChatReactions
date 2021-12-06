/*     */ package me.clip.chatreaction;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import java.util.Scanner;
/*     */ 
/*     */ 
/*     */ public class WordFile
/*     */ {
/*     */   private ChatReaction plugin;
/*     */   private File file;
/*     */   
/*     */   public WordFile(ChatReaction i) {
/*  18 */     this.plugin = i;
/*  19 */     this.file = new File(this.plugin.getDataFolder(), "words.txt");
/*     */   }
/*     */ 
/*     */   
/*     */   public List<String> getText() {
/*  24 */     File file = new File(this.plugin.getDataFolder(), "words.txt");
/*     */     
/*  26 */     if (!file.exists()) {
/*     */       try {
/*  28 */         file.createNewFile();
/*  29 */       } catch (IOException e) {
/*  30 */         e.printStackTrace();
/*     */       } 
/*  32 */       return null;
/*     */     } 
/*     */     
/*  35 */     List<String> text = new ArrayList<>();
/*     */     
/*     */     try {
/*  38 */       Scanner scanner = new Scanner(file);
/*  39 */       while (scanner.hasNextLine()) {
/*  40 */         String line = scanner.nextLine();
/*  41 */         if (line == null) {
/*     */           continue;
/*     */         }
/*  44 */         text.add(line);
/*     */       } 
/*  46 */       scanner.close();
/*  47 */     } catch (FileNotFoundException e) {
/*  48 */       e.printStackTrace();
/*  49 */       return null;
/*     */     } 
/*     */     
/*  52 */     return text;
/*     */   }
/*     */ 
/*     */   
/*     */   public List<String> loadWords(boolean eachLineWord) {
/*  57 */     List<String> words = new ArrayList<>();
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/*  62 */       if (!this.file.exists()) {
/*     */ 
/*     */         
/*     */         try {
/*  66 */           this.file.createNewFile();
/*     */         }
/*  68 */         catch (IOException e) {
/*     */           
/*  70 */           e.printStackTrace();
/*  71 */           return words;
/*     */         } 
/*     */         
/*  74 */         this.plugin.getLogger().info("Your words.txt file is empty!");
/*     */         
/*  76 */         return Arrays.asList(new String[] {
/*  77 */               "add", "words", "to", "your", "words.txt", "file"
/*     */             });
/*     */       } 
/*     */       
/*  81 */       Scanner scanner = new Scanner(this.file);
/*     */       
/*  83 */       while (scanner.hasNextLine()) {
/*     */         
/*  85 */         String line = scanner.nextLine();
/*     */         
/*  87 */         if (line == null || line.isEmpty()) {
/*     */           continue;
/*     */         }
/*     */         
/*  91 */         if (eachLineWord) {
/*  92 */           words.add(line);
/*     */           continue;
/*     */         } 
/*  95 */         if (line.contains(" ")) {
/*  96 */           String[] split = line.split(" ");
/*     */           
/*  98 */           if (split == null)
/*     */             continue;  byte b;
/*     */           int i;
/*     */           String[] arrayOfString1;
/* 102 */           for (i = (arrayOfString1 = split).length, b = 0; b < i; ) { String word = arrayOfString1[b];
/* 103 */             if (word != null && !word.isEmpty())
/*     */             {
/*     */ 
/*     */               
/* 107 */               words.add(word);
/*     */             }
/*     */             b++; }
/*     */           
/*     */           continue;
/*     */         } 
/* 113 */         words.add(line);
/*     */       } 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 119 */       scanner.close();
/*     */     }
/* 121 */     catch (FileNotFoundException e) {
/*     */       
/* 123 */       e.printStackTrace();
/* 124 */       return null;
/*     */     } 
/*     */     
/* 127 */     this.plugin.getLogger().info(String.valueOf(words.size()) + " words loaded!");
/*     */     
/* 129 */     return words;
/*     */   }
/*     */ }


/* Location:              E:\Downloads\ChatReaction.jar!\me\clip\chatreaction\WordFile.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */