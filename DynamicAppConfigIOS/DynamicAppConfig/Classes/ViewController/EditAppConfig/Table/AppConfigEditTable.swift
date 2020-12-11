//
//  AppConfigEditTable.swift
//  DynamicAppConfig Pod
//
//  Library table: edit configuration
//  The table view and data source for the edit config viewcontroller
//  Used internally
//

import UIKit

// Delegate protocol
protocol AppConfigEditTableDelegate: class {

    func saveConfig(newSettings: [String: Any])
    func cancelEditing()
    func revertConfig()
    func configChanged(newSettings: [String: Any])

}


// Class
class AppConfigEditTable : UIView, UITableViewDataSource, UITableViewDelegate, AppConfigEditSwitchCellViewDelegate, AppConfigSelectionPopupViewDelegate {
    
    // --
    // MARK: Members
    // --
    
    weak var delegate: AppConfigEditTableDelegate?
    var tableValues: [AppConfigEditTableValue] = []
    var configName = ""
    var newConfig = false
    let table = UITableView()

    
    // --
    // MARK: Initialization
    // --
    
    override init(frame : CGRect) {
        super.init(frame: frame)
        initialize()
    }
    
    convenience init() {
        self.init(frame: CGRect.zero)
    }
    
    required init?(coder aDecoder: NSCoder) {
        super.init(coder: aDecoder)
        initialize()
    }
    
    func initialize () {
        // Set up table view
        let tableFooter = UIView()
        if #available(iOS 13.0, *) {
            table.backgroundColor = UIColor.secondarySystemBackground
        } else {
            table.backgroundColor = UIColor.init(white: 0.95, alpha: 1)
        }
        tableFooter.frame = CGRect(x: 0, y: 0, width: 0, height: 8)
        table.tableFooterView = tableFooter
        addSubview(table)
        AppConfigViewUtility.addPinSuperViewEdgesConstraints(view: table, parentView: self)
        
        // Set table view properties
        table.rowHeight = UITableView.automaticDimension
        table.estimatedRowHeight = 40
        table.dataSource = self
        table.delegate = self
        table.separatorStyle = .none
        
        // Show loading indicator by default
        tableValues.append(AppConfigEditTableValue.valueForLoading(text: AppConfigBundle.localizedString(key: "CFLAC_SHARED_LOADING_CONFIGS")))
    }

    
    // --
    // MARK: Implementation
    // --
    
    func setConfiguration(settings: [String: Any], model: AppConfigBaseModel?) {
        // Add editable fields
        var rawTableValues: [AppConfigEditTableValue] = []
        tableValues = []
        if settings.count > 0 {
            // First add the name section + field for custom configurations
            let customizedCopy = newConfig || (AppConfigStorage.shared.isCustomConfig(config: configName) && !AppConfigStorage.shared.isConfigOverride(config: configName))
            if customizedCopy {
                for (key, value) in settings {
                    if key == "name" {
                        rawTableValues.append(AppConfigEditTableValue.valueForSection(text: AppConfigBundle.localizedString(key: "CFLAC_EDIT_SECTION_NAME")))
                        rawTableValues.append(AppConfigEditTableValue.valueForTextEntry(configSetting: key, andValue: value as? String ?? "", numberOnly: false))
                        break;
                    }
                }
            }

            // Add configuration values
            if let categorizedFields = model?.obtainConfigurationCategorizedFields() {
                // Using model and optional categories
                let modelValues = model?.obtainConfigurationValues() ?? [:]
                let hasMultipleCategories = categorizedFields.allKeys().count > 1
                var sortedCategories: [String] = []
                for category in categorizedFields.allKeys() {
                    if category.count > 0 {
                        sortedCategories.append(category)
                    }
                }
                for category in categorizedFields.allKeys() {
                    if category.count == 0 {
                        sortedCategories.append("")
                        break
                    }
                }
                for category in sortedCategories {
                    let categoryName = category.count > 0 ? category : AppConfigBundle.localizedString(key: "CFLAC_EDIT_SECTION_UNCATEGORIZED")
                    var configSectionAdded = false
                    for field in categorizedFields[category] ?? [] {
                        if field == "name" {
                            continue
                        }
                        if !configSectionAdded {
                            var baseCategoryName = ""
                            if customizedCopy {
                                baseCategoryName = AppConfigBundle.localizedString(key: "CFLAC_EDIT_SECTION_SETTINGS")
                            } else {
                                baseCategoryName = configName
                            }
                            if hasMultipleCategories {
                                baseCategoryName += ": " + categoryName
                            }
                            rawTableValues.append(AppConfigEditTableValue.valueForSection(text: baseCategoryName))
                            configSectionAdded = true
                        }
                        if modelValues[field] is Bool {
                            rawTableValues.append(AppConfigEditTableValue.valueForSwitchValue(configSetting: field, andSwitchedOn: settings[field] as? Bool ?? false))
                            continue
                        }
                        if model?.isRawRepresentable(field: field) ?? false {
                            let choices: [String] = model?.getRawRepresentableValues(forField: field) ?? []
                            rawTableValues.append(AppConfigEditTableValue.valueForSelection(configSetting: field, andValue: settings[field] as? String ?? "", andChoices: choices))
                            continue
                        }
                        if modelValues[field] is Int {
                            rawTableValues.append(AppConfigEditTableValue.valueForTextEntry(configSetting: field, andValue: "\(settings[field] ?? 0)", numberOnly: true))
                        } else {
                            rawTableValues.append(AppConfigEditTableValue.valueForTextEntry(configSetting: field, andValue: "\(settings[field] ?? "")", numberOnly: false))
                        }
                    }
                }
            } else {
                // Using raw dictionary
                var configSectionAdded = false
                for (key, value) in settings {
                    if key == "name" {
                        continue
                    }
                    if !configSectionAdded {
                        if customizedCopy {
                            rawTableValues.append(AppConfigEditTableValue.valueForSection(text: AppConfigBundle.localizedString(key: "CFLAC_EDIT_SECTION_SETTINGS")))
                        } else {
                            rawTableValues.append(AppConfigEditTableValue.valueForSection(text: configName))
                        }
                        configSectionAdded = true
                    }
                    if value is Bool {
                        rawTableValues.append(AppConfigEditTableValue.valueForSwitchValue(configSetting: key, andSwitchedOn: value as? Bool ?? false))
                        continue
                    }
                    rawTableValues.append(AppConfigEditTableValue.valueForTextEntry(configSetting: key, andValue: "\(value)", numberOnly: value is Int))
                }
            }
        }

        // Add actions
        rawTableValues.append(AppConfigEditTableValue.valueForSection(text: AppConfigBundle.localizedString(key: "CFLAC_EDIT_SECTION_ACTIONS")))
        if newConfig {
            rawTableValues.append(AppConfigEditTableValue.valueForAction(.save, andText: AppConfigBundle.localizedString(key: "CFLAC_EDIT_ACTION_CREATE")))
        } else {
            rawTableValues.append(AppConfigEditTableValue.valueForAction(.save, andText: AppConfigBundle.localizedString(key: "CFLAC_EDIT_ACTION_APPLY")))
        }
        if !newConfig {
            if !AppConfigStorage.shared.isCustomConfig(config: configName) || AppConfigStorage.shared.isConfigOverride(config: configName) {
                rawTableValues.append(AppConfigEditTableValue.valueForAction(.revert, andText: AppConfigBundle.localizedString(key: "CFLAC_EDIT_ACTION_RESET")))
            } else {
                rawTableValues.append(AppConfigEditTableValue.valueForAction(.revert, andText: AppConfigBundle.localizedString(key: "CFLAC_EDIT_ACTION_DELETE")))
            }
        }
        rawTableValues.append(AppConfigEditTableValue.valueForAction(.cancel, andText: AppConfigBundle.localizedString(key: "CFLAC_EDIT_ACTION_CANCEL")))

        // Style table by adding dividers and reload
        var prevType: AppConfigEditTableValueType = .unknown
        for tableValue in rawTableValues {
            if !prevType.isCellType() && tableValue.type.isCellType() {
                tableValues.append(AppConfigEditTableValue.valueForDivider(type: .topDivider))
            } else if prevType.isCellType() && !tableValue.type.isCellType() {
                tableValues.append(AppConfigEditTableValue.valueForDivider(type: .bottomDivider))
            } else if !prevType.isCellType() && !tableValue.type.isCellType() {
                tableValues.append(AppConfigEditTableValue.valueForDivider(type: .betweenDivider))
            }
            tableValues.append(tableValue)
            prevType = tableValue.type
        }
        if prevType.isCellType() {
            tableValues.append(AppConfigEditTableValue.valueForDivider(type: .bottomDivider))
        } else {
            tableValues.append(AppConfigEditTableValue.valueForDivider(type: .betweenDivider))
        }
        table.reloadData()
    }
    
    func obtainNewConfigurationSettings() -> [String: Any] {
        var result: [String: Any] = [:]
        result["name"] = configName
        for tableValue in tableValues {
            switch tableValue.type {
            case .textEntry:
                if tableValue.limitUsage {
                    let formatter = NumberFormatter()
                    formatter.numberStyle = .decimal
                    result[tableValue.configSetting!] = formatter.number(from: tableValue.labelString)
                } else {
                    result[tableValue.configSetting!] = tableValue.labelString
                }
                break
            case .switchValue:
                result[tableValue.configSetting!] = tableValue.booleanValue
                break
            case .selection:
                result[tableValue.configSetting!] = tableValue.labelString
                break
            default:
                break // Others are not editable cells
            }
        }
        return result
    }

    
    // --
    // MARK: UITableViewDataSource
    // --
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return tableValues.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        // Create cell (if needed)
        let tableValue = tableValues[indexPath.row]
        let nextType = indexPath.row + 1 < tableValues.count ? tableValues[indexPath.row + 1].type : AppConfigEditTableValueType.unknown
        let cell = tableView.dequeueReusableCell(withIdentifier: tableValue.type.rawValue) as? AppConfigTableCell ?? AppConfigTableCell()
        
        // Set up a loader cell
        if tableValue.type == .loading {
            // Create view
            if cell.cellView == nil {
                cell.cellView = AppConfigLoadingCellView()
            }
            let cellView = cell.cellView as? AppConfigLoadingCellView

            // Supply data
            cell.selectionStyle = .none
            cell.shouldHideDivider = !nextType.isCellType()
            cellView?.label = tableValue.labelString
        }

        // Set up an action cell
        if tableValue.type == .action {
            // Create view
            if cell.cellView == nil {
                cell.cellView = AppConfigItemCellView()
            }
            let cellView = cell.cellView as? AppConfigItemCellView
            
            // Supply data
            cell.selectionStyle = .default
            cell.accessoryType = .disclosureIndicator
            cell.shouldHideDivider = !nextType.isCellType()
            cellView?.label = tableValue.labelString
        }
        
        // Set up a text entry cell
        if tableValue.type == .textEntry {
            // Create view
            if cell.cellView == nil {
                cell.cellView = AppConfigEditableTextCellView()
            }
            let cellView = cell.cellView as? AppConfigEditableTextCellView
            
            // Supply data
            cell.selectionStyle = .default
            cell.shouldHideDivider = !nextType.isCellType()
            cell.accessibilityIdentifier = tableValue.configSetting
            cellView?.label = tableValue.configSetting
            cellView?.value = tableValue.labelString
            cellView?.applyNumberLimitation = tableValue.limitUsage
        }
        
        // Set up a switch value cell
        if tableValue.type == .switchValue {
            // Create view
            if cell.cellView == nil {
                cell.cellView = AppConfigEditSwitchCellView()
            }
            let cellView = cell.cellView as? AppConfigEditSwitchCellView

            // Supply data
            cell.selectionStyle = .default
            cell.shouldHideDivider = !nextType.isCellType()
            cell.accessibilityIdentifier = tableValue.configSetting
            cellView?.delegate = self
            cellView?.label = tableValue.configSetting
            cellView?.on = tableValue.booleanValue
        }
        
        // Set up a selection cell (for enums)
        if tableValue.type == .selection {
            // Create view
            if cell.cellView == nil {
                cell.cellView = AppConfigItemCellView()
            }
            let cellView = cell.cellView as? AppConfigItemCellView
            
            // Supply data
            cell.selectionStyle = .default
            cell.accessoryType = .disclosureIndicator
            cell.shouldHideDivider = !nextType.isCellType()
            cell.accessibilityIdentifier = tableValue.configSetting
            cellView?.label = "\(tableValue.configSetting ?? ""): \(tableValue.labelString)"
        }
        
        // Set up a section cell
        if tableValue.type == .section {
            // Create view
            if cell.cellView == nil {
                cell.cellView = AppConfigSectionCellView()
            }
            let cellView = cell.cellView as? AppConfigSectionCellView
            
            // Supply data
            cell.selectionStyle = .none
            cell.shouldHideDivider = true
            cellView?.label = tableValue.labelString
        }
        
        // Set up a divider cell
        if tableValue.type == .topDivider || tableValue.type == .bottomDivider || tableValue.type == .betweenDivider {
            // Create view
            if cell.cellView == nil {
                if tableValue.type == .betweenDivider {
                    cell.cellView = AppConfigCellSectionDividerView()
                } else {
                    cell.cellView = AppConfigCellSectionDividerView(location: tableValue.type == .topDivider ? .top : .bottom)
                }
            }
            
            // Supply data
            cell.selectionStyle = .none
            cell.shouldHideDivider = true
        }

        // Return result
        return cell
    }
    
    
    // --
    // MARK: UITableViewDelegate
    // --
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        let tableValue = tableValues[indexPath.row]
        UIApplication.shared.sendAction(#selector(resignFirstResponder), to: nil, from: nil, for: nil)
        if tableValue.type == .action && delegate != nil {
            switch tableValue.action {
            case .save:
                delegate?.saveConfig(newSettings: obtainNewConfigurationSettings())
                break
            case .cancel:
                delegate?.cancelEditing()
                break
            case .revert:
                delegate?.revertConfig()
                break
            default:
                break
            }
        } else if tableValue.type == .selection {
            let choicePopup = AppConfigSelectionPopupView()
            choicePopup.label = tableValue.configSetting
            choicePopup.choices = tableValue.selectionItems ?? []
            choicePopup.delegate = self
            choicePopup.token = tableValue.configSetting
            choicePopup.addToSuperview(self)
            AppConfigViewUtility.addPinSuperViewEdgesConstraints(view: choicePopup, parentView: self)
        } else if tableValue.type == .switchValue {
            if let cell = tableView.cellForRow(at: indexPath) as? AppConfigTableCell {
                if let switchCellView = cell.cellView as? AppConfigEditSwitchCellView {
                    switchCellView.toggleState()
                }
            }
        } else if tableValue.type == .textEntry {
            if let cell = tableView.cellForRow(at: indexPath) as? AppConfigTableCell {
                if let editTextCellView = cell.cellView as? AppConfigEditableTextCellView {
                    // Create alert box
                    let alert = UIAlertController(title: AppConfigBundle.localizedString(key: "CFLAC_SHARED_DIALOG_EDIT_VALUE_PREFIX") + " " + (editTextCellView.label ?? ""), message: nil, preferredStyle: .alert)
                    alert.addTextField { (textField) in
                        textField.text = editTextCellView.value
                        textField.placeholder = editTextCellView.label
                        textField.keyboardType = editTextCellView.applyNumberLimitation ? .numbersAndPunctuation : .alphabet
                    }
                    
                    // Observe buttons and present
                    alert.addAction(UIAlertAction(title: AppConfigBundle.localizedString(key: "CFLAC_SHARED_OK"), style: .default, handler: { [weak alert] (_) in
                        let enteredText = alert?.textFields?[0].text ?? ""
                        self.tableValues[indexPath.row] = AppConfigEditTableValue.valueForTextEntry(configSetting: tableValue.configSetting ?? "", andValue: enteredText, numberOnly: tableValue.limitUsage)
                        tableView.reloadRows(at: [indexPath], with: .none)
                        self.delegate?.configChanged(newSettings: self.obtainNewConfigurationSettings())
                    }))
                    alert.addAction(UIAlertAction(title: AppConfigBundle.localizedString(key: "CFLAC_SHARED_CANCEL"), style: .cancel, handler: { _ in
                        // No implementation
                    }))
                    (delegate as? UIViewController)?.present(alert, animated: true, completion: nil)
                }
            }
        }
        table.deselectRow(at: indexPath, animated: false)
    }
    

    // --
    // MARK: AppConfigEditSwitchCellViewDelegate
    // --
    
    func changedSwitchState(_ on: Bool, forConfigSetting: String) {
        for i in 0..<tableValues.count {
            let tableValue = tableValues[i]
            if tableValue.configSetting == forConfigSetting {
                tableValues[i] = AppConfigEditTableValue.valueForSwitchValue(configSetting: tableValue.configSetting!, andSwitchedOn: on)
                delegate?.configChanged(newSettings: obtainNewConfigurationSettings())
                break
            }
        }
    }

    
    // --
    // MARK: AppConfigSelectionPopupViewDelegate
    // --
    
    func selectedItem(_ item: String, token: String?) {
        for i in 0..<tableValues.count {
            let tableValue = tableValues[i]
            if tableValue.configSetting == token {
                let totalIndexPath = IndexPath.init(row: i, section: 0)
                tableValues[i] = AppConfigEditTableValue.valueForSelection(configSetting: tableValue.configSetting!, andValue: item, andChoices: tableValue.selectionItems!)
                table.beginUpdates()
                table.reloadRows(at: [totalIndexPath], with: .none)
                table.endUpdates()
                delegate?.configChanged(newSettings: obtainNewConfigurationSettings())
                break
            }
        }
    }
    
}
