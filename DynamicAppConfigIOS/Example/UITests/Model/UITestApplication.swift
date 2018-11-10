//
//  UITestApplication.swift
//  DynamicAppConfig Example
//
//  Application test model
//  Used as the main application model to reach into other flows
//

import XCTest

public class UITestApplication {

    // --
    // MARK: Singleton instance
    // --

    static let shared = UITestApplication()


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
