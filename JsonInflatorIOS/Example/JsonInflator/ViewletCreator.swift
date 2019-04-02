//
//  ViewletCreator.swift
//  Json inflator example
//
//  A custom json inflator for views
//

import UIKit
import JsonInflator

class ViewletCreator: JsonInflator {

    static let shared = ViewletCreator(inflatableKey: "viewlet", attributeSetKey: "viewletStyle")

}
