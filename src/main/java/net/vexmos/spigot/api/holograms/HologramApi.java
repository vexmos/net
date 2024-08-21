package net.vexmos.spigot.api.holograms;

import net.vexmos.spigot.api.holograms.IHologram;
import org.bukkit.Location;

/*******************************************************
 * Copyright (C) 2015-2019 Piinguiin <neuraxhd@gmail.com>
 *
 * This file is part of Holograms and was created at the 26.10.2019
 *
 * Holograms can not be copied and/or distributed without the express
 * permission of the owner.
 *
 *******************************************************/
public class HologramApi {

    public static double LINE_DISTANCE = 0.25;

    public static IHologram createNewHologram(final String[] text, final Location location){

        final boolean single = text.length == 1;

        if(single){

            return new SingleLineHologram(text[0],location);
        }

        return new Hologram(text,location);
    }

}