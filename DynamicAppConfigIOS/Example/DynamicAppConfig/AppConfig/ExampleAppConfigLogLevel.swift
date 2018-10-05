//
//  ExampleAppConfigLogLevel.swift
//  DynamicAppConfig Example
//
//  App config: application configuration log level
//  An enum used by the application build configuration
//

import UIKit
import DynamicAppConfig

// Enum definition using string raw values for storage
enum ExampleAppConfigLogLevel: String, CaseIterable {
    
    case logDisabled = "logDisabled"
    case logNormal = "logNormal"
    case logVerbose = "logVerbose"

}
