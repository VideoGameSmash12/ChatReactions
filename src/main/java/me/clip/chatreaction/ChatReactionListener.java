
package me.clip.chatreaction;


import java.util.List;

import me.clip.chatreaction.events.ReactionWinEvent;
import me.clip.chatreaction.reactionplayer.ReactionPlayer;
import me.clip.chatreaction.tasks.AddWinTask;
import me.clip.chatreaction.tasks.LoadPlayerTask;
import me.clip.chatreaction.updater.SpigotUpdater;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;


public class ChatReactionListener implements Listener
{
    private ChatReaction plugin;

    public ChatReactionListener(ChatReaction i)
    {
        this.plugin = i;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e)
    {
        if ((e.getPlayer().hasPermission("chatreaction.admin") || e.getPlayer().isOp()) && this.plugin.updater != null && SpigotUpdater.updateAvailable())
        {
            e.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', "&8&l» &7&oAn Update for &aChatReaction &7&ohas been found!"));
            e.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', "&8&l» &7Your Version: [&c" + this.plugin.getDescription().getVersion() + "&7] &8&l- &7New Version: [&c" + SpigotUpdater.getHighest() + "&7]"));
            e.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', "&8&l» &7&oDownload at &fhttp://www.spigotmc.org/resources/chatreaction.3748/"));
        }

        if (this.plugin.getOptions().trackStats())
        {
            Bukkit.getScheduler().runTaskAsynchronously(this.plugin, new LoadPlayerTask(this.plugin, e.getPlayer().getUniqueId().toString(), e.getPlayer().getName()));
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e)
    {
        if (this.plugin.getOptions().trackStats())
        {
            String uuid = e.getPlayer().getUniqueId().toString();

            if (ChatReaction.hasData(uuid))
            {
                ChatReaction.reactionPlayers.remove(uuid);
            }
        }
    }

    synchronized boolean handleReaction(Player p, String word)
    {
        if (ChatReaction.getCurrentWord() == null)
        {
            ChatReaction.setRunning(false);
            return false;
        }

        word = ChatColor.translateAlternateColorCodes('&', word);
        word = ChatColor.stripColor(word);

        if (word.endsWith("."))
        {
            word = word.substring(0, word.length() - 1);
        }

        if (this.plugin.getOptions().ignoreCase())
        {
            if (!word.equalsIgnoreCase(ChatReaction.getCurrentWord()))
            {
                return false;
            }
        }
        else if (!word.equals(ChatReaction.getCurrentWord()))
        {
            return false;
        }

        if (ChatReaction.getEndTask() != null)
        {
            ChatReaction.getEndTask().cancel();
            ChatReaction.setEndTask(null);
        }

        String time = this.plugin.getTime(System.currentTimeMillis());
        int wins = 1;
        boolean trackWins = false;

        if (this.plugin.getOptions().trackStats())
        {
            trackWins = true;

            if (ChatReaction.reactionPlayers != null && ChatReaction.reactionPlayers.containsKey(p.getUniqueId().toString()))
            {
                ReactionPlayer pl = ChatReaction.reactionPlayers.get(p.getUniqueId().toString());

                if (pl != null)
                {
                    wins = pl.getWins() + 1;
                }
            }
        }

        List<String> winMsg = null;

        if (ChatReaction.isScrambled())
        {
            winMsg = this.plugin.getOptions().scrambleWinMsg();
        }
        else
        {
            winMsg = this.plugin.getOptions().winMsg();
        }

        ReactionWinEvent event = new ReactionWinEvent(p, word, Double.parseDouble(time), trackWins, wins, winMsg);
        Bukkit.getPluginManager().callEvent(event);

        if (event.showWinMessage() && event.getWinMessage() != null)
        {
            for (String line : event.getWinMessage())
            {
                this.plugin.sendMsg(line.replace("%player%", p.getName()).replace("%word%", word).replace("%time%", time));
            }
        }

        ChatReaction.setRunning(false);
        ChatReaction.setCurrentWord(null);
        ChatReaction.setDisplayWord(null);
        ChatReaction.setScrambled(false);
        ChatReaction.setStartTime(0L);

        return true;
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onChat(AsyncPlayerChatEvent e)
    {
        if (!ChatReaction.isRunning())
        {
            return;
        }

        if (this.plugin.getOptions().disabledWorlds() != null && !this.plugin.getOptions().disabledWorlds().isEmpty())
        {
            if (this.plugin.getOptions().disabledWorlds().contains(e.getPlayer().getWorld().getName()))
            {
                return;
            }
        }

        if (handleReaction(e.getPlayer(), e.getMessage()))
        {
            final String name = e.getPlayer().getName();
            Bukkit.getScheduler().runTask(this.plugin, () -> ChatReactionListener.this.plugin.giveRewards(name));

            if (this.plugin.getOptions().trackStats())
            {
                Bukkit.getScheduler().runTaskAsynchronously(this.plugin, new AddWinTask(this.plugin, e.getPlayer().getUniqueId().toString(), e.getPlayer().getName()));
            }

            e.setCancelled(true);
        }
    }
}