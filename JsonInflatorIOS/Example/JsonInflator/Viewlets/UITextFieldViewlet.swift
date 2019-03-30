//
//  UITextFieldViewlet.swift
//  Json inflator example
//
//  Create a simple text input field
//

import UIKit
import JsonInflator

class UITextFieldViewlet: JsonInflatable {

    func create() -> Any {
        return UITextField()
    }
    
    func update(convUtil: InflatorConvUtil, object: Any, attributes: [String: Any], parent: Any?, binder: InflatorBinder?) -> Bool {
        if let textField = object as? UITextField {
            // Prefilled text and placeholder
            textField.text = convUtil.asString(value: attributes["text"])
            textField.placeholder = NSLocalizedString(convUtil.asString(value: attributes["placeholder"]) ?? "", comment: "")
            
            // Set keyboard mode
            textField.keyboardType = .default
            textField.autocapitalizationType = .sentences
            if let keyboardType = convUtil.asString(value: attributes["keyboardType"]) {
                if keyboardType == "email" {
                    textField.keyboardType = .emailAddress
                    textField.autocapitalizationType = .none
                } else if keyboardType == "url" {
                    textField.keyboardType = .URL
                    textField.autocapitalizationType = .none
                }
            }
            
            // Text style
            let fontSize = convUtil.asDimension(value: attributes["textSize"]) ?? 17
            if let font = convUtil.asString(value: attributes["font"]) {
                if font == "bold" {
                    textField.font = UIFont.boldSystemFont(ofSize: fontSize)
                } else if font == "italics" {
                    textField.font = UIFont.italicSystemFont(ofSize: fontSize)
                } else {
                    textField.font = UIFont(name: font, size: fontSize)
                }
            } else {
                textField.font = UIFont.systemFont(ofSize: fontSize)
            }
            
            // Standard view attributes
            UIViewViewlet.applyDefaultAttributes(convUtil: convUtil, view: textField, attributes: attributes)
            return true
        }
        return false
    }
    
    func canRecycle(convUtil: InflatorConvUtil, object: Any, attributes: [String : Any]) -> Bool {
        return object is UITextField
    }

}
