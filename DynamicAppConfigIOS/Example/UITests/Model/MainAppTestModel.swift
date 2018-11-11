//
//  MainAppTestModel.swift
//  DynamicAppConfig Example
//
//  Main application test model
//  Interaction related to the main example application
//

import XCTest

@available(iOS 9.0, *)
public class MainAppTestSetting {
    
    private let type: TestSettingType
    
    init(type: TestSettingType) {
        self.type = type
    }
    
    @discardableResult func toBe(_ value: Bool) -> MainAppTestModel {
        XCTAssertEqual(value ? "true" : "false", XCUIApplication().staticTexts[type.rawValue].label)
        return MainAppTestModel()
    }

    @discardableResult func toBe(_ value: Int) -> MainAppTestModel {
        XCTAssertEqual(String(value), XCUIApplication().staticTexts[type.rawValue].label)
        return MainAppTestModel()
    }

    @discardableResult func toBe(_ value: String) -> MainAppTestModel {
        XCTAssertEqual(value, XCUIApplication().staticTexts[type.rawValue].label)
        return MainAppTestModel()
    }

    @discardableResult func toBe(_ value: ExampleAppConfigRunType) -> MainAppTestModel {
        XCTAssertEqual(value.rawValue, XCUIApplication().staticTexts[type.rawValue].label)
        return MainAppTestModel()
    }

    @discardableResult func toBe(_ value: ExampleAppConfigLogLevel) -> MainAppTestModel {
        XCTAssertEqual(value.rawValue, XCUIApplication().staticTexts[type.rawValue].label)
        return MainAppTestModel()
    }

}

@available(iOS 9.0, *)
public class MainAppTestModel {

    // --
    // MARK: Interaction
    // --

    @discardableResult func openAppConfigurationsScreen() -> MainAppTestModel {
        XCUIApplication().navigationBars["Example App Config"].buttons["Change"].tap()
        return self
    }
    

    // --
    // MARK: Checks
    // --
    
    @discardableResult func expectAppConfigurationsScreen() -> ManageAppConfigTestModel {
        XCTAssertTrue(XCUIApplication().navigationBars["App configurations"].exists)
        return ManageAppConfigTestModel()
    }

    @discardableResult func expectSetting(_ setting: TestSettingType) -> MainAppTestSetting {
        return MainAppTestSetting(type: setting)
    }

}
