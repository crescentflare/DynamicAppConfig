//
//  AppConfigPlugin.swift
//  DynamicAppConfig Pod
//
//  Library plugin: a protocol to define a custom plugin
//  Plugins can be made to manage custom values or add custom interaction to the selection menu
//  For example: launching a custom view controller with development tools
//

public protocol AppConfigPlugin: class {
    
    // The name of the plugin, how it should be displayed in the selection menu
    func displayName() -> String
    
    // A value to display for the plugin, can be empty or nil if not applicable
    func displayValue() -> String?
    
    // Return true if the plugin can be edited
    func canEdit() -> Bool
    
    // Called when the plugin is tapped in the menu, can be used to launch other screens or adjust the value
    func interact(fromViewController: UIViewController)
    
    // Called when the plugin cell's edit action is triggered, look at canEdit() to enable this
    func edit(fromViewController: UIViewController)
    
}
