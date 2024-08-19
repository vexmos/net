package net.vexmos.bungee.commands;

import net.vexmos.bungee.Bungee;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


public class BlockCommand extends Commands {

    private static final Map<String, Set<String>> blockedUsers = new HashMap<>();

    public BlockCommand() {
        super("block");
    }

    public static boolean isBlocked(ProxiedPlayer blocker, ProxiedPlayer blocked) {
        Set<String> blockedSet = blockedUsers.get(blocker.getName());
        return blockedSet != null && blockedSet.contains(blocked.getName());
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        if (!(sender instanceof ProxiedPlayer)) {
            sender.sendMessage(new TextComponent("§cApenas jogadores podem usar este comando."));
            return;
        }

        ProxiedPlayer player = (ProxiedPlayer) sender;
        if (player.getServer().getInfo().getName().equalsIgnoreCase("login")) {
            player.sendMessage(new TextComponent("§cVocê precisa se autenticar para fazer isto."));
            return;
        }

        Set<String> blocked = blockedUsers.computeIfAbsent(player.getName(), k -> new HashSet<>());

        if (args.length == 0) {
            player.sendMessage(new TextComponent(""));
            player.sendMessage(new TextComponent("§6Uso §a/block§6:"));
            player.sendMessage(new TextComponent("§e/block <jogador> - §bBloquear alguém"));
            player.sendMessage(new TextComponent("§e/block remover <jogador> - §bDesbloquear algúem"));
            player.sendMessage(new TextComponent("§e/block lista - §bVer os jogadores bloqueados"));
            return;
        }

        if (args[0].equalsIgnoreCase("remover")) {
            if (args.length < 2) {
                player.sendMessage(new TextComponent("§cUso /block remover <jogador>"));
                return;
            }
            if (!blocked.contains(args[1])) {
                player.sendMessage(new TextComponent("§c" + args[1] + " não está bloqueado."));
                return;
            }
            blocked.remove(args[1]);
            player.sendMessage(new TextComponent("§a" + args[1] + " foi desbloqueado."));
            return;
        }

        if (args[0].equalsIgnoreCase("lista")) {
            if (blocked.isEmpty()) {
                player.sendMessage(new TextComponent("§cVocê não possui nenhum jogador bloqueado."));
                return;
            }
            player.sendMessage(new TextComponent("§eJogadores bloqueados: §b" + String.join("§e, §b", blocked)));
            return;
        }

        if (player.getName().equalsIgnoreCase(args[0])) {
            player.sendMessage(new TextComponent("§cVocê não pode bloquear a si mesmo."));
            return;
        }

        ProxiedPlayer target = Bungee.get().getProxy().getPlayer(args[0]);
        if (target == null) {
            player.sendMessage(new TextComponent("§cAlvo não encontrado."));
            return;
        }

        if (blocked.contains(target.getName())) {
            player.sendMessage(new TextComponent("§c" + target.getName() + " já está bloqueado."));
            return;
        }

        blocked.add(target.getName());
        player.sendMessage(new TextComponent("§a" + target.getName() + " foi bloqueado."));
    }
}
