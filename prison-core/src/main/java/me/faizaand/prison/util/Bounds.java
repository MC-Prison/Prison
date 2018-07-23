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

package me.faizaand.prison.util;

/**
 * Represents the area between two {@link GameLocation}s.
 *
 * @author Faizaan A. Datoo
 * @since API 1.0
 */
public class Bounds {

    private GameLocation min, max;

    public Bounds() {
    }

    public Bounds(GameLocation min, GameLocation max) {
        this.min = min;
        this.max = max;
    }

    /**
     * Returns the width of the area.
     * This will always be positive.
     *
     * @return A double.
     */
    public double getWidth() {
        return (Math.max(min.getX(), max.getX()) - Math.min(min.getX(), max.getX())) + 1;
    }

    /**
     * Returns the height of the area.
     * This will always be positive.
     *
     * @return A double.
     */
    public double getHeight() {
        return (Math.max(min.getY(), max.getY()) - Math.min(min.getY(), max.getY())) + 1;
    }

    /**
     * Returns the length of the area.
     * This will always be positive.
     *
     * @return A double.
     */
    public double getLength() {
        return (Math.max(min.getZ(), max.getZ()) - Math.min(min.getZ(), max.getZ())) + 1;
    }

    /**
     * Returns the area, which is represented by the formula <code>A=2(wl+hl+hw)</code>.
     *
     * @return A double.
     */
    // A=2(wl+hl+hw)
    public double getArea() {
        return 2 * (getWidth() * getLength() + getHeight() * getLength()
            + getHeight() * getWidth());
    }

    /**
     * Returns whether or not a single point is within these boundaries.
     *
     * @param location The {@link GameLocation} to check.
     * @return true if the location is within the bounds, false otherwise.
     */
    public boolean within(GameLocation location) {
        double minX = Math.min(min.getX(), max.getX());
        double minY = Math.min(min.getY(), max.getY());
        double minZ = Math.min(min.getZ(), max.getZ());
        double maxX = Math.max(min.getX(), max.getX());
        double maxY = Math.max(min.getY(), max.getY());
        double maxZ = Math.max(min.getZ(), max.getZ());

        double ourX = Math.floor(location.getX());
        double ourY = Math.floor(location.getY());
        double ourZ = Math.floor(location.getZ());

        return ourX >= minX && ourX <= maxX // Within X
            && ourY >= minY && ourY <= maxY // Within Y
            && ourZ >= minZ && ourZ <= maxZ; // Within Z
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

    @Override public String toString() {
        return "Bounds{" + "min=" + min.toCoordinates() + ", max=" + max.toCoordinates() + '}';
    }

    @Override public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Bounds)) {
            return false;
        }

        Bounds bounds = (Bounds) o;
        return min != null ?
            min.equals(bounds.min) :
            bounds.min == null && (max != null ? max.equals(bounds.max) : bounds.max == null);
    }

    @Override public int hashCode() {
        int result = min != null ? min.hashCode() : 0;
        result = 31 * result + (max != null ? max.hashCode() : 0);
        return result;
    }

}
