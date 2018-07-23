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

import me.faizaand.prison.internal.GameWorld;
import me.faizaand.prison.internal.block.Block;

/**
 * Represents a location in a Minecraft world.
 *
 * @author Faizaan A. Datoo
 * @since API 1.0
 */
public class GameLocation {

    private GameWorld world;
    private double x, y, z;
    private float pitch, yaw;

    public GameLocation(GameWorld world, double x, double y, double z, float pitch, float yaw) {
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
        this.pitch = pitch;
        this.yaw = yaw;
    }

    public GameLocation(GameWorld world, double x, double y, double z) {
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
        this.pitch = 0.0f;
        this.yaw = 0.0f;
    }

    public GameLocation() {
    }

    public GameWorld getWorld() {
        return world;
    }

    public void setWorld(GameWorld world) {
        this.world = world;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getZ() {
        return z;
    }

    public void setZ(double z) {
        this.z = z;
    }

    public float getPitch() {
        return pitch;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    public float getYaw() {
        return yaw;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }

    public int getBlockX() {
        return Math.toIntExact(Math.round(getX()));
    }

    public int getBlockY() {
        return Math.toIntExact(Math.round(getY()));
    }

    public int getBlockZ() {
        return Math.toIntExact(Math.round(getZ()));
    }

    public Block getBlockAt() {
        return world.getBlockAt(this);
    }

    @Override public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof GameLocation)) {
            return false;
        }

        GameLocation location = (GameLocation) o;

        return Double.compare(location.x, x) == 0 && Double.compare(location.y, y) == 0
            && Double.compare(location.z, z) == 0 && Float.compare(location.pitch, pitch) == 0
            && Float.compare(location.yaw, yaw) == 0 && (world != null ?
            world.getName().equals(location.world.getName()) :
            location.world == null);

    }

    @Override public int hashCode() {
        int result;
        long temp;
        result = world != null ? world.hashCode() : 0;
        temp = Double.doubleToLongBits(x);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(y);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(z);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (pitch != +0.0f ? Float.floatToIntBits(pitch) : 0);
        result = 31 * result + (yaw != +0.0f ? Float.floatToIntBits(yaw) : 0);
        return result;
    }

    @Override public String toString() {
        return "GameLocation{" + "world=" + world + ", x=" + x + ", y=" + y + ", z=" + z + ", pitch="
            + pitch + ", yaw=" + yaw + '}';
    }

    /**
     * Returns the values in coordinate (x, y, z) format.
     *
     * @return The {@link String} containing coordinates.
     */
    public String toCoordinates() {
        return "(" + x + ", " + y + ", " + z + ")";
    }

    /**
     * Returns the values in coordinate (x, y, z) format, to the nearest block (i.e. no decimals).
     *
     * @return The {@link String} containing coordinates.
     */
    public String toBlockCoordinates() {
        return "(" + Math.round(x) + ", " + Math.round(y) + ", " + Math.round(z) + ")";
    }

}
