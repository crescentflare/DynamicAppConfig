//
//  TestArguments.swift
//  DynamicAppConfig Example
//
//  Used to easily set and parse launch arguments from UI testing to the app
//

import UIKit
import DynamicAppConfig

public class TestArguments {

    // --
    // MARK: Members
    // --
    
    var clearConfig = false
    var selectConfig: String?
    var changeCurrentSettings = [String: String]()
    var changeGlobalSettings = [String: String]()


    // --
    // MARK: Serialization
    // --

    func toArgumentsArray() -> [String] {
        // Convert basic settings
        var arguments = [String]()
        if clearConfig {
            arguments.append("clearConfig")
        }
        if let selectConfig = selectConfig {
            arguments.append("selectConfig=\"\(selectConfig)\"")
        }
        
        // Convert manual setting changes
        for (key, value) in changeCurrentSettings {
            arguments.append("currentSetting:\"\(key)\"=\"\(value)\"")
        }
        for (key, value) in changeGlobalSettings {
            arguments.append("globalSetting:\"\(key)\"=\"\(value)\"")
        }
        return arguments
    }
    
    func parseArgumentsArray(arguments: [String]) {
        // Parse basic settings
        clearConfig = arguments.contains("clearConfig")
        selectConfig = nil
        for argument in arguments {
            if argument.hasPrefix("selectConfig=") {
                selectConfig = argument.replacingOccurrences(of: "selectConfig=", with: "").replacingOccurrences(of: "\"", with: "")
                break
            }
        }
        
        // Parse manual setting changes
        changeCurrentSettings = [:]
        changeGlobalSettings = [:]
        for argument in arguments {
            if argument.hasPrefix("currentSetting:") {
                let currentSettingPair = argument.replacingOccurrences(of: "currentSetting:", with: "").split(separator: "=")
                if currentSettingPair.count == 2 {
                    let key = currentSettingPair[0].replacingOccurrences(of: "\"", with: "")
                    let value = currentSettingPair[1].replacingOccurrences(of: "\"", with: "")
                    changeCurrentSettings[key] = value
                }
            } else if argument.hasPrefix("globalSetting:") {
                let globalSettingPair = argument.replacingOccurrences(of: "globalSetting:", with: "").split(separator: "=")
                if globalSettingPair.count == 2 {
                    let key = globalSettingPair[0].replacingOccurrences(of: "\"", with: "")
                    let value = globalSettingPair[1].replacingOccurrences(of: "\"", with: "")
                    changeGlobalSettings[key] = value
                }
            }
        }
    }

}
