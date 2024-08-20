package net.vexmos.proxy.listeners;

import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.event.ProxyPingEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.vexmos.proxy.api.BungeeConfig;

public class MotdListener implements Listener {

    private BungeeConfig config;
    private BungeeConfig maintenanceConfig;

    public MotdListener() {
        config = new BungeeConfig("motd.yml");
        maintenanceConfig = new BungeeConfig("manutencao.yml");
        config.saveDefault();
        config.saveDefaultConfig();
        maintenanceConfig.saveDefault();
        maintenanceConfig.saveDefaultConfig();
    }

    @SuppressWarnings("deprecation")
    @EventHandler
    public void onProxyPing(ProxyPingEvent event) {
        // Recarrega a configuração de manutenção antes de usá-la
        ServerPing ping = event.getResponse();
        maintenanceConfig.reloadConfig();

        boolean isMaintenance = maintenanceConfig.getConfig().getBoolean("manutencao");

        String motd;
        if (isMaintenance) {
            ping.getVersion().setName("§cX " + ping.getVersion().getName());
            motd = maintenanceConfig.getConfig().getString("MOTD_manutencao.line1") + "\n" +
                    maintenanceConfig.getConfig().getString("MOTD_manutencao.line2").replace("%data%", maintenanceConfig.getConfig().getString("data"));
        } else {
            motd = config.getConfig().getString("motd.line1") + "\n" +
                    config.getConfig().getString("motd.line2");
        }

        // & para cor e \n para quebra de linha
        motd = motd.replace("&", "§").replace("\\n", "\n");
        event.getResponse().setDescription(motd);
    }

}