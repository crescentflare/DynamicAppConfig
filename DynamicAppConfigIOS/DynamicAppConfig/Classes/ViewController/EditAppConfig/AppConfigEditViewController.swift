//
//  AppConfigEditViewController.swift
//  DynamicAppConfig Pod
//
//  Library view controller: edit configuration
//  Change values of a new or existing configuration
//

import UIKit

class AppConfigEditViewController : UIViewController, AppConfigEditTableDelegate {
    
    // --
    // MARK: Members
    // --
    
    var newConfig = false
    var configName = ""
    var saveBarButton: UIBarButtonItem?
    let editConfigTable = AppConfigEditTable()

    
    // --
    // MARK: Lifecycle
    // --
    
    init(configName: String, newConfig: Bool) {
        super.init(nibName: nil, bundle: nil)
        self.newConfig = newConfig
        self.configName = configName
        editConfigTable.newConfig = newConfig
        editConfigTable.configName = configName
    }
    
    required init?(coder aDecoder: NSCoder) {
        super.init(coder: aDecoder)
    }
    
    override func viewDidLoad() {
        // Set title
        super.viewDidLoad()
        navigationItem.title = AppConfigBundle.localizedString(key: newConfig ? "CFLAC_EDIT_NEW_TITLE" : "CFLAC_EDIT_TITLE")
        navigationController?.navigationBar.isTranslucent = false
        
        // Add button to close the configuration selection
        if navigationController != nil {
            // Obtain colors
            let tintColor = view.tintColor
            let highlightColor = tintColor?.withAlphaComponent(0.25)
            
            // Add cancel button
            let cancelButton = UIButton()
            cancelButton.titleLabel?.font = UIFont.systemFont(ofSize: 15)
            cancelButton.setTitle(AppConfigBundle.localizedString(key: "CFLAC_SHARED_CANCEL"), for: UIControl.State())
            cancelButton.setTitleColor(tintColor, for: UIControl.State())
            cancelButton.setTitleColor(highlightColor, for: UIControl.State.highlighted)
            cancelButton.setTitleColor(highlightColor, for: UIControl.State.disabled)
            let cancelButtonSize = cancelButton.sizeThatFits(CGSize.zero)
            cancelButton.frame = CGRect(x: 0, y: 0, width: cancelButtonSize.width, height: cancelButtonSize.height)
            cancelButton.addTarget(self, action: #selector(cancelButtonPressed), for: UIControl.Event.touchUpInside)
            
            // Wrap in bar button item
            let cancelButtonWrapper = UIBarButtonItem.init(customView: cancelButton)
            navigationItem.leftBarButtonItem = cancelButtonWrapper

            // Create button
            let saveButton = UIButton()
            saveButton.titleLabel?.font = UIFont.systemFont(ofSize: 15)
            saveButton.setTitle(AppConfigBundle.localizedString(key: "CFLAC_SHARED_SAVE"), for: UIControl.State())
            saveButton.setTitleColor(tintColor, for: UIControl.State())
            saveButton.setTitleColor(highlightColor, for: UIControl.State.highlighted)
            saveButton.setTitleColor(highlightColor, for: UIControl.State.disabled)
            let saveButtonSize = saveButton.sizeThatFits(CGSize.zero)
            saveButton.frame = CGRect(x: 0, y: 0, width: saveButtonSize.width, height: saveButtonSize.height)
            saveButton.addTarget(self, action: #selector(saveButtonPressed), for: UIControl.Event.touchUpInside)
            
            // Wrap in bar button item
            let saveButtonWrapper = UIBarButtonItem.init(customView: saveButton)
            navigationItem.rightBarButtonItem = saveButtonWrapper
            saveBarButton = saveButtonWrapper
            saveBarButton?.isEnabled = newConfig
        }
        
        // Update configuration list
        AppConfigStorage.shared.loadFromSource(completion: {
            self.editConfigTable.setConfiguration(settings: self.obtainSettings(), model: AppConfigStorage.shared.configManager()?.obtainBaseModelInstance())
        })
    }
    
    override func loadView() {
        editConfigTable.delegate = self
        view = editConfigTable
    }
    
    
    // --
    // MARK: Helpers
    // --
    
    func obtainSettings() -> [String: Any] {
        var settings = AppConfigStorage.shared.configSettings(config: configName) ?? [:]
        if newConfig {
            settings["name"] = "\(configName) \(AppConfigBundle.localizedString(key: "CFLAC_EDIT_COPY_SUFFIX"))"
        }
        return settings
    }
    
    func settingsEqual(currentSettings: [String: Any], newSettings: [String: Any]) -> Bool {
        for (key, value) in currentSettings {
            if let compareValue = newSettings[key] {
                var isEqual = false
                if value is String {
                    isEqual = value as? String == compareValue as? String
                } else if value is Int {
                    isEqual = value as? Int == compareValue as? Int
                } else if value is Float {
                    isEqual = value as? Float == compareValue as? Float
                } else if value is Double {
                    isEqual = value as? Double == compareValue as? Double
                } else if value is Bool {
                    isEqual = value as? Bool == compareValue as? Bool
                }
                if !isEqual {
                    return false
                }
            } else {
                return false
            }
        }
        return true
    }
    

    // --
    // MARK: Selectors
    // --
    
    @objc func cancelButtonPressed(_ sender: UIButton) {
        if newConfig || !settingsEqual(currentSettings: obtainSettings(), newSettings: editConfigTable.obtainNewConfigurationSettings()) {
            let alertBox = UIAlertController(title: AppConfigBundle.localizedString(key: "CFLAG_EDIT_CONFIRM_SAVE_CHANGES"), message: nil, preferredStyle: .alert)
            alertBox.addAction(UIAlertAction(title: AppConfigBundle.localizedString(key: "CFLAC_SHARED_CONFIRM"), style: .default, handler: { alertAction in
                self.saveConfig(newSettings: self.editConfigTable.obtainNewConfigurationSettings())
            }))
            alertBox.addAction(UIAlertAction(title: AppConfigBundle.localizedString(key: "CFLAC_SHARED_DENY"), style: .cancel, handler: { alertAction in
                self.cancelEditing()
            }))
            present(alertBox, animated: true, completion: nil)
        } else {
            cancelEditing()
        }
    }
    
    @objc func saveButtonPressed(_ sender: UIButton) {
        saveConfig(newSettings: editConfigTable.obtainNewConfigurationSettings())
    }
    

    // --
    // MARK: AppConfigEditTableDelegate
    // --
    
    func saveConfig(newSettings: [String: Any]) {
        let wasSelected = configName == AppConfigStorage.shared.selectedConfig()
        if AppConfigStorage.shared.isCustomConfig(config: configName) || AppConfigStorage.shared.isConfigOverride(config: configName) {
            AppConfigStorage.shared.removeConfig(config: configName)
        }
        var storeSettings = newSettings
        var newName = storeSettings["name"] as? String ?? ""
        if newName.trimmingCharacters(in: CharacterSet.whitespaces).count == 0 {
            newName = AppConfigBundle.localizedString(key: "CFLAC_EDIT_COPY_NONAME")
            storeSettings["name"] = newName
        }
        AppConfigStorage.shared.putCustomConfig(settings: storeSettings, forConfig: newName)
        if wasSelected {
            AppConfigStorage.shared.selectConfig(configName: newName)
        }
        AppConfigStorage.shared.synchronizeCustomConfigsWithUserDefaults()
        _ = navigationController?.popViewController(animated: true)
    }
    
    func cancelEditing() {
        _ = navigationController?.popViewController(animated: true)
    }
    
    func revertConfig() {
        let wasSelected = configName == AppConfigStorage.shared.selectedConfig()
        if AppConfigStorage.shared.isCustomConfig(config: configName) || AppConfigStorage.shared.isConfigOverride(config: configName) {
            AppConfigStorage.shared.removeConfig(config: configName)
            AppConfigStorage.shared.synchronizeCustomConfigsWithUserDefaults()
        }
        if wasSelected {
            AppConfigStorage.shared.selectConfig(configName: configName)
        }
        _ = navigationController?.popViewController(animated: true)
    }
    
    func configChanged(newSettings: [String : Any]) {
        if !newConfig {
            saveBarButton?.isEnabled = !settingsEqual(currentSettings: obtainSettings(), newSettings: newSettings)
        }
    }
    
}
