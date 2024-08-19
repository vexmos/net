package net.vexmos.bungee.configs;

import net.vexmos.bungee.api.BungeeConfig;
import net.vexmos.bungee.api.TwitterAPI;

public class TwitterConfig {

    public static BungeeConfig twitter = new BungeeConfig("twitterAPI.yml");

    public static void setup(){
        twitter.saveConfig();
        new TwitterAPI();
    }

}
