import XCTest
import JsonInflator

class Tests: XCTestCase {
    
    // ---
    // MARK: Test array conversion
    // ---
    
    func testAsStringArray() {
        XCTAssertEqual([ "first", "second" ], convUtilInstance().asStringArray(value: [ "first", "second" ]))
        XCTAssertEqual([ "10", "12", "99", "24" ], convUtilInstance().asStringArray(value: [ 10, 12, 99, 24 ]))
    }
    
    func testAsDoubleArray() {
        XCTAssertEqual([ 4.22, 8.9, 19.1, 11 ], convUtilInstance().asDoubleArray(value: [ "4.22", "8.9", "19.1", "11" ]))
        XCTAssertEqual([ 3.11, 16 ], convUtilInstance().asDoubleArray(value: [ 3.11, 16 ]))
    }
    
    func testAsFloatArray() {
        XCTAssertEqual([ 1.1, 5, 89.16, 2 ], convUtilInstance().asFloatArray(value: [ 1.1, 5, 89.16, 2 ]))
        XCTAssertEqual([ 67, 11 ], convUtilInstance().asFloatArray(value: [ 67, 11 ]))
    }
    
    func testAsIntArray() {
        XCTAssertEqual([ 325, -23 ], convUtilInstance().asIntArray(value: [ 325, -23 ]))
        XCTAssertEqual([ 6, 3, 12, 2150 ], convUtilInstance().asIntArray(value: [ "6.1", "3", "12", "2150.654" ]))
    }
    
    func testAsBoolArray() {
        XCTAssertEqual([ true, true, false, true ], convUtilInstance().asBoolArray(value: [ "true", "true", "false", "true" ]))
        XCTAssertEqual([ true, false, true ], convUtilInstance().asBoolArray(value: [ 12, 0, 1 ]))
        XCTAssertEqual([ false, true ], convUtilInstance().asBoolArray(value: [ false, true ]))
    }
    
    
    // ---
    // MARK: Test view related data conversion
    // ---
    
    func testAsPointValueArray() {
        let pixel = 1 / UIScreen.main.scale
        let widthPoint = CGFloat(UIScreen.main.bounds.width / 100)
        let heightPoint = CGFloat(UIScreen.main.bounds.height / 100)
        XCTAssertEqual(
            [ pixel, 10, 4, 9, CGFloat(Float(10)) * widthPoint, CGFloat(Float(5.2)) * heightPoint, CGFloat(Float(1.94)) * min(widthPoint, heightPoint), CGFloat(Float(-2.4)) * max(widthPoint, heightPoint)],
            convUtilInstance().asDimensionArray(value: [ "1px", "10dp", "4sp", "9dp", "10wp", "5.2hp", "1.94minp", "-2.4maxp" ])
        )
    }
    
    func testAsColor() {
        XCTAssertEqual(UIColor.red, convUtilInstance().asColor(value: "#ff0000"))
        XCTAssertEqual(UIColor(red: 0, green: 0, blue: 0, alpha: 0), convUtilInstance().asColor(value: "#00000000"))
        XCTAssertEqual(UIColor(red: 0, green: 0, blue: 0, alpha: 176.0 / 255), convUtilInstance().asColor(value: "#b0000000"))
        XCTAssertEqual(UIColor.yellow, convUtilInstance().asColor(value: "#ff0"))
        XCTAssertEqual(UIColor(red: 1, green: 1, blue: 1, alpha: 0), convUtilInstance().asColor(value: "#0fff"))
        XCTAssertEqual(UIColor(hue: 259.0 / 360, saturation: 99.0 / 100, brightness: 10.0 / 100, alpha: 1), convUtilInstance().asColor(value: "h259s99v10"))
        XCTAssertEqual(UIColor(hue: 164.0 / 360, saturation: 83.0 / 100, brightness: 95.0 / 100, alpha: 0.5), convUtilInstance().asColor(value: "h164s83v95a50"))
        XCTAssertEqual(UIColor(red: 0.3159, green: 0.40976, blue: 0.4641, alpha: 0.25), convUtilInstance().asColor(value: "H202 S19 L39 A25"))
    }
    
    func testAsPointValue() {
        let pixel = 1 / UIScreen.main.scale
        let widthPoint = UIScreen.main.bounds.width / 100
        let heightPoint = UIScreen.main.bounds.height / 100
        XCTAssertEqual(pixel, convUtilInstance().asDimension(value: "1px"))
        XCTAssertEqual(20, convUtilInstance().asDimension(value: "20dp"))
        XCTAssertEqual(12, convUtilInstance().asDimension(value: "12sp"))
        XCTAssertEqual(8, convUtilInstance().asDimension(value: 8))
        XCTAssertEqual(widthPoint * 12.5, convUtilInstance().asDimension(value: "12.5wp"))
        XCTAssertEqual(heightPoint * 99, convUtilInstance().asDimension(value: "99hp"))
        XCTAssertEqual(min(widthPoint, heightPoint) * 45, convUtilInstance().asDimension(value: "45minp"))
        XCTAssertEqual(max(widthPoint, heightPoint) * 3, convUtilInstance().asDimension(value: "3maxp"))
    }
    
    
    // ---
    // MARK: Test basic value conversion
    // ---
    
    func testAsDate() {
        XCTAssertEqual(dateFrom(year: 2016, month: 8, day: 19), convUtilInstance().asDate(value: "2016-08-19"))
        XCTAssertEqual(dateFrom(year: 2016, month: 5, day: 16, hour: 1, minute: 10, second: 28), convUtilInstance().asDate(value: "2016-05-16T01:10:28"))
        XCTAssertEqual(utcDateFrom(year: 2016, month: 2, day: 27, hour: 12, minute: 24, second: 11), convUtilInstance().asDate(value: "2016-02-27T12:24:11Z"))
        XCTAssertEqual(utcDateFrom(year: 2016, month: 2, day: 27, hour: 17, minute: 0, second: 0), convUtilInstance().asDate(value: "2016-02-27T19:00:00+02:00"))
    }
    
    func testAsString() {
        XCTAssertEqual("test", convUtilInstance().asString(value: "test"))
        XCTAssertEqual("12", convUtilInstance().asString(value: 12))
        XCTAssertEqual("14.42", convUtilInstance().asString(value: 14.42))
        XCTAssertEqual("true", convUtilInstance().asString(value: true))
    }
    
    func testAsDouble() {
        XCTAssertEqual(89.213, convUtilInstance().asDouble(value: "89.213"))
        XCTAssertEqual(31, convUtilInstance().asDouble(value: 31))
    }
    
    func testAsFloat() {
        XCTAssertEqual(21.3, convUtilInstance().asFloat(value: 21.3))
        XCTAssertEqual(1, convUtilInstance().asFloat(value: true))
    }
    
    func testAsInt() {
        XCTAssertEqual(3, convUtilInstance().asInt(value: "3"))
        XCTAssertEqual(45, convUtilInstance().asInt(value: 45.75))
    }
    
    func testAsBool() {
        XCTAssertEqual(false, convUtilInstance().asBool(value: "false"))
        XCTAssertEqual(true, convUtilInstance().asBool(value: 2))
    }
    
    
    // ---
    // MARK: Helper
    // ---
    
    func dateFrom(year: Int, month: Int, day: Int, hour: Int = 0, minute: Int = 0, second: Int = 0) -> Date {
        let calendar = Calendar(identifier: .gregorian)
        var components = DateComponents()
        components.year = year
        components.month = month
        components.day = day
        components.hour = hour
        components.minute = minute
        components.second = second
        return calendar.date(from: components)!
    }
    
    func utcDateFrom(year: Int, month: Int, day: Int, hour: Int = 0, minute: Int = 0, second: Int = 0) -> Date {
        var calendar = Calendar(identifier: .gregorian)
        var components = DateComponents()
        calendar.timeZone = TimeZone(identifier: "UTC")!
        components.year = year
        components.month = month
        components.day = day
        components.hour = hour
        components.minute = minute
        components.second = second
        return calendar.date(from: components)!
    }
    
    func convUtilInstance() -> InflatorConvUtil {
        return InflatorConvUtil()
    }

}
