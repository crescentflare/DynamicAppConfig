package com.crescentflare.dynamicappconfig.model;


import java.util.ArrayList;
import java.util.HashMap;

/**
 * Library model: storage item
 * A single configuration to be stored within the app config storage class
 */
public class AppConfigStorageItem
{
    // ---
    // Member
    // ---

    private HashMap<String, Object> storedSettings = new HashMap<>();


    // ---
    // Obtain settings
    // ---

    public boolean getBoolean(String setting)
    {
        Object settingObject = storedSettings.get(setting);
        if (settingObject != null)
        {
            if (settingObject instanceof Boolean)
            {
                return (Boolean)settingObject;
            }
            else if (settingObject instanceof Long)
            {
                return ((Long)settingObject) > 0;
            }
            else if (settingObject instanceof String)
            {
                return ((String)settingObject).equalsIgnoreCase("true");
            }
        }
        return false;
    }

    public int getInt(String setting)
    {
        Object settingObject = storedSettings.get(setting);
        if (settingObject != null)
        {
            if (settingObject instanceof Boolean)
            {
                return ((Boolean)settingObject) ? 1 : 0;
            }
            else if (settingObject instanceof Long)
            {
                return ((Long)settingObject).intValue();
            }
            else if (settingObject instanceof String)
            {
                try
                {
                    return Integer.valueOf((String) settingObject);
                }
                catch (Exception ignored)
                {
                }
            }
        }
        return 0;
    }

    public long getLong(String setting)
    {
        Object settingObject = storedSettings.get(setting);
        if (settingObject != null)
        {
            if (settingObject instanceof Boolean)
            {
                return ((Boolean)settingObject) ? 1 : 0;
            }
            else if (settingObject instanceof Long)
            {
                return (Long)settingObject;
            }
            else if (settingObject instanceof String)
            {
                try
                {
                    return Long.valueOf((String)settingObject);
                }
                catch (Exception ignored)
                {
                }
            }
        }
        return 0;
    }

    public String getString(String setting)
    {
        Object settingObject = storedSettings.get(setting);
        if (settingObject != null)
        {
            if (settingObject instanceof Boolean)
            {
                return ((Boolean)settingObject) ? "true" : "false";
            }
            else if (settingObject instanceof Long)
            {
                return "" + ((Long)settingObject);
            }
            else if (settingObject instanceof String)
            {
                return (String)settingObject;
            }
        }
        return null;
    }

    public String getStringNotNull(String setting)
    {
        String result = getString(setting);
        if (result == null)
        {
            result = "";
        }
        return result;
    }

    public Object get(String setting)
    {
        return storedSettings.get(setting);
    }

    public ArrayList<String> valueList()
    {
        ArrayList<String> list = new ArrayList<>();
        for (String key : storedSettings.keySet())
        {
            list.add(key);
        }
        return list;
    }


    // ---
    // Adjust settings
    // ---

    public void putBoolean(String setting, boolean value)
    {
        removeSetting(setting);
        storedSettings.put(setting, value);
    }

    public void putInt(String setting, int value)
    {
        putLong(setting, value);
    }

    public void putLong(String setting, long value)
    {
        removeSetting(setting);
        storedSettings.put(setting, value);
    }

    public void putString(String setting, String value)
    {
        removeSetting(setting);
        storedSettings.put(setting, value);
    }

    public void put(String setting, Object value)
    {
        if (value instanceof Boolean)
        {
            putBoolean(setting, (Boolean)value);
        }
        else if (value instanceof Integer)
        {
            putInt(setting, (Integer)value);
        }
        else if (value instanceof Long)
        {
            putLong(setting, (Long)value);
        }
        else if (value instanceof String)
        {
            putString(setting, (String)value);
        }
    }


    // ---
    // Other operations
    // ---

    public boolean removeSetting(String setting)
    {
        if (storedSettings.containsKey(setting))
        {
            storedSettings.remove(setting);
            return true;
        }
        return false;
    }

    public void copyValues(AppConfigStorageItem item)
    {
        if (item != null)
        {
            ArrayList<String> valueList = item.valueList();
            for (String value : valueList)
            {
                Object setting = item.get(value);
                if (setting instanceof Boolean)
                {
                    putBoolean(value, item.getBoolean(value) ? true : false);
                }
                else if (setting instanceof Integer)
                {
                    putInt(value, 0 + item.getInt(value));
                }
                else if (setting instanceof Long)
                {
                    putLong(value, 0 + item.getLong(value));
                }
                else if (setting instanceof String)
                {
                    putString(value, "" + item.getString(value));
                }
            }
        }
    }


    // ---
    // Comparison
    // ---

    @Override
    public boolean equals(Object o)
    {
        if (o instanceof AppConfigStorageItem)
        {
            // No need to check if the instance is the same
            if (o == this)
            {
                return true;
            }

            // Both should contain the same set of setting keys
            AppConfigStorageItem checkItem = (AppConfigStorageItem)o;
            for (String key : storedSettings.keySet())
            {
                if (!checkItem.storedSettings.containsKey(key))
                {
                    return false;
                }
            }
            for (String key : checkItem.storedSettings.keySet())
            {
                if (!storedSettings.containsKey(key))
                {
                    return false;
                }
            }

            // Values should be the same
            for (String key : storedSettings.keySet())
            {
                Object value1 = storedSettings.get(key);
                Object value2 = checkItem.storedSettings.get(key);
                if (!value1.equals(value2))
                {
                    return false;
                }
            }
            return true;
        }
        return false;
    }
}
