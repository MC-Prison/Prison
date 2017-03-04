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

package tech.mcprison.prison.sponge;

import tech.mcprison.prison.store.Storage;

import java.util.List;

/**
 * @author Faizaan A. Datoo
 */
public class SpongeStorage implements Storage {
    @Override public void write(String key, Object obj) {

    }

    @Override public <T> T read(String key, Class<T> type) {
        return null;
    }

    @Override public <T> List<T> readAll(Class<T> type) {
        return null;
    }
}
