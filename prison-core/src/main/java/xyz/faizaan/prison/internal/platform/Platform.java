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

package xyz.faizaan.prison.internal.platform;

import xyz.faizaan.prison.commands.PluginCommand;
import xyz.faizaan.prison.gui.GUI;
import xyz.faizaan.prison.internal.Player;
import xyz.faizaan.prison.internal.Scheduler;
import xyz.faizaan.prison.internal.World;
import xyz.faizaan.prison.internal.scoreboard.ScoreboardManager;
import xyz.faizaan.prison.store.Storage;
import xyz.faizaan.prison.util.Location;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * Represents an internal platform that Prison has been implemented for.
 * The internal platform is responsible for connecting Prison's APIs to the underlying server API.
 *
 * @author Faizaan A. Datoo
 * @author Camouflage100
 * @since API 1.0
 */
public interface Platform {

    /**
     * Returns the world with the specified name.
     */
    Optional<World> getWorld(String name);

    /**
     * Returns the player with the specified name.
     */
    Optional<Player> getPlayer(String name);

    /**
     * Returns the player with the specified UUID.
     */
    Optional<Player> getPlayer(UUID uuid);

    /**
     * Returns a list of all online players.
     */
    List<Player> getOnlinePlayers();

    /**
     * Returns the plugin's version.
     */
    String getPluginVersion();

    /**
     * Returns the {@link File} representing the plugin's designated storage folder.
     * This directory must have already been created by the implementation.
     */
    File getPluginDirectory();

    /**
     * Registers a command with the server implementation.
     *
     * @param command The {@link PluginCommand} to register.
     */
    void registerCommand(PluginCommand command);

    /**
     * Unregisters a registered command.
     * This does not support command aliases, because those are currently not definable anyway.
     *
     * @param command The command to unregister, without the preceding '/'.
     */
    void unregisterCommand(String command);

    /**
     * Returns a list of all registered commands.
     */
    List<PluginCommand> getCommands();

    /**
     * Runs a command as the console (i.e. with all privileges).
     *
     * @param cmd The command to run, without the '/'.
     */
    void dispatchCommand(String cmd);

    /**
     * Returns the {@link Scheduler}, which can be used to schedule tasks.
     */
    Scheduler getScheduler();

    /**
     * Creates a new {@link GUI} to show to players.
     *
     * @param title   The title of the GUI.
     * @param numRows The number of rows in the GUI; must be divisible by 9.
     * @return The {@link GUI}, ready for use.
     */
    GUI createGUI(String title, int numRows);

    /**
     * If an iron door is open, this method closes it.
     * If an iron door is closed, this method opens it.
     *
     * @param doorLocation The {@link Location} of the door.
     */
    void toggleDoor(Location doorLocation);

    /**
     * Log a colored message to the console (if supported).
     *
     * @param message The message. May include color codes, amp-prefixed.
     * @param format  The objects inserted via {@link String#format(String, Object...)}.
     */
    void log(String message, Object... format);

    /**
     * Logs a debug message to the console if the user has debug messages enabled.
     *
     * @param message The message. May include color codes, amp-prefixed.
     * @param format  The The objects inserted via {@link String#format(String, Object...)}.
     */
    void debug(String message, Object... format);

    /**
     * Runs the converter for this platform.
     *
     * @return The output of the converter. It will be sent to whoever ran the converter system (e.g. usually a command sender).
     */
    default String runConverter() {
        return "This operation is unsupported on this platform.";
    }

    /**
     * Returns a map of capabilities and whether or not this internal has them.
     */
    Map<Capability, Boolean> getCapabilities();

    /**
     * Send a title to a player
     *
     * @param player   The player that you want to send the title to
     * @param title    The text of the title
     * @param subtitle The text of the subtitle
     * @param fade     The length of the fade
     */
    void showTitle(Player player, String title, String subtitle, int fade);

    /**
     * Send an actionbar to a player
     *
     * @param player   The player that you want to send the actionbar to
     * @param text     The text of the actionbar
     * @param duration The amount of time to show the action bar, in seconds. Set to -1 for no duration (i.e. vanilla standard duration of ~3 seconds).
     */
    void showActionBar(Player player, String text, int duration);

    /**
     * Returns the scoreboard manager.
     */
    ScoreboardManager getScoreboardManager();

    /**
     * Returns the storage manager.
     */
    Storage getStorage();

    /**
     * Retrieves the {@link PluginCommand} object for a command with a certain label.
     *
     * @param label The command's label.
     * @return The {@link PluginCommand}, or null if no command exists by that label.
     */
    default Optional<PluginCommand> getCommand(String label) {
        for (PluginCommand command : getCommands()) {
            if (command.getLabel().equalsIgnoreCase(label)) {
                return Optional.of(command);
            }
        }
        return Optional.empty();
    }


    /**
     * Returns true if the server should show alerts to in-game players, false otherwise.
     * This is a configuration option.kkjksdf;erljnkx.jcsmka.f.fdlwe;s.x. frrer5
     */
    boolean shouldShowAlerts();
}
