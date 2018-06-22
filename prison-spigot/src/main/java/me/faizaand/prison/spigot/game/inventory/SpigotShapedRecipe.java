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
import me.faizaand.prison.internal.inventory.ShapedRecipe;
import me.faizaand.prison.spigot.SpigotUtil;
import me.faizaand.prison.util.BlockType;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by DMP9 on 04/02/2017.
 */
public class SpigotShapedRecipe extends SpigotRecipe implements ShapedRecipe {

    public SpigotShapedRecipe(org.bukkit.inventory.ShapedRecipe wrapper) {
        super(wrapper);
    }

    @Override public Map<Character, GameItemStack> getIngredientMap() {
        Map<Character, org.bukkit.inventory.ItemStack> stackMap =
            ((org.bukkit.inventory.ShapedRecipe) getWrapper()).getIngredientMap();
        Map<Character, GameItemStack> result = new HashMap<>();
        stackMap.forEach((x, y) -> result.put(x, SpigotUtil.bukkitItemStackToPrison(y)));
        return result;
    }

    @Override public String[] getShape() {
        return ((org.bukkit.inventory.ShapedRecipe) getWrapper()).getShape();
    }

    @Override public ShapedRecipe setIngredient(char key, BlockType ingredient) {
        ((org.bukkit.inventory.ShapedRecipe) getWrapper())
            .setIngredient(key, SpigotUtil.blockTypeToMaterial(ingredient));
        return this;
    }

    @Override public ShapedRecipe shape(String... shape) {
        ((org.bukkit.inventory.ShapedRecipe) getWrapper()).shape(shape);
        return this;
    }

}
