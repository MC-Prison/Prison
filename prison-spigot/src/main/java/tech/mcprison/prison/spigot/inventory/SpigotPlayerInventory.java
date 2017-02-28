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

package tech.mcprison.prison.spigot.inventory;

import tech.mcprison.prison.internal.ItemStack;
import tech.mcprison.prison.internal.inventory.PlayerInventory;
import tech.mcprison.prison.spigot.SpigotUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by DMP9 on 04/02/2017.
 */
public class SpigotPlayerInventory extends SpigotInventory implements PlayerInventory {
    public SpigotPlayerInventory(org.bukkit.inventory.PlayerInventory wrapper) {
        super(wrapper);
    }

    @Override public ItemStack[] getArmorContents() {
        List<ItemStack> items = new ArrayList<>();
        Arrays.asList(((org.bukkit.inventory.PlayerInventory) getWrapper()).getArmorContents())
            .forEach(x -> items.add(SpigotUtil.bukkitItemStackToPrison(x)));
        return (ItemStack[]) items.toArray();
    }

    @Override public ItemStack getBoots() {
        return SpigotUtil.bukkitItemStackToPrison(
            ((org.bukkit.inventory.PlayerInventory) getWrapper()).getBoots());
    }

    @Override public ItemStack getChestplate() {
        return SpigotUtil.bukkitItemStackToPrison(
            ((org.bukkit.inventory.PlayerInventory) getWrapper()).getChestplate());
    }

    @Override public int getHeldItemSlot() {
        return ((org.bukkit.inventory.PlayerInventory) getWrapper()).getHeldItemSlot();
    }

    @Override public ItemStack getHelmet() {
        return SpigotUtil.bukkitItemStackToPrison(
            ((org.bukkit.inventory.PlayerInventory) getWrapper()).getHelmet());
    }

    @Override public ItemStack getLeggings() {
        return SpigotUtil.bukkitItemStackToPrison(
            ((org.bukkit.inventory.PlayerInventory) getWrapper()).getLeggings());
    }

    @Override public void setArmorContents(ItemStack[] items) {
        List<org.bukkit.inventory.ItemStack> stacks = new ArrayList<>();
        Arrays.asList(items).forEach(x -> stacks.add(SpigotUtil.prisonItemStackToBukkit(x)));
        ((org.bukkit.inventory.PlayerInventory) getWrapper())
            .setArmorContents((org.bukkit.inventory.ItemStack[]) stacks.toArray());
    }

    @Override public ItemStack getItemInLeftHand() {
        return SpigotUtil.bukkitItemStackToPrison(
            ((org.bukkit.inventory.PlayerInventory) getWrapper()).getItemInOffHand());
    }

    @Override public ItemStack getItemInRightHand() {
        return SpigotUtil.bukkitItemStackToPrison(
            ((org.bukkit.inventory.PlayerInventory) getWrapper()).getItemInMainHand());
    }

    @Override public void setBoots(ItemStack boots) {
        ((org.bukkit.inventory.PlayerInventory) getWrapper())
            .setBoots(SpigotUtil.prisonItemStackToBukkit(boots));
    }

    @Override public void setChestplate(ItemStack chestplate) {
        ((org.bukkit.inventory.PlayerInventory) getWrapper())
            .setBoots(SpigotUtil.prisonItemStackToBukkit(chestplate));
    }

    @Override public void setLeggings(ItemStack leggings) {
        ((org.bukkit.inventory.PlayerInventory) getWrapper())
            .setBoots(SpigotUtil.prisonItemStackToBukkit(leggings));
    }

    @Override public void setHelmet(ItemStack helmet) {
        ((org.bukkit.inventory.PlayerInventory) getWrapper())
            .setBoots(SpigotUtil.prisonItemStackToBukkit(helmet));
    }

    @Override public void setItemInLeftHand(ItemStack stack) {
        ((org.bukkit.inventory.PlayerInventory) getWrapper())
            .setItemInOffHand(SpigotUtil.prisonItemStackToBukkit(stack));
    }

    @Override public void setItemInRightHand(ItemStack stack) {
        ((org.bukkit.inventory.PlayerInventory) getWrapper())
            .setItemInMainHand(SpigotUtil.prisonItemStackToBukkit(stack));
    }

    @Override public void setHeldItemSlot(int slot) {
        ((org.bukkit.inventory.PlayerInventory) getWrapper()).setHeldItemSlot(slot);
    }
}
