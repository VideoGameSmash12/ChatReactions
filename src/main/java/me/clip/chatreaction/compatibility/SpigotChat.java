package me.clip.chatreaction.compatibility;

import java.util.List;

import me.clip.chatreaction.ChatReaction;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class SpigotChat
{
    private ChatReaction plugin;

    public SpigotChat(ChatReaction instance)
    {
        this.plugin = instance;
    }

    public void sendMessage(String message, String tooltip)
    {
        if (tooltip == null)
        {
            this.plugin.sendMsg(message);

            return;
        }

        TextComponent com = new TextComponent(TextComponent.fromLegacyText(message));
        com.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText(tooltip)));

        List<String> disabled = this.plugin.getOptions().disabledWorlds();

        if (disabled == null || disabled.isEmpty())
        {
            for (World w : Bukkit.getWorlds())
            {
                if (w.getPlayers().isEmpty())
                {
                    continue;
                }

                for (Player p : w.getPlayers())
                {
                    p.spigot().sendMessage(com);
                }
            }

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
                p.spigot().sendMessage(com);
        }
    }
}