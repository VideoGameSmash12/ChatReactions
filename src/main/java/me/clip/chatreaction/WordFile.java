package me.clip.chatreaction;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class WordFile
{
    private ChatReaction plugin;
    private File file;

    public WordFile(ChatReaction i)
    {
        this.plugin = i;
        this.file = new File(this.plugin.getDataFolder(), "words.txt");
    }

    public List<String> getText()
    {
        File file = new File(this.plugin.getDataFolder(), "words.txt");

        if (!file.exists())
        {
            try
            {
                file.createNewFile();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }

            return null;
        }

        List<String> text = new ArrayList<>();

        try
        {
            Scanner scanner = new Scanner(file);

            while (scanner.hasNextLine())
            {
                String line = scanner.nextLine();
                if (line == null)
                {
                    continue;
                }

                text.add(line);
            }

            scanner.close();
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
            return null;
        }

        return text;
    }

    public List<String> loadWords(boolean eachLineWord)
    {
        List<String> words = new ArrayList<>();

        try
        {
            if (!this.file.exists())
            {
                try
                {
                    this.file.createNewFile();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                    return words;
                }

                this.plugin.getLogger().info("Your words.txt file is empty!");
                return Arrays.asList("add", "words", "to", "your", "words.txt", "file");
            }

            Scanner scanner = new Scanner(this.file);
            while (scanner.hasNextLine())
            {
                String line = scanner.nextLine();
                if (line == null || line.isEmpty())
                {
                    continue;
                }

                if (eachLineWord)
                {
                    words.add(line);
                    continue;
                }

                if (line.contains(" "))
                {
                    String[] split = line.split(" ");

                    if (split == null)
                        continue;
                    byte b;
                    int i;

                    String[] arrayOfString1;
                    for (i = (arrayOfString1 = split).length, b = 0; b < i; )
                    {
                        String word = arrayOfString1[b];
                        if (word != null && !word.isEmpty())
                        {
                            words.add(word);
                        }

                        b++;
                    }

                    continue;
                }

                words.add(line);
            }

            scanner.close();
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
            return null;
        }

        this.plugin.getLogger().info(words.size() + " words loaded!");
        return words;
    }
}