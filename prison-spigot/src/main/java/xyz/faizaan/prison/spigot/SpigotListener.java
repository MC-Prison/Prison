/*
 * Prison is a Minecraft plugin for the prison game mode.
 * Copyright (C) 2018 The Prison Team
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package xyz.faizaan.prison.spigot;

import org.bukkit.Bukkit;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.*;
import xyz.faizaan.prison.Prison;
import xyz.faizaan.prison.internal.events.Cancelable;
import xyz.faizaan.prison.internal.events.player.PlayerChatEvent;
import xyz.faizaan.prison.internal.events.player.PlayerPickUpItemEvent;
import xyz.faizaan.prison.spigot.compat.Compatibility;
import xyz.faizaan.prison.spigot.game.SpigotPlayer;
import xyz.faizaan.prison.spigot.game.SpigotWorld;
import xyz.faizaan.prison.util.BlockType;
import xyz.faizaan.prison.util.ChatColor;
import xyz.faizaan.prison.util.Location;

/**
 * Posts Prison's internal events.
 *
 * @author Faizaan A. Datoo
 */
public class SpigotListener implements Listener {

    private SpigotPrison spigotPrison;

    public SpigotListener(SpigotPrison spigotPrison) {
        this.spigotPrison = spigotPrison;
    }

    public void init() {
        Bukkit.getServer().getPluginManager().registerEvents(this, this.spigotPrison);
    }

    @EventHandler public void onPlayerJoin(PlayerJoinEvent e) {
        Prison.get().getEventBus().post(
            new xyz.faizaan.prison.internal.events.player.PlayerJoinEvent(
                new SpigotPlayer(e.getPlayer())));
    }

    @EventHandler public void onPlayerQuit(PlayerQuitEvent e) {
        Prison.get().getEventBus().post(
            new xyz.faizaan.prison.internal.events.player.PlayerQuitEvent(
                new SpigotPlayer(e.getPlayer())));
    }

    @EventHandler public void onPlayerKicked(PlayerKickEvent e) {
        Prison.get().getEventBus().post(
            new xyz.faizaan.prison.internal.events.player.PlayerKickEvent(
                new SpigotPlayer(e.getPlayer()), e.getReason()));
    }

    @EventHandler public void onBlockPlace(BlockPlaceEvent e) {
        org.bukkit.Location block = e.getBlockPlaced().getLocation();
        xyz.faizaan.prison.internal.events.block.BlockPlaceEvent event =
            new xyz.faizaan.prison.internal.events.block.BlockPlaceEvent(
                BlockType.getBlock(e.getBlock().getTypeId()),
                new Location(new SpigotWorld(block.getWorld()), block.getX(), block.getY(),
                    block.getZ()), (new SpigotPlayer(e.getPlayer())));
        Prison.get().getEventBus().post(event);
        doCancelIfShould(event, e);
    }

    @EventHandler public void onBlockBreak(BlockBreakEvent e) {
        org.bukkit.Location block = e.getBlock().getLocation();
        xyz.faizaan.prison.internal.events.block.BlockBreakEvent event =
            new xyz.faizaan.prison.internal.events.block.BlockBreakEvent(
                BlockType.getBlock(e.getBlock().getTypeId()),
                new Location(new SpigotWorld(block.getWorld()), block.getX(), block.getY(),
                    block.getZ()), (new SpigotPlayer(e.getPlayer())),e.getExpToDrop());
        Prison.get().getEventBus().post(event);
        doCancelIfShould(event, e);
    }

    @EventHandler public void onPlayerInteract(PlayerInteractEvent e) {
        // TODO Accept air events (block is null when air is clicked...)

        // Check to see if we support the Action
        xyz.faizaan.prison.internal.events.player.PlayerInteractEvent.Action[] values = xyz.faizaan.prison.internal.events.player.PlayerInteractEvent.Action.values();
        boolean has = false;
        for (xyz.faizaan.prison.internal.events.player.PlayerInteractEvent.Action value : values) {
            if(value.name().equals(e.getAction().name())) has = true;
        }
        if(!has) return; // we don't support this Action

        // This one's a workaround for the double-interact event glitch.
        // The wand can only be used in the main hand
        if (spigotPrison.compatibility.getHand(e) != Compatibility.EquipmentSlot.HAND) {
            return;
        }

        org.bukkit.Location block = e.getClickedBlock().getLocation();
        xyz.faizaan.prison.internal.events.player.PlayerInteractEvent event =
            new xyz.faizaan.prison.internal.events.player.PlayerInteractEvent(
                new SpigotPlayer(e.getPlayer()),
                SpigotUtil.bukkitItemStackToPrison(spigotPrison.compatibility.getItemInMainHand(e)),
                xyz.faizaan.prison.internal.events.player.PlayerInteractEvent.Action
                    .valueOf(e.getAction().name()),
                new Location(new SpigotWorld(block.getWorld()), block.getX(), block.getY(),
                    block.getZ()));
        Prison.get().getEventBus().post(event);
        doCancelIfShould(event, e);
    }

    @EventHandler public void onPlayerDropItem(PlayerDropItemEvent e) {
        xyz.faizaan.prison.internal.events.player.PlayerDropItemEvent event =
            new xyz.faizaan.prison.internal.events.player.PlayerDropItemEvent(
                new SpigotPlayer(e.getPlayer()),
                SpigotUtil.bukkitItemStackToPrison(e.getItemDrop().getItemStack()));
        Prison.get().getEventBus().post(event);
        doCancelIfShould(event, e);
    }

    @EventHandler public void onPlayerPickUpItem(PlayerPickupItemEvent e) {
        PlayerPickUpItemEvent event = new PlayerPickUpItemEvent(new SpigotPlayer(e.getPlayer()),
            SpigotUtil.bukkitItemStackToPrison(e.getItem().getItemStack()));
        Prison.get().getEventBus().post(event);
        doCancelIfShould(event, e);
    }

    @EventHandler public void onPlayerChat(AsyncPlayerChatEvent e) {
        PlayerChatEvent event =
            new PlayerChatEvent(new SpigotPlayer(e.getPlayer()), e.getMessage(), e.getFormat());
        Prison.get().getEventBus().post(event);
        e.setFormat(ChatColor.translateAlternateColorCodes('&', event.getFormat() + "&r"));
        e.setMessage(event.getMessage());
        doCancelIfShould(event, e);
    }

    private void doCancelIfShould(Cancelable ours, Cancellable theirs) {
        if(ours.isCanceled()) {
            // We shouldn't set this to false, because some event handlers check for that.
            theirs.setCancelled(true);
        }
    }

}
