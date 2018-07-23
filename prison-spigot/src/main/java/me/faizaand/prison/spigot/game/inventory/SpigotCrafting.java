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

package me.faizaand.prison.spigot.game.inventory;

import me.faizaand.prison.internal.GameItemStack;
import me.faizaand.prison.internal.inventory.CraftingInventory;
import me.faizaand.prison.internal.inventory.FurnaceRecipe;
import me.faizaand.prison.internal.inventory.Recipe;
import me.faizaand.prison.spigot.SpigotUtil;
import org.bukkit.inventory.ShapelessRecipe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by DMP9 on 04/02/2017.
 */
public class SpigotCrafting extends SpigotInventory implements CraftingInventory {

    public SpigotCrafting(org.bukkit.inventory.CraftingInventory wrapper) {
        super(wrapper);
    }

    @Override public GameItemStack[] getMatrix() {
        List<org.bukkit.inventory.ItemStack> stackList =
            Arrays.asList(((org.bukkit.inventory.CraftingInventory) getWrapper()).getMatrix());
        List<GameItemStack> stacks = new ArrayList<>();
        stackList.forEach(x -> stacks.add(SpigotUtil.bukkitItemStackToPrison(x)));
        return (GameItemStack[]) stacks.toArray();
    }

    @Override public void setMatrix(GameItemStack[] contents) {
        List<GameItemStack> stackList = Arrays.asList(contents);
        List<org.bukkit.inventory.ItemStack> stacks = new ArrayList<>();
        stackList.forEach(x -> stacks.add(SpigotUtil.prisonItemStackToBukkit(x)));
        ((org.bukkit.inventory.CraftingInventory) getWrapper())
            .setMatrix((org.bukkit.inventory.ItemStack[]) stacks.toArray());
    }

    @Override public Recipe getRecipe() {
        if (((org.bukkit.inventory.CraftingInventory) getWrapper())
            .getRecipe() instanceof FurnaceRecipe) {
            return new SpigotFurnaceRecipe(
                (org.bukkit.inventory.FurnaceRecipe) ((org.bukkit.inventory.CraftingInventory) getWrapper())
                    .getRecipe());
        } else if (((org.bukkit.inventory.CraftingInventory) getWrapper())
            .getRecipe() instanceof ShapelessRecipe) {
            return new SpigotShapelessRecipe(
                (org.bukkit.inventory.ShapelessRecipe) ((org.bukkit.inventory.CraftingInventory) getWrapper())
                    .getRecipe());
        } else if (((org.bukkit.inventory.CraftingInventory) getWrapper())
            .getRecipe() instanceof org.bukkit.inventory.ShapedRecipe) {
            return new SpigotShapedRecipe(
                (org.bukkit.inventory.ShapedRecipe) ((org.bukkit.inventory.CraftingInventory) getWrapper())
                    .getRecipe());
        } else {
            return null;
        }
    }

    @Override public GameItemStack getResult() {
        return SpigotUtil.bukkitItemStackToPrison(
            ((org.bukkit.inventory.CraftingInventory) getWrapper()).getResult());
    }

    @Override public void setResult(GameItemStack newResult) {
        ((org.bukkit.inventory.CraftingInventory) getWrapper())
            .setResult(SpigotUtil.prisonItemStackToBukkit(newResult));
    }

}
