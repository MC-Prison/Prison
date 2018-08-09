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

package xyz.faizaan.prison.spigot.inventory;

import xyz.faizaan.prison.internal.ItemStack;
import xyz.faizaan.prison.internal.inventory.Recipe;
import xyz.faizaan.prison.spigot.SpigotUtil;

/**
 * Created by DMP9 on 04/02/2017.
 */
public class SpigotRecipe implements Recipe {

    org.bukkit.inventory.Recipe wrapper;

    public SpigotRecipe(org.bukkit.inventory.Recipe wrapper) {
        this.wrapper = wrapper;
    }

    public org.bukkit.inventory.Recipe getWrapper() {
        return wrapper;
    }

    @Override public ItemStack getResult() {
        return SpigotUtil.bukkitItemStackToPrison(wrapper.getResult());
    }

}
