//
//  ExampleAppConfigRunType.swift
//  DynamicAppConfig Example
//
//  App config: application configuration run setting
//  An enum used by the application build configuration
//

import UIKit
import DynamicAppConfig

// Enum definition using string raw values for storage
enum ExampleAppConfigRunType: String, CaseIterable {
    
    case runNormally = "runNormally"
    case runQuickly = "runQuickly"
    case runStrictly = "runStrictly"

}
