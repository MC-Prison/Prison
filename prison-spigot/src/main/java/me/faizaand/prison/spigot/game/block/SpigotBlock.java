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

package me.faizaand.prison.spigot.game.block;

import me.faizaand.prison.internal.GameItemStack;
import me.faizaand.prison.internal.block.Block;
import me.faizaand.prison.internal.block.BlockFace;
import me.faizaand.prison.internal.block.BlockState;
import me.faizaand.prison.spigot.SpigotUtil;
import me.faizaand.prison.util.BlockType;
import me.faizaand.prison.util.GameLocation;
import org.bukkit.material.MaterialData;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Faizaan A. Datoo
 */
public class SpigotBlock implements Block {

    private org.bukkit.block.Block bBlock;

    public SpigotBlock(org.bukkit.block.Block bBlock) {
        this.bBlock = bBlock;
    }

    @Override public GameLocation getLocation() {
        return SpigotUtil.bukkitLocationToPrison(bBlock.getLocation());
    }

    @Override public Block getRelative(BlockFace face) {
        return new SpigotBlock(bBlock.getRelative(org.bukkit.block.BlockFace.valueOf(face.name())));
    }

    @Override public BlockType getType() {
        return SpigotUtil.materialToBlockType(bBlock.getType());
    }

    @Override public void setType(BlockType type) {
        MaterialData materialData = SpigotUtil.blockTypeToMaterial(type);
        bBlock.setType(materialData.getItemType());
        bBlock.setData(materialData.getData());
    }

    @Override public BlockState getState() {
        switch (getType()) {
            case LEVER:
                return new SpigotLever(this);
            case STANDING_SIGN_BLOCK:
            case WALL_MOUNTED_SIGN_BLOCK:
                return new SpigotSign(this);
            case ACACIA_DOOR_BLOCK:
            case OAK_DOOR_BLOCK:
            case BIRCH_DOOR_BLOCK:
            case SPRUCE_DOOR_BLOCK:
            case DARK_OAK_DOOR_BLOCK:
            case IRON_DOOR_BLOCK:
            case JUNGLE_DOOR_BLOCK:
                return new SpigotDoor(this);
            default:
                return new SpigotBlockState(this);
        }
    }

    @Override public boolean breakNaturally() {
        return bBlock.breakNaturally();
    }

    @Override public List<GameItemStack> getDrops() {
        List<GameItemStack> ret = new ArrayList<>();

        bBlock.getDrops()
            .forEach(itemStack -> ret.add(SpigotUtil.bukkitItemStackToPrison(itemStack)));

        return ret;
    }

    @Override public List<GameItemStack> getDrops(GameItemStack tool) {
        List<GameItemStack> ret = new ArrayList<>();

        bBlock.getDrops(SpigotUtil.prisonItemStackToBukkit(tool))
            .forEach(itemStack -> ret.add(SpigotUtil.bukkitItemStackToPrison(itemStack)));

        return ret;
    }

    public org.bukkit.block.Block getWrapper() {
        return bBlock;
    }

}
