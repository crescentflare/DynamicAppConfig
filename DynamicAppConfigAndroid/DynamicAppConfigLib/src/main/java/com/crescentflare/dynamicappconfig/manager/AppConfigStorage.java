package com.crescentflare.dynamicappconfig.manager;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;

import com.crescentflare.dynamicappconfig.model.AppConfigStorageItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Library manager: storage class
 * Contains app config storage and provides the main interface
 */
public class AppConfigStorage
{
    // ---
    // Constants
    // ---

    private static final String PREFERENCE_FILE_NAME = "custom.app.config";
    private static final String SELECTED_PREFIX = "selected.";
    private static final String CUSTOM_PREFIX = "custom.";
    private static final String GLOBAL_PREFIX = "global.";


    // ---
    // Members
    // ---

    public static AppConfigStorage instance = new AppConfigStorage();
    private AppConfigBaseManager configManager = null;
    private LinkedHashMap<String, AppConfigStorageItem> storedConfigs = new LinkedHashMap<>();
    private LinkedHashMap<String, AppConfigStorageItem> customConfigs = new LinkedHashMap<>();
    private AppConfigStorageItem globalConfig = new AppConfigStorageItem();
    private ArrayList<ChangedConfigListener> changedConfigListeners = new ArrayList<>();
    private String loadFromAssetFile = null;
    private String selectedItem = "";
    private boolean customConfigLoaded = false;
    private boolean initialized = false;


    // ---
    // Initialization
    // ---

    private AppConfigStorage()
    {
    }

    public void init(Context context)
    {
        init(context, null);
    }

    public void init(Context context, AppConfigBaseManager manager)
    {
        configManager = manager;
        loadSelectedItemFromPreferences(context);
        loadGlobalConfigFromPreferences(context);
        if (configManager != null)
        {
            configManager.applyCurrentConfig(selectedItem, getSelectedConfigNotNull(), globalConfig);
        }
        initialized = true;
    }

    public boolean isInitialized()
    {
        return initialized;
    }

    public boolean isLoaded()
    {
        return customConfigLoaded;
    }


    // ---
    // Obtain manager instance, only used internally if the given manager is a singleton (which is recommended)
    // ---

    public AppConfigBaseManager getConfigManager()
    {
        return configManager;
    }


    // ---
    // Obtain from storage
    // ---

    public AppConfigStorageItem getConfig(String config)
    {
        if (customConfigs.containsKey(config))
        {
            return customConfigs.get(config);
        }
        if (storedConfigs.containsKey(config))
        {
            return storedConfigs.get(config);
        }
        return null;
    }

    public AppConfigStorageItem getConfigNotNull(String config)
    {
        AppConfigStorageItem item = getConfig(config);
        if (item == null)
        {
            item = new AppConfigStorageItem();
        }
        return item;
    }

    public AppConfigStorageItem getSelectedConfig()
    {
        return getConfig(selectedItem);
    }

    public AppConfigStorageItem getSelectedConfigNotNull()
    {
        return getConfigNotNull(selectedItem);
    }

    public AppConfigStorageItem getGlobalConfig()
    {
        return globalConfig;
    }

    public String getSelectedConfigName()
    {
        return selectedItem;
    }

    public ArrayList<String> configList()
    {
        return nameListOfConfigMap(storedConfigs);
    }

    public ArrayList<String> customConfigList()
    {
        return nameListOfConfigMap(customConfigs);
    }

    private ArrayList<String> nameListOfConfigMap(LinkedHashMap<String, AppConfigStorageItem> map)
    {
        ArrayList<String> list = new ArrayList<>();
        for (String key : map.keySet())
        {
            list.add(key);
        }
        return list;
    }


    // ---
    // Add to storage
    // ---

    public void putConfig(String config, AppConfigStorageItem item)
    {
        removeConfig(config);
        if (item != null)
        {
            storedConfigs.put(config, item);
        }
    }

    public void putCustomConfig(String config, AppConfigStorageItem item)
    {
        if (customConfigs.containsKey(config))
        {
            removeConfig(config);
        }
        if (item != null)
        {
            if (!storedConfigs.containsKey(config) || !storedConfigs.get(config).equals(item))
            {
                customConfigs.put(config, item);
            }
        }
    }


    // ---
    // Other operations
    // ---

    public boolean removeConfig(String config)
    {
        boolean removed = false;
        if (customConfigs.containsKey(config))
        {
            customConfigs.remove(config);
            removed = true;
        }
        else if (storedConfigs.containsKey(config))
        {
            storedConfigs.remove(config);
            removed = true;
        }
        if (removed && config.equals(selectedItem))
        {
            selectedItem = "";
            if (configManager != null)
            {
                configManager.applyCurrentConfig(selectedItem, getSelectedConfigNotNull(), globalConfig);
            }
            for (ChangedConfigListener listener : changedConfigListeners)
            {
                listener.onChangedConfig();
            }
        }
        return removed;
    }

    public void selectConfig(Context context, String config)
    {
        if (customConfigs.containsKey(config))
        {
            selectedItem = config;
        }
        else if (storedConfigs.containsKey(config))
        {
            selectedItem = config;
        }
        else
        {
            selectedItem = "";
        }
        storeSelectedItemInPreferences(context);
        if (configManager != null)
        {
            configManager.applyCurrentConfig(selectedItem, getSelectedConfigNotNull(), globalConfig);
        }
        for (ChangedConfigListener listener : changedConfigListeners)
        {
            listener.onChangedConfig();
        }
    }

    public void updateGlobalConfig(Context context, AppConfigStorageItem item)
    {
        globalConfig = item;
        storeGlobalConfigInPreferences(context);
        if (configManager != null)
        {
            configManager.applyCurrentConfig(selectedItem, getSelectedConfigNotNull(), globalConfig);
        }
        for (ChangedConfigListener listener : changedConfigListeners)
        {
            listener.onChangedConfig();
        }
    }

    public boolean isCustomConfig(String config)
    {
        return customConfigs.containsKey(config) && !storedConfigs.containsKey(config);
    }

    public boolean isConfigOverride(String config)
    {
        return customConfigs.containsKey(config) && storedConfigs.containsKey(config);
    }

    public void clearAllToDefaults(Context context)
    {
        for (String config : customConfigs.keySet())
        {
            removeCustomItemFromPreferences(context, config);
        }
        customConfigs.clear();
        selectedItem = "";
        storeSelectedItemInPreferences(context);
        globalConfig = new AppConfigStorageItem();
        storeGlobalConfigInPreferences(context);
        if (configManager != null)
        {
            configManager.applyCurrentConfig(selectedItem, getSelectedConfigNotNull(), globalConfig);
        }
        for (ChangedConfigListener listener : changedConfigListeners)
        {
            listener.onChangedConfig();
        }
    }

    public void manuallyChangeCurrentConfig(Context context, String key, String value)
    {
        getSelectedConfigNotNull().putString(key, value);
        storeSelectedItemInPreferences(context);
        if (configManager != null)
        {
            configManager.applyCurrentConfig(selectedItem, getSelectedConfigNotNull(), globalConfig);
        }
        for (ChangedConfigListener listener : changedConfigListeners)
        {
            listener.onChangedConfig();
        }
    }

    public void manuallyChangeGlobalConfig(Context context, String key, String value)
    {
        globalConfig.putString(key, value);
        storeGlobalConfigInPreferences(context);
        if (configManager != null)
        {
            configManager.applyCurrentConfig(selectedItem, getSelectedConfigNotNull(), globalConfig);
        }
        for (ChangedConfigListener listener : changedConfigListeners)
        {
            listener.onChangedConfig();
        }
    }


    // ---
    // Loading
    // ---

    public void setLoadingSourceAssetFile(String fileName)
    {
        loadFromAssetFile = fileName;
    }

    public void loadFromSource(final Context context, final Runnable runnable)
    {
        final Handler handler = new Handler();
        final String assetFile = loadFromAssetFile;
        Thread thread = new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                final LinkedHashMap<String, AppConfigStorageItem> loadedConfigs = loadFromSourceInternal(context, assetFile);
                handler.post(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        if (loadedConfigs != null)
                        {
                            storedConfigs = loadedConfigs;
                            loadFromAssetFile = null;
                        }
                        if (!customConfigLoaded)
                        {
                            loadCustomItemsFromPreferences(context);
                            customConfigLoaded = true;
                        }
                        runnable.run();
                    }
                });
            }
        });
        thread.start();
    }

    public void loadFromSourceNotThreaded(Context context)
    {
        final LinkedHashMap<String, AppConfigStorageItem> loadedConfigs = loadFromSourceInternal(context, loadFromAssetFile);
        if (loadedConfigs != null)
        {
            storedConfigs = loadFromSourceInternal(context, loadFromAssetFile);
            loadFromAssetFile = null;
            if (!customConfigLoaded)
            {
                loadCustomItemsFromPreferences(context);
                customConfigLoaded = true;
            }
        }
    }

    private LinkedHashMap<String, AppConfigStorageItem> loadFromSourceInternal(Context context, String assetFile)
    {
        // Return early if specified asset file is not supported or empty
        if (assetFile == null || !assetFile.endsWith(".json"))
        {
            return null;
        }

        // Create default item from model (if it exists)
        AppConfigStorageItem defaultItem = null;
        if (configManager != null)
        {
            ArrayList<String> values = configManager.getBaseModelInstance().configurationValueList();
            if (values.size() > 0)
            {
                defaultItem = new AppConfigStorageItem();
                for (String value : values)
                {
                    if (value.equals("name"))
                    {
                        continue;
                    }
                    Object setting = configManager.getBaseModelInstance().getCurrentValue(value);
                    if (setting instanceof Long)
                    {
                        defaultItem.putLong(value, (Long)setting);
                    }
                    else if (setting instanceof Integer)
                    {
                        defaultItem.putInt(value, (Integer)setting);
                    }
                    else if (setting instanceof Boolean)
                    {
                        defaultItem.putBoolean(value, (Boolean)setting);
                    }
                    else
                    {
                        defaultItem.putString(value, setting.toString());
                    }
                }
            }
        }

        // Prepare input stream for loading
        LinkedHashMap<String, AppConfigStorageItem> loadedConfigs = new LinkedHashMap<>();
        InputStream inputStream = null;
        try
        {
            inputStream = context.getAssets().open(assetFile);
        }
        catch (IOException ignored)
        {
        }

        // Load file
        String result = "{}";
        if (inputStream != null)
        {
            StringBuilder stringBuilder = new StringBuilder();
            try
            {
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                String line = null;
                String ls = System.getProperty("line.separator");
                while ((line = reader.readLine()) != null)
                {
                    stringBuilder.append(line);
                    stringBuilder.append(ls);
                }
            }
            catch (IOException ignored){ }
            result = stringBuilder.toString();
        }

        // Parse JSON
        try
        {
            JSONArray configArray = new JSONArray(result);
            for (int i = 0; i < configArray.length(); i++)
            {
                addStorageItemFromJson(loadedConfigs, configArray.optJSONObject(i), defaultItem);
            }
        }
        catch (JSONException ignored)
        {
        }
        return loadedConfigs;
    }

    private void addStorageItemFromJson(HashMap<String, AppConfigStorageItem> loadedConfigs, JSONObject json, AppConfigStorageItem parent)
    {
        // Add item
        AppConfigStorageItem addItem = new AppConfigStorageItem();
        Iterator<String> iterator = json.keys();
        addItem.copyValues(parent);
        while (iterator.hasNext())
        {
            String key = iterator.next();
            if (!key.equals("subConfigs") && !key.equals("name") && !json.isNull(key))
            {
                addItem.put(key, json.opt(key));
            }
        }
        if (json.has("name"))
        {
            loadedConfigs.put(json.optString("name"), addItem);
        }

        // Find sub configurations with overrided values
        JSONArray subConfigs = json.optJSONArray("subConfigs");
        if (subConfigs != null)
        {
            for (int i = 0; i < subConfigs.length(); i++)
            {
                addStorageItemFromJson(loadedConfigs, subConfigs.optJSONObject(i), addItem);
            }
        }
    }


    // ---
    // Preferences handling
    // ---

    public void synchronizeCustomConfigWithPreferences(Context context, String config)
    {
        if (customConfigs.containsKey(config))
        {
            storeCustomItemInPreferences(context, config);
        }
        else
        {
            removeCustomItemFromPreferences(context, config);
        }
    }

    private void loadSelectedItemFromPreferences(Context context)
    {
        String preferencesFileName = context.getPackageName() + "." + PREFERENCE_FILE_NAME;
        SharedPreferences preferences = context.getSharedPreferences(preferencesFileName, Context.MODE_PRIVATE);
        Map<String, ?> objectMap = preferences.getAll();
        AppConfigStorageItem selectedItem = new AppConfigStorageItem();
        String configurationName = "";
        for (String key : objectMap.keySet())
        {
            if (key.startsWith(SELECTED_PREFIX))
            {
                key = key.substring(SELECTED_PREFIX.length());
                if (key.equals("name"))
                {
                    configurationName = (String)objectMap.get(SELECTED_PREFIX + key);
                }
                else
                {
                    selectedItem.put(key, objectMap.get(SELECTED_PREFIX + key));
                }
            }
        }
        if (configurationName.length() > 0)
        {
            putConfig(configurationName, selectedItem);
            this.selectedItem = configurationName;
        }
    }

    private void storeSelectedItemInPreferences(Context context)
    {
        String preferencesFileName = context.getPackageName() + "." + PREFERENCE_FILE_NAME;
        SharedPreferences preferences = context.getSharedPreferences(preferencesFileName, Context.MODE_PRIVATE);
        Map<String, ?> objectMap = preferences.getAll();
        SharedPreferences.Editor editor = preferences.edit();
        for (String key : objectMap.keySet())
        {
            if (key.startsWith(SELECTED_PREFIX))
            {
                editor.remove(key);
            }
        }
        if (selectedItem.length() > 0)
        {
            AppConfigStorageItem item = getSelectedConfigNotNull();
            ArrayList<String> valueList = item.valueList();
            editor.putString(SELECTED_PREFIX + "name", selectedItem);
            for (String key : valueList)
            {
                Object itemValue = item.get(key);
                if (itemValue instanceof Boolean)
                {
                    editor.putBoolean(SELECTED_PREFIX + key, item.getBoolean(key));
                }
                else if (itemValue instanceof Integer)
                {
                    editor.putInt(SELECTED_PREFIX + key, item.getInt(key));
                }
                else if (itemValue instanceof Long)
                {
                    editor.putLong(SELECTED_PREFIX + key, item.getLong(key));
                }
                else
                {
                    editor.putString(SELECTED_PREFIX + key, item.getStringNotNull(key));
                }
            }
        }
        editor.apply();
    }

    private void loadCustomItemsFromPreferences(Context context)
    {
        LinkedHashMap<String, AppConfigStorageItem> loadedConfigs = new LinkedHashMap<>();
        String preferencesFileName = context.getPackageName() + "." + PREFERENCE_FILE_NAME;
        SharedPreferences preferences = context.getSharedPreferences(preferencesFileName, Context.MODE_PRIVATE);
        Map<String, ?> objectMap = preferences.getAll();
        customConfigs.clear();
        for (String key : objectMap.keySet())
        {
            if (key.startsWith(CUSTOM_PREFIX))
            {
                String customKey = key.substring(CUSTOM_PREFIX.length());
                int dotPos = customKey.indexOf('.');
                if (dotPos >= 0)
                {
                    // Add config if not already in
                    String configName = customKey.substring(0, dotPos);
                    String keyName = customKey.substring(dotPos + 1);
                    if (!loadedConfigs.containsKey(configName))
                    {
                        loadedConfigs.put(configName, new AppConfigStorageItem());
                    }

                    // Add value
                    AppConfigStorageItem item = loadedConfigs.get(configName);
                    Object addObject = objectMap.get(key);
                    if (addObject instanceof Boolean)
                    {
                        item.putBoolean(keyName, (Boolean)objectMap.get(key));
                    }
                    else if (addObject instanceof Integer)
                    {
                        item.putInt(keyName, (Integer) objectMap.get(key));
                    }
                    else if (addObject instanceof Long)
                    {
                        item.putLong(keyName, (Long) objectMap.get(key));
                    }
                    else if (addObject instanceof String)
                    {
                        item.putString(keyName, (String)objectMap.get(key));
                    }
                }
            }
        }
        customConfigs = loadedConfigs;
        if (selectedItem.length() > 0)
        {
            selectConfig(context, selectedItem);
        }
    }

    private void removeCustomItemFromPreferences(Context context, String config)
    {
        String preferencesFileName = context.getPackageName() + "." + PREFERENCE_FILE_NAME;
        SharedPreferences preferences = context.getSharedPreferences(preferencesFileName, Context.MODE_PRIVATE);
        Map<String, ?> objectMap = preferences.getAll();
        SharedPreferences.Editor editor = preferences.edit();
        for (String key : objectMap.keySet())
        {
            if (key.startsWith(CUSTOM_PREFIX + config + "."))
            {
                editor.remove(key);
            }
        }
        editor.apply();
    }

    private void storeCustomItemInPreferences(Context context, String config)
    {
        if (customConfigs.containsKey(config))
        {
            // First remove existing item
            String preferencesFileName = context.getPackageName() + "." + PREFERENCE_FILE_NAME;
            SharedPreferences preferences = context.getSharedPreferences(preferencesFileName, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            removeCustomItemFromPreferences(context, config);

            // Add new one
            AppConfigStorageItem item = customConfigs.get(config);
            ArrayList<String> valueList = item.valueList();
            for (String key : valueList)
            {
                Object itemValue = item.get(key);
                if (itemValue instanceof Boolean)
                {
                    editor.putBoolean(CUSTOM_PREFIX + config + "." + key, item.getBoolean(key));
                }
                else if (itemValue instanceof Integer)
                {
                    editor.putInt(CUSTOM_PREFIX + config + "." + key, item.getInt(key));
                }
                else if (itemValue instanceof Long)
                {
                    editor.putLong(CUSTOM_PREFIX + config + "." + key, item.getLong(key));
                }
                else
                {
                    editor.putString(CUSTOM_PREFIX + config + "." + key, item.getStringNotNull(key));
                }
            }
            editor.apply();
        }
    }

    private void loadGlobalConfigFromPreferences(Context context)
    {
        String preferencesFileName = context.getPackageName() + "." + PREFERENCE_FILE_NAME;
        SharedPreferences preferences = context.getSharedPreferences(preferencesFileName, Context.MODE_PRIVATE);
        Map<String, ?> objectMap = preferences.getAll();
        AppConfigStorageItem globalConfig = new AppConfigStorageItem();
        for (String key : objectMap.keySet())
        {
            if (key.startsWith(GLOBAL_PREFIX))
            {
                key = key.substring(GLOBAL_PREFIX.length());
                globalConfig.put(key, objectMap.get(GLOBAL_PREFIX + key));
            }
        }
        this.globalConfig = globalConfig;
    }

    private void storeGlobalConfigInPreferences(Context context)
    {
        String preferencesFileName = context.getPackageName() + "." + PREFERENCE_FILE_NAME;
        SharedPreferences preferences = context.getSharedPreferences(preferencesFileName, Context.MODE_PRIVATE);
        Map<String, ?> objectMap = preferences.getAll();
        SharedPreferences.Editor editor = preferences.edit();
        for (String key : objectMap.keySet())
        {
            if (key.startsWith(GLOBAL_PREFIX))
            {
                editor.remove(key);
            }
        }
        ArrayList<String> valueList = globalConfig.valueList();
        for (String key : valueList)
        {
            Object itemValue = globalConfig.get(key);
            if (itemValue instanceof Boolean)
            {
                editor.putBoolean(GLOBAL_PREFIX + key, globalConfig.getBoolean(key));
            }
            else if (itemValue instanceof Integer)
            {
                editor.putInt(GLOBAL_PREFIX + key, globalConfig.getInt(key));
            }
            else if (itemValue instanceof Long)
            {
                editor.putLong(GLOBAL_PREFIX + key, globalConfig.getLong(key));
            }
            else
            {
                editor.putString(GLOBAL_PREFIX + key, globalConfig.getStringNotNull(key));
            }
        }
        editor.apply();
    }


    // ---
    // Manager listeners
    // ---

    public void addChangedConfigListener(ChangedConfigListener listener)
    {
        if (!changedConfigListeners.contains(listener))
        {
            changedConfigListeners.add(listener);
        }
    }

    public void removeChangedConfigListener(ChangedConfigListener listener)
    {
        if (changedConfigListeners.contains(listener))
        {
            changedConfigListeners.remove(listener);
        }
    }


    // ---
    // Listener for current selection changes
    // ---

    public interface ChangedConfigListener
    {
        void onChangedConfig();
    }
}
