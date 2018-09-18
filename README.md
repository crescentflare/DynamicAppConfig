# DynamicAppConfig

[![CI Status](http://img.shields.io/travis/crescentflare/DynamicAppConfig.svg?style=flat)](https://travis-ci.org/crescentflare/DynamicAppConfig)
[![License](https://img.shields.io/cocoapods/l/DynamicAppConfig.svg?style=flat)](http://cocoapods.org/pods/DynamicAppConfig)
[![Version](https://img.shields.io/cocoapods/v/DynamicAppConfig.svg?style=flat)](http://cocoapods.org/pods/DynamicAppConfig)
[![Version](https://img.shields.io/bintray/v/crescentflare/maven/DynamicAppConfigLib.svg?style=flat)](https://bintray.com/crescentflare/maven/DynamicAppConfigLib)

A useful library to support multiple build configurations or global settings in one application build.

For example: be able to make one build with a build selector that contains development, test, acceptance and a production configuration. There would be no need to deliver multiple builds for each environment for testing, it can all be done from one build.


### Features

- Be able to configure several app configurations using JSON (Android) or a plist file (iOS)
- A built-in app configuration selection screen
- Edit app configurations to customize them from within the app
- Easily access the currently selected configuration (or last stored selection) everywhere
- Separate global settings which work across different configurations
- Be able to write custom plugins, like development tools, making them accessible through the selection menu
- Dynamic configurations can be disabled to prevent them from being available on distribution (App Store) or Google Play builds
