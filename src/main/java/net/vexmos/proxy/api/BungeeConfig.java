package net.vexmos.proxy.api;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;
import net.vexmos.proxy.VexmosPROXY;

public class BungeeConfig {

    private File file;
    private Configuration config;

    /**
     * @param name Pega o nome da Configuração do Proxy
     */
    public BungeeConfig(String name) {
        File dataFolder = VexmosPROXY.get().getDataFolder();
        if (!dataFolder.exists()) {
            dataFolder.mkdirs();
        }
        this.file = new File(dataFolder, name);
        if (!this.file.getParentFile().exists()) {
            this.file.getParentFile().mkdirs();
        }
        if (!this.file.exists())
            saveDefaultConfig();
        reloadConfig();
    }


    /**
     * @return Retorna a config do Proxy
     */
    public Configuration getConfig() {
        return this.config;
    }

    /**
     * Reinicia a Configuração do Proxy
     */
    public void reloadConfig() {
        try {
            this.config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(this.file);
            InputStream inputStream = VexmosPROXY.get().getResourceAsStream(this.file.getName());
            if (inputStream != null) {
                Configuration inputConfig = ConfigurationProvider.getProvider(YamlConfiguration.class).load(inputStream);
                setDefaults(inputConfig);
            }
        } catch (IOException e) {
            VexmosPROXY.get().getLogger().info("Não foi possível reiniciar a config " + file.getName());
        }
    }

    /**
     * Salvando a Configuração do Proxy
     * sem nenhum tipo de texto dentro do arquivo ou
     * se for incluido qualquer configuração após a criação
     */
    public void saveConfig() {
        try {
            ConfigurationProvider.getProvider(YamlConfiguration.class).save(this.config, this.file);
        } catch (IOException e) {
            VexmosPROXY.get().getLogger().info("Não foi possível salvar a config " + file.getName());
        }
    }

    /**
     * Salvando a Configuração do Proxy
     * com todos os textos que foram escritos
     * no arquivo que foi criado dentro da pasta
     * resources
     */
    public void saveDefault() {
        setDefaults(ConfigurationProvider.getProvider(YamlConfiguration.class).load(VexmosPROXY.get().getResourceAsStream(this.file.getName())));
        saveConfig();
    }

    /**
     * Copia um arquivo de certo caminho e cola em outro caminho.
     *
     */
    public void saveResource(String resourcePath, boolean b) {
        InputStream in = VexmosPROXY.get().getResourceAsStream(resourcePath);
        if (in != null) {
            File outFile = new File(VexmosPROXY.get().getDataFolder(), resourcePath);
            FileOutputStream out = null;
            try {
                out = new FileOutputStream(outFile);
                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0)
                    out.write(buf, 0, len);
            } catch (IOException e) {
                VexmosPROXY.get().getLogger().info("Erro ao salvar o recurso " + resourcePath + ": " + e.getMessage());
            } finally {
                try {
                    in.close();
                    if (out != null)
                        out.close();
                } catch (IOException e) {
                    VexmosPROXY.get().getLogger().info("Recurso " + resourcePath + " não encontrado");
                }
            }
        }
    }

    /**
     * Cria um arquivo de configuração caso não exista outro arquivo.
     */
    public void saveDefaultConfig() {
        File configFile = new File(VexmosPROXY.get().getDataFolder(), this.file.getName());
        if (!configFile.exists()) {
            try {
                configFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        saveResource(this.file.getName(), true);
    }

    /**
     * @param defaults Salva valores padrão em uma configuração
     *                 em tempo de execução.
     */
    public void setDefaults(Configuration defaults) {
        for (String key : defaults.getKeys()) {
            if (!getConfig().contains(key))
                getConfig().set(key, defaults.get(key));
        }
    }
}
