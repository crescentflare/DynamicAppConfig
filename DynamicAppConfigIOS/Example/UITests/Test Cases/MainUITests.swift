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
                .selectConfig(configuration: .test)
                .expectMainAppScreen()
                .expectSetting(.name).toBe("Test server")
                .expectSetting(.apiURL).toBe("https://test.example.com/")
                .expectSetting(.runType).toBe(.runNormally)
                .expectSetting(.acceptAllSSL).toBe(false)
                .expectSetting(.networkTimeoutSeconds).toBe(20)
        }
    }

}
