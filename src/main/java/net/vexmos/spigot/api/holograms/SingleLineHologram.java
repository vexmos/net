package net.vexmos.spigot.api.holograms;

import net.minecraft.server.v1_8_R3.EntityArmorStand;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_8_R3.PacketPlayOutSpawnEntityLiving;
import net.vexmos.spigot.api.holograms.reflection.Reflection;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
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
public class SingleLineHologram extends Reflection implements IHologram {

    private final String text;
    private final Location location;
    private EntityArmorStand armorStand;

    public SingleLineHologram(final String text, final Location location) {
        this.text = text;
        this.location = location;
        create();
    }

    @Override
    public void show() {
        Bukkit.getOnlinePlayers().forEach(p -> show(p));
    }

    @Override
    public void show(final Player player) {
        final PacketPlayOutSpawnEntityLiving packet = new PacketPlayOutSpawnEntityLiving(this.armorStand);
        sendPacket(packet);
    }

    @Override
    public void hide() {
        Bukkit.getOnlinePlayers().forEach(p -> hide(p));
    }

    @Override
    public void hide(final Player player) {
        final PacketPlayOutEntityDestroy packet = new PacketPlayOutEntityDestroy(this.armorStand.getId());
        sendPacket(packet);
    }

    @Override
    public void show(final List<Player> players) {
        players.forEach(p -> show(p));
    }

    @Override
    public void hide(final List<Player> players) {
        players.forEach(p -> hide(p));
    }

    private void create(){

        final EntityArmorStand armorStand = new EntityArmorStand(((CraftWorld)this.location.getWorld()).getHandle()
                ,this.location.getX(), this.location.getY(),this.location.getZ());
        armorStand.setCustomName(this.text);
        armorStand.setCustomNameVisible(true);
        armorStand.setInvisible(true);
        armorStand.setGravity(false);
        this.armorStand = armorStand;
    }
}