//
//  PluginUITests.swift
//  DynamicAppConfig Example
//
//  Feature: I can use a custom plugin
//  As an app developer
//  I want to be able to add custom plugins in the selection menu
//  So I can integrate my own tools within the app config library easily
//

import XCTest
@testable import DynamicAppConfig

@available(iOS 9.0, *)
class PluginUITests: XCTestCase {

    // --
    // MARK: Lifecycle
    // --

    override func setUp() {
        let app = XCUIApplication()
        let arguments = TestArguments()
        arguments.clearConfig = true
        arguments.speedyAnimations = true
        continueAfterFailure = false
        app.launchArguments = arguments.toArgumentsArray()
        app.launch()
    }


    // --
    // MARK: Scenarios
    // --

    // Scenario: Using a custom plugin
    // Given I am on the "Example App Config" page
    // And I open the "App Configurations" page
    // When I select the "View log" custom plugin
    // Then I see the "Show log" screen
    func testViewLog() {
        UITestApplication.shared
            .expectMainAppScreen()
            .openAppConfigurationsScreen()
            .expectAppConfigurationsScreen()
            .openCustomPlugin(.viewLog)
            .expectShowLogScreen()
    }
    
}
