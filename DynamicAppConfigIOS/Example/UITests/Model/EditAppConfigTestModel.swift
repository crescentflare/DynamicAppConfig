//
//  EditAppConfigTestModel.swift
//  DynamicAppConfig Example
//
//  Edit app config test model
//  Interaction related to the edit configuration and new custom configuration screens
//

import XCTest

@available(iOS 9.0, *)
public class EditAppConfigTestSetting {
    
    private let type: TestSettingType
    
    init(type: TestSettingType) {
        self.type = type
    }
    
    @discardableResult func to(_ value: Bool) -> EditAppConfigTestModel {
        var currentValue = false
        if let currentValueString = XCUIApplication().tables.cells[type.rawValue].switches.firstMatch.value as? String {
            currentValue = currentValueString != "0"
        }
        if currentValue != value {
            XCUIApplication().tables.cells[type.rawValue].tap()
        }
        return EditAppConfigTestModel()
    }
    
    @discardableResult func to(_ value: Int) -> EditAppConfigTestModel {
        XCUIApplication().tables.cells[type.rawValue].tap()
        let collectionViewsQuery = XCUIApplication().alerts["Value for \(type.rawValue)"].collectionViews
        collectionViewsQuery.textFields[type.rawValue].clearText()
        collectionViewsQuery.textFields[type.rawValue].typeText(String(value))
        collectionViewsQuery.buttons["OK"].tap()
        _ = XCUIApplication().keyboards.firstMatch.waitForNotExistence(timeout: 10)
        return EditAppConfigTestModel()
    }
    
    @discardableResult func to(_ value: String) -> EditAppConfigTestModel {
        XCUIApplication().tables.cells[type.rawValue].tap()
        let collectionViewsQuery = XCUIApplication().alerts["Value for \(type.rawValue)"].collectionViews
        collectionViewsQuery.textFields[type.rawValue].clearText()
        collectionViewsQuery.textFields[type.rawValue].typeText(value)
        collectionViewsQuery.buttons["OK"].tap()
        _ = XCUIApplication().keyboards.firstMatch.waitForNotExistence(timeout: 10)
        return EditAppConfigTestModel()
    }
    
    @discardableResult func to(_ value: ExampleAppConfigRunType) -> EditAppConfigTestModel {
        XCUIApplication().tables.cells[type.rawValue].tap()
        XCUIApplication().tables.cells.staticTexts[value.rawValue].tap()
        return EditAppConfigTestModel()
    }
    
}

@available(iOS 9.0, *)
public class EditAppConfigTestModel {

    // --
    // MARK: Interaction
    // --

    @discardableResult func changeSetting(_ setting: TestSettingType) -> EditAppConfigTestSetting {
        return EditAppConfigTestSetting(type: setting)
    }

    @discardableResult func applyChanges() -> EditAppConfigTestModel {
        XCUIApplication().tables.staticTexts["Apply changes"].tap()
        return self
    }

    
    // --
    // MARK: Checks
    // --
    
    @discardableResult func expectAppConfigurationsScreen() -> ManageAppConfigTestModel {
        XCTAssertTrue(XCUIApplication().navigationBars["App configurations"].exists)
        return ManageAppConfigTestModel()
    }

}
