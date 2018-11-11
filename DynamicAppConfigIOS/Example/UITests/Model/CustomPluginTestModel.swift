//
//  ManageAppConfigTestModel.swift
//  DynamicAppConfig Example
//
//  Manage app config test model
//  Interaction related to the app configurations screen
//

import XCTest

@available(iOS 9.0, *)
public class CustomPluginTestModel {

    // --
    // MARK: Checks
    // --
    
    @discardableResult func expectShowLogScreen() -> CustomPluginTestModel {
        XCTAssertTrue(XCUIApplication().navigationBars["View log"].exists)
        return self
    }

}
