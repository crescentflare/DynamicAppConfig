# DynamicAppConfig

[![CI Status](http://img.shields.io/travis/crescentflare/DynamicAppConfig.svg?style=flat)](https://travis-ci.org/crescentflare/DynamicAppConfig)
[![License](https://img.shields.io/cocoapods/l/DynamicAppConfig.svg?style=flat)](http://cocoapods.org/pods/DynamicAppConfig)
[![Version](https://img.shields.io/cocoapods/v/DynamicAppConfig.svg?style=flat)](http://cocoapods.org/pods/DynamicAppConfig)
[![Version](https://img.shields.io/bintray/v/crescentflare/maven/DynamicAppConfigLib.svg?style=flat)](https://bintray.com/crescentflare/maven/DynamicAppConfigLib)

A useful library to support multiple build configurations or global settings in one application build.

For example: be able to make one build with a build selector that contains development, test, acceptance and a production configuration. There would be no need to deliver multiple builds for each environment for testing, it can all be done from one build.

✓ Supports iOS and Android\
✓ Lightweight\
✓ Dependency free\
✓ Ready for test automation\
✓ Proven in multiple projects

![iOS screenshot](https://raw.githubusercontent.com/crescentflare/DynamicAppConfig/develop/Screenshots/screenshot_ios.png)&nbsp;&nbsp;
![Android screenshot](https://raw.githubusercontent.com/crescentflare/DynamicAppConfig/develop/Screenshots/screenshot_android.png)


## Features

- Be able to configure several app configurations using JSON (Android) or a plist file (iOS)
- A built-in app configuration selection screen
- Edit app configurations to customize them from within the app
- Easily access the currently selected configuration (or last stored selection) everywhere
- Separate global settings which work across different configurations
- Be able to write custom plugins, like development tools, making them accessible through the selection menu
- Dynamic configurations can be disabled to prevent them from being available on distribution (App Store) or Google Play builds


### iOS Integration guide

The library is available through [CocoaPods](http://cocoapods.org). To install it, simply add one of the following lines to your Podfile.

```ruby
pod "DynamicAppConfig", '~> 1.3.4'
```

The above version is for Swift 5.0. For older Swift versions use the following:
- Swift 4.2: DynamicAppConfig 1.3.0
- Swift 4.1: DynamicAppConfig 1.2.0
- Other swift versions: The old AppConfigSwift library, available [here](https://github.com/crescentflare/AppConfigSwift)


### Android Integration guide
When using gradle, the library can easily be imported into the build.gradle file of your project. Add the following dependency:

```
implementation 'com.crescentflare.dynamicappconfig:DynamicAppConfigLib:1.3.0'
```

Make sure that jcenter is added as a repository. The above is for Android API level 14 and higher. To support older Android versions use the old library, available [here](https://github.com/crescentflare/DynamicAppConfigAndroid).


### Integration documentation

An explanation on how to further integrate and use the library itself is available on the [wiki](https://github.com/crescentflare/DynamicAppConfig/wiki). It contains documentation for both iOS and Android separately.


### Storage

When existing configurations are edited or custom ones are being added, the changes are saved in the user preferences (Android) or user defaults (iOS) of the device. Also the last selected configuration and global settings are stored there. This makes sure that it remembers the correct settings, even if the app is restarted. It also works correctly when an Android app is closed silently when the device is running out of memory.


### Automated testing

The library is ready for automated testing using Espresso (for Android) and UI testing (for iOS). The example project provides a demonstration on how to modify the configuration within automated test scripts.


### Security

Because the library can give a lot of control on the product (by making its settings configurable), it's important to prevent any code (either the selection menu itself, or the JSON/plist configuration data like test servers and passwords) from being deployed to Google Play or the App Store. Take a look at the example project for more information. For the release configuration it doesn't activate the app config and places an empty configuration json in the asset folder (for that specific configuration) on Android, or just excludes the plist file (on iOS).


### Example

The provided example shows how to set up a configuration model, define configuration settings and launch the configuration tool. It also includes a demo of using global settings and a custom logging tool.


### Status

The library is stable and has been used in many projects. Improvements may be added in the future.
