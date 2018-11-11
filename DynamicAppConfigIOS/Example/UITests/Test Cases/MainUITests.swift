//
//  MainUITests.swift
//  DynamicAppConfig Example
//
//  Feature: I can change my application configuration or a global setting
//  As an app developer or tester
//  I want to be able to change the configuration or a global setting
//  So I can test multiple configurations and settings in one build
//

import XCTest
@testable import DynamicAppConfig

class MainUITests: XCTestCase {

    // --
    // MARK: Lifecycle
    // --

    override func setUp() {
        continueAfterFailure = false
        if #available(iOS 9.0, *) {
            let app = XCUIApplication()
            app.launchArguments = ["clearAppConfig"]
            app.launch()
        } else {
            // Won't be executed, because UI testing is not available anymore on old iOS versions
        }
    }


    // --
    // MARK: Scenarios
    // --

    // Scenario: Selecting a configuration
    // Given I am on the "Example App Config" page
    // And I open the "App Configurations" page
    // When I select the "Test server" configuration
    // Then I see the "Test server" settings
    func testSelectConfiguration() {
        if #available(iOS 9.0, *) {
            UITestApplication.shared
                .expectMainAppScreen()
                .openAppConfigurationsScreen()
                .expectAppConfigurationsScreen()
                .selectConfig(.test)
                .expectMainAppScreen()
                .expectSetting(.name).toBe("Test server")
                .expectSetting(.apiURL).toBe("https://test.example.com/")
                .expectSetting(.runType).toBe(.runNormally)
                .expectSetting(.acceptAllSSL).toBe(false)
                .expectSetting(.networkTimeoutSeconds).toBe(20)
        }
    }

    // Scenario: Editing global settings
    // Given I am on the "Example App Config" page
    // And I open the "App Configurations" page
    // When I change "consoleUrl" into string "https://console.example.com"
    // And I change "consoleTimeoutSec" into number "100"
    // And I change "logLevel" into enum "logVerbose"
    // And I change "consoleEnabled" into boolean "true"
    // And I select the "Mock server" configuration
    // Then I see "consoleUrl" set to "https://console.example.com"
    // And I see "consoleTimeoutSec" set to "100"
    // And I see "logLevel" set to "logVerbose"
    // And I see "consoleEnabled" set to "true"
    func testEditGlobalSettings() {
        if #available(iOS 9.0, *) {
            UITestApplication.shared
                .expectMainAppScreen()
                .openAppConfigurationsScreen()
                .expectAppConfigurationsScreen()
                .changeGlobalSetting(.consoleURL).to("https://console.example.com")
                .changeGlobalSetting(.consoleTimeoutSeconds).to(100)
                .changeGlobalSetting(.logLevel).to(.logVerbose)
                .changeGlobalSetting(.consoleEnabled).to(true)
                .selectConfig(.mock)
                .expectMainAppScreen()
                .expectSetting(.consoleURL).toBe("https://console.example.com")
                .expectSetting(.consoleTimeoutSeconds).toBe(100)
                .expectSetting(.logLevel).toBe(.logVerbose)
                .expectSetting(.consoleEnabled).toBe(true)
        }
    }
    
}
