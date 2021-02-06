//
//  ExampleAppConfigLogPlugin.swift
//  DynamicAppConfig Example
//
//  App config: custom plugin for logging
//  This plugin is used to display the log when clicking on it from the app config menu
//

import UIKit
import DynamicAppConfig

class ExampleAppConfigLogPlugin : AppConfigPlugin {
    
    // --
    // MARK: Implementation
    // --
    
    func displayName() -> String {
        return "View log"
    }
    
    func displayValue() -> String? {
        let logString = Logger.logString()
        let logLines = logString.split(separator: "\n")
        return "\(logLines.count) lines"
    }
    
    func canEdit() -> Bool {
        return true
    }
    
    func interact(fromViewController: UIViewController) {
        fromViewController.navigationController?.pushViewController(ShowLogViewController(), animated: true)
    }
    
    func edit(fromViewController: UIViewController) {
        let alert = UIAlertController(title: "Clear log?", message: nil, preferredStyle: .alert)
        alert.addAction(UIAlertAction(title: "Yes", style: .default, handler: { (_) in
            Logger.clear()
            fromViewController.navigationController?.pushViewController(ShowLogViewController(), animated: true)
        }))
        alert.addAction(UIAlertAction(title: "No", style: .cancel, handler: { _ in
            // No implementation
        }))
        fromViewController.present(alert, animated: true, completion: nil)
    }
    
}
