//
//  MainAppTestModel.swift
//  DynamicAppConfig Example
//
//  Main application test model
//  Interaction related to the main example application
//

import XCTest

public class MainAppTestSetting {
    
    private let type: TestSettingType
    
    init(type: TestSettingType) {
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

    @discardableResult func toBe(_ value: ExampleAppConfigLogLevel) -> MainAppTestModel {
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

    @discardableResult func expectSetting(_ setting: TestSettingType) -> MainAppTestSetting {
        return MainAppTestSetting(type: setting)
    }

}
