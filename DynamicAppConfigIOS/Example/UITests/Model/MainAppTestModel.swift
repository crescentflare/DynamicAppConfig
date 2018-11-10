//
//  MainAppTestModel.swift
//  DynamicAppConfig Example
//
//  Main application test model
//  Interaction related to the main example application
//

import XCTest

public enum MainAppTestSettingType: String {
    
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

public class MainAppTestSetting {
    
    private let type: MainAppTestSettingType
    
    init(type: MainAppTestSettingType) {
        self.type = type
    }
    
    @discardableResult func toBe(_ value: Bool) -> MainAppTestModel {
        if #available(iOS 9.0, *) {
            XCTAssertEqual(value ? "true" : "false", XCUIApplication().staticTexts[type.rawValue].label)
        }
        return MainAppTestModel()
    }

    @discardableResult func toBe(_ value: Int) -> MainAppTestModel {
        if #available(iOS 9.0, *) {
            XCTAssertEqual(String(value), XCUIApplication().staticTexts[type.rawValue].label)
        }
        return MainAppTestModel()
    }

    @discardableResult func toBe(_ value: String) -> MainAppTestModel {
        if #available(iOS 9.0, *) {
            XCTAssertEqual(value, XCUIApplication().staticTexts[type.rawValue].label)
        }
        return MainAppTestModel()
    }

    @discardableResult func toBe(_ value: ExampleAppConfigRunType) -> MainAppTestModel {
        if #available(iOS 9.0, *) {
            XCTAssertEqual(value.rawValue, XCUIApplication().staticTexts[type.rawValue].label)
        }
        return MainAppTestModel()
    }

}

public class MainAppTestModel {

    // --
    // MARK: Interaction
    // --

    @discardableResult func openAppConfigurationsScreen() -> MainAppTestModel {
        if #available(iOS 9.0, *) {
            XCUIApplication().navigationBars["Example App Config"].buttons["Change"].tap()
        }
        return self
    }
    

    // --
    // MARK: Checks
    // --
    
    @discardableResult func expectAppConfigurationsScreen() -> ManageAppConfigTestModel {
        if #available(iOS 9.0, *) {
            XCTAssertTrue(XCUIApplication().navigationBars["App configurations"].exists)
        }
        return ManageAppConfigTestModel()
    }

    @discardableResult func expectSetting(_ setting: MainAppTestSettingType) -> MainAppTestSetting {
        return MainAppTestSetting(type: setting)
    }

}
