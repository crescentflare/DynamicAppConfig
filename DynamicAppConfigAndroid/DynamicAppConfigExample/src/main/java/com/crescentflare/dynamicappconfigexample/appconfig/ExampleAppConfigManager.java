package com.crescentflare.dynamicappconfigexample.appconfig;


import com.crescentflare.dynamicappconfig.manager.AppConfigBaseManager;
import com.crescentflare.dynamicappconfig.model.AppConfigBaseModel;

/**
 * App config: manager class to facilitate app config selection and model definition
 */
public class ExampleAppConfigManager extends AppConfigBaseManager {

    // --
    // Members
    // --

    public static ExampleAppConfigManager instance = new ExampleAppConfigManager();


    // --
    // Return an instance of the app base model
    // --

    @Override
    public AppConfigBaseModel getBaseModelInstance() {
        return new ExampleAppConfigModel();
    }


    // --
    // Return the current selected config, cast to the custom model
    // --

    public static ExampleAppConfigModel currentConfig() {
        return (ExampleAppConfigModel)instance.getCurrentConfigInstance();
    }
}
