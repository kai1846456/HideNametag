package me.yourname.hideownnametag;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.WrappedScoreboardTeam;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.event.EventHandler;

import java.util.Collections;

public class HideOwnNametagPlugin extends JavaPlugin implements Listener {

    private ProtocolManager manager;

    @Override
    public void onEnable() {
        this.manager = ProtocolLibrary.getProtocolManager();
        getServer().getPluginManager().registerEvents(this, this);

        for (Player player : Bukkit.getOnlinePlayers()) {
            hideOwnNametag(player);
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        hideOwnNametag(event.getPlayer());
    }

    private void hideOwnNametag(Player player) {
        try {
            PacketContainer packet = manager.createPacket(PacketType.Play.Server.SCOREBOARD_TEAM);

            String teamName = "hide_self_" + player.getName().substring(0, Math.min(14, player.getName().length()));

            packet.getStrings().write(0, teamName);
            packet.getIntegers().write(0, 0);
            packet.getStrings().write(1, teamName);
            packet.getStrings().write(2, "");
            packet.getStrings().write(3, "");
            packet.getEnumModifier(WrappedScoreboardTeam.NameTagVisibility.class, 0)
                  .write(0, WrappedScoreboardTeam.NameTagVisibility.NEVER);
            packet.getSpecificModifier(Collection.class)
                  .write(0, Collections.singleton(player.getName()));

            manager.sendServerPacket(player, packet);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
