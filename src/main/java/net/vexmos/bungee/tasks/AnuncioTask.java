package net.vexmos.bungee.tasks;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class AnuncioTask implements Runnable {

    private final List<String> anuncios;
    private final String prefixo;
    private final AtomicInteger indice;

    public AnuncioTask(List<String> anuncios, String prefixo) {
        this.anuncios = anuncios;
        this.prefixo = prefixo;
        this.indice = new AtomicInteger(0);
    }

    @Override
    public void run() {
        if (anuncios.isEmpty()) {
            return;
        }

        int index = indice.getAndIncrement();
        if (index >= anuncios.size()) {
            indice.set(0);
            index = 0;
        }

        String anuncio = anuncios.get(index);
        ProxyServer.getInstance().broadcast(new TextComponent(TextComponent.fromLegacyText(converterParaCor(prefixo) + converterParaCor(anuncio))));
    }

    // Método para converter & para §
    private String converterParaCor(String mensagem) {
        return mensagem.replaceAll("&", "§");
    }
}
