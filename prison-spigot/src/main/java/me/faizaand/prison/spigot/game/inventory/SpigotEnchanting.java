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
import me.faizaand.prison.internal.inventory.EnchantingInventory;
import me.faizaand.prison.spigot.SpigotUtil;

/**
 * Created by DMP9 on 04/02/2017.
 */
public class SpigotEnchanting extends SpigotInventory implements EnchantingInventory {

    public SpigotEnchanting(org.bukkit.inventory.EnchantingInventory wrapper) {
        super(wrapper);
    }

    @Override public GameItemStack getItem() {
        return SpigotUtil.bukkitItemStackToPrison(
            ((org.bukkit.inventory.EnchantingInventory) getWrapper()).getItem());
    }

    @Override public void setItem(GameItemStack item) {
        ((org.bukkit.inventory.EnchantingInventory) getWrapper())
            .setItem(SpigotUtil.prisonItemStackToBukkit(item));
    }

}
