//
//  Application.swift
//  DynamicAppConfig Example
//
//  Subclasses UIApplication to support shake gestures
//

import UIKit
import DynamicAppConfig

class Application: UIApplication {

    override func sendEvent(_ event: UIEvent) {
        super.sendEvent(event)
        if event.subtype == .motionShake {
            AppConfigManageViewController.launch()
        }
    }

}
