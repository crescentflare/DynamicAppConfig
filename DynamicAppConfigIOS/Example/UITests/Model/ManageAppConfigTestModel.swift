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
        let collectionViewsQuery = XCUIApplication().alerts["Value for \(type.rawValue)"].collectionViews
        collectionViewsQuery.textFields[type.rawValue].clearText()
        collectionViewsQuery.textFields[type.rawValue].typeText(String(value))
        collectionViewsQuery.buttons["OK"].tap()
        _ = XCUIApplication().keyboards.firstMatch.waitForNotExistence(timeout: 5)
        return ManageAppConfigTestModel()
    }
    
    @discardableResult func to(_ value: String) -> ManageAppConfigTestModel {
        XCUIApplication().tables.cells[type.rawValue].tap()
        let collectionViewsQuery = XCUIApplication().alerts["Value for \(type.rawValue)"].collectionViews
        collectionViewsQuery.textFields[type.rawValue].clearText()
        collectionViewsQuery.textFields[type.rawValue].typeText(value)
        collectionViewsQuery.buttons["OK"].tap()
        _ = XCUIApplication().keyboards.firstMatch.waitForNotExistence(timeout: 5)
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
    
    @discardableResult func changeGlobalSetting(_ setting: TestSettingType) -> ManageAppConfigTestSetting {
        return ManageAppConfigTestSetting(type: setting)
    }


    // --
    // MARK: Checks
    // --
    
    @discardableResult func expectMainAppScreen() -> MainAppTestModel {
        XCTAssertTrue(XCUIApplication().navigationBars["Example App Config"].exists)
        return MainAppTestModel()
    }

}
