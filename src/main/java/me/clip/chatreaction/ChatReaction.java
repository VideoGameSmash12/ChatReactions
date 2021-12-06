
package me.clip.chatreaction;


import java.lang.reflect.Method;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Level;

import me.clip.chatreaction.compatibility.SpigotChat;
import me.clip.chatreaction.database.Database;
import me.clip.chatreaction.database.MySQL;
import me.clip.chatreaction.events.ReactionFailEvent;
import me.clip.chatreaction.hooks.VaultHook;
import me.clip.chatreaction.reactionplayer.ReactionPlayer;
import me.clip.chatreaction.tasks.IntervalTask;
import me.clip.chatreaction.tasks.LoadTopTask;
import me.clip.chatreaction.updater.SpigotUpdater;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;


public class ChatReaction extends JavaPlugin
{
    public ReactionConfig config = new ReactionConfig(this);
    public WordFile wordFile = new WordFile(this);
    private ReactionOptions options;
    final String CHARS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static boolean isRunning;
    private static boolean scrambled;
    private static String currentWord = null;
    private static String displayWord = null;
    private static long startTime;
    private static BukkitTask iTask = null;
    private static BukkitTask endTask = null;
    protected static Database database = null;
    protected SpigotUpdater updater = null;
    public static Map<String, ReactionPlayer> reactionPlayers = new HashMap<>();
    public static List<ReactionPlayer> topPlayers = new ArrayList<>();
    private SpigotChat chat;
    private static boolean useSpigotChat = false;
    private VaultHook vaultHook;

    public void onEnable()
    {
        if (checkSpigot())
        {
            getLogger().info("You are using Spigot! Tooltips in reaction start messages are enabled!");
        }
        else
        {
            getLogger().warning("You are not using Spigot! Tooltips in chat will not work with your server software!");
        }

        this.config.loadCfg();
        this.options = new ReactionOptions(this);

        Bukkit.getPluginManager().registerEvents(new ChatReactionListener(this), this);

        getCommand("reaction").setExecutor(new ReactionCommands(this));

        start();

        if (startDatabase())
        {
            Bukkit.getScheduler().runTaskLaterAsynchronously((Plugin) this, (Runnable) new LoadTopTask(this), 20L);
        }

        if (getConfig().getBoolean("check_updates"))
        {
            this.updater = new SpigotUpdater(this);
            this.updater.checkUpdates();

            if (SpigotUpdater.updateAvailable())
            {
                System.out.println("---------------------------");
                System.out.println("     ChatReaction Updater");
                System.out.println(" ");
                System.out.println("An update for ChatReaction has been found!");
                System.out.println("ChatReaction " + SpigotUpdater.getHighest());
                System.out.println("You are running " + getDescription().getVersion());
                System.out.println(" ");
                System.out.println("Download at http://www.spigotmc.org/resources/chatreaction.3748/");
                System.out.println("---------------------------");
            }
            else
            {
                System.out.println("---------------------------");
                System.out.println("     ChatReaction Updater");
                System.out.println(" ");
                System.out.println("You are running " + getDescription().getVersion());
                System.out.println("The latest version");
                System.out.println("of ChatReaction!");
                System.out.println(" ");
                System.out.println("---------------------------");
            }
        }

        setupHooks();
    }

    private void setupHooks()
    {
        if (Bukkit.getPluginManager().getPlugin("Vault") != null)
        {
            this.vaultHook = new VaultHook();
            if (!this.vaultHook.setup())
            {
                this.vaultHook = null;
            }
        }
    }

    private boolean checkSpigot()
    {
        try
        {
            Class.forName("org.spigotmc.SpigotConfig");
            this.chat = new SpigotChat(this);
            useSpigotChat = true;
        }
        catch (Exception exception)
        {
            useSpigotChat = false;
        }

        return (useSpigotChat && this.chat != null);
    }

    public void onDisable()
    {
        if (getOptions().trackStats() && database != null)
        {
            database.close();
            database = null;

        }

        stop();

        Bukkit.getScheduler().cancelTasks((Plugin) this);
        this.options = null;
        setCurrentWord(null);
        setDisplayWord(null);
        reactionPlayers = null;
        topPlayers = null;
    }

    public void debug(Level lv, String msg)
    {
        if (getOptions().debug())
        {
            System.out.println("[RDebug " + lv.getName() + "] " + msg);
        }
    }

    private boolean startDatabase()
    {
        if (!getOptions().trackStats())
        {
            debug(Level.INFO, "track stats was set to false in the config!");
            getLogger().info("Reaction stats are disabled!!");
            return false;
        }

        try
        {
            getLogger().info("Creating MySQL connection ...");
            database = new MySQL(getConfig().getString("reaction_stats.prefix"),
                    getConfig().getString("reaction_stats.hostname"), (new StringBuilder(
                    String.valueOf(getConfig().getInt("reaction_stats.port")))).toString(), getConfig()
                    .getString("reaction_stats.database"), getConfig()
                    .getString("reaction_stats.username"), getConfig()
                    .getString("reaction_stats.password"),
                    getConfig().getBoolean("reaction_stats.ssl", false));
            database.open();

            if (!database.checkTable(String.valueOf(database.getTablePrefix()) + "reactionstats"))
            {
                getLogger().info("Creating MySQL table ...");
                database.createTable("CREATE TABLE IF NOT EXISTS `" +
                        database.getTablePrefix() + "reactionstats` (" +
                        "  `uuid` varchar(50) NOT NULL," +
                        "  `name` varchar(50) NOT NULL," +
                        "  `wins` integer NOT NULL," +
                        "  PRIMARY KEY (`uuid`)" +
                        ") ENGINE=InnoDB DEFAULT CHARSET=latin1;");
            }

            return true;
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            database = null;
            getOptions().setTrackStats(false);
            getLogger().warning("Could not connect to database! Reaction stats will be disabled!!");
            return false;
        }
    }

    public void savePlayer(ReactionPlayer pl)
    {
        if (pl == null || pl.getUuid() == null || pl.getName() == null)
        {
            return;
        }

        if (getOptions().trackStats() && database != null)
        {
            debug(Level.INFO, "Initializing save for player: " + pl.getName());

            if (database.getConnection() == null)
            {
                getLogger().warning("Could not connect to database! Failed to save data for player: " + pl.getName() + "!");
                return;
            }

            try
            {
                String query = "SELECT wins,name FROM `" + database.getTablePrefix() + "reactionstats` WHERE uuid=?";
                PreparedStatement statement = database.prepare(query);
                statement.setString(1, pl.getUuid());
                ResultSet result = statement.executeQuery();

                if (result.next())
                {
                    debug(Level.INFO, "Player has stats stored, updating stats.");
                    query = "UPDATE `" + database.getTablePrefix() + "reactionstats` SET wins=?,name=? WHERE uuid=?";
                    PreparedStatement updateStatement = database.prepare(query);
                    updateStatement.setInt(1, pl.getWins());
                    updateStatement.setString(2, pl.getName());
                    updateStatement.setString(3, pl.getUuid());
                    updateStatement.execute();
                    updateStatement.close();

                }
                else
                {
                    debug(Level.INFO, "Player does not have any stats stored, inserting stats.");
                    query = "INSERT INTO `" + database.getTablePrefix() + "reactionstats` (uuid,name,wins) VALUES (?,?,?)";
                    PreparedStatement insertStatement = database.prepare(query);
                    insertStatement.setString(1, pl.getUuid());
                    insertStatement.setString(2, pl.getName());
                    insertStatement.setInt(3, pl.getWins());
                    insertStatement.execute();
                    insertStatement.close();
                }

                statement.close();
                result.close();
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
            }

            database.close();
        }
    }

    public void updateTopWins()
    {
        if (database == null || !getOptions().trackStats())
        {
            return;
        }

        debug(Level.INFO, "Updating top players....");

        if (database.getConnection() == null)
        {
            getLogger().warning("Could not connect to database! Failed to update top wins!");
            return;
        }
        List<ReactionPlayer> top = new ArrayList<>();

        try
        {
            String query = "SELECT uuid,name,wins FROM `" + database.getTablePrefix() + "reactionstats` ORDER BY wins DESC LIMIT " + getOptions().topPlayersSize() + ";";
            PreparedStatement statement = database.prepare(query);
            ResultSet result = statement.executeQuery();

            while (result.next())
            {
                String uuid = result.getString(1);
                String name = result.getString(2);
                int wins = result.getInt(3);
                top.add(new ReactionPlayer(uuid, name, wins));

            }

            if (top != null && !top.isEmpty())
            {
                debug(Level.INFO, String.valueOf(top.size()) + " top wins loaded!");

            }
            else
            {
                debug(Level.INFO, "No top wins loaded!");

            }

            topPlayers = top;
            statement.close();
            result.close();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }

        database.close();
    }

    public ReactionPlayer loadPlayer(String uuid)
    {
        if (!getOptions().trackStats() || database == null)
        {
            return null;
        }

        debug(Level.INFO, "Loading wins for uuid: " + uuid);

        if (database.getConnection() == null)
        {
            getLogger().warning("Could not connect to database! Failed to load data for uuid: " + uuid + "!");
            return null;
        }

        try
        {
            if (database.getConnection().isClosed())
            {
                database.open();
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            return null;
        }

        try
        {
            String query = "SELECT * FROM `" + database.getTablePrefix() + "reactionstats` WHERE uuid=?";
            PreparedStatement statement = database.getConnection().prepareStatement(query);
            statement.setString(1, uuid);
            ResultSet result = statement.executeQuery();

            if (result.next())
            {
                debug(Level.INFO, "Player data found! Loading data...");
                String uid = result.getString(1);
                String name = result.getString(2);
                int wins = result.getInt(3);
                statement.close();
                result.close();
                database.close();
                return new ReactionPlayer(uid, name, wins);
            }

            statement.close();
            result.close();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }

        debug(Level.INFO, "Player data was not found for uuid " + uuid);
        database.close();
        return null;
    }

    public ReactionPlayer loadPlayerByName(String playerName)
    {
        if (!getOptions().trackStats() || database == null)
        {
            return null;
        }

        debug(Level.INFO, "Loading wins for player: " + playerName);

        if (database.getConnection() == null)
        {
            getLogger().warning("Could not connect to database! Failed to load data for player: " + playerName + "!");
            return null;

        }

        try
        {
            String query = "SELECT * FROM `" + database.getTablePrefix() + "reactionstats` WHERE name=?";
            PreparedStatement statement = database.prepare(query);
            statement.setString(1, playerName);
            ResultSet result = statement.executeQuery();

            if (result.next())
            {
                debug(Level.INFO, "Player data found! Loading data...");
                String uid = result.getString(1);
                String name = result.getString(2);
                int wins = result.getInt(3);
                statement.close();
                result.close();
                database.close();
                return new ReactionPlayer(uid, name, wins);
            }

            statement.close();
            result.close();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }

        debug(Level.INFO, "Player data was not found for player " + playerName);
        database.close();
        return null;
    }

    protected void reload()
    {
        if (isRunning())
        {
            Bukkit.getScheduler().runTaskAsynchronously(this, () -> Bukkit.getPluginManager().callEvent(new ReactionFailEvent(ChatReaction.getCurrentWord(), ChatReaction.getDisplayWord(), ChatReaction.isScrambled(), null)));
        }

        stop();

        Bukkit.getScheduler().cancelTasks(this);

        reloadConfig();
        saveConfig();

        this.options = new ReactionOptions(this);
        start();
    }

    protected void start()
    {
        if (endTask != null)
        {
            debug(Level.INFO, "Cancelling previous reaction end task");
            endTask.cancel();
            endTask = null;
        }

        if (iTask == null)
        {
            debug(Level.INFO, "Starting interval task!");
            iTask = Bukkit.getScheduler().runTaskTimerAsynchronously(this, new IntervalTask(this), (getOptions().interval() * 20), (getOptions().interval() * 20));
        }
        else
        {
            debug(Level.INFO, "Stopping old interval task!");
            iTask.cancel();
            iTask = null;
            debug(Level.INFO, "Starting new interval task!");
            iTask = Bukkit.getScheduler().runTaskTimerAsynchronously(this, new IntervalTask(this), (getOptions().interval() * 20), (getOptions().interval() * 20));
        }
    }

    protected void stop()
    {
        if (endTask != null)
        {
            debug(Level.INFO, "Stopping reaction end task!");
            endTask.cancel();
            endTask = null;

        }
        if (iTask != null)
        {
            debug(Level.INFO, "Stopping reaction interval task!");
            iTask.cancel();
            iTask = null;
        }
    }

    public String scramble(String input)
    {
        if (!input.contains(" "))
        {
            List<Character> characters = new ArrayList<>();
            byte b1;
            int j;

            char[] arrayOfChar;
            for (j = (arrayOfChar = input.toCharArray()).length, b1 = 0; b1 < j; )
            {
                char c = arrayOfChar[b1];
                characters.add(Character.valueOf(c));

                b1++;
            }

            StringBuilder output = new StringBuilder(input.length());

            while (characters.size() > 0)
            {
                int bound = (characters.size() - 1 > 0) ? (characters.size() - 1) : characters.size();
                int rndm = ThreadLocalRandom.current().nextInt(bound);
                output.append(characters.remove(rndm));

            }

            return output.toString();
        }

        String out = "";
        byte b;
        int i;

        String[] arrayOfString;
        for (i = (arrayOfString = input.split(" ")).length, b = 0; b < i; )
        {
            String part = arrayOfString[b];

            List<Character> characters = new ArrayList<>();
            byte b1;
            int j;

            char[] arrayOfChar;
            for (j = (arrayOfChar = part.toCharArray()).length, b1 = 0; b1 < j; )
            {
                char c = arrayOfChar[b1];
                characters.add(c);

                b1++;
            }

            StringBuilder output = new StringBuilder(part.length());

            while (characters.size() > 0)
            {
                int bound = (characters.size() - 1 > 0) ? (characters.size() - 1) : characters.size();
                int rndm = ThreadLocalRandom.current().nextInt(bound);
                output.append(characters.remove(rndm));
            }
            out = out + output + " ";

            b++;
        }

        return out.trim();
    }


    public String pickWord()
    {
        String s = "";

        if (getOptions().useCustomWords() && getOptions().customWords() != null && !getOptions().customWords().isEmpty())
        {
            s = getOptions().customWords().get(ThreadLocalRandom.current().nextInt(getOptions().customWords().size()));
        }
        else
        {
            for (int i = 0; i < getOptions().charLength(); i++)
            {
                s = s + "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".charAt(ThreadLocalRandom.current().nextInt("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".length()));
            }
        }

        return s;
    }

    public ReactionOptions getOptions()
    {
        if (this.options == null)
        {
            this.options = new ReactionOptions(this);
        }

        return this.options;
    }


    public void giveRewards(String playerName)
    {
        List<String> rewards = getOptions().rewards();

        if (rewards == null || rewards.isEmpty())
        {
            return;
        }

        List<String> give = new ArrayList<>();

        for (int i = 0; i < getOptions().rewardAmt(); i++)
        {
            String g = rewards.get(ThreadLocalRandom.current().nextInt(rewards.size()));

            if (give.contains(g))
            {
                g = rewards.get(ThreadLocalRandom.current().nextInt(rewards.size()));

                if (give.contains(g))
                {
                    continue;
                }
            }
            give.add(g);

            continue;
        }
        if (give.isEmpty())
        {
            return;
        }

        for (String command : give)
        {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command.replace("@p", playerName));
        }
    }


    public void sendMsg(final String message)
    {
        Bukkit.getScheduler().runTask(this, () -> {
            List<String> disabled = ChatReaction.this.getOptions().disabledWorlds();

            if (disabled == null || disabled.isEmpty())
            {
                Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', message));

                return;
            }
            for (World w : Bukkit.getWorlds())
            {
                if (disabled.contains(w.getName()))
                {
                    continue;
                }

                if (w.getPlayers() == null || w.getPlayers().isEmpty())
                {
                    continue;
                }

                for (Player p : w.getPlayers())
                {
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
                }
            }
        });
    }

    public void sendMsg(final String message, final String tooltip)
    {
        if (tooltip == null || this.chat == null || !useSpigotChat)
        {
            sendMsg(message);

            return;
        }
        Bukkit.getScheduler().runTask(this, () -> ChatReaction.this.chat.sendMessage(ChatColor.translateAlternateColorCodes('&', message), ChatColor.translateAlternateColorCodes('&', tooltip)));

    }


    public static int getOnline()
    {
        try
        {
            Method method = Bukkit.class.getMethod("getOnlinePlayers", new Class[0]);
            Object players = method.invoke(null, new Object[0]);

            if (players instanceof Player[])
            {
                Player[] oldPlayers = (Player[]) players;
                return oldPlayers.length;
            }

            Collection<Player> newPlayers = (Collection<Player>) players;
            return newPlayers.size();
        }
        catch (NoSuchMethodException | IllegalAccessException | java.lang.reflect.InvocationTargetException e)
        {
            e.printStackTrace();
            return 0;
        }
    }

    public String getTime(long time)
    {
        if (startTime <= 0L)
        {
            return "0";
        }

        double s = (time - startTime) / 1000.0D;
        return String.format(Locale.US, "%.2f", s);
    }

    public static boolean hasData(String uuid)
    {
        if (reactionPlayers == null || reactionPlayers.isEmpty())
        {
            return false;
        }

        return (reactionPlayers.keySet().contains(uuid) && reactionPlayers.get(uuid) != null);
    }

    public static boolean isRunning()
    {
        return isRunning;
    }

    public static void setRunning(boolean isRunning)
    {
        ChatReaction.isRunning = isRunning;
    }

    public static String getCurrentWord()
    {
        return currentWord;
    }

    public static void setCurrentWord(String currentWord)
    {
        ChatReaction.currentWord = currentWord;
    }

    public static String getDisplayWord()
    {
        return displayWord;
    }

    public static void setDisplayWord(String displayWord)
    {
        ChatReaction.displayWord = displayWord;
    }

    public static long getStartTime()
    {
        return startTime;
    }

    public static void setStartTime(long time)
    {
        startTime = time;
    }

    public static BukkitTask getIntervalTask()
    {
        return iTask;
    }

    public static void setIntervalTask(BukkitTask task)
    {
        iTask = task;
    }

    public static BukkitTask getEndTask()
    {
        return endTask;
    }

    public static void setEndTask(BukkitTask task)
    {
        endTask = task;
    }

    public static boolean isScrambled()
    {
        return scrambled;
    }

    public static void setScrambled(boolean scramble)
    {
        scrambled = scramble;
    }

    public VaultHook getVaultHook()
    {
        return this.vaultHook;
    }

    public void setVaultHook(VaultHook vaultHook)
    {
        this.vaultHook = vaultHook;
    }
}