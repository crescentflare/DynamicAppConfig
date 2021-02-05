//
//  AppConfigManageViewController.swift
//  DynamicAppConfig Pod
//
//  Library view controller: edit configuration
//  Be able to select, add and edit app configurations
//

import UIKit

extension UIViewController {
    
    static var ac_topmostViewController: UIViewController? {
        return UIApplication.shared.keyWindow?.ac_topmostViewController
    }
    
    @objc var ac_topmostViewController: UIViewController? {
        return presentedViewController?.ac_topmostViewController ?? self
    }

}

extension UINavigationController {
    
    override var ac_topmostViewController: UIViewController? {
        return visibleViewController?.ac_topmostViewController
    }

}

extension UITabBarController {
    
    override var ac_topmostViewController: UIViewController? {
        return selectedViewController?.ac_topmostViewController
    }

}

extension UIWindow {
    
    var ac_topmostViewController: UIViewController? {
        return rootViewController?.ac_topmostViewController
    }

}

public class AppConfigManageViewController : UIViewController, AppConfigManageTableDelegate {
    
    // --
    // MARK: Members
    // --
    
    static var isOpenCounter = 0
    var isPresentedController = false
    var isLoaded = false
    var manageConfigTable = AppConfigManageTable()

    
    // --
    // MARK: Launching
    // --
    
    public static func launch(presentationStyle: UIModalPresentationStyle = .fullScreen) {
        if AppConfigManageViewController.isOpenCounter == 0 && AppConfigStorage.shared.isActivated() {
            let viewController = AppConfigManageViewController()
            let navigationController = UINavigationController.init(rootViewController: viewController)
            navigationController.modalPresentationStyle = presentationStyle
            ac_topmostViewController?.present(navigationController, animated: true, completion: nil)
        }
    }

    
    // --
    // MARK: Lifecycle
    // --
    
    public init() {
        super.init(nibName: nil, bundle: nil)
        AppConfigManageViewController.isOpenCounter += 1
    }
    
    public required init?(coder aDecoder: NSCoder) {
        super.init(coder: aDecoder)
        AppConfigManageViewController.isOpenCounter += 1
    }
    
    public override func viewDidLoad() {
        // Set title
        super.viewDidLoad()
        navigationItem.title = AppConfigBundle.localizedString(key: "CFLAC_MANAGE_TITLE")
        navigationController?.navigationBar.isTranslucent = false
        
        // Add button to close the configuration selection
        if navigationController != nil {
            // Obtain colors
            let tintColor = view.tintColor
            let highlightColor = tintColor?.withAlphaComponent(0.25)
            
            // Create button
            let doneButton = UIButton()
            doneButton.titleLabel?.font = UIFont.systemFont(ofSize: 15)
            doneButton.setTitle(AppConfigBundle.localizedString(key: "CFLAC_SHARED_DONE"), for: UIControl.State())
            doneButton.setTitleColor(tintColor, for: UIControl.State())
            doneButton.setTitleColor(highlightColor, for: UIControl.State.highlighted)
            let size = doneButton.sizeThatFits(CGSize.zero)
            doneButton.frame = CGRect(x: 0, y: 0, width: size.width, height: size.height)
            doneButton.addTarget(self, action: #selector(doneButtonPressed), for: UIControl.Event.touchUpInside)
            
            // Wrap in bar button item
            let doneButtonWrapper = UIBarButtonItem.init(customView: doneButton)
            navigationItem.leftBarButtonItem = doneButtonWrapper
        }
        
        // Update configuration list
        AppConfigStorage.shared.loadFromSource(completion: {
            self.isLoaded = true
            self.manageConfigTable.setConfigurations(AppConfigStorage.shared.obtainConfigList(), customConfigurations: AppConfigStorage.shared.obtainCustomConfigList(), lastSelected: AppConfigStorage.shared.selectedConfig())
        })
    }
    
    public override func loadView() {
        isPresentedController = navigationController?.isBeingPresented ?? isBeingPresented
        manageConfigTable.delegate = self
        view = manageConfigTable
    }
    
    public override func viewDidAppear(_ animated: Bool) {
        if isLoaded {
            self.manageConfigTable.setConfigurations(AppConfigStorage.shared.obtainConfigList(), customConfigurations: AppConfigStorage.shared.obtainCustomConfigList(), lastSelected: AppConfigStorage.shared.selectedConfig())
        }
    }
    
    deinit {
        if AppConfigManageViewController.isOpenCounter > 0 {
            AppConfigManageViewController.isOpenCounter -= 1
        }
    }
    
    
    // --
    // MARK: Selectors
    // --
    
    @objc func doneButtonPressed(_ sender: UIButton) {
        AppConfigStorage.shared.updateGlobalConfig(settings: self.manageConfigTable.obtainNewGlobalSettings())
        if isPresentedController {
            dismiss(animated: true, completion: nil)
        } else {
            _ = navigationController?.popViewController(animated: true)
        }
    }
    

    // --
    // MARK: AppConfigManageTableDelegate
    // --
    
    func selectedConfig(configName: String) {
        AppConfigStorage.shared.updateGlobalConfig(settings: self.manageConfigTable.obtainNewGlobalSettings())
        AppConfigStorage.shared.selectConfig(configName: configName)
        if isLoaded {
            self.manageConfigTable.setConfigurations(AppConfigStorage.shared.obtainConfigList(), customConfigurations: AppConfigStorage.shared.obtainCustomConfigList(), lastSelected: AppConfigStorage.shared.selectedConfig())
        }
    }
    
    func editConfig(configName: String) {
        AppConfigStorage.shared.updateGlobalConfig(settings: self.manageConfigTable.obtainNewGlobalSettings())
        let viewController = AppConfigEditViewController(configName: configName, newConfig: false)
        navigationController?.pushViewController(viewController, animated: true)
    }
    
    func newCustomConfigFrom(configName: String) {
        AppConfigStorage.shared.updateGlobalConfig(settings: self.manageConfigTable.obtainNewGlobalSettings())
        let viewController = AppConfigEditViewController(configName: configName, newConfig: true)
        navigationController?.pushViewController(viewController, animated: true)
    }
    
    func interactWithPlugin(plugin: AppConfigPlugin) {
        plugin.interact(fromViewController: self)
    }
    
    func editPlugin(plugin: AppConfigPlugin) {
        plugin.edit(fromViewController: self)
    }
    
}
