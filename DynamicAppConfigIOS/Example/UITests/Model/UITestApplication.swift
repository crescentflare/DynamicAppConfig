//
//  UITestApplication.swift
//  DynamicAppConfig Example
//
//  Application test model
//  Used as the main application model to reach into other flows
//

import XCTest

@available(iOS 9.0, *)
public class UITestApplication {

    // --
    // MARK: Singleton instance
    // --

    static let shared = UITestApplication()


    // --
    // MARK: Checks
    // --
    
    @discardableResult func expectMainAppScreen() -> MainAppTestModel {
        XCTAssertTrue(XCUIApplication().navigationBars["Example App Config"].exists)
        return MainAppTestModel()
    }

}
