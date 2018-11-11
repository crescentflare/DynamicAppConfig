//
//  ManageAppConfigTestModel.swift
//  DynamicAppConfig Example
//
//  Manage app config test model
//  Interaction related to the app configurations screen
//

import XCTest

public enum ManageAppConfigType: String {
    
    case mock = "Mock server"
    case test = "Test server"
    case testInsecure = "Test server (insecure)"
    case accept = "Acceptation server"
    case production = "Production"

}

public enum ManageAppPluginType: String {
    
    case viewLog = "View log"
    
}

@available(iOS 9.0, *)
public class ManageAppConfigTestSetting {
    
    private let type: TestSettingType
    
    init(type: TestSettingType) {
        self.type = type
    }
    
    @discardableResult func to(_ value: Bool) -> ManageAppConfigTestModel {
        var currentValue = false
        if let currentValueString = XCUIApplication().tables.cells[type.rawValue].switches.firstMatch.value as? String {
            currentValue = currentValueString != "0"
        }
        if currentValue != value {
            XCUIApplication().tables.cells[type.rawValue].tap()
        }
        return ManageAppConfigTestModel()
    }
    
    @discardableResult func to(_ value: Int) -> ManageAppConfigTestModel {
        XCUIApplication().tables.cells[type.rawValue].tap()
        let alert = XCUIApplication().alerts["Value for \(type.rawValue)"]
        alert.collectionViews.textFields[type.rawValue].clearText()
        alert.collectionViews.textFields[type.rawValue].typeText(String(value))
        alert.buttons["OK"].tap()
        _ = XCUIApplication().keyboards.firstMatch.waitForNotExistence(timeout: 10)
        return ManageAppConfigTestModel()
    }
    
    @discardableResult func to(_ value: String) -> ManageAppConfigTestModel {
        XCUIApplication().tables.cells[type.rawValue].tap()
        let alert = XCUIApplication().alerts["Value for \(type.rawValue)"]
        alert.collectionViews.textFields[type.rawValue].clearText()
        alert.collectionViews.textFields[type.rawValue].typeText(value)
        alert.buttons["OK"].tap()
        _ = XCUIApplication().keyboards.firstMatch.waitForNotExistence(timeout: 10)
        return ManageAppConfigTestModel()
    }
    
    @discardableResult func to(_ value: ExampleAppConfigRunType) -> ManageAppConfigTestModel {
        XCUIApplication().tables.cells[type.rawValue].tap()
        XCUIApplication().tables.cells.staticTexts[value.rawValue].tap()
        return ManageAppConfigTestModel()
    }
    
    @discardableResult func to(_ value: ExampleAppConfigLogLevel) -> ManageAppConfigTestModel {
        XCUIApplication().tables.cells[type.rawValue].tap()
        XCUIApplication().tables.cells.staticTexts[value.rawValue].tap()
        return ManageAppConfigTestModel()
    }
    
}

@available(iOS 9.0, *)
public class ManageAppConfigTestModel {

    // --
    // MARK: Interaction
    // --

    @discardableResult func selectConfig(_ configuration: ManageAppConfigType) -> ManageAppConfigTestModel {
        XCUIApplication().tables.cells.staticTexts[configuration.rawValue].tap()
        XCUIApplication().navigationBars["App configurations"].buttons["Done"].tap()
        return self
    }

    @discardableResult func editConfig(_ configuration: ManageAppConfigType) -> ManageAppConfigTestModel {
        XCUIApplication().tables.cells.staticTexts[configuration.rawValue].swipeLeft()
        XCUIApplication().tables.cells.buttons["Edit"].tap()
        return self
    }

    @discardableResult func changeGlobalSetting(_ setting: TestSettingType) -> ManageAppConfigTestSetting {
        return ManageAppConfigTestSetting(type: setting)
    }

    @discardableResult func openCustomPlugin(_ plugin: ManageAppPluginType) -> CustomPluginTestModel {
        XCUIApplication().tables.cells[plugin.rawValue].tap()
        return CustomPluginTestModel()
    }

    
    // --
    // MARK: Checks
    // --
    
    @discardableResult func expectMainAppScreen() -> MainAppTestModel {
        XCTAssertTrue(XCUIApplication().navigationBars["Example App Config"].exists)
        return MainAppTestModel()
    }

    @discardableResult func expectEditConfigScreen() -> EditAppConfigTestModel {
        XCTAssertTrue(XCUIApplication().navigationBars["Edit configuration"].exists)
        return EditAppConfigTestModel()
    }

}
