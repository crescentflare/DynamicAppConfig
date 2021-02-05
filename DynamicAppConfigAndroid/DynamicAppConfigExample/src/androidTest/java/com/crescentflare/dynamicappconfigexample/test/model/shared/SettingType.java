package com.crescentflare.dynamicappconfigexample.test.model.shared;

/**
 * Test model shared items: setting type
 * The type of settings which can be changed
 */
public enum SettingType {

    Name("name"),
    ApiURL("apiUrl"),
    RunType("runType"),
    AcceptAllSSL("acceptAllSSL"),
    NetworkTimeoutSeconds("networkTimeoutSec"),
    ConsoleURL("consoleUrl"),
    ConsoleEnabled("consoleEnabled"),
    ConsoleTimeoutSeconds("consoleTimeoutSec"),
    LogLevel("logLevel");

    private final String text;

    SettingType(String text) {
        this.text = text;
    }

    public String toString() {
        return text;
    }
}
