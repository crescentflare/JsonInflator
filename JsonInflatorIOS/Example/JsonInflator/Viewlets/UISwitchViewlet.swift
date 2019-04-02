//
//  UISwitchViewlet.swift
//  Json inflator example
//
//  Create a simple switch
//

import UIKit
import JsonInflator

class UISwitchViewlet: JsonInflatable {

    func create() -> Any {
        return UISwitch()
    }
    
    func update(convUtil: InflatorConvUtil, object: Any, attributes: [String: Any], parent: Any?, binder: InflatorBinder?) -> Bool {
        if let switchControl = object as? UISwitch {
            // Default state
            switchControl.setOn(convUtil.asBool(value: attributes["on"]) ?? false, animated: false)

            // Standard view attributes
            UIViewViewlet.applyDefaultAttributes(convUtil: convUtil, view: switchControl, attributes: attributes)
            return true
        }
        return false
    }
    
    func canRecycle(convUtil: InflatorConvUtil, object: Any, attributes: [String : Any]) -> Bool {
        return object is UISwitch
    }

}
