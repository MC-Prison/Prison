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

package me.faizaand.prison.selection;

import me.faizaand.prison.internal.GamePlayer;
import me.faizaand.prison.util.Bounds;
import me.faizaand.prison.util.GameLocation;

/**
 * Represents an individual selection.
 *
 * @author Faizaan A. Datoo
 * @since API 1.0
 */
public class Selection {

    private GamePlayer owner;
    private GameLocation min, max;

    public Selection() {
    }

    public Selection(GameLocation min, GameLocation max) {
        this.min = min;
        this.max = max;
    }

    public Selection(GamePlayer owner, GameLocation min, GameLocation max) {
        this.owner = owner;
        this.min = min;
        this.max = max;
    }

    public GamePlayer getOwner() {
        return owner;
    }

    public void setOwner(GamePlayer owner) {
        this.owner = owner;
    }

    public GameLocation getMin() {
        return min;
    }

    public void setMin(GameLocation min) {
        this.min = min;
    }

    public GameLocation getMax() {
        return max;
    }

    public void setMax(GameLocation max) {
        this.max = max;
    }

    /**
     * Returns whether or not both the minimum and maximum GameLocations are set.
     *
     * @return true if they are, false otherwise.
     */
    public boolean isComplete() {
        return min != null && max != null;
    }

    public Bounds asBounds() {
        return new Bounds(min, max);
    }

}
