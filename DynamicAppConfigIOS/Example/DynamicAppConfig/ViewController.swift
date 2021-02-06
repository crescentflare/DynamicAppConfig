//
//  ViewController.swift
//  DynamicAppConfig Example
//
//  A simple screen which launches the app config selection screen
//  It will show the selected values on-screen
//

import UIKit
import DynamicAppConfig

class ViewController: UIViewController {

    // --
    // MARK: View components
    // --
    
    @IBOutlet var changeButton: UIButton?
    @IBOutlet var selectedConfigValue: UILabel?
    @IBOutlet var apiUrlValue: UILabel?
    @IBOutlet var runTypeValue: UILabel?
    @IBOutlet var acceptAllSslValue: UILabel?
    @IBOutlet var networkTimeoutValue: UILabel?
    @IBOutlet var consoleUrlValue: UILabel?
    @IBOutlet var consoleTimeoutValue: UILabel?
    @IBOutlet var enableConsoleValue: UILabel?
    @IBOutlet var logLevelValue: UILabel?

    
    // --
    // MARK: Lifecycle
    // --
    
    override func viewDidLoad() {
        super.viewDidLoad()
        self.updateConfigurationValues()
        AppConfigStorage.shared.addDataObserver(self, selector: #selector(configurationDidUpdate), name: AppConfigStorage.configurationChanged)
        if !AppConfigStorage.shared.isActivated() {
            changeButton?.isHidden = true
        }
    }
    
    deinit {
        AppConfigStorage.shared.removeDataObserver(self, name: AppConfigStorage.configurationChanged)
    }


    // --
    // MARK: Helper
    // --

    @objc func configurationDidUpdate() {
        Logger.log(text: "Configuration changed")
        updateConfigurationValues()
    }
    
    func updateConfigurationValues() {
        // Log settings
        Logger.logVerbose(text: "apiUrl set to: " + ExampleAppConfigManager.currentConfig().apiUrl)
        Logger.logVerbose(text: "runType set to: " + ExampleAppConfigManager.currentConfig().runType.rawValue)
        Logger.logVerbose(text: "acceptAllSsl set to: " + (ExampleAppConfigManager.currentConfig().acceptAllSSL ? "true" : "false"))
        Logger.logVerbose(text: "networkTimeout set to: " + String(ExampleAppConfigManager.currentConfig().networkTimeoutSec))

        // Update UI
        selectedConfigValue?.text = ExampleAppConfigManager.currentConfig().name
        apiUrlValue?.text = ExampleAppConfigManager.currentConfig().apiUrl
        runTypeValue?.text = ExampleAppConfigManager.currentConfig().runType.rawValue
        acceptAllSslValue?.text = ExampleAppConfigManager.currentConfig().acceptAllSSL ? "true" : "false"
        networkTimeoutValue?.text = String(ExampleAppConfigManager.currentConfig().networkTimeoutSec)
        consoleUrlValue?.text = ExampleAppConfigManager.currentConfig().consoleUrl
        consoleTimeoutValue?.text = String(ExampleAppConfigManager.currentConfig().consoleTimeoutSec)
        enableConsoleValue?.text = ExampleAppConfigManager.currentConfig().consoleEnabled ? "true" : "false"
        logLevelValue?.text = ExampleAppConfigManager.currentConfig().logLevel.rawValue
    }
    
    
    // --
    // MARK: Selector
    // --
    
    @IBAction func changeConfiguration() {
        AppConfigManageViewController.launch()
    }
    
}
