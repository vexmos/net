package net.vexmos.bungee.commands;

import net.vexmos.bungee.Bungee;
import net.vexmos.bungee.api.BungeeConfig;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PreLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class MaintenanceCommand extends Commands implements Listener {

    private BungeeConfig config;

    public MaintenanceCommand() {
        super("maintenance", "manutencao");
        config = new BungeeConfig("manutencao.yml");
        config.saveDefault();
        Bungee.get().getProxy().getPluginManager().registerListener(Bungee.get(), this);
    }

    private static final List<String> REQUIRED_PERMISSIONS = Arrays.asList(
            "group.diretor", "group.dev"
    );

    private boolean hasRequiredPermission(CommandSender sender) {
        for (String permission : REQUIRED_PERMISSIONS) {
            if (sender.hasPermission(permission)) {
                return true;
            }
        }
        return false;
    }
    
    
    public void kickNonAllowedPlayers() {
        boolean isMaintenance = config.getConfig().getBoolean("manutencao");

        if (isMaintenance) {
            for (ProxiedPlayer player : ProxyServer.getInstance().getPlayers()) {
                String playerName = player.getName();
                if (!config.getConfig().getStringList("ManutencaoLista").contains(playerName)) {
                    player.disconnect(new TextComponent(config.getConfig().getString("Kickmessage")
                            .replace("&", "§")
                            .replace("\\n", "\n")));
                }
            }
        }
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        if (!hasRequiredPermission(sender)) {
            sender.sendMessage(new TextComponent("§cVocê não tem permissão para isso."));
            return;
        }

        if (args.length < 1) {
            sender.sendMessage(new TextComponent("§cUso: /manutencao <on/off/add/remove/reload/lista>"));
            return;
        }

        List<String> allowedPlayers = config.getConfig().getStringList("ManutencaoLista");
        String jogador;

        switch (args[0].toLowerCase()) {
	        case "on":
	            config.getConfig().set("manutencao", true);
	            config.getConfig().set("data", new SimpleDateFormat("HH:mm").format(new Date()));
	            config.saveConfig();
	            config.reloadConfig(); // Adicionado para garantir que a configuração mais recente seja usada
	            sender.sendMessage(new TextComponent("§aManutenção ativada com sucesso!"));
	            kickNonAllowedPlayers();
	            break;
            case "off":
                config.getConfig().set("manutencao", false);
                config.saveConfig();
                config.reloadConfig(); // Adicionado para garantir que a configuração mais recente seja usada
                sender.sendMessage(new TextComponent("§aManutenção desativada com sucesso!"));
                break;
            case "add":
            case "adicionar":
                if (args.length < 2) {
                    sender.sendMessage(new TextComponent("§cUso: /manutencao add <jogador>"));
                    return;
                }
                jogador = args[1];
                if (!allowedPlayers.contains(jogador)) {
                    allowedPlayers.add(jogador);
                    config.getConfig().set("ManutencaoLista", allowedPlayers);
                    config.saveConfig();
                    config.reloadConfig(); // Adicionado para garantir que a configuração mais recente seja usada
                    sender.sendMessage(new TextComponent("§aJogador " + jogador + " adicionado à lista de manutenção com sucesso!"));
                } else {
                    sender.sendMessage(new TextComponent("§cJogador " + jogador + " já está na lista de manutenção."));
                }
                break;
            case "remove":
            case "remover":
                if (args.length < 2) {
                    sender.sendMessage(new TextComponent("§cUso: /manutencao remove <jogador>"));
                    return;
                }
                jogador = args[1];
                if (allowedPlayers.contains(jogador)) {
                    allowedPlayers.remove(jogador);
                    config.getConfig().set("ManutencaoLista", allowedPlayers);
                    config.saveConfig();
                    config.reloadConfig(); // Adicionado para garantir que a configuração mais recente seja usada
                    sender.sendMessage(new TextComponent("§aJogador " + jogador + " removido da lista de manutenção com sucesso!"));
                    boolean isMaintenance = config.getConfig().getBoolean("manutencao");
                    if (isMaintenance) {
                    	ProxiedPlayer player = ProxyServer.getInstance().getPlayer(jogador);

                        if (player != null && player.isConnected()) {
                        	player.disconnect(new TextComponent("&cSua conexão foi interrompida."
                                    .replace("&", "§")
                                    .replace("\\n", "\n")));;
                        }
                    }
                } else {
                    sender.sendMessage(new TextComponent("§cJogador " + jogador + " não está na lista de manutenção."));
                }
                break;
            case "reload":
                config.saveConfig();
                config.reloadConfig();
                sender.sendMessage(new TextComponent("§aConfiguração de manutenção recarregada com sucesso!"));
                break;
            case "lista":
            case "list":
                if (allowedPlayers.isEmpty()) {
                    sender.sendMessage(new TextComponent("§cA lista de jogadores em manutenção está vazia."));
                } else {
                    sender.sendMessage(new TextComponent("§aLista de jogadores na manutenção:"));
                    for (String player : allowedPlayers) {
                        sender.sendMessage(new TextComponent("§a- " + player));
                    }
                }
                break;
            default:
                sender.sendMessage(new TextComponent("§cUso: /manutencao <on/off/add/remove/reload/lista>"));
                break;
        }
    }

    @SuppressWarnings("deprecation")
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPreLogin(PreLoginEvent event) {
        if (config.getConfig().getBoolean("manutencao")) {
            String jogador = event.getConnection().getName();
            List<String> allowedPlayers = config.getConfig().getStringList("ManutencaoLista");
            if (!allowedPlayers.contains(jogador)) {
                event.setCancelReason(new TextComponent(config.getConfig().getString("Proibido_manutencao")
                        .replace("&", "§")
                        .replace("\\n", "\n")
                        .replace("%data%", config.getConfig().getString("data"))));
                event.setCancelled(true);
            }
        }
    }
}
