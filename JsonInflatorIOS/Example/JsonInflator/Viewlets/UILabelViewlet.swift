//
//  UILabelViewlet.swift
//  Json inflator example
//
//  Create a simple label
//

import UIKit
import JsonInflator

class UILabelViewlet: JsonInflatable {

    func create() -> Any {
        return UILabel()
    }
    
    func update(convUtil: InflatorConvUtil, object: Any, attributes: [String: Any], parent: Any?, binder: InflatorBinder?) -> Bool {
        if let label = object as? UILabel {
            // Text
            label.text = NSLocalizedString(convUtil.asString(value: attributes["text"]) ?? "", comment: "")
            
            // Text style
            let fontSize = convUtil.asDimension(value: attributes["textSize"]) ?? 17
            if let font = convUtil.asString(value: attributes["font"]) {
                if font == "bold" {
                    label.font = UIFont.boldSystemFont(ofSize: fontSize)
                } else if font == "italics" {
                    label.font = UIFont.italicSystemFont(ofSize: fontSize)
                } else {
                    label.font = UIFont(name: font, size: fontSize)
                }
            } else {
                label.font = UIFont.systemFont(ofSize: fontSize)
            }
            label.textColor = convUtil.asColor(value: attributes["textColor"]) ?? UIColor.darkText
            
            // Other properties
            label.numberOfLines = convUtil.asInt(value: attributes["maxLines"]) ?? 0
            if let textAlignment = convUtil.asString(value: attributes["textAlignment"]) {
                if textAlignment == "center" {
                    label.textAlignment = .center
                } else if textAlignment == "right" {
                    label.textAlignment = .right
                }
            }

            // Standard view attributes
            UIViewViewlet.applyDefaultAttributes(convUtil: convUtil, view: label, attributes: attributes)
            return true
        }
        return false
    }
    
    func canRecycle(convUtil: InflatorConvUtil, object: Any, attributes: [String : Any]) -> Bool {
        return object is UILabel
    }

}
