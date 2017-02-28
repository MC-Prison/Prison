/*
 *  Prison is a Minecraft plugin for the prison game mode.
 *  Copyright (C) 2017 The Prison Team
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package tech.mcprison.prison.spigot;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.material.MaterialData;
import org.bukkit.material.Openable;
import tech.mcprison.prison.Prison;
import tech.mcprison.prison.commands.PluginCommand;
import tech.mcprison.prison.economy.Economy;
import tech.mcprison.prison.gui.GUI;
import tech.mcprison.prison.internal.*;
import tech.mcprison.prison.internal.platform.Capability;
import tech.mcprison.prison.internal.platform.Platform;
import tech.mcprison.prison.internal.scoreboard.ScoreboardManager;
import tech.mcprison.prison.spigot.economies.EssentialsEconomy;
import tech.mcprison.prison.spigot.economies.VaultEconomy;
import tech.mcprison.prison.spigot.game.SpigotCommandSender;
import tech.mcprison.prison.spigot.game.SpigotPlayer;
import tech.mcprison.prison.spigot.game.SpigotSign;
import tech.mcprison.prison.spigot.game.SpigotWorld;
import tech.mcprison.prison.spigot.gui.SpigotGUI;
import tech.mcprison.prison.spigot.permissions.VaultPermission;
import tech.mcprison.prison.spigot.scoreboard.SpigotScoreboardManager;
import tech.mcprison.prison.util.Location;
import tech.mcprison.prison.util.Text;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Faizaan A. Datoo
 */
class SpigotPlatform implements Platform {

    private SpigotPrison plugin;
    private List<PluginCommand> commands = new ArrayList<>();
    private Map<String, World> worlds = new HashMap<>();
    private List<Player> players = new ArrayList<>();

    private ScoreboardManager scoreboardManager;

    SpigotPlatform(SpigotPrison plugin) {
        this.plugin = plugin;
        this.scoreboardManager = new SpigotScoreboardManager();
    }

    @Override public Optional<World> getWorld(String name) {
        if (worlds.containsKey(name)) {
            return Optional.of(worlds.get(name));
        }

        if (Bukkit.getWorld(name) == null) {
            return Optional.empty(); // Avoid NPE
        }
        SpigotWorld newWorld = new SpigotWorld(Bukkit.getWorld(name));
        worlds.put(newWorld.getName(), newWorld);
        return Optional.of(newWorld);
    }

    @Override public Optional<Player> getPlayer(String name) {
        return Optional.ofNullable(
            players.stream().filter(player -> player.getName().equals(name)).findFirst()
                .orElseGet(() -> {
                    if (Bukkit.getPlayer(name) == null) {
                        return null;
                    }
                    SpigotPlayer player = new SpigotPlayer(Bukkit.getPlayer(name));
                    players.add(player);
                    return player;
                }));
    }

    @Override public Optional<Player> getPlayer(UUID uuid) {
        return Optional.ofNullable(
            players.stream().filter(player -> player.getUUID().equals(uuid)).findFirst()
                .orElseGet(() -> {
                    if (Bukkit.getPlayer(uuid) == null) {
                        return null;
                    }
                    SpigotPlayer player = new SpigotPlayer(Bukkit.getPlayer(uuid));
                    players.add(player);
                    return player;
                }));
    }

    @Override public List<Player> getOnlinePlayers() {
        return Bukkit.getOnlinePlayers().stream()
            .map(player -> getPlayer(player.getUniqueId()).get()).collect(Collectors.toList());
    }

    @Override public Sign getSign(Location location) {
        org.bukkit.block.Sign sign = (org.bukkit.block.Sign) new org.bukkit.Location(
            Bukkit.getWorld(location.getWorld().getName()), location.getX(), location.getY(),
            location.getZ()).getBlock().getState();
        return new SpigotSign(sign);
    }

    @Override public Economy getEconomy() {
        if (Bukkit.getServer().getPluginManager().isPluginEnabled("Essentials")) {
            return new EssentialsEconomy();
        } else if (Bukkit.getServer().getPluginManager().isPluginEnabled("Vault")) {
            return new VaultEconomy();
        } else {
            return null;
        }
    }

    @Override public Permission getPermission() {
        if (Bukkit.getServer().getPluginManager().isPluginEnabled("Vault")) {
            return new VaultPermission();
        } else {
            return null;
        }
    }

    @Override public String getPluginVersion() {
        return plugin.getDescription().getVersion();
    }

    @Override public File getPluginDirectory() {
        return plugin.getDataFolder();
    }

    @Override public void registerCommand(PluginCommand command) {
        try {
            ((SimpleCommandMap) plugin.commandMap.get(Bukkit.getServer()))
                .register(command.getLabel(), "prison",
                    new Command(command.getLabel(), command.getDescription(), command.getUsage(),
                        Collections.emptyList()) {

                        @Override public boolean execute(CommandSender sender, String commandLabel,
                            String[] args) {
                            if (sender instanceof org.bukkit.entity.Player) {
                                return Prison.get().getCommandHandler()
                                    .onCommand(new SpigotPlayer((org.bukkit.entity.Player) sender),
                                        command, commandLabel, args);
                            }
                            return Prison.get().getCommandHandler()
                                .onCommand(new SpigotCommandSender(sender), command, commandLabel,
                                    args);
                        }

                    });
            commands.add(command);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked") @Override public void unregisterCommand(String command) {
        try {
            ((Map<String, Command>) plugin.knownCommands
                .get(plugin.commandMap.get(Bukkit.getServer()))).remove(command);
            this.commands.removeIf(pluginCommand -> pluginCommand.getLabel().equals(command));
        } catch (IllegalAccessException e) {
            e.printStackTrace(); // This should only happen if something's wrong up there.
        }
    }

    @Override public List<PluginCommand> getCommands() {
        return commands;
    }

    @Override public Scheduler getScheduler() {
        return plugin.scheduler;
    }

    @Override public GUI createGUI(String title, int numRows) {
        return new SpigotGUI(title, numRows);
    }

    public void toggleDoor(Location doorLocation) {
        org.bukkit.Location bLoc =
            new org.bukkit.Location(Bukkit.getWorld(doorLocation.getWorld().getName()),
                doorLocation.getX(), doorLocation.getY(), doorLocation.getZ());
        Block block = bLoc.getWorld().getBlockAt(bLoc).getRelative(BlockFace.DOWN);
        if (!isDoor(block.getType())) {
            return;
        }

        BlockState state = block.getState();
        Openable openable = (Openable) state.getData();
        openable.setOpen(!openable.isOpen());
        state.setData((MaterialData) openable);
        state.update();
        plugin.compatibility.playIronDoorSound(block.getLocation());
    }

    @Override public void log(String message, Object... format) {
        message = Text.translateAmpColorCodes(String.format(message, format));

        ConsoleCommandSender sender = Bukkit.getConsoleSender();
        if (sender == null) {
            Bukkit.getLogger().info(ChatColor.stripColor(message));
        } else {
            sender.sendMessage(message);
        }
    }

    @Override public void showTitle(Player player, String title, String subtitle, int fade) {
        org.bukkit.entity.Player play = Bukkit.getPlayer(player.getName());
        play.sendTitle(title, subtitle);
    }

    @Override public void showActionBar(Player player, String text) {
        org.bukkit.entity.Player play = Bukkit.getPlayer(player.getName());
    }

    @Override public ScoreboardManager getScoreboardManager() {
        return scoreboardManager;
    }

    private boolean isDoor(Material block) {
        return block == Material.ACACIA_DOOR || block == Material.BIRCH_DOOR
            || block == Material.DARK_OAK_DOOR || block == Material.IRON_DOOR_BLOCK
            || block == Material.JUNGLE_DOOR || block == Material.WOODEN_DOOR
            || block == Material.SPRUCE_DOOR;
    }

    @Override public Map<Capability, Boolean> getCapabilities() {
        Map<Capability, Boolean> capabilities = new HashMap<>();
        capabilities.put(Capability.GUI, true);
        capabilities.put(Capability.ECONOMY, getEconomy() != null);
        capabilities.put(Capability.PERMISSIONS, getPermission() != null);
        return capabilities;
    }

}
