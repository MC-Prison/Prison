/*
 * Prison is a Minecraft plugin for the prison game mode.
 * Copyright (C) 2018 The Prison Team
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package xyz.faizaan.prison.mines;

import com.google.common.eventbus.Subscribe;
import xyz.faizaan.prison.internal.ItemStack;
import xyz.faizaan.prison.internal.Player;
import xyz.faizaan.prison.internal.events.block.BlockBreakEvent;
import xyz.faizaan.prison.internal.inventory.PlayerInventory;
import xyz.faizaan.prison.selection.SelectionCompletedEvent;
import xyz.faizaan.prison.util.BlockType;
import xyz.faizaan.prison.util.Bounds;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Faizaan A. Datoo, Dylan M. Perks
 */
public class MinesListener {

    @Subscribe
    public void onSelectionComplete(SelectionCompletedEvent e) {
        Bounds bounds = e.getSelection().asBounds();
        String dimensions = bounds.getWidth() + "x" + bounds.getHeight() + "x" + bounds.getLength();
        e.getPlayer().sendMessage("&3Ready. &7Your mine will be &8" + dimensions
            + "&7 blocks. Type /mines create to create it.");
    }

    /**
     * Powertool helper
     */
    @Subscribe
    public void onBlockBreak(BlockBreakEvent e) {
        if (PrisonMines.getInstance().getPlayerManager().hasAutosmelt(e.getPlayer())) {
            smelt(e.getBlockLocation().getBlockAt()
                .getDrops(((PlayerInventory) e.getPlayer().getInventory()).getItemInRightHand()));
        }
        if (PrisonMines.getInstance().getPlayerManager().hasAutopickup(e.getPlayer())) {
            e.getPlayer().getInventory()
                .addItem(e.getBlockLocation().getBlockAt().getDrops().toArray(new ItemStack[]{}));
            e.getBlockLocation().getBlockAt().setType(BlockType.AIR);
            e.setCanceled(true);
        }
        if (PrisonMines.getInstance().getPlayerManager().hasAutoblock(e.getPlayer())) {
            block(e.getPlayer());
        }
    }

    private void smelt(List<ItemStack> drops) {
        drops.replaceAll(x -> {
            if (x.getMaterial() == BlockType.GOLD_ORE) {
                return new ItemStack(x.getAmount(), BlockType.GOLD_INGOT);
            } else if (x.getMaterial() == BlockType.IRON_ORE) {
                return new ItemStack(x.getAmount(), BlockType.IRON_INGOT);
            } else {
                return x;
            }
        });
    }

    private void block(Player player) {
        List<ItemStack> itemList = Arrays.asList(player.getInventory().getItems());
        List<ItemStack> giveBack = new ArrayList<>();
        itemList.replaceAll(x -> {
            if (x != null) {
                if (x.getMaterial() == BlockType.DIAMOND) {
                    if (x.getAmount() % 9 > 0) {
                        giveBack.add(new ItemStack(x.getAmount() % 9, x.getMaterial()));
                    }
                    return new ItemStack(x.getAmount() / 9, BlockType.DIAMOND_BLOCK);
                } else if (x.getMaterial() == BlockType.EMERALD) {
                    if (x.getAmount() % 9 > 0) {
                        giveBack.add(new ItemStack(x.getAmount() % 9, x.getMaterial()));
                    }
                    return new ItemStack(x.getAmount() / 9, BlockType.EMERALD_BLOCK);
                } else if (x.getMaterial() == BlockType.IRON_INGOT) {
                    if (x.getAmount() % 9 > 0) {
                        giveBack.add(new ItemStack(x.getAmount() % 9, x.getMaterial()));
                    }
                    return new ItemStack(x.getAmount() / 9, BlockType.IRON_BLOCK);
                } else if (x.getMaterial() == BlockType.GLOWSTONE_DUST) {
                    if (x.getAmount() % 4 > 0) {
                        giveBack.add(new ItemStack(x.getAmount() % 9, x.getMaterial()));
                    }
                    return new ItemStack(x.getAmount() / 4, BlockType.GLOWSTONE);
                } else if (x.getMaterial() == BlockType.GOLD_INGOT) {
                    if (x.getAmount() % 9 > 0) {
                        giveBack.add(new ItemStack(x.getAmount() % 9, x.getMaterial()));
                    }
                    return new ItemStack(x.getAmount() / 9, BlockType.GOLD_BLOCK);
                } else if (x.getMaterial() == BlockType.COAL) {
                    if (x.getAmount() % 9 > 0) {
                        giveBack.add(new ItemStack(x.getAmount() % 9, x.getMaterial()));
                    }
                    return new ItemStack(x.getAmount() / 9, BlockType.BLOCK_OF_COAL);
                } else if (x.getMaterial() == BlockType.REDSTONE) {
                    if (x.getAmount() % 9 > 0) {
                        giveBack.add(new ItemStack(x.getAmount() % 9, x.getMaterial()));
                    }
                    return new ItemStack(x.getAmount() / 9, BlockType.REDSTONE_BLOCK);
                } else if (x.getMaterial() == BlockType.LAPIS_LAZULI) {
                    return new ItemStack(x.getAmount() / 9, BlockType.LAPIS_LAZULI_BLOCK);
                } else {
                    return x;
                }
            } else {
                return x;
            }
        });
        player.getInventory().setItems(itemList);
        player.getInventory().addItem((ItemStack[]) giveBack.toArray());
    }

}