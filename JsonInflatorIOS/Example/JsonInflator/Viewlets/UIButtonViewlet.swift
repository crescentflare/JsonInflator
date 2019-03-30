//
//  UIButtonViewlet.swift
//  Json inflator example
//
//  Create a simple button
//

import UIKit
import JsonInflator

class UIButtonViewlet: JsonInflatable {

    func create() -> Any {
        return UIButton()
    }
    
    func update(convUtil: InflatorConvUtil, object: Any, attributes: [String: Any], parent: Any?, binder: InflatorBinder?) -> Bool {
        if let button = object as? UIButton {
            // Text
            button.setTitle(NSLocalizedString(convUtil.asString(value: attributes["text"]) ?? "", comment: ""), for: .normal)
            
            // Text style
            let fontSize = convUtil.asDimension(value: attributes["textSize"]) ?? 17
            if let font = convUtil.asString(value: attributes["font"]) {
                if font == "bold" {
                    button.titleLabel?.font = UIFont.boldSystemFont(ofSize: fontSize)
                } else if font == "italics" {
                    button.titleLabel?.font = UIFont.italicSystemFont(ofSize: fontSize)
                } else {
                    button.titleLabel?.font = UIFont(name: font, size: fontSize)
                }
            } else {
                button.titleLabel?.font = UIFont.systemFont(ofSize: fontSize)
            }
            button.setTitleColor(convUtil.asColor(value: attributes["textColor"]) ?? button.tintColor, for: .normal)
            
            // Standard view attributes
            UIViewViewlet.applyDefaultAttributes(convUtil: convUtil, view: button, attributes: attributes)
            return true
        }
        return false
    }
    
    func canRecycle(convUtil: InflatorConvUtil, object: Any, attributes: [String : Any]) -> Bool {
        return object is UIButton
    }

}
