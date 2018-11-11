//
//  ManualUITests.swift
//  DynamicAppConfig Example
//
//  Feature: I can manually change the active configuration or a global setting
//  As an app developer or tester
//  I want to be able to manually change the active configuration or a global setting
//  So I can optimize testing and make smaller test scripts
//

import XCTest
@testable import DynamicAppConfig

@available(iOS 9.0, *)
class ManualUITests: XCTestCase {
    
    // --
    // MARK: Lifecycle
    // --
    
    override func setUp() {
        continueAfterFailure = false
    }
    
    
    // --
    // MARK: Scenarios
    // --
    
    // Scenario: Manually change a configuration
    // Given I am on the "Example App Config" page
    // And I manually select the "Test server" configuration
    // And I manually change "apiUrl" into "https://manualchange.example.com/"
    // Then I see "apiUrl" set to "https://manualchange.example.com/"
    func testManuallyChangeConfiguration() {
        let app = XCUIApplication()
        let arguments = TestArguments()
        arguments.clearConfig = true
        arguments.selectConfig = ManageAppConfigType.test.rawValue
        arguments.changeCurrentSettings = [TestSettingType.apiURL.rawValue: "https://manualchange.example.com/"]
        app.launchArguments = arguments.toArgumentsArray()
        app.launch()
        UITestApplication.shared
            .expectMainAppScreen()
            .expectSetting(.apiURL).toBe("https://manualchange.example.com/")
    }
    
    // Scenario: Manually change a global setting
    // Given I am on the "Example App Config" page
    // And I manually select the "Test server" configuration
    // And I manually change "logLevel" into "logVerbose"
    // Then I see "logLevel" set to "logVerbose"
    func testManuallyChangeGlobalSetting() {
        let app = XCUIApplication()
        let arguments = TestArguments()
        arguments.clearConfig = true
        arguments.selectConfig = ManageAppConfigType.test.rawValue
        arguments.changeGlobalSettings = [TestSettingType.logLevel.rawValue: ExampleAppConfigLogLevel.logVerbose.rawValue]
        app.launchArguments = arguments.toArgumentsArray()
        app.launch()
        UITestApplication.shared
            .expectMainAppScreen()
            .expectSetting(.logLevel).toBe(.logVerbose)
    }
    
}
