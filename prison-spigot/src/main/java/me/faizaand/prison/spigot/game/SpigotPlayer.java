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

package me.faizaand.prison.spigot.game;

import me.faizaand.prison.internal.GameItemStack;
import me.faizaand.prison.internal.GamePlayer;
import me.faizaand.prison.internal.inventory.Inventory;
import me.faizaand.prison.internal.scoreboard.Scoreboard;
import me.faizaand.prison.output.Output;
import me.faizaand.prison.spigot.SpigotUtil;
import me.faizaand.prison.spigot.game.inventory.SpigotPlayerInventory;
import me.faizaand.prison.spigot.game.scoreboard.SpigotScoreboard;
import me.faizaand.prison.util.GameLocation;
import me.faizaand.prison.util.Gamemode;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Optional;
import java.util.UUID;

/**
 * @author Faizaan A. Datoo
 */
public class SpigotPlayer extends SpigotCommandSender implements GamePlayer {

    private org.bukkit.entity.Player bukkitPlayer;

    public SpigotPlayer(org.bukkit.entity.Player bukkitPlayer) {
        super(bukkitPlayer);
        this.bukkitPlayer = bukkitPlayer;
    }

    @Override public UUID getUUID() {
        return bukkitPlayer.getUniqueId();
    }

    @Override public String getDisplayName() {
        return bukkitPlayer.getDisplayName();
    }

    @Override public void setDisplayName(String newDisplayName) {
        bukkitPlayer.setDisplayName(newDisplayName);
    }

    @Override public void give(GameItemStack itemStack) {
        bukkitPlayer.getInventory().addItem(SpigotUtil.prisonItemStackToBukkit(itemStack));
    }

    @Override public GameLocation getLocation() {
        return SpigotUtil.bukkitLocationToPrison(bukkitPlayer.getLocation());
    }

    @Override public void teleport(GameLocation location) {
        bukkitPlayer.teleport(SpigotUtil.prisonLocationToBukkit(location),
            PlayerTeleportEvent.TeleportCause.PLUGIN);
    }

    @Override public boolean isOnline() {
        return bukkitPlayer.isOnline();
    }

    @Override public void setScoreboard(Scoreboard scoreboard) {
        bukkitPlayer.setScoreboard(((SpigotScoreboard) scoreboard).getWrapper());
    }

    @Override public Gamemode getGamemode() {
        return Gamemode.valueOf(getWrapper().getGameMode().toString());
    }

    @Override public void setGamemode(Gamemode gamemode) {
        getWrapper().setGameMode(GameMode.valueOf(gamemode.toString()));
    }

    @Override public Optional<String> getLocale() {
        if (NmsHelper.hasSupport()) {
            try {
                return Optional.ofNullable(NmsHelper.getLocale(getWrapper()));
            } catch (IllegalAccessException | InvocationTargetException ex) {
                Output.get().logWarn("Could not get locale of player " + getName(), ex);
            }
        }
        return Optional.empty();
    }

    @Override public boolean isOp() {
        return bukkitPlayer.isOp();
    }

    public org.bukkit.entity.Player getWrapper() {
        return bukkitPlayer;
    }

    @Override public Inventory getInventory() {
        return new SpigotPlayerInventory(getWrapper().getInventory());
    }

    @Override public void updateInventory() {
        bukkitPlayer.updateInventory();
    }

    /**
     * This class is an adaptation of the NmsHelper class in the Rosetta library by Max Roncace. The
     * library is licensed under the New BSD License. See the {@link me.faizaand.prison.localization}
     * package for the full license.
     *
     * @author Max Roncacé
     */
    private static class NmsHelper {

        private static final boolean SUPPORT;

        private static final String PACKAGE_VERSION;

        private static final Method PLAYER_SPIGOT;
        private static final Method PLAYER$SPIGOT_GETLOCALE;
        private static final Method CRAFTPLAYER_GETHANDLE;

        private static final Field ENTITY_PLAYER_LOCALE;
        private static final Field LOCALE_LANGUAGE_WRAPPED_STRING;

        static {
            String[] array = Bukkit.getServer().getClass().getPackage().getName().split("\\.");
            PACKAGE_VERSION = array.length == 4 ? array[3] + "." : "";

            Method player_spigot = null;
            Method player$spigot_getLocale = null;
            Method craftPlayer_getHandle = null;
            Field entityPlayer_locale = null;
            Field localeLanguage_wrappedString = null;
            try {

                Class<?> craftPlayer = getCraftClass("entity.CraftPlayer");

                // for reasons not known to me Paper decided to make EntityPlayer#locale null by default and have the
                // fallback defined in CraftPlayer$Spigot#getLocale. Rosetta will use that method if possible and fall
                // back to accessing the field directly.
                try {
                    player_spigot = org.bukkit.entity.Player.class.getMethod("spigot");
                    Class<?> player$spigot = Class.forName("org.bukkit.entity.Player$Spigot");
                    player$spigot_getLocale = player$spigot.getMethod("getLocale");
                } catch (NoSuchMethodException ignored) { // we're non-Spigot or old
                }

                if (player$spigot_getLocale == null) { // fallback for non-Spigot software
                    craftPlayer_getHandle = craftPlayer.getMethod("getHandle");

                    entityPlayer_locale = getNmsClass("EntityPlayer").getDeclaredField("locale");
                    entityPlayer_locale.setAccessible(true);
                    if (entityPlayer_locale.getType().getSimpleName().equals("LocaleLanguage")) {
                        // On versions prior to 1.6, the locale is stored as a LocaleLanguage object.
                        // The actual locale string is wrapped within it.
                        // On 1.5, it's stored in field "e".
                        // On 1.3 and 1.4, it's stored in field "d".
                        try { // try for 1.5
                            localeLanguage_wrappedString =
                                entityPlayer_locale.getType().getDeclaredField("e");
                        } catch (NoSuchFieldException ex) { // we're pre-1.5
                            localeLanguage_wrappedString =
                                entityPlayer_locale.getType().getDeclaredField("d");
                        }
                    }
                }
            } catch (ClassNotFoundException | NoSuchFieldException | NoSuchMethodException ex) {
                Output.get().logError(
                    "Cannot initialize NMS components - per-player localization disabled.", ex);
            }
            PLAYER_SPIGOT = player_spigot;
            PLAYER$SPIGOT_GETLOCALE = player$spigot_getLocale;
            CRAFTPLAYER_GETHANDLE = craftPlayer_getHandle;
            ENTITY_PLAYER_LOCALE = entityPlayer_locale;
            LOCALE_LANGUAGE_WRAPPED_STRING = localeLanguage_wrappedString;
            SUPPORT = CRAFTPLAYER_GETHANDLE != null;
        }

        private static boolean hasSupport() {
            return SUPPORT;
        }

        private static String getLocale(org.bukkit.entity.Player player)
            throws IllegalAccessException, InvocationTargetException {
            if (PLAYER$SPIGOT_GETLOCALE != null) {
                return (String) PLAYER$SPIGOT_GETLOCALE.invoke(PLAYER_SPIGOT.invoke(player));
            }

            Object entityPlayer = CRAFTPLAYER_GETHANDLE.invoke(player);
            Object locale = ENTITY_PLAYER_LOCALE.get(entityPlayer);
            if (LOCALE_LANGUAGE_WRAPPED_STRING != null) {
                return (String) LOCALE_LANGUAGE_WRAPPED_STRING.get(locale);
            } else {
                return (String) locale;
            }
        }

        private static Class<?> getCraftClass(String className) throws ClassNotFoundException {
            return Class.forName("org.bukkit.craftbukkit." + PACKAGE_VERSION + className);
        }

        private static Class<?> getNmsClass(String className) throws ClassNotFoundException {
            return Class.forName("net.minecraft.server." + PACKAGE_VERSION + className);
        }

    }

}
