package com.crescentflare.dynamicappconfig.model;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

/**
 * Unit test: storage item
 */
public class AppConfigStorageItemTest {

    // --
    // Test cases
    // --

    @Test
    public void testStorage() {
        AppConfigStorageItem item = new AppConfigStorageItem();
        item.putString("string", "test");
        item.putLong("long", -90234);
        item.putInt("int", 512);
        item.putBoolean("boolean", true);
        Assert.assertEquals("test", item.getString("string"));
        Assert.assertEquals(-90234, item.getLong("long"));
        Assert.assertEquals(512, item.getInt("int"));
        Assert.assertTrue(item.getBoolean("boolean"));
    }

    @Test
    public void testConversion() {
        AppConfigStorageItem item = new AppConfigStorageItem();
        item.putString("string", "-1234");
        item.putLong("long", -90234);
        item.putInt("int", 1);
        item.putBoolean("boolean", true);
        Assert.assertEquals(-1234, item.getInt("string"));
        Assert.assertEquals(-90234, item.getInt("long"));
        Assert.assertTrue(item.getBoolean("int"));
        Assert.assertEquals("true", item.getString("boolean"));
    }

    @Test
    public void testValueList() {
        AppConfigStorageItem item = new AppConfigStorageItem();
        item.putString("string", "test");
        item.putString("extraString", "testExtra");
        item.putLong("long", -90234);
        item.putInt("int", 512);
        item.putBoolean("boolean", true);
        item.removeSetting("string");
        String[] expectedValues = { "extraString", "long", "int", "boolean" };
        String[] itemValues = item.valueList().toArray(new String[0]);
        Arrays.sort(expectedValues);
        Arrays.sort(itemValues);
        Assert.assertArrayEquals(expectedValues, itemValues);
    }
}
