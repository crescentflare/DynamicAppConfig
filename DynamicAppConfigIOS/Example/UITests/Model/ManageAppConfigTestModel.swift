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

public class ManageAppConfigTestModel {

    // --
    // MARK: Interaction
    // --

    @discardableResult func selectConfig(configuration: ManageAppConfigType) -> ManageAppConfigTestModel {
        if #available(iOS 9.0, *) {
            XCUIApplication().tables.cells.staticTexts[configuration.rawValue].tap()
            XCUIApplication().navigationBars["App configurations"].buttons["Done"].tap()
        }
        return self
    }
    

    // --
    // MARK: Checks
    // --
    
    @discardableResult func expectMainAppScreen() -> MainAppTestModel {
        if #available(iOS 9.0, *) {
            XCTAssertTrue(XCUIApplication().navigationBars["Example App Config"].exists)
        }
        return MainAppTestModel()
    }

}
