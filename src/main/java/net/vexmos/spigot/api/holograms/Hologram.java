package net.vexmos.spigot.api.holograms;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/*******************************************************
 * Copyright (C) 2015-2019 Piinguiin <neuraxhd@gmail.com>
 *
 * This file is part of Holograms and was created at the 26.10.2019
 *
 * Holograms can not be copied and/or distributed without the express
 * permission of the owner.
 *
 *******************************************************/
public class Hologram implements IHologram {

    private final String[] text;
    private final List<SingleLineHologram> singleLineHolograms;
    private final Location location;

    public Hologram(final String[] text, final Location location) {
        this.text = text;
        this.location = location;
        this.singleLineHolograms = new ArrayList<>();
        create();
    }

    private void create(){

        for(int i = 0; i < this.text.length; i++){

            final String line = this.text[i];
            final double v = i * HologramApi.LINE_DISTANCE;
            final Location loc = this.location.clone().add(0,v,0);

            final SingleLineHologram singleLineHologram = new SingleLineHologram(line,loc);
            this.singleLineHolograms.add(singleLineHologram);
        }


    }

    @Override
    public void show() {
        this.singleLineHolograms.forEach(SingleLineHologram::show);
    }

    @Override
    public void show(final Player player) {
        this.singleLineHolograms.forEach( h -> h.show(player));
    }

    @Override
    public void show(final List<Player> players) {
        this.singleLineHolograms.forEach( h -> h.show(players));
    }

    @Override
    public void hide() {
        this.singleLineHolograms.forEach(SingleLineHologram::hide);
    }

    @Override
    public void hide(final Player player) {
        this.singleLineHolograms.forEach( h -> h.hide(player));
    }

    @Override
    public void hide(final List<Player> players) {
        this.singleLineHolograms.forEach( h -> h.show(players));
    }
}