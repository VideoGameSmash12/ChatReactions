package me.clip.chatreaction.updater;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Pattern;

import me.clip.chatreaction.ChatReaction;

public class SpigotUpdater
{
    private ChatReaction plugin;
    final int resource = 3748;
    private static String latestVersion = "";
    private static boolean updateAvailable = false;

    public SpigotUpdater(ChatReaction i)
    {
        this.plugin = i;
    }

    private String getSpigotVersion()
    {
        try
        {
            HttpURLConnection con = (HttpURLConnection) (new URL(
                    "http://www.spigotmc.org/api/general.php")).openConnection();
            con.setDoOutput(true);
            con.setRequestMethod("POST");
            con.getOutputStream()
                    .write("key=98BE0FE67F88AB82B4C197FAF1DC3B69206EFDCC4D3B80FC83A00037510B99B4&resource=3748"
                            .getBytes("UTF-8"));
            String version = (new BufferedReader(new InputStreamReader(
                    con.getInputStream()))).readLine();
            if (version.length() <= 7)
            {
                return version;
            }
        }
        catch (Exception ex)
        {
            System.out.println("---------------------------");
            System.out.println("     ChatReaction Updater");
            System.out.println(" ");
            System.out.println("Could not connect to spigotmc.org");
            System.out.println("to check for updates! ");
            System.out.println(" ");
            System.out.println("---------------------------");
        }
        return null;
    }

    private boolean checkHigher(String currentVersion, String newVersion)
    {
        String current = toReadable(currentVersion);
        String newVers = toReadable(newVersion);
        return (current.compareTo(newVers) < 0);
    }

    public boolean checkUpdates()
    {
        if (getHighest() != "")
        {
            return true;
        }
        String version = getSpigotVersion();
        if (version != null &&
                checkHigher(this.plugin.getDescription().getVersion(), version))
        {
            latestVersion = version;
            updateAvailable = true;
            return true;
        }

        return false;
    }

    public static boolean updateAvailable()
    {
        return updateAvailable;
    }

    public static String getHighest()
    {
        return latestVersion;
    }

    private String toReadable(String version)
    {
        String[] split = Pattern.compile(".", 16).split(
                version.replace("v", ""));
        version = "";
        byte b;
        int i;
        String[] arrayOfString1;
        for (i = (arrayOfString1 = split).length, b = 0; b < i; )
        {
            String s = arrayOfString1[b];
            version = String.valueOf(version) + String.format("%4s", new Object[]{s});
            b++;
        }
        return version;
    }
}