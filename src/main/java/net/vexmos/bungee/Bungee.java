package net.vexmos.bungee;

import net.vexmos.bungee.api.PermissionAPI;
import net.vexmos.bungee.commands.Commands;
import net.vexmos.bungee.configs.Configs;
import net.vexmos.bungee.listeners.MotdListener;
import net.vexmos.bungee.tasks.AnuncioImplements;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;
import net.vexmos.database.bungee.ConnectBungee;

public class Bungee extends Plugin {


    @Override
    public void onEnable() {
        Commands.setup();
        new ConnectBungee();
        new MotdListener();
        AnuncioImplements.setup();
        getProxy().getPluginManager().registerListener(this, new MotdListener());
        new Configs();
    }

    @Override
    public void onDisable() {
        // Cleanup if needed
    }

    public static Bungee get() {
        return (Bungee)ProxyServer.getInstance().getPluginManager().getPlugin("VexmosNET");
    }
}


    /*// auth
    public void authenticateSessionServer() {
        // Carregar configuração
        BungeeConfig sessionConfig = new BungeeConfig("session.yml");
        String pass = sessionConfig.getConfig().getString("pass");
        String token = sessionConfig.getConfig().getString("token");

        // Verificar as credenciais
        if (verifyCredentials(pass, token)) {
            String sessionId = UUID.randomUUID().toString();
            ProxyServer.getInstance().getLogger().info("");
            ProxyServer.getInstance().getLogger().info("");
            ProxyServer.getInstance().getLogger().info("");
            ProxyServer.getInstance().getLogger().info("");
            ProxyServer.getInstance().getLogger().info("\n\n" +
            	    "\n __       __    __  .__   __.  __   __  ___ \n" +
            	    "|  |     |  |  |  | |  \\ |  | |  | |  |/  / \n" +
            	    "|  |     |  |  |  | |   \\|  | |  | |  '  /  \n" +
            	    "|  |     |  |  |  | |  . `  | |  | |    <   \n" +
            	    "|  `----.|  `--'  | |  |\\   | |  | |  .  \\  \n" +
            	    "|_______| \\______/  |__| \\__| |__| |__|\\__\\ \n" +
            	    "                                            "
            	);


            ProxyServer.getInstance().getLogger().info("§aSessão autorizada com sucesso, LunikMC © 2024");
            ProxyServer.getInstance().getLogger().info("§eID: " + sessionId);
            ProxyServer.getInstance().getLogger().info("");
            ProxyServer.getInstance().getLogger().info("");
            ProxyServer.getInstance().getLogger().info("");
            ProxyServer.getInstance().getLogger().info("");
        } else {
        	ProxyServer.getInstance().getLogger().severe("");
            ProxyServer.getInstance().getLogger().severe("");
            ProxyServer.getInstance().getLogger().severe("");
            ProxyServer.getInstance().getLogger().severe("");
            ProxyServer.getInstance().getLogger().info("\n\n" +
            	    "\n __       __    __  .__   __.  __   __  ___ \n" +
            	    "|  |     |  |  |  | |  \\ |  | |  | |  |/  / \n" +
            	    "|  |     |  |  |  | |   \\|  | |  | |  '  /  \n" +
            	    "|  |     |  |  |  | |  . `  | |  | |    <   \n" +
            	    "|  `----.|  `--'  | |  |\\   | |  | |  .  \\  \n" +
            	    "|_______| \\______/  |__| \\__| |__| |__|\\__\\ \n" +
            	    "                                            "
            	);


            ProxyServer.getInstance().getLogger().severe("");
            ProxyServer.getInstance().getLogger().severe("§cA sua sessão é inválida, e a validação não foi autorizada com exito.");
            ProxyServer.getInstance().getLogger().severe("§cLunikSystem, propriedade de LunikMC e não afiliado à Mojang AB. Todos os direitos reservados © 2024.");
            ProxyServer.getInstance().getLogger().severe("A utilização não autorizada deste artefacto intelectual constitui uma violação da Lei de Direitos Autorais Brasileira (Lei nº 9.610/1998).");
            ProxyServer.getInstance().getLogger().severe("Segundo o Art. 184, é punível com pena de detenção de 6 meses a 2 anos, além de multa, a violação de direitos autorais que envolve reprodução, distribuição ou comercialização não autorizada de obras protegidas.");
            ProxyServer.getInstance().getLogger().severe("Em caso de infração, a LunikMC poderá buscar reparação legal e compensação financeira pelos danos causados..");
            ProxyServer.getInstance().getLogger().severe("");
            ProxyServer.getInstance().stop(); // Desliga o plugin
        }
    }

        private boolean verifyCredentials(String pass, String token) {
        try {
            URL url = new URL("https://pastebin.com/raw/yDm741q9");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder content = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();

            String pastebinContent = content.toString();
            return pastebinContent.contains("pass:" + pass) && pastebinContent.contains("token:" + token);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }*/



