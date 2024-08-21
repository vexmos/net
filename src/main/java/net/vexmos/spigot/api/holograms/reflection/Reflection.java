package net.vexmos.spigot.api.holograms.reflection;

/*******************************************************
 * Copyright (C) 2015-2019 Piinguiin <neuraxhd@gmail.com>
 *
 * This file is part of Holograms and was created at the 26.10.2019
 *
 * Holograms can not be copied and/or distributed without the express
 * permission of the owner.
 *
 *******************************************************/
import net.minecraft.server.v1_8_R3.Packet;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;

public class Reflection {

    public void setVaule(final Object obj, final String name, final Object value){
        try{

            final Field field = obj.getClass().getDeclaredField(name);
            field.setAccessible(true);
            field.set(obj,value);
        }catch  (final NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public Object getValue(final Object object, final String name){

        try{

            final Field field = object.getClass().getDeclaredField(name);
            field.setAccessible(true);
            return field.get(object);

        }catch (final IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void sendPacket(final Packet<?> packet, final Player player){
        ((CraftPlayer)player).getHandle().playerConnection.sendPacket(packet);
    }

    public void sendPacket(final Packet<?> packet){
        Bukkit.getOnlinePlayers().forEach(player -> sendPacket(packet,player));
    }

}