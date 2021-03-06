//
//  AppConfigCellSectionDividerView.swift
//  DynamicAppConfig Pod
//
//  Library view: shared component
//  A divider view between cell sections (to make them stand out more)
//

import UIKit

// Value type enum
enum AppConfigCellSectionLocation: String {
    
    case none = "none"
    case top = "top"
    case bottom = "bottom"
    
}

// View component
@IBDesignable class AppConfigCellSectionDividerView : UIView {
    
    // --
    // MARK: Members
    // --
    
    private var _location = AppConfigCellSectionLocation.none
    private var _dividerLine: UIView?
    private var _dividerLineConstraint: NSLayoutConstraint?

    
    // --
    // MARK: Properties which can be used in interface builder
    // --
    
    @IBInspectable var dividerLocation: String = "" {
        didSet {
            location = AppConfigCellSectionLocation.init(rawValue: dividerLocation) ?? AppConfigCellSectionLocation.none
        }
    }

    var location: AppConfigCellSectionLocation? {
        set {
            _location = newValue ?? .none
            if let dividerLine = _dividerLine {
                if let removeConstraint = _dividerLineConstraint {
                    _dividerLine?.removeConstraint(removeConstraint)
                }
                _dividerLineConstraint = AppConfigViewUtility.addPinSuperViewEdgeConstraint(view: dividerLine, parentView: self, edge: _location == .top ? .bottom : .top)
                _dividerLine?.isHidden = _location == .none
            }
        }
        get { return _location }
    }

    
    // --
    // MARK: Initialization
    // --
    
    override init(frame: CGRect) {
        super.init(frame: frame)
        setupView()
    }
    
    required init?(coder aDecoder: NSCoder) {
        super.init(coder: aDecoder)
        setupView()
    }

    convenience init(location: AppConfigCellSectionLocation) {
        self.init(frame: CGRect.zero)
        setupDivider(location: location)
    }
    
    func setupView() {
        if #available(iOS 13.0, *) {
            self.backgroundColor = UIColor.secondarySystemBackground
        } else {
            self.backgroundColor = UIColor.init(white: 0.95, alpha: 1)
        }
    }
    
    func setupDivider(location: AppConfigCellSectionLocation) {
        _dividerLine = UIView()
        if #available(iOS 13.0, *) {
            _dividerLine?.backgroundColor = UIColor.opaqueSeparator
        } else {
            _dividerLine?.backgroundColor = UIColor.init(white: 0.75, alpha: 1)
        }
        if let addView = _dividerLine {
            addSubview(addView)
            AppConfigViewUtility.addPinSuperViewHorizontalEdgesConstraints(view: addView, parentView: self)
            AppConfigViewUtility.addHeightConstraint(view: addView, height: 1 / UIScreen.main.scale)
        }
        self.location = location
    }
    

    // --
    // MARK: Layout
    // --

    override var intrinsicContentSize : CGSize {
        return CGSize(width: 0, height: 8)
    }

}
