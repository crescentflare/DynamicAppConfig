//
//  AppConfigSelectionPopupView.swift
//  DynamicAppConfig Pod
//
//  Library view: shared component
//  A popup with a table to select from
//

import UIKit

// Delegate protocol
protocol AppConfigSelectionPopupViewDelegate: class {
    
    func selectedItem(_ item: String, token: String?)
    
}

// View component
@IBDesignable class AppConfigSelectionPopupView : UIView, UITableViewDataSource, UITableViewDelegate {
    
    // --
    // MARK: Members
    // --
    
    weak var delegate: AppConfigSelectionPopupViewDelegate?
    var token: String?
    private var _contentView: UIView! = nil
    @IBOutlet private var _label: UILabel! = nil
    @IBOutlet private var _tableView: UITableView! = nil
    private var _tableChoices: [String] = []

    
    // --
    // MARK: Properties which can be used in interface builder
    // --
    
    @IBInspectable var labelText: String = "" {
        didSet {
            label = labelText
        }
    }
    
    @IBInspectable var tableChoices: String = "" {
        didSet {
            choices = tableChoices.split{ $0 == "," }.map(String.init)
        }
    }

    var label: String? {
        set {
            _label!.text = newValue
        }
        get { return _label!.text }
    }
    
    var choices: [String] {
        set {
            _tableChoices = newValue
            _tableView.reloadData()
        }
        get { return _tableChoices }
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
        // Load nib to content view
        _contentView = AppConfigViewUtility.loadNib(named: "SelectionPopup", parentView: self)

        // Set up table view
        let tableFooter = UIView()
        if #available(iOS 13.0, *) {
            _tableView.backgroundColor = UIColor.secondarySystemBackground
        } else {
            _tableView.backgroundColor = UIColor.init(white: 0.95, alpha: 1)
        }
        tableFooter.frame = CGRect(x: 0, y: 0, width: 0, height: 8)
        _tableView.tableFooterView = tableFooter
        
        // Set table view properties
        _tableView.rowHeight = UITableView.automaticDimension
        _tableView.estimatedRowHeight = 40
        _tableView.dataSource = self
        _tableView.delegate = self
        _tableView.separatorStyle = .none

        // Empty state
        _label.text = ""
    }

    
    // --
    // MARK: Add/remove
    // --
    
    @IBAction private func closePopup() {
        dismiss()
    }
    
    func dismiss(animated: Bool = true) {
        if (animated) {
            UIView.animate(withDuration: 0.25, animations: { () -> Void in
                self.transform = CGAffineTransform(scaleX: 1.3, y: 1.3)
                self.alpha = 0
            }, completion: { (finished: Bool) -> Void in
                if finished {
                    self.removeFromSuperview()
                }
            })
        } else {
            removeFromSuperview()
        }
    }
    
    func addToSuperview(_ superView: UIView, animated: Bool = true) {
        superView.addSubview(self)
        transform = CGAffineTransform(scaleX: 1.3, y: 1.3)
        alpha = 0
        if (animated) {
            UIView.animate(withDuration: 0.25, animations: { () -> Void in
                self.transform = CGAffineTransform(scaleX: 1, y: 1)
                self.alpha = 1
            }, completion: { (finished: Bool) -> Void in
            })
        }
    }
    

    // --
    // MARK: UITableViewDataSource
    // --
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return _tableChoices.count + 1
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        // Create cell (if needed)
        let cell = tableView.dequeueReusableCell(withIdentifier: "ignored") as? AppConfigTableCell ?? AppConfigTableCell()
        
        // Regular cell view
        if indexPath.row < _tableChoices.count {
            // Create view
            if cell.cellView == nil {
                cell.cellView = AppConfigItemCellView()
            }
            let cellView = cell.cellView as? AppConfigItemCellView
            
            // Supply data and return the cell
            cell.selectionStyle = .default
            cell.accessoryType = .disclosureIndicator
            cell.shouldHideDivider = indexPath.row + 1 >= _tableChoices.count
            cellView?.label = _tableChoices[indexPath.row]
        }
        
        // Bottom divider
        if indexPath.row >= _tableChoices.count {
            // Create view
            if cell.cellView == nil {
                cell.cellView = AppConfigCellSectionDividerView(location: .bottom)
            }
            
            // Supply data
            cell.selectionStyle = .none
            cell.shouldHideDivider = true
        }
        
        return cell
    }
    

    // --
    // MARK: UITableViewDelegate
    // --
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        _tableView.deselectRow(at: indexPath, animated: false)
        if indexPath.row < _tableChoices.count {
            if delegate != nil {
                delegate?.selectedItem(_tableChoices[indexPath.row], token: token)
            }
            dismiss()
        }
    }

}
