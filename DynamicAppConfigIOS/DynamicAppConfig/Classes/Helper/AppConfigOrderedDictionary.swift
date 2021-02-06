//
//  AppConfigOrderedDictionary.swift
//  DynamicAppConfig Pod
//
//  Library helper: fixed order dictionary
//  Works like a dictionary but has a fixed order and is always mutable
//

public struct AppConfigOrderedDictionary<Tk: Hashable, Tv> {
    
    // --
    // MARK: Members
    // --
    
    var keys = [Tk]()
    var values = [Tk: Tv]()

    
    // --
    // MARK: Initialization
    // --
    
    public init() { }
    
    
    // --
    // MARK: Implementation
    // --
    
    public func allKeys() -> [Tk] {
        return keys
    }
    
    public mutating func removeAllObjects() {
        keys.removeAll()
        values.removeAll()
    }
    
    
    // --
    // MARK: Indexing
    // --
    
    subscript(key: Tk) -> Tv? {
        get {
            return self.values[key]
        }
        set (newValue) {
            if let value = newValue {
                let oldValue = self.values.updateValue(value, forKey: key)
                if oldValue == nil {
                    self.keys.append(key)
                }
            } else {
                self.values.removeValue(forKey: key)
                self.keys = self.keys.filter {$0 != key}
            }
        }
    }
    
    
    // --
    // MARK: Describe object
    // --
    
    var description: String {
        var result = "{\n"
        for i in 0..<self.keys.count {
            let key = self.keys[i]
            if let item = self[key] {
                result += "[\(i)]: \(key) => \(item)\n"
            }
        }
        result += "}"
        return result
    }
    
}
