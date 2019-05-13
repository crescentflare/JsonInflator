//
//  InflatorNestedResult.swift
//  Json inflator Pod
//
//  Library utility: the result of inflating nested items
//  Contains information about the objects that were added, removed or recycled
//

public class InflatorNestedResult {
    
    // --
    // MARK: Members
    // --

    private var internalItems = [Any]()
    private var internalRemovedItems = [Any]()
    private var itemRecycled = [Bool]()
    private var itemAttributes = [[String: Any]]()


    // --
    // MARK: Access result
    // --

    var items: [Any] {
        get {
            return internalItems
        }
    }

    var removedItems: [Any] {
        get {
            return internalRemovedItems
        }
    }
    
    func isRecycled(item: Any) -> Bool {
        return isRecycled(index: internalItems.firstIndex { $0 as AnyObject === item as AnyObject } ?? -1)
    }
    
    func isRecycled(index: Int) -> Bool {
        if index >= 0 && index < internalItems.count && index < itemRecycled.count {
            return itemRecycled[index]
        }
        return false
    }
    
    func getAttributes(item: Any) -> [String: Any] {
        return getAttributes(index: internalItems.firstIndex { $0 as AnyObject === item as AnyObject } ?? -1)
    }
    
    func getAttributes(index: Int) -> [String: Any] {
        if index >= 0 && index < internalItems.count && index < itemAttributes.count {
            return itemAttributes[index]
        }
        return [:]
    }
    

    // --
    // MARK: Modify items, used internally
    // --

    func addItem(_ item: Any, attributes: [String: Any], recycled: Bool) {
        internalItems.append(item)
        itemAttributes.append(attributes)
        itemRecycled.append(recycled)
    }
    
    func addRemovedItem(_ item: Any) {
        internalRemovedItems.append(item)
    }

}
