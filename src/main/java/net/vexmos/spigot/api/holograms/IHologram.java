package net.vexmos.spigot.api.holograms;

import org.bukkit.entity.Player;

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
public interface IHologram {

    void show();

    void show(Player player);

    void show(List<Player> players);

    void hide();

    void hide(Player player);

    void hide(List<Player> players);

}