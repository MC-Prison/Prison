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

package me.faizaand.prison.spigot;

import me.faizaand.prison.internal.GameItemStack;
import me.faizaand.prison.internal.inventory.InventoryType;
import me.faizaand.prison.internal.inventory.Viewable;
import me.faizaand.prison.spigot.game.SpigotWorld;
import me.faizaand.prison.util.BlockType;
import me.faizaand.prison.util.GameLocation;
import me.faizaand.prison.util.Text;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Utilities for converting Prison-Core types to Spigot types.
 *
 * @author Faizaan A. Datoo
 */
public class SpigotUtil {

    private SpigotUtil() {
    }

    /*
     * BlockType and Material
     */

    public static BlockType materialToBlockType(Material material) {
        return BlockType.getBlock(material.getId()); // To be safe, we use legacy ID
    }

    public static MaterialData blockTypeToMaterial(BlockType type) {
        Material material = Material.getMaterial(type.getLegacyId());
        return new MaterialData(material, (byte) type.getData()); // To be safe, we use legacy ID
    }

    /*
     * GameLocation
     */

    public static GameLocation bukkitLocationToPrison(Location bukkitLocation) {
        return new GameLocation(new SpigotWorld(bukkitLocation.getWorld()), bukkitLocation.getX(),
                bukkitLocation.getY(), bukkitLocation.getZ(), bukkitLocation.getPitch(),
                bukkitLocation.getYaw());
    }

    public static org.bukkit.Location prisonLocationToBukkit(GameLocation prisonLocation) {
        return new Location(Bukkit.getWorld(prisonLocation.getWorld().getName()),
                prisonLocation.getX(), prisonLocation.getY(), prisonLocation.getZ(),
                prisonLocation.getYaw(), prisonLocation.getPitch());
    }

    /*
     * GameItemStack
     */

    public static GameItemStack bukkitItemStackToPrison(
            ItemStack bukkitStack) {
        if (bukkitStack == null || bukkitStack.getType().equals(Material.AIR)) {
            return new GameItemStack(0, BlockType.AIR);
        }

        ItemMeta meta;
        if (!bukkitStack.hasItemMeta()) {
            meta = Bukkit.getItemFactory().getItemMeta(bukkitStack.getType());
        } else {
            meta = bukkitStack.getItemMeta();
        }

        String displayName = null;

        if (meta.hasDisplayName()) {
            displayName = meta.getDisplayName();
        }

        int amount = bukkitStack.getAmount();

        BlockType type = materialToBlockType(bukkitStack.getType());

        List<String> lore = meta.hasLore() ? meta.getLore() : Collections.emptyList();
        String[] lore_arr = lore.toArray(new String[lore.size()]);

        return new GameItemStack(displayName, amount, type, lore_arr);
    }

    public static ItemStack prisonItemStackToBukkit(
            GameItemStack prisonStack) {
        int amount = prisonStack.getAmount();
        MaterialData materialData = blockTypeToMaterial(prisonStack.getMaterial());

        ItemStack bukkitStack = new ItemStack(materialData.getItemType(), amount);
        bukkitStack.setData(materialData);

        ItemMeta meta;
        if (bukkitStack.getItemMeta() == null || !bukkitStack.hasItemMeta()) {
            meta = Bukkit.getItemFactory().getItemMeta(bukkitStack.getType());
        } else {
            meta = bukkitStack.getItemMeta();
        }

        if (meta != null) {
            if (prisonStack.getDisplayName() != null) {
                meta.setDisplayName(Text.translateAmpColorCodes(prisonStack.getDisplayName()));
            }
            if (prisonStack.getLore() != null) {
                List<String> colored = new ArrayList<>();
                for (String uncolor : prisonStack.getLore()) {
                    colored.add(Text.translateAmpColorCodes(uncolor));
                }
                meta.setLore(colored);
            }
            bukkitStack.setItemMeta(meta);
        }

        return bukkitStack;
    }

    /*
     * InventoryType
     */

    public static InventoryType bukkitInventoryTypeToPrison(
            org.bukkit.event.inventory.InventoryType type) {
        return InventoryType.valueOf(type.name());
    }

    public static org.bukkit.event.inventory.InventoryType prisonInventoryTypeToBukkit(
            InventoryType type) {
        return org.bukkit.event.inventory.InventoryType.valueOf(type.name());
    }

    public static InventoryType.SlotType bukkitSlotTypeToPrison(
            org.bukkit.event.inventory.InventoryType.SlotType type) {

        return InventoryType.SlotType.valueOf(type.name());
    }

    /*
     * Property
     */

    public static InventoryView.Property prisonPropertyToBukkit(Viewable.Property property) {
        return InventoryView.Property.valueOf(property.name());
    }

}
