package net.vexmos.proxy.commands;

import net.vexmos.proxy.VexmosPROXY;
import net.vexmos.database.bungee.ConnectBungee;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.*;

public class GroupCommand extends Commands {

    private final ConnectBungee database;
    private final Map<String, String> groupMap;



    public GroupCommand(ConnectBungee database) {
        super("group", "grupos");
        this.database = database;
        this.groupMap = new HashMap<>();
        setupGroupMap();
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

    private void setupGroupMap() {
        groupMap.put("membro", "§7Membro");
        groupMap.put("diretor", "§4Diretor");
        groupMap.put("admin", "§cAdmin");
        groupMap.put("dev", "§aDev");
        groupMap.put("construtor", "§eConstrutores");
        groupMap.put("mod", "§5Mod");
        groupMap.put("emerald", "§aEmerald");
        groupMap.put("diamond", "§bDiamond");
        groupMap.put("gold", "§6Gold");
        groupMap.put("suporte", "§3Suporte");
    }

    public String getPlayerGroup(ProxiedPlayer player) {
        String group = database.getPlayerGroup(player.getName());
        return groupMap.getOrDefault(group, "membro");
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        if (!hasRequiredPermission(sender)) {
            sender.sendMessage(new TextComponent("§cVocê não tem permissão para isso."));
            return;
        }
        if (args.length < 1) {
            sender.sendMessage(new TextComponent("§cUso correto: /group <add/check> <jogador> [grupo]"));
            return;
        }

        String action = args[0].toLowerCase();

        if (action.equals("add")) {
            if (args.length < 3) {
                sender.sendMessage(new TextComponent("§cUso correto: /group add <jogador> <grupo>"));
                return;
            }

            String playerzinho = args[1];;
            ProxiedPlayer target = VexmosPROXY.get().getProxy().getPlayer(args[1]);
            String group = args[2].toLowerCase();

            if (target == null) {
                database.setPlayerGroup(playerzinho, group);
                sender.sendMessage(new TextComponent("§cO jogador está offline, esteja ciente."));
                sender.sendMessage(new TextComponent("§aO grupo de " + playerzinho + " foi atualizado para " + groupMap.get(group) + "."));
                return;
            }

            if (!groupMap.containsKey(group)) {
                sender.sendMessage(new TextComponent("§cGrupo inválido. Grupos válidos: " + String.join(", ", groupMap.keySet())));
                return;
            }

            database.setPlayerGroup(target.getName(), group);

            target.sendMessage(new TextComponent("§eSeu grupo foi atualizado para " + groupMap.get(group)));
            sender.sendMessage(new TextComponent("§aO grupo de " + target.getName() + " foi atualizado para " + groupMap.get(group) + "."));

        } else if (action.equals("check")) {
            if (args.length < 2) {
                sender.sendMessage(new TextComponent("§cUso correto: /group check <jogador>"));
                return;
            }

            ProxiedPlayer target = VexmosPROXY.get().getProxy().getPlayer(args[1]);
            if (target == null) {
                sender.sendMessage(new TextComponent("§cJogador não encontrado."));
                return;
            }

            String group = database.getPlayerGroup(target.getName());

            sender.sendMessage(new TextComponent("§aO grupo de " + target.getName() + " é " + groupMap.getOrDefault(group, "§7Membro") + "."));

        } else {
            sender.sendMessage(new TextComponent("§cAção inválida. Use /group <add/check>"));
        }
    }}