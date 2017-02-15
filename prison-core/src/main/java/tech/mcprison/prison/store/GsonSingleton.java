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

package tech.mcprison.prison.store;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import tech.mcprison.prison.util.Location;

/**
 * A package-local utility for creating and accessing a GSON instance.
 *
 * @author Faizaan A. Datoo
 */
class GsonSingleton {

    private static GsonSingleton instance;
    private Gson gson = null;

    public static GsonSingleton getInstance() {
        if (instance == null) {
            instance = new GsonSingleton();
        }
        return instance;
    }

    public GsonSingleton() {
        gson = new GsonBuilder().setExclusionStrategies(new AnnotationExclusionStrategy())
            .setPrettyPrinting().disableHtmlEscaping()
            .registerTypeAdapter(Location.class, new LocationAdapter()).create();
    }

    public Gson getGson() {
        return gson;
    }

    class AnnotationExclusionStrategy implements ExclusionStrategy {

        @Override public boolean shouldSkipField(FieldAttributes f) {
            return f.getAnnotation(Exclude.class) != null;
        }

        @Override public boolean shouldSkipClass(Class<?> clazz) {
            return false;
        }

    }


}
