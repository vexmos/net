package net.vexmos.proxy.configs;

import net.vexmos.proxy.api.BungeeConfig;
import net.vexmos.proxy.api.TwitterAPI;

public class TwitterConfig {

    public static BungeeConfig twitter = new BungeeConfig("twitterAPI.yml");

    public static void setup(){
        new TwitterAPI();
        twitter.saveConfig();
    }

}
