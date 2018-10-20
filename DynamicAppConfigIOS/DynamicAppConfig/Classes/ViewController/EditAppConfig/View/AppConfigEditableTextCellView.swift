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
    private var _isEmpty = true

    
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
            _isEmpty = newValue?.count == 0
            _value!.text = _isEmpty ? AppConfigBundle.localizedString(key: "CFLAC_EDIT_VALUE_EMPTY") : newValue
            _value!.textColor = _isEmpty ? UIColor.gray : UIColor.black
            _value!.font = _isEmpty ? UIFont.italicSystemFont(ofSize: 14) : UIFont.systemFont(ofSize: 14)
        }
        get {
            if _isEmpty {
                return ""
            }
            return _value!.text
        }
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
        _value.textColor = UIColor.gray
        _value.text = AppConfigBundle.localizedString(key: "CFLAC_EDIT_VALUE_EMPTY")
        _value.font = UIFont.italicSystemFont(ofSize: 14)
    }
    
}
