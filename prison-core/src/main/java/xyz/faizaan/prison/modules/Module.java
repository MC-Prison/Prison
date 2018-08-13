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

package xyz.faizaan.prison.modules;

import xyz.faizaan.prison.Prison;
import xyz.faizaan.prison.error.ErrorManager;

import java.io.File;

/**
 * Represents a module, which is a part of Prison that can be enabled and
 * disabled independently from the rest of the modules.
 *
 * @author Faizaan A. Datoo
 * @since API 1.0
 */
public abstract class Module implements PluginEntity {

    /*
     * Fields & Constants
     */

    private String name, version;
    private File dataFolder;
    private int apiTarget;
    private ModuleStatus status;
    private ErrorManager errorManager;

    /*
     * Constructor
     */

    /**
     * Initialize your module.
     *
     * @param name    The name of the module.
     * @param version The version of the module.
     * @param target  The API level to target.
     */
    public Module(String name, String version, int target) {
        this.name = name;
        this.version = version;
        this.dataFolder = new File(Prison.get().getModuleManager().getModuleRoot(),
            name.toLowerCase().replace(" ", "_"));
        this.apiTarget = target;
        this.status = new ModuleStatus();
        if (!this.dataFolder.exists()) {
            this.dataFolder.mkdir();
        }
        this.errorManager = new ErrorManager(this);
    }

    /*
     * Methods, to be overridden
     */

    /**
     * Called when the module is to be enabled.
     */
    public void enable() {
    }

    /**
     * Called when a module is to be disabled.
     */
    public void disable() {
    }

    /**
     * Tell the module loader that this module failed to start.
     *
     * @param reason The reason why this failed to start. May include amp-prefixed color codes.
     */
    protected void fail(String reason) {
        getStatus().toFailed(reason);
    }

    /*
     * Getters & Setters
     */

    public String getName() {
        return name;
    }

    public String getVersion() {
        return version;
    }

    public int getApiTarget() {
        return apiTarget;
    }

    /**
     * Returns the module manager, which stores instances of all registered {@link
     * Module}s and manages their state.
     *
     * @return The {@link ModuleManager}.
     */
    public ModuleManager getModuleManager() {
        return Prison.get().getModuleManager();
    }

    public ErrorManager getErrorManager() {
        return errorManager;
    }

    /**
     * The package name is used to identify the module on an internal level.
     */
    public String getPackageName() {
        return "Prison" + name;
    }

    public boolean isEnabled() {
        return status.getStatus() == ModuleStatus.Status.ENABLED;
    }

    public void setEnabled(boolean enabled) {
        if (enabled) {
            getStatus().toEnabled();
        } else {
            getStatus().toDisabled();
        }
    }

    public ModuleStatus getStatus() {
        return status;
    }

    /**
     * Returns this module's data folder, where all data can be stored.
     * It is located in the Prison data folder, and has the name of the module.
     * It is automatically generated.
     *
     * @return The {@link File} representing the data folder.
     */
    public File getDataFolder() {
        return dataFolder;
    }

}
