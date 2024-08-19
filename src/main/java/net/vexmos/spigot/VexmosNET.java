package net.vexmos.spigot;

//import com.lunikmc.spigot.api.ImageHandlerAPI;
import net.vexmos.database.SetupDatabases;
import net.vexmos.spigot.api.ImageHandlerAPI;
import net.vexmos.spigot.commands.Commands;
import net.vexmos.spigot.listeners.Listeners;
import net.vexmos.spigot.listeners.player.gamerules.GameRules;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;


public final class VexmosNET extends JavaPlugin {


    @Override
    public void onEnable() {
        //authenticateSessionServer();
        Listeners.setup();
        Commands.setup();
        GameRules.setup();
        new SetupDatabases();
        ImageHandlerAPI.setup();
        Bukkit.getWorld("world").setTime(1000);

    }

    @Override
    public void onDisable() {

    }

    public static VexmosNET get() {
        return VexmosNET.getPlugin(VexmosNET.class);
    }


    /*// auth system
    public void authenticateSessionServer() {
        // Carregar configuração
        SpigotConfig sessionConfig = new SpigotConfig("session.yml");

        String pass = sessionConfig.getConfig().getString("pass");
        String token = sessionConfig.getConfig().getString("token");

        // Verificar as credenciais
        if (verifyCredentials(pass, token)) {
            String sessionId = UUID.randomUUID().toString();
            Bukkit.getLogger().info("");
            Bukkit.getLogger().info("");
            Bukkit.getLogger().info("");
            Bukkit.getLogger().info("");
            Bukkit.getLogger().info("\n\n" +
                    "\n __       __    __  .__   __.  __   __  ___ \n" +
                    "|  |     |  |  |  | |  \\ |  | |  | |  |/  / \n" +
                    "|  |     |  |  |  | |   \\|  | |  | |  '  /  \n" +
                    "|  |     |  |  |  | |  . `  | |  | |    <   \n" +
                    "|  `----.|  `--'  | |  |\\   | |  | |  .  \\  \n" +
                    "|_______| \\______/  |__| \\__| |__| |__|\\__\\ \n" +
                    "                                            "
            );

            Bukkit.getLogger().info("§aSessão autorizada com sucesso, LunikMC © 2024");
            Bukkit.getLogger().info("§eID: " + sessionId);
            Bukkit.getLogger().info("");
            Bukkit.getLogger().info("");
            Bukkit.getLogger().info("");
            Bukkit.getLogger().info("");
        } else {
            Bukkit.getLogger().severe("");
            Bukkit.getLogger().severe("");
            Bukkit.getLogger().severe("");
            Bukkit.getLogger().severe("");
            Bukkit.getLogger().info("\n\n" +
                    "\n __       __    __  .__   __.  __   __  ___ \n" +
                    "|  |     |  |  |  | |  \\ |  | |  | |  |/  / \n" +
                    "|  |     |  |  |  | |   \\|  | |  | |  '  /  \n" +
                    "|  |     |  |  |  | |  . `  | |  | |    <   \n" +
                    "|  `----.|  `--'  | |  |\\   | |  | |  .  \\  \n" +
                    "|_______| \\______/  |__| \\__| |__| |__|\\__\\ \n" +
                    "                                            "
            );

            Bukkit.getLogger().severe("");
            Bukkit.getLogger().severe("§cA sua sessão é inválida, e a validação não foi autorizada com exito.");
            Bukkit.getLogger().severe("§cLunikSystem, propriedade de LunikMC e não afiliado à Mojang AB. Todos os direitos reservados © 2024.");
            Bukkit.getLogger().severe("A utilização não autorizada deste artefacto intelectual constitui uma violação da Lei de Direitos Autorais Brasileira (Lei nº 9.610/1998).");
            Bukkit.getLogger().severe("Segundo o Art. 184, é punível com pena de detenção de 6 meses a 2 anos, além de multa, a violação de direitos autorais que envolve reprodução, distribuição ou comercialização não autorizada de obras protegidas.");
            Bukkit.getLogger().severe("Em caso de infração, a LunikMC poderá buscar reparação legal e compensação financeira pelos danos causados.");
            Bukkit.getLogger().severe("");
            Bukkit.getLogger().severe("");
            Bukkit.getLogger().severe("");
            Bukkit.getLogger().severe("");
            Bukkit.shutdown(); // Desliga o servidor
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
            return false;
        }
    }*/


}





