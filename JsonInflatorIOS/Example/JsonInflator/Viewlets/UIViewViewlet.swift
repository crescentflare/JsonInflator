//
//  UIViewViewlet.swift
//  Json inflator example
//
//  Create a simple view
//

import UIKit
import JsonInflator

class UIViewViewlet: JsonInflatable {

    // --
    // MARK: Implementation
    // --

    func create() -> Any {
        return UIView()
    }
    
    func update(convUtil: InflatorConvUtil, object: Any, attributes: [String: Any], parent: Any?, binder: InflatorBinder?) -> Bool {
        if let view = object as? UIView {
            UIViewViewlet.applyDefaultAttributes(convUtil: convUtil, view: view, attributes: attributes)
        }
        return true
    }
    
    func canRecycle(convUtil: InflatorConvUtil, object: Any, attributes: [String : Any]) -> Bool {
        return false
    }
    
    
    // --
    // MARK: Shared
    // --
    
    static func applyDefaultAttributes(convUtil: InflatorConvUtil, view: UIView, attributes: [String: Any]) {
        view.backgroundColor = convUtil.asColor(value: attributes["backgroundColor"]) ?? UIColor.clear
        view.isHidden = convUtil.asBool(value: attributes["hidden"]) ?? false
    }
    
}
