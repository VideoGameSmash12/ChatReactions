package me.clip.chatreaction;


import me.clip.chatreaction.reactionplayer.ReactionPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;


public class ReactionCommands implements CommandExecutor
{
    private final ChatReaction plugin;

    public ReactionCommands(ChatReaction i)
    {
        this.plugin = i;
    }

    public boolean onCommand(CommandSender s, Command c, String label, String[] args)
    {
        if (args.length == 0)
        {
            sms(s, "&8&m+----------------+");
            sms(s, "&e&lChatReaction &f&o" + this.plugin.getDescription().getVersion());
            sms(s, "&7Created by &f&oextended_clip");
            sms(s, "&8&m+----------------+");
            return true;
        }

        if (!(s instanceof Player))
        {
            if (args.length > 0 && args[0].equalsIgnoreCase("help"))
            {


                sms(s, "&8&m+----------------+");

                sms(s, "&e&lChatReaction &f&oHelp");

                sms(s, " ");

                sms(s, "&e/reaction");

                sms(s, "&f&oView plugin information");

                sms(s, "&e/reaction status");

                sms(s, "&f&oView current status");

                sms(s, "&e/reaction start");

                sms(s, "&f&oStart ChatReaction");

                sms(s, "&e/reaction stop");

                sms(s, "&f&oStop ChatReaction");

                sms(s, "&e/reaction reload");

                sms(s, "&f&oReload the config file");

                sms(s, "&8&m+----------------+");

                return true;

            }

            if (args.length > 0 && args[0].equalsIgnoreCase("reload"))
            {


                this.plugin.reload();

                sms(s, "&8&m+----------------+");

                sms(s, "&e&lChatReaction &f&oReloaded");

                sms(s, "&8&m+----------------+");

                return true;

            }

            if (args.length > 0 && args[0].equalsIgnoreCase("status"))
            {


                sms(s, "&8&m+----------------+");

                sms(s, "&e&lChatReaction &f&ostatus");

                sms(s, " ");

                if (ChatReaction.getIntervalTask() != null)
                {


                    sms(s, "&fInterval task: &arunning");


                    if (ChatReaction.isRunning() && ChatReaction.getCurrentWord() != null)
                    {

                        sms(s, "&fCurrent word: &a" + ChatReaction.getCurrentWord());

                    }


                    if (this.plugin.getOptions().useCustomWords())
                    {


                        sms(s, "&fUse custom words: &ayes");


                        if (this.plugin.getOptions().customWords() != null)
                        {

                            sms(s, "&fWords loaded: &f" + this.plugin.getOptions().customWords().size());

                        }
                        else
                        {

                            sms(s, "&fWords loaded: &cnone");

                        }

                    }
                    else
                    {

                        sms(s, "&fUse custom words: &cno");

                    }


                    sms(s, "&fPlayers needed to start: &7(&f" +
                            ChatReaction.getOnline() + "&7/&a" +
                            this.plugin.getOptions().playersNeeded() + "&7)");

                }

                else
                {


                    sms(s, "&fInterval task: &cstopped");

                }

                sms(s, "&8&m+----------------+");

                return true;

            }

            if (args.length > 0 && args[0].equalsIgnoreCase("start"))
            {

                if (ChatReaction.getIntervalTask() == null)
                {

                    this.plugin.start();

                    sms(s, "&8&m+----------------+");
                    sms(s, "&e&lChatReaction &f&ostarted");
                    sms(s, "&8&m+----------------+");

                }

                else
                {

                    sms(s, "&8&m+----------------+");
                    sms(s, "&e&lChatReaction &f&ois already started");
                    sms(s, "&8&m+----------------+");

                }


                return true;
            }
            if (args.length > 0 && args[0].equalsIgnoreCase("stop"))
            {

                if (ChatReaction.getIntervalTask() != null)
                {

                    this.plugin.stop();

                    sms(s, "&8&m+----------------+");
                    sms(s, "&e&lChatReaction &f&ostopped");
                    sms(s, "&8&m+----------------+");

                }

                else
                {

                    sms(s, "&8&m+----------------+");
                    sms(s, "&e&lChatReaction &f&ois already stopped");
                    sms(s, "&8&m+----------------+");

                }

                return true;

            }

            sms(s, "&8&m+----------------+");
            sms(s, "&c&oIncorrect usage! Use &e/reaction help");
            sms(s, "&8&m+----------------+");
            return true;

        }

        Player p = (Player) s;

        if (args[0].equalsIgnoreCase("help"))
        {
            sms(s, "&8&m+----------------+");
            sms(s, "&e&lChatReaction &f&oHelp");
            sms(s, " ");
            sms(s, "&e/reaction");
            sms(s, "&f&oView plugin information");
            if (this.plugin.getOptions().trackStats())
            {
                sms(s, "&e/reaction wins (player)");
                sms(s, "&f&oView your/others wins");
                sms(s, "&e/reaction top (page)");
                sms(s, "&f&oView the top winners");
            }

            if (p.hasPermission("chatreaction.admin"))
            {
                sms(s, "&e/reaction status");
                sms(s, "&f&oView current status");
                sms(s, "&e/reaction start");
                sms(s, "&f&oStart ChatReaction");
                sms(s, "&e/reaction stop");
                sms(s, "&f&oStop ChatReaction");
                sms(s, "&e/reaction reload");
                sms(s, "&f&oReload the config file");
            }

            sms(s, "&8&m+----------------+");
            return true;
        }
        if (args[0].equalsIgnoreCase("wins"))
        {
            if (!this.plugin.getOptions().trackStats())
            {
                sms(s, "&7Stats are currently disabled!");
                return true;
            }

            if (args.length == 1)
            {
                if (!p.hasPermission("chatreaction.wins"))
                {
                    sms(s, "&c&oYou don't have permission to do that!");
                    return true;
                }

                sms(s, "&8&m+----------------+");

                int i = 0;

                if (ChatReaction.hasData(p.getUniqueId().toString()))
                {
                    i = (ChatReaction.reactionPlayers.get(p.getUniqueId().toString())).getWins();
                }

                sms(s, "&aYour reaction wins: &f" + i);
                sms(s, "&8&m+----------------+");
                return true;
            }

            if (!p.hasPermission("chatreaction.wins.others"))
            {
                sms(s, "&c&oYou don't have permission to do that!");
                return true;
            }

            final String playerName = args[1];

            Player target = Bukkit.getPlayer(playerName);

            if (target == null)
            {
                sms(s, playerName + " &cis not online! Fetching from database!");

                if (ChatReaction.topPlayers != null && !ChatReaction.topPlayers.isEmpty())
                {
                    for (ReactionPlayer pl : ChatReaction.topPlayers)
                    {
                        if (pl.getName().equalsIgnoreCase(playerName))
                        {
                            sms(s, "&8&m+----------------+");
                            sms(s,
                                    "&f" + pl.getName() +
                                            "'s &areaction wins: &f" +
                                            pl.getWins());
                            sms(s, "&8&m+----------------+");
                            return true;
                        }
                    }

                    final String sender = s.getName();

                    Bukkit.getScheduler().runTaskAsynchronously(plugin,
                            () -> {
                                final ReactionPlayer pl = plugin
                                        .loadPlayerByName(playerName);

                                Bukkit.getScheduler().runTask(plugin,
                                        new Runnable()
                                        {
                                            public void run()
                                            {
                                                Player waiting =
                                                        Bukkit.getServer()
                                                                .getPlayer(
                                                                        sender);

                                                if (waiting != null)
                                                {
                                                    if (pl != null)
                                                    {
                                                        sms(waiting, "&8&m+----------------+");
                                                        sms(waiting, "&f" + pl.getName() + "'s &areaction wins: &f" + pl.getWins());
                                                        sms(waiting, "&8&m+----------------+");
                                                    }
                                                    else
                                                    {
                                                        sms(waiting, "&8&m+----------------+");
                                                        sms(waiting, "&cNo win data saved for &f" + playerName);
                                                        sms(waiting, "&8&m+----------------+");
                                                    }
                                                }
                                            }
                                        });
                            });
                }

                return true;
            }

            sms(s, "&8&m+----------------+");

            int wins = 0;

            if (ChatReaction.hasData(target.getUniqueId().toString()))
            {
                wins = ChatReaction.reactionPlayers.get(target.getUniqueId().toString()).getWins();
            }

            sms(s, "&f" + target.getName() + "'s &areaction wins: &f" +
                    wins);

            sms(s, "&8&m+----------------+");

            return true;
        }

        if (args[0].equalsIgnoreCase("top"))
        {
            if (!this.plugin.getOptions().trackStats())
            {
                sms(s, "&7Stats are currently disabled!");
                return true;
            }

            if (!p.hasPermission("chatreaction.top"))
            {
                sms(s, "&c&oYou don't have permission to do that!");
                return true;
            }

            sms(s, "&8&m+----------------+");

            if (ChatReaction.topPlayers == null ||
                    ChatReaction.topPlayers.isEmpty())
            {
                sms(s,
                        "&fThere are no top players loaded at the moment. Try back later!");
                sms(s, "&8&m+----------------+");
                return true;
            }

            int page = 1;
            if (args.length > 1)
            {
                try
                {
                    page = Integer.parseInt(args[1]);
                }
                catch (Exception e)
                {
                    sms(s, "&cPage &f" + args[1] + " &cis invalid!");
                    sms(s, "&8&m+----------------+");
                    return true;
                }
            }

            int pages = ChatReaction.topPlayers.size() / 10;

            if (ChatReaction.topPlayers.size() % 10 > 0)
            {
                pages++;
            }

            if (pages < page)
            {
                sms(s, "&cIncorrect page. Max pages: &f" + pages);
                sms(s, "&8&m+----------------+");
                return true;
            }

            int max = page * 10;
            int min = max - 10;

            int rank = 1;

            for (ReactionPlayer top : ChatReaction.topPlayers)
            {
                if (rank < min)
                {
                    rank++;
                    continue;
                }
                if (rank > max)
                {
                    break;
                }

                sms(s, rank + "&7: &f" + top.getName() + " &aWins&7: &f" + top.getWins());
                rank++;
            }

            sms(s, "&8&m+----------------+");
            return true;
        }

        if (!p.hasPermission("chatreaction.admin"))
        {
            sms(s, "&c&oYou don't have permission to do that!");
            return true;
        }

        if (args[0].equalsIgnoreCase("reload"))
        {
            plugin.reload();
            sms(s, "&8&m+----------------+");
            sms(s, "&e&lChatReaction &f&oReloaded");
            sms(s, "&8&m+----------------+");
            return true;
        }
        if (args[0].equalsIgnoreCase("status"))
        {
            sms(s, "&8&m+----------------+");
            sms(s, "&e&lChatReaction &f&ostatus");
            sms(s, " ");
            if (ChatReaction.getIntervalTask() != null)
            {
                sms(s, "&fInterval task: &arunning");

                if (ChatReaction.isRunning() && ChatReaction.getCurrentWord() != null)
                {
                    sms(s, "&fCurrent word: &a" + ChatReaction.getCurrentWord());
                }

                if (this.plugin.getOptions().useCustomWords())
                {
                    sms(s, "&fUse custom words: &ayes");

                    if (this.plugin.getOptions().customWords() != null)
                    {
                        sms(s, "&fWords loaded: &f" + this.plugin.getOptions().customWords().size());
                    }
                    else
                    {
                        sms(s, "&fWords loaded: &cnone");
                    }
                }
                else
                {
                    sms(s, "&fUse custom words: &cno");
                }

                sms(s, "&fPlayers needed to start: &7(&f" +
                    ChatReaction.getOnline() + "&7/&a" +
                    this.plugin.getOptions().playersNeeded() + "&7)");
            }

            else
            {
                sms(s, "&fInterval task: &cstopped");
            }
            sms(s, "&8&m+----------------+");

            return true;
        }
        if (args[0].equalsIgnoreCase("start"))
        {
            if (ChatReaction.getIntervalTask() == null)
            {
                this.plugin.start();

                sms(s, "&8&m+----------------+");
                sms(s, "&e&lChatReaction &f&ostarted");
                sms(s, "&8&m+----------------+");
            }
            else
            {
                sms(s, "&8&m+----------------+");
                sms(s, "&e&lChatReaction &f&ois already started");
                sms(s, "&8&m+----------------+");
            }

            return true;
        }
        if (args[0].equalsIgnoreCase("stop"))
        {
            if (ChatReaction.getIntervalTask() != null)
            {
                this.plugin.stop();

                sms(s, "&8&m+----------------+");
                sms(s, "&e&lChatReaction &f&ostopped");
                sms(s, "&8&m+----------------+");
            }
            else
            {
                sms(s, "&8&m+----------------+");
                sms(s, "&e&lChatReaction &f&ois already stopped");
                sms(s, "&8&m+----------------+");
            }

            return true;
        }

        sms(s, "&8&m+----------------+");
        sms(s, "&c&oIncorrect usage! Use &e/reaction help");
        sms(s, "&8&m+----------------+");
        return true;
    }

    public void sms(CommandSender s, String msg)
    {
        s.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
    }
}