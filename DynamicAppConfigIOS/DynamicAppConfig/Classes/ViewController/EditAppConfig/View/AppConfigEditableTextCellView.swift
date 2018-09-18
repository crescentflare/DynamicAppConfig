//
//  AppConfigEditableTextCellView.swift
//  DynamicAppConfig Pod
//
//  Library view: edit configuration
//  A cell to edit a section by entering text or a number
//

import UIKit

// View component
@IBDesignable class AppConfigEditableTextCellView : UIView, UITextFieldDelegate {
    
    // --
    // MARK: Members
    // --
    
    private var _contentView: UIView! = nil
    @IBOutlet private var _label: UILabel! = nil
    @IBOutlet private var _value: UILabel! = nil
    private var _applyNumberLimitation = false

    
    // --
    // MARK: Properties which can be used in interface builder
    // --
    
    @IBInspectable var labelText: String = "" {
        didSet {
            label = labelText
        }
    }
    
    @IBInspectable var valueText: String = "" {
        didSet {
            value = valueText
        }
    }

    @IBInspectable var limitedToNumbers: Bool = false {
        didSet {
            applyNumberLimitation = limitedToNumbers
        }
    }

    var label: String? {
        set {
            _label!.text = newValue
        }
        get { return _label!.text }
    }

    var value: String? {
        set {
            _value!.text = newValue
        }
        get { return _value!.text }
    }
    
    var applyNumberLimitation: Bool {
        set {
            _applyNumberLimitation = newValue
        }
        get { return _applyNumberLimitation }
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
    
    func setupView() {
        _contentView = AppConfigViewUtility.loadNib(named: "EditableTextCell", parentView: self)
        _label.textColor = tintColor
        _label.text = ""
        _value.textColor = UIColor.black
        _value.text = ""
    }
    
}
