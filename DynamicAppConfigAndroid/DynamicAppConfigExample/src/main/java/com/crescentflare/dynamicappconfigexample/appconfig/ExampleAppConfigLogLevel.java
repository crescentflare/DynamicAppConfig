package com.crescentflare.dynamicappconfigexample.appconfig;

/**
 * App config: application configuration log setting
 * An enum used by the application build configuration
 */
public enum ExampleAppConfigLogLevel {

    LogDisabled("logDisabled"),
    LogNormal("logNormal"),
    LogVerbose("logVerbose");

    private final String text;

    ExampleAppConfigLogLevel(String text) {
        this.text = text;
    }

    public static ExampleAppConfigLogLevel fromString(String text) {
        if (text != null)
            for (ExampleAppConfigLogLevel e : ExampleAppConfigLogLevel.values())
                if (text.equalsIgnoreCase(e.text))
                    return e;
        return LogDisabled;
    }

    public String toString() {
        return text;
    }
}
