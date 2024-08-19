package net.vexmos.spigot.api;

import net.vexmos.spigot.VexmosNET;
import net.minecraft.server.v1_8_R3.EntityWither;
import net.minecraft.server.v1_8_R3.PacketPlayOutSpawnEntityLiving;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_8_R3.World;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class BossBarAPI {

	private final Map<UUID, Integer> activeBossBars = new HashMap<>();

	public void sendBossbar(Player player, String message, float health) {
		if (activeBossBars.containsKey(player.getUniqueId())) {
			removeBossbar(player);
		}

		World world = ((CraftWorld) player.getWorld()).getHandle();
		EntityWither wither = new EntityWither(world);
		wither.setCustomName(message);
		wither.setCustomNameVisible(true);
		wither.setInvisible(true);
		wither.setHealth(health);

		PacketPlayOutSpawnEntityLiving packet = new PacketPlayOutSpawnEntityLiving(wither);
		((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);

		int entityId = wither.getId();
		activeBossBars.put(player.getUniqueId(), entityId);

		Bukkit.getScheduler().runTaskLater(VexmosNET.get(), () -> {
			if (activeBossBars.containsKey(player.getUniqueId())) {
				removeBossbar(player);
			}
		}, 100L); // 5 segundos
	}

	public void removeBossbar(Player player) {
		Integer entityId = activeBossBars.remove(player.getUniqueId());
		if (entityId != null) {
			PacketPlayOutEntityDestroy packet = new PacketPlayOutEntityDestroy(entityId);
			((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
		}
	}
}
