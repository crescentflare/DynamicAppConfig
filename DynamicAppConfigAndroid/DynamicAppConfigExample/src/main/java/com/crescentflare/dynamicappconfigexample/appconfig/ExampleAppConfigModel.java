package com.crescentflare.dynamicappconfigexample.appconfig;


import com.crescentflare.dynamicappconfig.model.AppConfigBaseModel;
import com.crescentflare.dynamicappconfig.model.AppConfigModelCategory;
import com.crescentflare.dynamicappconfig.model.AppConfigModelGlobal;
import com.crescentflare.dynamicappconfig.model.AppConfigModelSort;

/**
 * App config: application configuration
 * A convenience model used by the build environment selector and has strict typing
 * Important: this model should always reflect a production situation
 */
public class ExampleAppConfigModel extends AppConfigBaseModel {

    // --
    // Configuration settings
    // --

    private String name = "Production";

    @AppConfigModelSort(0)
    @AppConfigModelCategory("API related")
    private String apiUrl = "https://production.example.com/";

    @AppConfigModelSort(1)
    @AppConfigModelCategory("API related")
    private int networkTimeoutSec = 20;

    @AppConfigModelSort(2)
    @AppConfigModelCategory("API related")
    private boolean acceptAllSSL = false;

    @AppConfigModelSort(3)
    private ExampleAppConfigRunType runType = ExampleAppConfigRunType.RunNormally;


    // --
    // Global settings
    // --

    @AppConfigModelGlobal
    @AppConfigModelSort(0)
    @AppConfigModelCategory("Console related")
    private String consoleUrl = "http://127.0.0.1:4000/";

    @AppConfigModelGlobal
    @AppConfigModelSort(1)
    @AppConfigModelCategory("Console related")
    private int consoleTimeoutSec = 10;

    @AppConfigModelGlobal
    @AppConfigModelSort(2)
    @AppConfigModelCategory("Console related")
    private boolean consoleEnabled = false;

    @AppConfigModelGlobal
    @AppConfigModelSort(3)
    private ExampleAppConfigLogLevel logLevel = ExampleAppConfigLogLevel.LogDisabled;


    // --
    // Generated code
    // --

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getApiUrl() {
        return apiUrl;
    }

    public void setApiUrl(String apiUrl) {
        this.apiUrl = apiUrl;
    }

    public ExampleAppConfigRunType getRunType() {
        return runType;
    }

    public void setRunType(ExampleAppConfigRunType runType) {
        this.runType = runType;
    }

    public boolean isAcceptAllSSL() {
        return acceptAllSSL;
    }

    public void setAcceptAllSSL(boolean acceptAllSSL) {
        this.acceptAllSSL = acceptAllSSL;
    }

    public int getNetworkTimeoutSec() {
        return networkTimeoutSec;
    }

    public void setNetworkTimeoutSec(int networkTimeoutSec) {
        this.networkTimeoutSec = networkTimeoutSec;
    }

    public String getConsoleUrl() {
        return consoleUrl;
    }

    public void setConsoleUrl(String consoleUrl) {
        this.consoleUrl = consoleUrl;
    }

    public int getConsoleTimeoutSec() {
        return consoleTimeoutSec;
    }

    public void setConsoleTimeoutSec(int consoleTimeoutSec) {
        this.consoleTimeoutSec = consoleTimeoutSec;
    }

    public boolean isConsoleEnabled() {
        return consoleEnabled;
    }

    public void setConsoleEnabled(boolean consoleEnabled) {
        this.consoleEnabled = consoleEnabled;
    }

    public ExampleAppConfigLogLevel getLogLevel() {
        return logLevel;
    }

    public void setLogLevel(ExampleAppConfigLogLevel logLevel) {
        this.logLevel = logLevel;
    }
}
