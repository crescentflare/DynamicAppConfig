//
//  AppConfigTableCell.swift
//  DynamicAppConfig Pod
//
//  Library table cell: shared component
//  Custom table view cell for dynamic table cells (in view and layout)
//  Used internally
//

import UIKit

class AppConfigTableCell : UITableViewCell {
    
    // --
    // MARK: Members
    // --
    
    var shouldHideDivider: Bool = false
    var _dividerLine: UIView?
    var _cellView: UIView?
    var cellView: UIView? {
        set {
            _cellView?.removeFromSuperview()
            _cellView = newValue
            if let addView = _cellView {
                contentView.addSubview(addView)
                AppConfigViewUtility.addPinSuperViewEdgesConstraints(view: addView, parentView: contentView)
            }
        }
        get { return _cellView }
    }

    
    // --
    // MARK: Initialize
    // --

    override init(style: UITableViewCell.CellStyle, reuseIdentifier: String?) {
        super.init(style: style, reuseIdentifier: reuseIdentifier)
        setupView()
    }
    
    required init?(coder aDecoder: NSCoder) {
        super.init(coder: aDecoder)
        setupView()
    }
    
    func setupView() {
        _dividerLine = UIView()
        if #available(iOS 13.0, *) {
            _dividerLine?.backgroundColor = UIColor.separator
        } else {
            _dividerLine?.backgroundColor = UIColor.init(white: 0.8, alpha: 1)
        }
        if let addView = _dividerLine {
            addSubview(addView)
            AppConfigViewUtility.addHeightConstraint(view: addView, height: 1 / UIScreen.main.scale)
            AppConfigViewUtility.addPinSuperViewEdgeConstraint(view: addView, parentView: self, edge: .left, constant: 16)
            AppConfigViewUtility.addPinSuperViewEdgeConstraint(view: addView, parentView: self, edge: .right)
            AppConfigViewUtility.addPinSuperViewEdgeConstraint(view: addView, parentView: self, edge: .bottom)
        }
    }

    
    // --
    // MARK: Layout
    // --
    
    override func layoutSubviews() {
        super.layoutSubviews()
        _dividerLine?.isHidden = shouldHideDivider
    }
    
}
