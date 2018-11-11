//
//  ManageAppConfigTestModel.swift
//  DynamicAppConfig Example
//
//  Custom plugin test model
//  Interaction related to the custom plugin screen
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
