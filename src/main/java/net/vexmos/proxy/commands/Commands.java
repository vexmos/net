package net.vexmos.proxy.commands;

import net.vexmos.proxy.VexmosPROXY;
import net.vexmos.database.bungee.ConnectBungee;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.sql.SQLException;

public abstract class Commands extends Command {

    public Commands(String name, String... aliases) {
        super(name, null, aliases);
        VexmosPROXY.get().getProxy().getPluginManager().registerCommand(VexmosPROXY.get(), this);
    }

    public static void setup(){
        //new BanCommand();
        new KickCommand();
        new ReportCommand();
        //new TempBanCommand();
        //new UnbanCommand();
        new BlockCommand();
        new ReplyCommand();
        new TellCommand();
        new MaintenanceCommand();
        new AnuncioCommand();
        new ClearChatCommand();
        new GoCommand();
        new StatsCommand();
        new PingCommand();

        new CoinSystem();
        new CristalSystem();

        ConnectBungee database = new ConnectBungee();
        new GroupCommand(database);
        //new PunishCommand(database);
    }

    public abstract void perform(CommandSender sender, String[] args) throws SQLException;

    public void execute(CommandSender sender, String[] args) {
        if (sender instanceof ProxiedPlayer) {
            ProxiedPlayer p = (ProxiedPlayer) sender;
            String serverNome = p.getServer().getInfo().getName();
            if (serverNome.equalsIgnoreCase("login")) {
                p.sendMessage(new TextComponent("§cVoce deve se autenticar para fazer isto."));
            } else {
                try {
                    this.perform(sender, args);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }        
        } else {
            try {
                this.perform(sender, args);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }


}
