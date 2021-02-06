//
//  AppConfigBundle.swift
//  DynamicAppConfig Pod
//
//  Library helper: bundle access utility
//  Provides helper functions to acquire resources from the Pod bundle (like images and strings)
//

class AppConfigBundle {
    
    // --
    // MARK: Initialization
    // --
    
    init() { }
    
    
    // --
    // MARK: Implementation
    // --
    
    static func image(named: String) -> UIImage? {
        return UIImage.init(named: named, in: AppConfigBundle.podBundle(), compatibleWith: nil)
    }
    
    static func loadNib(named: String, owner: AnyObject, options: [AnyHashable: Any]? = nil) -> UINib? {
        return UINib(nibName: named, bundle: AppConfigBundle.podBundle())
    }

    static func localizedString(key: String) -> String {
        if let bundle = AppConfigBundle.podBundle() {
            return NSLocalizedString(key, tableName: "Localizable", bundle: bundle, value: key, comment: "")
        }
        return key
    }

    
    // --
    // MARK: Internal helper
    // --
    
    private static func podBundle() -> Bundle? {
        if let url = Bundle.init(for: AppConfigBundle.self).url(forResource: "DynamicAppConfig", withExtension: "bundle") {
            return Bundle.init(url: url)
        }
        return nil
    }
    
}
