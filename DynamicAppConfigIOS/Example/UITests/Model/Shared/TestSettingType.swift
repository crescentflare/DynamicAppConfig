//
//  TestSettingType.swift
//  DynamicAppConfig Example
//
//  Setting type enum
//  Used to check or change config settings during testing
//

import XCTest

public enum TestSettingType: String {
    
    case name = "name"
    case apiURL = "apiUrl"
    case runType = "runType"
    case acceptAllSSL = "acceptAllSSL"
    case networkTimeoutSeconds = "networkTimeoutSec"
    case consoleURL = "consoleUrl"
    case consoleEnabled = "consoleEnabled"
    case consoleTimeoutSeconds = "consoleTimeoutSec"
    case logLevel = "logLevel"
    
}
