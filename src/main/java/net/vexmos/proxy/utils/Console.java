package net.vexmos.proxy.utils;

public class Console {

    public static void sendColorMessage(String message){
        message = message.replace("§0", "\u001B[30m");
        message = message.replace("§1", "\u001B[34m");
        message = message.replace("§2", "\u001B[32m");
        message = message.replace("§3", "\u001B[36m");
        message = message.replace("§4", "\u001B[31m");
        message = message.replace("§5", "\u001B[35m");
        message = message.replace("§6", "\u001B[33m");
        message = message.replace("§7", "\u001B[37m");
        message = message.replace("§8", "\u001B[90m");
        message = message.replace("§9", "\u001B[94m");
        message = message.replace("§a", "\u001B[92m");
        message = message.replace("§b", "\u001B[96m");
        message = message.replace("§c", "\u001B[91m");
        message = message.replace("§d", "\u001B[95m");
        message = message.replace("§e", "\u001B[93m");
        message = message.replace("§f", "\u001B[97m");
        message = message.replace("§r", "\u001B[0m");

        System.out.println(message + "\u001B[0m");
    }

}
