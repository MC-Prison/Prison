/*
 *  Prison is a Minecraft plugin for the prison game mode.
 *  Copyright (C) 2016 The Prison Team
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

package io.github.prison.commands.handlers;

import io.github.prison.commands.ArgumentHandler;
import io.github.prison.commands.CommandArgument;
import io.github.prison.commands.TransformError;
import io.github.prison.internal.CommandSender;
import io.github.prison.util.Block;

public class BlockArgumentHandler extends ArgumentHandler<Block> {

    public BlockArgumentHandler() {
        setMessage("parse_error", "The parameter [%p] is not a valid block.");
        setMessage("include_error", "There is no block named %1");
        setMessage("exclude_error", "There is no block named %1");
    }

    @Override
    public Block transform(CommandSender sender, CommandArgument argument, String value) throws TransformError {
        Block m = null;

        // Try block legacy (numerical) ID first
        try {
            m = Block.getBlock(Integer.parseInt(value));
        } catch (NumberFormatException ignored) {
        }

        if (m != null)
            return m;

        // Now try new block IDs

        m = Block.getBlock(value);

        if (m != null)
            return m;

        // Now try id:data format
        if(value.contains(":")) {
            int id;
            short data;
            try {
                id = Integer.parseInt(value.split(":")[0]);
                data = Short.parseShort(value.split(":")[1]);
            } catch(NumberFormatException ignored) {
                throw new TransformError(argument.getMessage("parse_error"));
            }
            m = Block.getBlockWithData(id, data);
        }

        if(m != null)
            return m;

        // No more checks, just fail

        throw new TransformError(argument.getMessage("parse_error"));
    }
}
