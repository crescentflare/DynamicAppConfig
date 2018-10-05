//
//  AppConfigBaseModelTests.swift
//  DynamicAppConfig Example
//
//  Unit test: tests the base model
//

import UIKit
import XCTest
@testable import DynamicAppConfig

// Test class
class AppConfigBaseModelTests: XCTestCase {
    
    // --
    // MARK: Test cases
    // --

    func testObtainConfigurationValueFields() {
        let model = DerivedModel()
        let expectedFields = [ "enumField", "stringField", "intField", "boolField" ].sorted()
        let modelFields = model.obtainConfigurationValues().keys.sorted()
        XCTAssertEqual(expectedFields, modelFields)
    }
    
    func testObtainGlobalConfigurationValueFields() {
        let model = DerivedModel()
        let expectedFields = [ "globalEnumField", "globalStringField" ].sorted()
        let modelFields = model.obtainGlobalValues().keys.sorted()
        XCTAssertEqual(expectedFields, modelFields)
    }
    
    func testObtainConfigurationCategorizedFields() {
        let model = DerivedModel()
        let expectedCategories = [ "Enums", "Strings", "Numbers", "" ].sorted()
        let modelCategories = model.obtainConfigurationCategorizedFields().allKeys().sorted()
        XCTAssertEqual(expectedCategories, modelCategories)
    }
    
    func testObtainGlobalCategorizedFields() {
        let model = DerivedModel()
        let expectedCategories = [ "GlobalEnums", "GlobalStrings" ].sorted()
        let modelCategories = model.obtainGlobalCategorizedFields().allKeys().sorted()
        XCTAssertEqual(expectedCategories, modelCategories)
    }
    
    func testObtainConfigurationValues() {
        let model = DerivedModel()
        model.enumField = .test
        model.stringField = "Test string"
        model.intField = 834892
        model.boolField = true
        XCTAssertEqual(TestEnum.test.rawValue, model.obtainConfigurationValues()["enumField"] as? String)
        XCTAssertEqual("Test string", model.obtainConfigurationValues()["stringField"] as? String)
        XCTAssertEqual(834892, model.obtainConfigurationValues()["intField"] as? Int)
        XCTAssertEqual(true, model.obtainConfigurationValues()["boolField"] as? Bool)
    }
    
    func testApplyOverrides() {
        let model = DerivedModel()
        let overrides: [String: Any] = [
            "enumField": "enumValues",
            "stringField": "Override test",
            "intField": -19028234,
            "boolField": false
        ]
        model.apply(overrides: overrides, globalOverrides: [:], name: "")
        XCTAssertEqual(.enumValues, model.enumField)
        XCTAssertEqual("Override test", model.stringField)
        XCTAssertEqual(-19028234, model.intField)
        XCTAssertEqual(false, model.boolField)
    }
    
}

// A test string enum
enum TestEnum: String, CaseIterable {

    case unknown = ""
    case test = "test"
    case application = "application"
    case enumValues = "enumValues"

}

// A derived model using public fields
class DerivedModel : AppConfigBaseModel {
    
    var enumField = TestEnum.unknown
    var stringField = ""
    var intField = 0
    var boolField = false
    
    var globalEnumField = TestEnum.unknown
    var globalStringField = ""

    override func map(mapper: AppConfigModelMapper) {
        // Configuration serialization
        mapper.map(key: "enumField", value: &enumField, fallback: .unknown, allValues: TestEnum.allCases, category: "Enums")
        mapper.map(key: "stringField", value: &stringField, category: "Strings")
        mapper.map(key: "intField", value: &intField, category: "Numbers")
        mapper.map(key: "boolField", value: &boolField)
        
        // Global serialization
        mapper.map(key: "globalEnumField", value: &globalEnumField, fallback: .unknown, allValues: TestEnum.allCases, category: "GlobalEnums", global: true)
        mapper.map(key: "globalStringField", value: &globalStringField, category: "GlobalStrings", global: true)
    }

}
