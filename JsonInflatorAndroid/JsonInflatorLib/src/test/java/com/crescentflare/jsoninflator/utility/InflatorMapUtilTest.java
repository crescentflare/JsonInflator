package com.crescentflare.jsoninflator.utility;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

/**
 * Utility test: map utility
 */
public class InflatorMapUtilTest
{
    // --
    // Map conversion
    // --

    @Test
    public void asStringObjectMap()
    {
        Map<String, String> stringMap = new HashMap<>();
        Map<String, Integer> integerMap = new HashMap<>();
        Map<Double, Date> dateMap = new HashMap<>();
        stringMap.put("test", "value");
        integerMap.put("test", 20);
        dateMap.put(0.2, new Date());
        Assert.assertNotNull(mapUtilInstance().asStringObjectMap(stringMap));
        Assert.assertNotNull(mapUtilInstance().asStringObjectMap(integerMap));
        Assert.assertNull(mapUtilInstance().asStringObjectMap(dateMap));
    }

    @Test
    public void isMap()
    {
        Assert.assertTrue(mapUtilInstance().isMap(new HashMap<String, String>()));
        Assert.assertFalse(mapUtilInstance().isMap(new ArrayList<String>()));
        Assert.assertFalse(mapUtilInstance().isMap("string"));
    }


    // --
    // Fetch and convert lists
    // --

    @Test
    public void optionalObjectList()
    {
        Map<String, Object> map = new HashMap<>();
        map.put("list", new ArrayList<>(Arrays.asList("item", "item2")));
        map.put("string", "string");
        Assert.assertEquals(2, mapUtilInstance().optionalObjectList(map, "list").size());
        Assert.assertEquals(0, mapUtilInstance().optionalObjectList(map, "string").size());
    }

    @Test
    public void optionalStringList()
    {
        Map<String, Object> map = new HashMap<>();
        map.put("strings", new ArrayList<>(Arrays.asList("first", "second")));
        map.put("ints", new ArrayList<>(Arrays.asList(10, 64, 5)));
        Assert.assertEquals(Arrays.asList("first", "second"), mapUtilInstance().optionalStringList(map, "strings"));
        Assert.assertEquals(Arrays.asList("10", "64", "5"), mapUtilInstance().optionalStringList(map, "ints"));
    }

    @Test
    public void optionalDoubleList()
    {
        Map<String, Object> map = new HashMap<>();
        map.put("strings", new ArrayList<>(Arrays.asList("4.23", "16", "1.2345")));
        map.put("doubles", new ArrayList<>(Arrays.asList(4.2, 12.11)));
        Assert.assertEquals(Arrays.asList(4.23, 16.0, 1.2345), mapUtilInstance().optionalDoubleList(map, "strings"));
        Assert.assertEquals(Arrays.asList(4.2, 12.11), mapUtilInstance().optionalDoubleList(map, "doubles"));
    }

    @Test
    public void optionalFloatList()
    {
        Map<String, Object> map = new HashMap<>();
        map.put("floats", new ArrayList<>(Arrays.asList(5f, 823.123f, 11f)));
        map.put("ints", new ArrayList<>(Arrays.asList(9, 3214, 41, 22)));
        Assert.assertEquals(Arrays.asList(5f, 823.123f, 11f), mapUtilInstance().optionalFloatList(map, "floats"));
        Assert.assertEquals(Arrays.asList(9f, 3214f, 41f, 22f), mapUtilInstance().optionalFloatList(map, "ints"));
    }

    @Test
    public void optionalIntegerList()
    {
        Map<String, Object> map = new HashMap<>();
        map.put("ints", new ArrayList<>(Arrays.asList(99, 5126)));
        map.put("strings", new ArrayList<>(Arrays.asList("4.25", "87", "234.8", "11")));
        Assert.assertEquals(Arrays.asList(99, 5126), mapUtilInstance().optionalIntegerList(map, "ints"));
        Assert.assertEquals(Arrays.asList(4, 87, 234, 11), mapUtilInstance().optionalIntegerList(map, "strings"));
    }

    @Test
    public void optionalBooleanList()
    {
        Map<String, Object> map = new HashMap<>();
        map.put("strings", new ArrayList<>(Arrays.asList("true", "false")));
        map.put("ints", new ArrayList<>(Arrays.asList(0, 5, 1)));
        map.put("booleans", new ArrayList<>(Arrays.asList(false, false, true)));
        Assert.assertEquals(Arrays.asList(true, false), mapUtilInstance().optionalBooleanList(map, "strings"));
        Assert.assertEquals(Arrays.asList(false, true, true), mapUtilInstance().optionalBooleanList(map, "ints"));
        Assert.assertEquals(Arrays.asList(false, false, true), mapUtilInstance().optionalBooleanList(map, "booleans"));
    }


    // --
    // Fetch and convert view related values
    // --

    @Test
    public void optionalColor()
    {
        Map<String, Object> map = new HashMap<>();
        map.put("red", "#ff0000");
        map.put("transparent", "#00000000");
        map.put("semiTransparent", "#b0000000");
        map.put("yellow", "#ff0");
        map.put("invisibleWhite", "#0fff");
        map.put("hsvNonAlpha", "h259s99v10");
        map.put("hsvWithAlpha", "h164s83v95a50");
        map.put("hslSpaced", "H202 S19 L39 A25");
        Assert.assertEquals(0xFFFF0000, mapUtilInstance().optionalColor(map, "red", 0));
        Assert.assertEquals(0x00000000, mapUtilInstance().optionalColor(map, "transparent", 0));
        Assert.assertEquals(0xB0000000, mapUtilInstance().optionalColor(map, "semiTransparent", 0));
        Assert.assertEquals(0xFFFFFF00, mapUtilInstance().optionalColor(map, "yellow", 0));
        Assert.assertEquals(0x00FFFFFF, mapUtilInstance().optionalColor(map, "invisibleWhite", 0));
        Assert.assertEquals(0xFF080019, mapUtilInstance().optionalColor(map, "hsvNonAlpha", 0));
        Assert.assertEquals(0x7F29F2BC, mapUtilInstance().optionalColor(map, "hsvWithAlpha", 0));
        Assert.assertEquals(0x3F506876, mapUtilInstance().optionalColor(map, "hslSpaced", 0));
    }


    // --
    // Fetch and convert basic values
    // --

    @Test
    public void optionalDate()
    {
        Map<String, Object> map = new HashMap<>();
        map.put("first", "2016-08-19");
        map.put("second", "2016-05-16T01:10:28");
        map.put("third", "2016-02-27T12:24:11Z");
        map.put("fourth", "2016-02-27T19:00:00+02:00");
        Assert.assertEquals(dateFrom(2016, 8, 19).toString(), mapUtilInstance().optionalDate(map, "first", null).toString());
        Assert.assertEquals(dateFrom(2016, 5, 16, 1, 10, 28).toString(), mapUtilInstance().optionalDate(map, "second", null).toString());
        Assert.assertEquals(utcDateFrom(2016, 2, 27, 12, 24, 11).toString(), mapUtilInstance().optionalDate(map, "third", null).toString());
        Assert.assertEquals(utcDateFrom(2016, 2, 27, 17, 0, 0).toString(), mapUtilInstance().optionalDate(map, "fourth", null).toString());
    }

    @Test
    public void optionalString()
    {
        Map<String, Object> map = new HashMap<>();
        map.put("string", "text");
        map.put("int", 20);
        Assert.assertEquals("text", mapUtilInstance().optionalString(map, "string", null));
        Assert.assertEquals("20", mapUtilInstance().optionalString(map, "int", null));
        Assert.assertEquals("default", mapUtilInstance().optionalString(map, "invalid", "default"));
        Assert.assertNull(mapUtilInstance().optionalString(map, "invalid", null));
    }

    @Test
    public void optionalDouble()
    {
        Map<String, Object> map = new HashMap<>();
        map.put("string", "4.142");
        map.put("double", 8.9);
        Assert.assertEquals(4.142, mapUtilInstance().optionalDouble(map, "string", 0), 0);
        Assert.assertEquals(8.9, mapUtilInstance().optionalDouble(map, "double", 0), 0);
    }

    @Test
    public void optionalFloat()
    {
        Map<String, Object> map = new HashMap<>();
        map.put("int", 8);
        map.put("float", 5.156f);
        Assert.assertEquals(8.0f, mapUtilInstance().optionalFloat(map, "int", 0), 0.0f);
        Assert.assertEquals(5.156f, mapUtilInstance().optionalFloat(map, "float", 0), 0.0f);
    }

    @Test
    public void optionalInteger()
    {
        Map<String, Object> map = new HashMap<>();
        map.put("int", 78465);
        map.put("string", "5.8");
        Assert.assertEquals(78465, mapUtilInstance().optionalInteger(map, "int", 0));
        Assert.assertEquals(5, mapUtilInstance().optionalInteger(map, "string", 0));
    }

    @Test
    public void optionalBoolean()
    {
        Map<String, Object> map = new HashMap<>();
        map.put("int", 0);
        map.put("boolean", true);
        Assert.assertFalse(mapUtilInstance().optionalBoolean(map, "int", true));
        Assert.assertTrue(mapUtilInstance().optionalBoolean(map, "boolean", false));
    }


    // --
    // Helpers
    // --

    private Date dateFrom(int year, int month, int day)
    {
        return dateFrom(year, month, day, 0, 0, 0);
    }

    private Date dateFrom(int year, int month, int day, int hour, int minute, int second)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, day, hour, minute, second);
        return calendar.getTime();
    }

    private Date utcDateFrom(int year, int month, int day)
    {
        return utcDateFrom(year, month, day, 0, 0, 0);
    }

    private Date utcDateFrom(int year, int month, int day, int hour, int minute, int second)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, day, hour, minute, second);
        calendar.setTimeZone(TimeZone.getTimeZone("UTC"));
        return calendar.getTime();
    }

    private InflatorMapUtil mapUtilInstance()
    {
        return new InflatorMapUtil();
    }
}
