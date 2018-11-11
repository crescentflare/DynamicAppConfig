//
//  XCUIElement+waitForNotExistence.swift
//  DynamicAppConfig Example
//
//  Wait for not existence extension
//  The inverted version of waiting for existence, wait until an element disappears
//

import XCTest

@available(iOS 9.0, *)
public extension XCUIElement {
    
    public func waitForNotExistence(timeout: TimeInterval) -> Bool {
        let notExistPredicate = NSPredicate(format: "exists == false")
        let myExpectation = XCTNSPredicateExpectation(predicate: notExistPredicate, object: self)
        let result = XCTWaiter().wait(for: [myExpectation], timeout: timeout)
        return result == .completed
    }
    
}
