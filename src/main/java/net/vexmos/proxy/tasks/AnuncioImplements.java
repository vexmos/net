package net.vexmos.proxy.tasks;

import net.vexmos.proxy.VexmosPROXY;
import net.vexmos.proxy.api.BungeeConfig;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.scheduler.TaskScheduler;

import java.util.List;

public class AnuncioImplements extends Plugin {

    public static void setup() {
        carregarAnuncios();
        iniciarTarefaDeAnuncios();
    }

    private static List<String> anuncios;
    private static String prefixo;
    private static int cooldown;

    static BungeeConfig config = new BungeeConfig("anuncios.yml");

    public static void carregarAnuncios() {
        config = new BungeeConfig("anuncios.yml");
        config.saveDefault();

        prefixo = config.getConfig().getString("prefix-anuncios", "&6&lVEXMOS &7» ");
        cooldown = config.getConfig().getInt("cooldown-anuncios", 21);
        anuncios = config.getConfig().getStringList("anuncios");

    }

    public static void iniciarTarefaDeAnuncios() {
        TaskScheduler scheduler = VexmosPROXY.get().getProxy().getScheduler();
        scheduler.schedule(VexmosPROXY.get(), new AnuncioTask(anuncios, prefixo), 0, cooldown, java.util.concurrent.TimeUnit.SECONDS);
    }

    public String getPrefixo() {
        return prefixo;
    }
}
