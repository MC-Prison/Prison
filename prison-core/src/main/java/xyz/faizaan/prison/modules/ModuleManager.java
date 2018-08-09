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

package xyz.faizaan.prison.modules;

import xyz.faizaan.prison.Prison;
import xyz.faizaan.prison.PrisonAPI;
import xyz.faizaan.prison.output.Output;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Keeps track of each module and each module's status.
 *
 * @author Faizaan A. Datoo
 * @since API 1.0
 */
public class ModuleManager {

    private List<Module> modules;
    private File moduleRoot;

    public ModuleManager() {
        modules = new ArrayList<>();
        moduleRoot = new File(PrisonAPI.getPluginDirectory(), "module_conf");
        if (!moduleRoot.exists()) {
            moduleRoot.mkdir();
        }
    }

    /**
     * Register a new module.
     */
    public void registerModule(Module module) {
        if (getModule(module.getName()).isPresent()) {
            return; // Already added
        }
        modules.add(module);
        enableModule(module);
    }

    private void validateVersion(Module module) {
        if (module.getApiTarget() == Prison.API_LEVEL) {
            return; // Version matches, no need to continue
        }

        module.getStatus().setMessage("&6Version mismatch (update module)");
        Output.get().logWarn(
            "API level mismatch! " + module.getPackageName() + " is on API " + module.getApiTarget()
                + ", while prison-core is on API " + Prison.API_LEVEL
                + ".\nThis may cause problems.");
    }

    /**
     * Enable an already loaded module.
     *
     * @param module The {@link Module} to enable.
     * @return true if the enable succeeded, false otherwise.
     */
    public boolean enableModule(Module module) {
        long startTime = System.currentTimeMillis();
        Output.get().logInfo("%s enable start...", module.getName());

        module.setEnabled(true);
        module.enable();
        validateVersion(module);

        if (module.getStatus().getStatus() != ModuleStatus.Status.ENABLED) {
            // Anything else and we assume that the enable failed.
            Output.get().logInfo("%s enable &cfailed&f, in %d milliseconds.", module.getName(),
                (System.currentTimeMillis() - startTime));
            return false;
        }

        Output.get().logInfo("%s enable succeeded, in %d milliseconds.", module.getName(),
            (System.currentTimeMillis() - startTime));
        return true;
    }

    /**
     * Unregister a module. This will disable it and then remove it from the list.
     *
     * @param module The {@link Module} to enable.
     */
    public void unregisterModule(Module module) {
        disableModule(module);
        getModule(module.getName()).ifPresent(modules::remove);
    }

    /**
     * Disable an already loaded module.
     *
     * @param module The {@link Module} to disable.
     */
    public void disableModule(Module module) {
        if(!module.isEnabled()) return; // Don't disable enabled modules
        module.disable();
        module.getStatus().toDisabled();
    }

    /**
     * Unregister all modules.
     *
     * @see #unregisterModule(Module)
     */
    public void unregisterAll() {
        modules.forEach(this::disableModule);
        modules.clear();
    }

    /**
     * Returns the {@link Module} with the specified name.
     */
    public Optional<Module> getModule(String name) {
        return modules.stream().filter(module -> module.getName().equalsIgnoreCase(name))
            .findFirst();
    }

    /**
     * Returns the {@link Module} with the specified package name.
     */
    public Optional<Module> getModuleByPackageName(String name) {
        return modules.stream().filter(module -> module.getPackageName().equalsIgnoreCase(name))
            .findFirst();
    }

    /**
     * Returns a list of all modules.
     */
    public List<Module> getModules() {
        return modules;
    }

    public File getModuleRoot() {
        return moduleRoot;
    }

    /**
     * Returns the status of a module (enabled or error message), in the form of a color-coded string.
     * This is meant to show to users.
     *
     * @deprecated Use {@link Module#getStatus()} instead.
     */
    @Deprecated public String getStatus(String moduleName) {
        Optional<Module> moduleOptional = getModule(moduleName);
        return moduleOptional.map(module -> module.getStatus().getMessage()).orElse(null);
    }

    /**
     * Set the status of a module.
     *
     * @param moduleName The name of the module.
     * @param newStatus  The module's status. May include color codes, amp-prefixed.
     * @deprecated Use {@link Module#getStatus()} instead.
     */
    @Deprecated public void setStatus(String moduleName, String newStatus) {
        Optional<Module> moduleOptional = getModule(moduleName);
        if (!moduleOptional.isPresent()) {
            return;
        }
        Module module = moduleOptional.get();

        if (newStatus.toLowerCase().contains("enabled")) {
            module.getStatus().toEnabled();
        } else if (newStatus.toLowerCase().contains("disabled")) {
            module.getStatus().toDisabled();
        } else {
            module.getStatus().toFailed(newStatus);
        }

    }

}
