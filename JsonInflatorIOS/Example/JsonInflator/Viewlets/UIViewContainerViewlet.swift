//
//  UIViewContainerViewlet.swift
//  Json inflator example
//
//  Create a simple view container, subclass UIView to be able to recognize it
//

import UIKit
import JsonInflator

class UIViewContainer: UIView {
}

class ViewletConstraint: NSLayoutConstraint {
}

class UIViewContainerViewlet: JsonInflatable {

    // --
    // MARK: Implementation
    // --

    func create() -> Any {
        return UIViewContainer()
    }
    
    func update(convUtil: InflatorConvUtil, object: Any, attributes: [String: Any], parent: Any?, binder: InflatorBinder?) -> Bool {
        if let container = object as? UIViewContainer {
            // First clear all constraints
            removeViewletConstraints(view: container, constraints: container.constraints)
            for view in container.subviews {
                removeViewletConstraints(view: view, constraints: view.constraints)
            }

            // Inflate
            let newSubviews = ViewletCreator.shared.attributesForNestedInflatableList(attributes["subviews"])
            let result = ViewletCreator.shared.inflateNestedItemList(currentItems: container.subviews, newItems: newSubviews, enableRecycling: true, parent: container, binder: binder)

            // Remove views that could not be recycled
            for removeItem in result.removedItems {
                if let subview = removeItem as? UIView {
                    removeViewletConstraints(view: subview, constraints: subview.constraints)
                    subview.removeFromSuperview()
                }
            }

            // Add or update views that are new or could be recycled
            let internalBinder = InflatorDictBinder()
            for i in result.items.indices {
                if let view = result.items[i] as? UIView {
                    if !result.isRecycled(index: i) {
                        container.insertSubview(view, at: i)
                    }
                    if let refId = result.getAttributes(index: i)["refId"] as? String {
                        internalBinder.onBind(refId: refId, object: view)
                        if binder != nil {
                            binder?.onBind(refId: refId, object: view)
                        }
                    }
                }
            }
            
            // Apply constraints to subviews
            if let constraintsSet = attributes["constraints"] as? [String: Any] {
                for (key, constraintsSetItem) in constraintsSet {
                    if key != "manual" {
                        if let constraintView = internalBinder.findByReference(key) as? UIView {
                            if let constraints = constraintsSetItem as? [String: Any] {
                                applyConstraints(convUtil: convUtil, view: container, constraintView: constraintView, constraints: constraints, boundViews: internalBinder)
                            }
                        }
                    }
                }
                if let manualConstraints = constraintsSet["manual"] as? [[String: Any]] {
                    applyManualConstraints(selfView: container, constraints: manualConstraints, boundViews: internalBinder)
                }
            }
            
            // Standard view attributes
            UIViewViewlet.applyDefaultAttributes(convUtil: convUtil, view: container, attributes: attributes)
            return true
        }
        return false
    }
    
    func canRecycle(convUtil: InflatorConvUtil, object: Any, attributes: [String : Any]) -> Bool {
        return object is UIViewContainer
    }
    
    
    // --
    // MARK: Add constraints
    // --

    private func applyConstraints(convUtil: InflatorConvUtil, view: UIView, constraintView: UIView, constraints: [String: Any], boundViews: InflatorDictBinder) {
        // Apply width and height
        constraintView.translatesAutoresizingMaskIntoConstraints = false
        if let widthRelatedConstraint = constraints["width"] as? String {
            if let relatedView = boundViews.findByReference(widthRelatedConstraint) {
                view.addConstraint(ViewletConstraint(item: constraintView, attribute: .width, relatedBy: .equal, toItem: relatedView, attribute: .width, multiplier: 1, constant: 0))
            }
        } else if let widthConstraint = convUtil.asDimension(value: constraints["width"]) {
            constraintView.addConstraint(ViewletConstraint(item: constraintView, attribute: .width, relatedBy: .equal, toItem: nil, attribute: .notAnAttribute, multiplier: 1, constant: widthConstraint))
        }
        if let heightRelatedConstraint = constraints["height"] as? String {
            if let relatedView = boundViews.findByReference(heightRelatedConstraint) {
                view.addConstraint(ViewletConstraint(item: constraintView, attribute: .height, relatedBy: .equal, toItem: relatedView, attribute: .height, multiplier: 1, constant: 0))
            }
        } else if let heightConstraint = convUtil.asDimension(value: constraints["height"]) {
            constraintView.addConstraint(ViewletConstraint(item: constraintView, attribute: .height, relatedBy: .equal, toItem: nil, attribute: .notAnAttribute, multiplier: 1, constant: heightConstraint))
        }
        
        // Apply superview edge alignment
        if let superviewAlignment = constraints["alignToParent"] as? Bool {
            if superviewAlignment {
                view.addConstraint(ViewletConstraint(item: constraintView, attribute: .left, relatedBy: .equal, toItem: view, attribute: .left, multiplier: 1, constant: 0))
                view.addConstraint(ViewletConstraint(item: constraintView, attribute: .top, relatedBy: .equal, toItem: view, attribute: .top, multiplier: 1, constant: 0))
                view.addConstraint(ViewletConstraint(item: constraintView, attribute: .right, relatedBy: .equal, toItem: view, attribute: .right, multiplier: 1, constant: 0))
                view.addConstraint(ViewletConstraint(item: constraintView, attribute: .bottom, relatedBy: .equal, toItem: view, attribute: .bottom, multiplier: 1, constant: 0))
            }
        } else if let superviewAlignments = constraints["alignToParent"] as? [String] {
            for alignment in superviewAlignments {
                if alignment == "left" {
                    view.addConstraint(ViewletConstraint(item: constraintView, attribute: .left, relatedBy: .equal, toItem: view, attribute: .left, multiplier: 1, constant: 0))
                } else if alignment == "top" {
                    view.addConstraint(ViewletConstraint(item: constraintView, attribute: .top, relatedBy: .equal, toItem: view, attribute: .top, multiplier: 1, constant: 0))
                } else if alignment == "right" {
                    view.addConstraint(ViewletConstraint(item: constraintView, attribute: .right, relatedBy: .equal, toItem: view, attribute: .right, multiplier: 1, constant: 0))
                } else if alignment == "bottom" {
                    view.addConstraint(ViewletConstraint(item: constraintView, attribute: .bottom, relatedBy: .equal, toItem: view, attribute: .bottom, multiplier: 1, constant: 0))
                }
            }
        } else if let superviewAlignmentOffsets = constraints["alignToParent"] as? [String: Any] {
            if let leftAlignment = convUtil.asDimension(value: superviewAlignmentOffsets["left"]) {
                view.addConstraint(ViewletConstraint(item: constraintView, attribute: .left, relatedBy: .equal, toItem: view, attribute: .left, multiplier: 1, constant: leftAlignment))
            }
            if let topAlignment = convUtil.asDimension(value: superviewAlignmentOffsets["top"]) {
                view.addConstraint(ViewletConstraint(item: constraintView, attribute: .top, relatedBy: .equal, toItem: view, attribute: .top, multiplier: 1, constant: topAlignment))
            }
            if let rightAlignment = convUtil.asDimension(value: superviewAlignmentOffsets["right"]) {
                view.addConstraint(ViewletConstraint(item: constraintView, attribute: .right, relatedBy: .equal, toItem: view, attribute: .right, multiplier: 1, constant: -rightAlignment))
            }
            if let bottomAlignment = convUtil.asDimension(value: superviewAlignmentOffsets["bottom"]) {
                view.addConstraint(ViewletConstraint(item: constraintView, attribute: .bottom, relatedBy: .equal, toItem: view, attribute: .bottom, multiplier: 1, constant: -bottomAlignment))
            }
        }
        
        // Apply superview center alignment
        if let superviewCenter = constraints["centerInParent"] as? Bool {
            if superviewCenter {
                view.addConstraint(ViewletConstraint(item: constraintView, attribute: .centerX, relatedBy: .equal, toItem: view, attribute: .centerX, multiplier: 1, constant: 0))
                view.addConstraint(ViewletConstraint(item: constraintView, attribute: .centerY, relatedBy: .equal, toItem: view, attribute: .centerY, multiplier: 1, constant: 0))
            }
        } else if let superviewCenterAxis = constraints["centerInParent"] as? String {
            if superviewCenterAxis == "x" {
                view.addConstraint(ViewletConstraint(item: constraintView, attribute: .centerX, relatedBy: .equal, toItem: view, attribute: .centerX, multiplier: 1, constant: 0))
            } else if superviewCenterAxis == "y" {
                view.addConstraint(ViewletConstraint(item: constraintView, attribute: .centerY, relatedBy: .equal, toItem: view, attribute: .centerX, multiplier: 1, constant: 0))
            }
        }
        
        // Apply sibling alignment
        if let siblingAlignmentOffsets = constraints["alignToSibling"] as? [String: [String: Any]] {
            for (siblingKey, edges) in siblingAlignmentOffsets {
                if let siblingView = boundViews.findByReference(siblingKey) {
                    if let leftOffset = convUtil.asDimension(value: edges["toLeft"]) {
                        view.addConstraint(ViewletConstraint(item: constraintView, attribute: .right, relatedBy: .equal, toItem: siblingView, attribute: .left, multiplier: 1, constant: -leftOffset))
                    }
                    if let topOffset = convUtil.asDimension(value: edges["toTop"]) {
                        view.addConstraint(ViewletConstraint(item: constraintView, attribute: .bottom, relatedBy: .equal, toItem: siblingView, attribute: .top, multiplier: 1, constant: -topOffset))
                    }
                    if let rightOffset = convUtil.asDimension(value: edges["toRight"]) {
                        view.addConstraint(ViewletConstraint(item: constraintView, attribute: .left, relatedBy: .equal, toItem: siblingView, attribute: .right, multiplier: 1, constant: rightOffset))
                    }
                    if let bottomOffset = convUtil.asDimension(value: edges["toBottom"]) {
                        view.addConstraint(ViewletConstraint(item: constraintView, attribute: .top, relatedBy: .equal, toItem: siblingView, attribute: .bottom, multiplier: 1, constant: bottomOffset))
                    }
                    if let leftOffset = convUtil.asDimension(value: edges["atLeft"]) {
                        view.addConstraint(ViewletConstraint(item: constraintView, attribute: .left, relatedBy: .equal, toItem: siblingView, attribute: .left, multiplier: 1, constant: leftOffset))
                    }
                    if let topOffset = convUtil.asDimension(value: edges["atTop"]) {
                        view.addConstraint(ViewletConstraint(item: constraintView, attribute: .top, relatedBy: .equal, toItem: siblingView, attribute: .top, multiplier: 1, constant: topOffset))
                    }
                    if let rightOffset = convUtil.asDimension(value: edges["atRight"]) {
                        view.addConstraint(ViewletConstraint(item: constraintView, attribute: .right, relatedBy: .equal, toItem: siblingView, attribute: .right, multiplier: 1, constant: rightOffset))
                    }
                    if let bottomOffset = convUtil.asDimension(value: edges["atBottom"]) {
                        view.addConstraint(ViewletConstraint(item: constraintView, attribute: .bottom, relatedBy: .equal, toItem: siblingView, attribute: .bottom, multiplier: 1, constant: bottomOffset))
                    }
                }
            }
        } else if let siblingAlignment = constraints["alignToSibling"] as? [String: Any] {
            for (siblingKey, siblingConstraint) in siblingAlignment {
                if let siblingView = boundViews.findByReference(siblingKey) {
                    if let siblingEdge = siblingConstraint as? String {
                        if siblingEdge == "toLeft" {
                            view.addConstraint(ViewletConstraint(item: constraintView, attribute: .right, relatedBy: .equal, toItem: siblingView, attribute: .left, multiplier: 1, constant: 0))
                        } else if siblingEdge == "toTop" {
                            view.addConstraint(ViewletConstraint(item: constraintView, attribute: .bottom, relatedBy: .equal, toItem: siblingView, attribute: .top, multiplier: 1, constant: 0))
                        } else if siblingEdge == "toRight" {
                            view.addConstraint(ViewletConstraint(item: constraintView, attribute: .left, relatedBy: .equal, toItem: siblingView, attribute: .right, multiplier: 1, constant: 0))
                        } else if siblingEdge == "toBottom" {
                            view.addConstraint(ViewletConstraint(item: constraintView, attribute: .top, relatedBy: .equal, toItem: siblingView, attribute: .bottom, multiplier: 1, constant: 0))
                        } else if siblingEdge == "atLeft" {
                            view.addConstraint(ViewletConstraint(item: constraintView, attribute: .left, relatedBy: .equal, toItem: siblingView, attribute: .left, multiplier: 1, constant: 0))
                        } else if siblingEdge == "atTop" {
                            view.addConstraint(ViewletConstraint(item: constraintView, attribute: .top, relatedBy: .equal, toItem: siblingView, attribute: .top, multiplier: 1, constant: 0))
                        } else if siblingEdge == "atRight" {
                            view.addConstraint(ViewletConstraint(item: constraintView, attribute: .right, relatedBy: .equal, toItem: siblingView, attribute: .right, multiplier: 1, constant: 0))
                        } else if siblingEdge == "atBottom" {
                            view.addConstraint(ViewletConstraint(item: constraintView, attribute: .bottom, relatedBy: .equal, toItem: siblingView, attribute: .bottom, multiplier: 1, constant: 0))
                        }
                    }
                }
            }
        }
        
        // Apply aspect ratio
        if let aspectRatioConstraint = constraints["aspectRatio"] as? CGFloat {
            constraintView.addConstraint(ViewletConstraint(item: constraintView, attribute: .width, relatedBy: .equal, toItem: constraintView, attribute: .height, multiplier: aspectRatioConstraint, constant: 0))
        }
    }
    
    private func applyManualConstraints(selfView: UIView, constraints: [[String: Any]], boundViews: InflatorDictBinder) {
        for constraint in constraints {
            var targetView = selfView
            var item: UIView?
            if let checkTarget = constraint["target"] as? String {
                if let newTarget = viewByReference(selfView: selfView, ref: checkTarget, boundViews: boundViews) {
                    targetView = newTarget
                } else {
                    continue
                }
            }
            item = viewByReference(selfView: selfView, ref: constraint["item"] as? String, boundViews: boundViews)
            if item == nil {
                continue
            }
            targetView.addConstraint(ViewletConstraint(item: item!,
                                                        attribute: constraintAttributeFrom(string: constraint["attribute"] as? String),
                                                        relatedBy: constraintRelatedByFrom(string: constraint["relatedBy"] as? String),
                                                        toItem: viewByReference(selfView: selfView, ref: constraint["toItem"] as? String, boundViews: boundViews),
                                                        attribute: constraintAttributeFrom(string: constraint["toAttribute"] as? String),
                                                        multiplier: constraint["multiplier"] as? CGFloat ?? 1,
                                                        constant: constraint["constant"] as? CGFloat ?? 0))
        }
    }
    
    
    // --
    // MARK: Constraint helpers
    // --

    private func viewByReference(selfView: UIView, ref: String?, boundViews: InflatorDictBinder) -> UIView? {
        if ref == nil {
            return nil
        }
        if ref! == "self" {
            return selfView
        }
        return boundViews.findByReference(ref!) as? UIView
    }
    
    private func constraintAttributeFrom(string: String?) -> NSLayoutConstraint.Attribute {
        if string != nil {
            if string! == "left" {
                return .left
            } else if string! == "top" {
                return .top
            } else if string! == "right" {
                return .right
            } else if string! == "bottom" {
                return .bottom
            } else if string! == "leftMargin" {
                return .leftMargin
            } else if string! == "topMargin" {
                return .topMargin
            } else if string! == "rightMargin" {
                return .rightMargin
            } else if string! == "bottomMargin" {
                return .bottomMargin
            } else if string! == "centerX" {
                return .centerX
            } else if string! == "centerY" {
                return .centerY
            } else if string! == "centerXWithinMargins" {
                return .centerXWithinMargins
            } else if string! == "centerYWithinMargins" {
                return .centerYWithinMargins
            } else if string! == "width" {
                return .width
            } else if string! == "height" {
                return .height
            } else if string! == "firstBaseline" {
                return .firstBaseline
            } else if string! == "lastBaseline" {
                return .lastBaseline
            } else if string! == "leading" {
                return .leading
            } else if string! == "trailing" {
                return .trailing
            } else if string! == "leadingMargin" {
                return .leadingMargin
            } else if string! == "trailingMargin" {
                return .trailingMargin
            }
        }
        return .notAnAttribute
    }
    
    private func constraintRelatedByFrom(string: String?) -> NSLayoutConstraint.Relation {
        if string != nil {
            if string! == "greaterThanOrEqual" {
                return .greaterThanOrEqual
            } else if string! == "lessThanOrEqual" {
                return .lessThanOrEqual
            }
        }
        return .equal
    }
    
    private func removeViewletConstraints(view: UIView, constraints: [NSLayoutConstraint]) {
        for constraint in constraints {
            if constraint is ViewletConstraint {
                view.removeConstraint(constraint)
            }
        }
    }

}
