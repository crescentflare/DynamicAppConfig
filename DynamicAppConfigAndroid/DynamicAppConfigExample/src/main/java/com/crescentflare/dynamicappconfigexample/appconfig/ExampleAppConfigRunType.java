package com.crescentflare.dynamicappconfigexample.appconfig;

/**
 * App config: application configuration run setting
 * An enum used by the application build configuration
 */
public enum ExampleAppConfigRunType
{
    RunNormally("runNormally"),
    RunQuickly("runQuickly"),
    RunStrictly("runStrictly");

    private String text;

    ExampleAppConfigRunType(String text)
    {
        this.text = text;
    }

    public static ExampleAppConfigRunType fromString(String text)
    {
        if (text != null)
            for (ExampleAppConfigRunType e : ExampleAppConfigRunType.values())
                if (text.equalsIgnoreCase(e.text))
                    return e;
        return RunNormally;
    }

    public String toString()
    {
        return text;
    }
}
