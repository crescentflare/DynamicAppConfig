//
//  AppDelegate.swift
//  DynamicAppConfig Example
//
//  The application delegate, handling global events while the app is running
//

import UIKit
import DynamicAppConfig

class AppDelegate: UIResponder, UIApplicationDelegate {

    // --
    // MARK: Members
    // --
    
    var window: UIWindow?

    
    // --
    // MARK: Lifecycle callbacks
    // --
    
    func application(_ application: UIApplication, didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey: Any]?) -> Bool {
        // Initialize library if needed
        #if RELEASE
            // Disable library
        #else
            // Parse arguments
            let testArguments = TestArguments()
            testArguments.parseArgumentsArray(arguments: ProcessInfo.processInfo.arguments)
        
            // Clear all configuration data if specified (by the UI tests)
            if testArguments.clearConfig {
                AppConfigStorage.shared.clearAllToDefaults()
            }

            // Start app config
            ExampleAppConfigManager.shared.addPlugin(ExampleAppConfigLogPlugin())
            AppConfigStorage.shared.activate(manager: ExampleAppConfigManager.shared)
            AppConfigStorage.shared.setLoadingSourceAsset(filePath: Bundle.main.path(forResource: "AppConfig", ofType: "plist"))
        
            // Pre-select config if specified (by the UI tests, force loading is needed)
            if let selectConfig = testArguments.selectConfig {
                AppConfigStorage.shared.loadFromSourceSync()
                AppConfigStorage.shared.selectConfig(configName: selectConfig)
            }
        
            // Manually adjust settings if specified (by the UI tests)
            for (key, value) in testArguments.changeCurrentSettings {
                AppConfigStorage.shared.manuallyChangeCurrentConfig(key: key, value: value)
            }
            for (key, value) in testArguments.changeGlobalSettings {
                AppConfigStorage.shared.manuallyChangeGlobalConfig(key: key, value: value)
            }
        #endif
        
        // Prepare logger
        Logger.clear()
        Logger.log(text: "Application started")
        return true
    }
    
    func applicationWillResignActive(_ application: UIApplication) {
    }

    func applicationDidEnterBackground(_ application: UIApplication) {
        Logger.log(text: "Application suspended to the background")
    }

    func applicationWillEnterForeground(_ application: UIApplication) {
    }

    func applicationDidBecomeActive(_ application: UIApplication) {
        Logger.log(text: "Application resumed")
    }

    func applicationWillTerminate(_ application: UIApplication) {
    }

}

