package com.crescentflare.dynamicappconfig.manager;


import com.crescentflare.dynamicappconfig.model.AppConfigBaseModel;
import com.crescentflare.dynamicappconfig.model.AppConfigStorageItem;
import com.crescentflare.dynamicappconfig.plugin.AppConfigPlugin;

import java.util.ArrayList;
import java.util.List;

/**
 * Library manager: base manager for app customization
 * Derive your custom config manager from this class for integration
 */
public class AppConfigBaseManager
{
    // ---
    // Members
    // ---

    AppConfigBaseModel currentConfig = null;
    List<AppConfigPlugin> plugins = new ArrayList<>();


    // ---
    // Return an instance of the app base model, override this function to return a specific model class for the app
    // ---

    public AppConfigBaseModel getBaseModelInstance()
    {
        return new AppConfigBaseModel();
    }


    // ---
    // Used by the derived manager to return the selected configuration instance, typecasted to the specific model class for the app
    // ---

    protected AppConfigBaseModel getCurrentConfigInstance()
    {
        if (currentConfig == null)
        {
            return getBaseModelInstance();
        }
        return currentConfig;
    }


    // ---
    // Used internally to convert the config item to the model
    // ---

    public void applyCurrentConfig(String configName, AppConfigStorageItem item, AppConfigStorageItem globalItem)
    {
        currentConfig = getBaseModelInstance();
        if (item != null)
        {
            currentConfig.applyCustomSettings(configName, item);
            currentConfig.applyCustomSettings(configName, globalItem);
        }
    }


    // --
    // Plugins
    // --

    public List<AppConfigPlugin> getPlugins()
    {
        return plugins;
    }

    public void addPlugin(AppConfigPlugin plugin)
    {
        plugins.add(plugin);
    }

    public void setPlugins(List<AppConfigPlugin> plugins)
    {
        this.plugins = plugins;
    }

    public void removeAllPlugins()
    {
        plugins = new ArrayList<>();
    }
}
