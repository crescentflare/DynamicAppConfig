//
//  EditUITests.swift
//  DynamicAppConfig Example
//
//  Feature: I can edit an application configuration
//  As an app developer or tester
//  I want to be able to edit a specific configuration
//  So I can further customize app behavior during testing
//

import XCTest
@testable import DynamicAppConfig

@available(iOS 9.0, *)
class EditUITests: XCTestCase {

    // --
    // MARK: Lifecycle
    // --

    override func setUp() {
        let app = XCUIApplication()
        let arguments = TestArguments()
        arguments.clearConfig = true
        continueAfterFailure = false
        app.launchArguments = arguments.toArgumentsArray()
        app.launch()
    }


    // --
    // MARK: Scenarios
    // --

    // Scenario: Edit a configuration
    // Given I am on the "Example App Config" page
    // And I open the "App Configurations" page
    // When I edit the "Test server" configuration
    // Then I see the "Edit configuration" page
    // When I change "apiUrl" into string "https://changed.example.com/"
    // And I change "networkTimeoutSec" into number "10"
    // And I change "runType" into enum "runQuickly"
    // And I change "acceptAllSSL" into boolean "true"
    // And I apply the changes
    // And I select the "Test server" configuration
    // Then I see "apiUrl" set to "https://changed.example.com/"
    // And I see "networkTimeoutSec" set to "10"
    // And I see "runType" set to "runQuickly"
    // And I see "acceptAllSSL" set to "true"
    func testEditConfiguration() {
        UITestApplication.shared
            .expectMainAppScreen()
            .openAppConfigurationsScreen()
            .expectAppConfigurationsScreen()
            .editConfig(.test)
            .expectEditConfigScreen()
            .changeSetting(.apiURL).to("https://changed.example.com/")
            .changeSetting(.networkTimeoutSeconds).to(10)
            .changeSetting(.runType).to(.runQuickly)
            .changeSetting(.acceptAllSSL).to(true)
            .applyChanges()
            .expectAppConfigurationsScreen()
            .selectConfig(.test)
            .expectMainAppScreen()
            .expectSetting(.apiURL).toBe("https://changed.example.com/")
            .expectSetting(.networkTimeoutSeconds).toBe(10)
            .expectSetting(.runType).toBe(.runQuickly)
            .expectSetting(.acceptAllSSL).toBe(true)
    }
    
}
