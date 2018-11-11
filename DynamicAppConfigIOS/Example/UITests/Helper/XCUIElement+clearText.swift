//
//  XCUIElement+clearText.swift
//  DynamicAppConfig Example
//
//  Clear text extension
//  Allows to easily clear a text field for UI testing
//

import XCTest

@available(iOS 9.0, *)
public extension XCUIElement {
    
    public func clearText() {
        // Obtain value
        guard let stringValue = self.value as? String else {
            return
        }
        if let placeholderString = self.placeholderValue, placeholderString == stringValue {
            return
        }
        
        // Convert to delete characters and type
        var deleteString = String()
        for _ in stringValue {
            deleteString += XCUIKeyboardKey.delete.rawValue
        }
        self.typeText(deleteString)
    }
    
}
