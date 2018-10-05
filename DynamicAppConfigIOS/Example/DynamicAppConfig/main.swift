//
//  main.swift
//  DynamicAppConfig Example
//
//  The main swift file to activate the custom application class
//

import UIKit

_ = UIApplicationMain(
    CommandLine.argc,
    CommandLine.unsafeArgv,
    NSStringFromClass(Application.self),
    NSStringFromClass(AppDelegate.self)
)
