package fr.nkri.japi.utils.configs;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public class ConfigFile {

    private final Plugin main;
    private final File file;
    private YamlConfiguration conf;

    public ConfigFile(final Plugin main, final String fileName) {
        this.main = main;
        this.file = new File(main.getDataFolder(), fileName);

        if (!file.exists())
            try {
                if (!file.getParentFile().exists())
                    file.getParentFile().mkdirs();
                InputStream in = main.getResource(fileName);

                if (in != null) {
                    OutputStream out = Files.newOutputStream(file.toPath());

                    byte[] buf = new byte[1024 * 4];
                    int len = in.read(buf);

                    while (len != -1) {
                        out.write(buf, 0, len);
                        len = in.read(buf);
                    }
                    out.close();
                    in.close();
                } else
                    file.createNewFile();
            } catch (Exception e) {
                e.printStackTrace();
            }
        reload();
    }

    public void reload() {
        try {
            conf = YamlConfiguration.loadConfiguration(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public YamlConfiguration get() {
        return conf;
    }

    public void save() {
        try {
            conf.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}