package net.vexmos.bungee.api;

import net.md_5.bungee.config.Configuration;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;

public class TwitterAPI {

    private Twitter twitter;
    private String lastTweetId;
    private final BungeeConfig config;

    public TwitterAPI() {
        config = new BungeeConfig("twitterAPI.yml");
        loadConfig();
    }

    private void loadConfig() {
        Configuration cfg = config.getConfig();

        String consumerKey = cfg.getString("twitter.consumerKey");
        String consumerSecret = cfg.getString("twitter.consumerSecret");
        String accessToken = cfg.getString("twitter.accessToken");
        String accessTokenSecret = cfg.getString("twitter.accessTokenSecret");

        twitter = new TwitterFactory().getInstance();
        twitter.setOAuthConsumer(consumerKey, consumerSecret);
        twitter.setOAuthAccessToken(new AccessToken(accessToken, accessTokenSecret));
    }

    /**
     * Posta uma mensagem no twitter
     *
     * @param message conteudo da mensagem para postar
     */
    public void postTweet(String message) {
        try {
            lastTweetId = String.valueOf(twitter.updateStatus(message).getId());
        } catch (TwitterException e) {
            e.printStackTrace();
        }
    }

    /**
     * Deleta a última mensagem no twitter
     */
    public void deleteLastTweet() {
        try {
            if (lastTweetId != null) {
                twitter.destroyStatus(Long.parseLong(lastTweetId));
                lastTweetId = null;
            }
        } catch (TwitterException e) {
            e.printStackTrace();
        }
    }

    /**
     * Reinicia a configuração da TwitterAPI
     */
    public void reloadConfig() {
        config.reloadConfig();
        loadConfig();
    }

    /**
     * @return Retorna a configuraçâo da TwitterAPI
     */
    public Configuration getConfig() {
        return config.getConfig();
    }
}
