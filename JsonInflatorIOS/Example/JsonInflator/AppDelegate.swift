//
//  AppDelegate.swift
//  Json inflator example
//
//  The application delegate, handling global events while the app is running
//

import UIKit

@UIApplicationMain
class AppDelegate: UIResponder, UIApplicationDelegate {

    // --
    // MARK: Window member (used to contain the navigation controller)
    // --
    
    var window: UIWindow?
    
    
    // --
    // MARK: Lifecycle callbacks
    // --
    
    func application(_ application: UIApplication, didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey: Any]?) -> Bool {
        registerViewlets()
        window = UIWindow(frame: UIScreen.main.bounds)
        window?.backgroundColor = UIColor.white
        window?.rootViewController = UINavigationController(rootViewController: ViewController())
        window?.makeKeyAndVisible()
        return true
    }
    
    func applicationWillResignActive(_ application: UIApplication) {
        // No implementation
    }
    
    func applicationDidEnterBackground(_ application: UIApplication) {
        // No implementation
    }
    
    func applicationWillEnterForeground(_ application: UIApplication) {
        // No implementation
    }
    
    func applicationDidBecomeActive(_ application: UIApplication) {
        // No implementation
    }
    
    func applicationWillTerminate(_ application: UIApplication) {
        // No implementation
    }

    
    // --
    // MARK: Viewlet registration
    // --
    
    func registerViewlets() {
        ViewletCreator.shared.register(name: "view", inflatable: UIViewViewlet())
        ViewletCreator.shared.register(name: "viewContainer", inflatable: UIViewContainerViewlet())
        ViewletCreator.shared.register(name: "label", inflatable: UILabelViewlet())
        ViewletCreator.shared.register(name: "button", inflatable: UIButtonViewlet())
        ViewletCreator.shared.register(name: "textField", inflatable: UITextFieldViewlet())
        ViewletCreator.shared.register(name: "switch", inflatable: UISwitchViewlet())
    }
    
}

