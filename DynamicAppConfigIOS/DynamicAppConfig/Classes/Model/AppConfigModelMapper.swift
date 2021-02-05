//
//  AppConfigModelMapper.swift
//  DynamicAppConfig Pod
//
//  Library model: mapping between dictionary and model
//  Used to map values dynamically between the custom model and internally stored dictionaries (for overriding settings)
//  Also provides optional categorization
//

// Enum for mapping mode
public enum AppConfigModelMapperMode {
    
    case collectKeys
    case toDictionary
    case fromDictionary
    
}

// Mapping class
public class AppConfigModelMapper {

    // --
    // MARK: Members
    // --

    var categorizedFields = AppConfigOrderedDictionary<String, [String]>()
    var globalCategorizedFields = AppConfigOrderedDictionary<String, [String]>()
    var rawRepresentableFields = [String]()
    var rawRepresentableFieldValues = [String: [String]]()
    var dictionary = [String: Any]()
    var globalDictionary = [String: Any]()
    var mode: AppConfigModelMapperMode

    
    // --
    // MARK: Initialization
    // --
    
    // Initialization (for everything except FromDictionary mode)
    public init(mode: AppConfigModelMapperMode) {
        self.mode = mode
    }

    // Initialization (to be used with FromDictionary mode)
    public init(dictionary: [String: Any], globalDictionary: [String: Any], mode: AppConfigModelMapperMode) {
        self.dictionary = dictionary
        self.globalDictionary = globalDictionary
        self.mode = mode
    }
    
    
    // --
    // MARK: Mapping
    // --
    
    // Map between key and value: boolean
    public func map(key: String, value: inout Bool, category: String = "", global: Bool = false) {
        if mode == .toDictionary {
            if global {
                globalDictionary[key] = value
            } else {
                dictionary[key] = value
            }
        } else if mode == .fromDictionary && global && globalDictionary[key] != nil {
            value = globalDictionary[key] as? Bool ?? false
        } else if mode == .fromDictionary && !global && dictionary[key] != nil {
            value = dictionary[key] as? Bool ?? false
        } else if mode == .collectKeys {
            add(key: key, category: category, global: global)
        }
    }

    // Map between key and value: int
    public func map(key: String, value: inout Int, category: String = "", global: Bool = false) {
        if mode == .toDictionary {
            if global {
                globalDictionary[key] = value
            } else {
                dictionary[key] = value
            }
        } else if mode == .fromDictionary && global && globalDictionary[key] != nil {
            value = globalDictionary[key] as? Int ?? 0
        } else if mode == .fromDictionary && !global && dictionary[key] != nil {
            value = dictionary[key] as? Int ?? 0
        } else if mode == .collectKeys {
            add(key: key, category: category, global: global)
        }
    }

    // Map between key and value: string
    public func map(key: String, value: inout String, category: String = "", global: Bool = false) {
        if mode == .toDictionary {
            if global {
                globalDictionary[key] = value
            } else {
                dictionary[key] = value
            }
        } else if mode == .fromDictionary && global && globalDictionary[key] != nil {
            value = globalDictionary[key] as? String ?? ""
        } else if mode == .fromDictionary && !global && dictionary[key] != nil {
            value = dictionary[key] as? String ?? ""
        } else if mode == .collectKeys {
            add(key: key, category: category, global: global)
        }
    }
    
    // Map between key and value: an enum containing a raw value (preferably string)
    public func map<T: RawRepresentable>(key: String, value: inout T, fallback: T, allValues: [T], category: String = "", global: Bool = false) {
        if mode == .toDictionary {
            if global {
                globalDictionary[key] = value.rawValue
            } else {
                dictionary[key] = value.rawValue
            }
        } else if mode == .fromDictionary && global && globalDictionary[key] != nil {
            if let raw = globalDictionary[key] as? T.RawValue {
                value = T(rawValue: raw) ?? fallback
            } else {
                value = fallback
            }
        } else if mode == .fromDictionary && !global && dictionary[key] != nil {
            if let raw = dictionary[key] as? T.RawValue {
                value = T(rawValue: raw) ?? fallback
            } else {
                value = fallback
            }
        } else if mode == .collectKeys {
            var stringValues = [String]()
            for value in allValues {
                if let stringRawValue = value.rawValue as? String {
                    stringValues.append(stringRawValue)
                }
            }
            if stringValues.count > 0 {
                rawRepresentableFieldValues[key] = stringValues
            }
            add(key: key, category: category, global: global, isRawRepresentable: true)
        }
    }
    
    // After calling mapping on the model with to dictionary mode, retrieve the result using this function, for configuration settings
    public func getDictionaryValues() -> [String: Any] {
        return dictionary
    }

    // After calling mapping on the model with to dictionary mode, retrieve the result using this function, for global settings
    public func getGlobalDictionaryValues() -> [String: Any] {
        return globalDictionary
    }

    
    // --
    // MARK: Field grouping
    // --

    // After calling mapping on the model with this object, retrieve the grouped/categorized fields, for configuration settings
    public func getCategorizedFields() -> AppConfigOrderedDictionary<String, [String]> {
        return categorizedFields
    }
    
    // After calling mapping on the model with this object, retrieve the grouped/categorized fields, for global settings
    public func getGlobalCategorizedFields() -> AppConfigOrderedDictionary<String, [String]> {
        return globalCategorizedFields
    }

    // After calling mapping on the model with this object, check if a given field is a raw representable class
    public func isRawRepresentable(field: String) -> Bool {
        return rawRepresentableFields.contains(field)
    }
    
    // After calling mapping on the model with this object, return a list of possible values (only for raw representable types)
    public func getRawRepresentableValues(forField: String) -> [String]? {
        return rawRepresentableFieldValues[forField]
    }
    
    // Internal method to keep track of keys and categories
    private func add(key: String, category: String, global: Bool, isRawRepresentable: Bool = false) {
        if global {
            if globalCategorizedFields[category] == nil {
                globalCategorizedFields[category] = []
            }
            globalCategorizedFields[category]?.append(key)
        } else {
            if categorizedFields[category] == nil {
                categorizedFields[category] = []
            }
            categorizedFields[category]?.append(key)
        }
        if isRawRepresentable && !rawRepresentableFields.contains(key) {
            rawRepresentableFields.append(key)
        }
    }
    
}
