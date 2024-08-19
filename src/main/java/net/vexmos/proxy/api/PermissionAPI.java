package net.vexmos.proxy.api;

import net.vexmos.database.bungee.ConnectBungee;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Listener;
import java.util.HashSet;
import java.util.Set;

public class PermissionAPI implements Listener {

    /**
     * Variável de instância
     */
    private static PermissionAPI instance;
    /**
     * Variável de conexão com Database do Proxy
     */
    private final ConnectBungee connectBungee;
    /**
     * Variável para retornar configuração de Permissões
     * do Proxy.
     */
    private final BungeeConfig config;

    /**
     * Inicialização do PermissionAPI
     */
    public PermissionAPI() {
        config = new BungeeConfig("permissions.yml");
        connectBungee = new ConnectBungee();
    }

    /**
     * @return Cria instância do PermissionAPI
     */
    public static PermissionAPI getInstance() {
        if (instance == null) {
            instance = new PermissionAPI();
        }
        return instance;
    }

    /**
     * Obtenha as permissões de um jogador com base em seu grupo.
     *
     * @param player o jogador
     * @return um conjunto de permissões
     */
    public Set<String> getPermissions(ProxiedPlayer player) {
        Set<String> permissions = new HashSet<>();
        String group = getGroup(player);

        if (group != null && config.getConfig().getSection("permissions").contains(group)) {
            permissions.addAll(config.getConfig().getStringList("permissions." + group));
        }

        return permissions;
    }

    /**
     * Verifique se um jogador tem uma permissão específica.
     *
     * @param player Parametro para pegar o Jogador
     * @param permission a permissão para verificar
     * @return verdadeiro se o jogador tiver permissão, falso caso contrário
     */
    public boolean hasPermission(ProxiedPlayer player, String permission) {
        return getPermissions(player).contains(permission);
    }

    /**
     * Pega o grupo do player na Databse do Proxy
     *
     * @param player Parametro para pegar o Jogador
     */
    public String getGroup(ProxiedPlayer player) {
        return connectBungee.getPlayerGroup(player.getName());
    }

    /**
     * Reinicia a Configuração da PermissionAPI
     */
    public void reloadConfig() {
        config.reloadConfig();
    }

    /**
     * Seta o grupo do player na Databse do Proxy
     *
     * @param player Parametro para pegar o Jogador
     * @param group Parametro para pegar o grupo que será inserido na
     *              Database do Proxy para o Jogador
     */
    public void setGroup(ProxiedPlayer player, String group) {
        connectBungee.setPlayerGroup(player.getName(), group);
    }

}