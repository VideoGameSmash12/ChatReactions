package me.clip.chatreaction.hooks;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;

public class VaultHook
{
    private Economy econ;

    public boolean setup()
    {
        RegisteredServiceProvider<Economy> rsp = Bukkit.getServer().getServicesManager().getRegistration(Economy.class);

        if (rsp == null)
        {
            return false;
        }

        this.econ = rsp.getProvider();

        return (this.econ != null);
    }

    public boolean isAvailable()
    {
        return (this.econ != null);
    }

    public Economy getEcon()
    {
        return this.econ;
    }
}