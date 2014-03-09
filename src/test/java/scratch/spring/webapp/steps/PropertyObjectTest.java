package scratch.spring.webapp.steps;

import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

import static java.util.Collections.singletonMap;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

public class PropertyObjectTest {

    private static final String ONE = "one";
    private static final String TWO = "two";
    private static final String THREE = "three";
    private static final String ONE_TWO = ONE + "." + TWO;
    private static final String ONE_TWO_THREE = ONE_TWO + "." + THREE;
    private static final String TWO_THREE = TWO + "." + THREE;

    private static final String FOUR = "four";
    private static final String FIVE = "five";
    private static final String SIX = "six";
    private static final String SEVEN = "seven";
    private static final String FOUR_FIVE = FOUR + "." + FIVE;
    private static final String FOUR_FIVE_SIX = FOUR_FIVE + "." + SIX;
    private static final String FOUR_FIVE_SIX_SEVEN = FOUR_FIVE_SIX + "." + SEVEN;
    private static final String FIVE_SIX = FIVE + "." + SIX;

    private static final String EIGHT = "eight";
    private static final String NINE = "nine";
    private static final String TEN = "ten";
    private static final String EIGHT_NINE = EIGHT + "." + NINE;
    private static final String EIGHT_NINE_TEN = EIGHT_NINE + "." + TEN;

    private static final String NEW_VALUE = "new value";

    private static final String THREE_VALUE = "third layer value";
    private static final Map<String, Object> TWO_VALUE = new HashMap<String, Object>() {{
        put(THREE, THREE_VALUE);
    }};
    private static final Map<String, Object> ONE_VALUE = new HashMap<String, Object>() {{
        put(TWO, TWO_VALUE);
    }};

    private static final Float FOUR_VALUE = 4.0F;

    private static final String SIX_VALUE = "second layer value";

    private static final Map<String, Object> FIVE_VALUE = new HashMap<String, Object>() {{
        put(SIX, SIX_VALUE);
    }};

    private static final Map<String, Object> MAP = new HashMap<String, Object>() {{
        put(ONE, ONE_VALUE);
        put(FOUR, FOUR_VALUE);
        put(FIVE, FIVE_VALUE);
    }};

    private PropertyObject propertyObject;
    private PropertyObject copiedPropertyObject;

    @Before
    public void setUp() {

        propertyObject = new PropertyObject(MAP);
        copiedPropertyObject = new PropertyObject(propertyObject);
    }

    @Test
    public void testCreate() {

        new PropertyObject();
    }

    @Test(expected = NullPointerException.class)
    public void testCreateWithPropertyObjectNull() {

        new PropertyObject((PropertyObject) null);
    }

    @Test(expected = NullPointerException.class)
    public void testCreateWithMapNull() {

        new PropertyObject((Map<String, Object>) null);
    }

    @Test
    public void testGet() {

        getTest(propertyObject);
        getTest(copiedPropertyObject);
    }

    public static void getTest(PropertyObject propertyObject) {

        assertEquals("value one should be correct.", ONE_VALUE, propertyObject.get(ONE));
        assertThat("value one should be a new map.", (Map<String, Object>) propertyObject.get(ONE),
                not(sameInstance(ONE_VALUE)));

        assertEquals("value one.two should be correct.", TWO_VALUE, propertyObject.get(ONE_TWO));
        assertThat("value two should be a new map.", (Map<String, Object>) propertyObject.get(ONE_TWO),
                not(sameInstance(TWO_VALUE)));

        assertEquals("value one.two.three should be correct.", THREE_VALUE,
                propertyObject.get(ONE_TWO_THREE));

        assertEquals("value four should be correct.", FOUR_VALUE, propertyObject.get(FOUR));

        assertEquals("value five should be correct.", FIVE_VALUE, propertyObject.get(FIVE));
        assertThat("value five should be a new map.", (Map<String, Object>) propertyObject.get(FIVE),
                not(sameInstance(FIVE_VALUE)));

        assertEquals("value five.six should be correct.", SIX_VALUE, propertyObject.get(FIVE_SIX));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetInvalidLayerOneProperty() {

        propertyObject.get("invalid");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetInvalidLayerTwoProperty() {

        propertyObject.get(ONE + ".invalid");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetInvalidLayerThreeProperty() {

        propertyObject.get(ONE_TWO + ".invalid");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetInvalidMultiLayerProperty() {

        propertyObject.get("invalid1.invalid2.invalid3");
    }

    @Test
    public void testPropertyObjectHasNoSharedReference() {

        final Map<String, Object> map = referenceMap();
        final Map<String, Object> two = referenceMap(map, TWO);
        final Map<String, Object> four = referenceMap(map, FOUR);
        final Map<String, Object> five = referenceMap(four, FIVE);

        final PropertyObject propertyObject = new PropertyObject(map);

        map.put(ONE, 2);
        assertEquals("value one should be correct after the dependency map is changed.", 1, propertyObject.get(ONE));

        two.put(THREE, 4);
        assertEquals("value two.three should be correct after the dependency map is changed.", 3,
                propertyObject.get(TWO_THREE));

        four.put(FIVE, 5);
        assertEquals("value four.five should be correct after the dependency map is changed.", five,
                propertyObject.get(FOUR_FIVE));

        five.put(SIX, 7);
        assertEquals("value four.five.six should be correct after the dependency map is changed.", 6,
                propertyObject.get(FOUR_FIVE_SIX));

    }

    @Test
    @SuppressWarnings("unchecked")
    public void testGetHasNoLeakedReference() {

        final PropertyObject propertyObject = new PropertyObject(referenceMap());

        final Map<String, Object> two = propertyObject.get(TWO);
        final Map<String, Object> four = propertyObject.get(FOUR);
        final Map<String, Object> five = propertyObject.get(FOUR + "." + FIVE);

        two.put(THREE, 4);
        assertEquals("value two.three should be correct after the dependency map is changed.", 3,
                propertyObject.get(TWO + "." + THREE));

        ((Map<String, Object>) four.get(FIVE)).put(SIX, 7);
        assertEquals("value four.five should be correct after the dependency map is changed.", five,
                propertyObject.get(FOUR_FIVE));

        five.put(SIX, 8);
        assertEquals("value four.five.six should be correct after the dependency map is changed.", 6,
                propertyObject.get(FOUR_FIVE_SIX));
    }

    @Test
    public void testSet() {

        setTest(propertyObject);
        setTest(copiedPropertyObject);
    }

    public static void setTest(PropertyObject propertyObject) {

        propertyObject.set(ONE_TWO_THREE, NEW_VALUE);
        assertEquals("value one.two.three should be correct.", NEW_VALUE,
                propertyObject.get(ONE_TWO_THREE));

        propertyObject.set(ONE_TWO, NEW_VALUE);
        assertEquals("value one.two should be correct.", NEW_VALUE,
                propertyObject.get(ONE_TWO));

        propertyObject.set(ONE, NEW_VALUE);
        assertEquals("value one should be correct.", NEW_VALUE,
                propertyObject.get(ONE));

        propertyObject.set(FOUR, NEW_VALUE);
        assertEquals("value four should be correct.", NEW_VALUE,
                propertyObject.get(FOUR));

        propertyObject.set(FOUR_FIVE_SIX_SEVEN, NEW_VALUE);
        assertEquals("value five.seven should be correct.", singletonMap(SEVEN, NEW_VALUE),
                propertyObject.get(FOUR_FIVE_SIX));
        assertEquals("value five.seven.eight should be correct.", NEW_VALUE,
                propertyObject.get(FOUR_FIVE_SIX_SEVEN));

        propertyObject.set(EIGHT_NINE_TEN, NEW_VALUE);
        assertEquals("value eight should be correct.",
                singletonMap(NINE, singletonMap(TEN, NEW_VALUE)),
                propertyObject.get(EIGHT));
        assertEquals("value eight.nine should be correct.", singletonMap(TEN, NEW_VALUE),
                propertyObject.get(EIGHT_NINE));
        assertEquals("value eight.nine.ten should be correct.", NEW_VALUE,
                propertyObject.get(EIGHT_NINE_TEN));
    }

    @Test(expected = NullPointerException.class)
    public void testSetWithNullPropertyPath() {

        propertyObject.set(null, NEW_VALUE);
    }

    @Test
    public void testSetWithEmptyPropertyPath() {

        propertyObject.set("", NEW_VALUE);
        assertEquals("value for empty string should be correct.", NEW_VALUE, propertyObject.get(""));
    }

    @Test
    public void testSetWithEmptyFinalPropertyName() {

        propertyObject.set(ONE_TWO_THREE + ".", NEW_VALUE);
        assertEquals("value one.two.three. should be correct.", NEW_VALUE, propertyObject.get(ONE_TWO_THREE + "."));
    }

    @Test
    public void testSetWithNullValue() {

        propertyObject.set(ONE_TWO_THREE, null);
        assertNull("value one.two.three should be null.", propertyObject.get(ONE_TWO_THREE));

        propertyObject.set(ONE_TWO, null);
        assertNull("value one.two should be null.", propertyObject.get(ONE_TWO));

        propertyObject.set(ONE, null);
        assertNull("value one should be null.", propertyObject.get(ONE));
    }

    @Test
    public void testRemove() {

        removeTest(propertyObject);
        removeTest(copiedPropertyObject);
    }

    public static void removeTest(final PropertyObject propertyObject) {

        final String oneTwoThree = new PropertyObject(propertyObject).remove(ONE_TWO_THREE);
        assertEquals("value one.two.three should be correct.", THREE_VALUE, oneTwoThree);
        assertException(IllegalArgumentException.class, new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                propertyObject.get(ONE_TWO_THREE);
                return null;
            }
        });

        final Map<String, Object> oneTwo = new PropertyObject(propertyObject).remove(ONE_TWO);
        assertEquals("value one.two should be correct.", TWO_VALUE, oneTwo);
        assertException(IllegalArgumentException.class, new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                propertyObject.get(ONE_TWO);
                return null;
            }
        });

        final Map<String, Object> one = new PropertyObject(propertyObject).remove(ONE);
        assertEquals("value one should be correct.", ONE_VALUE, one);
        assertException(IllegalArgumentException.class, new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                propertyObject.get(ONE_TWO_THREE);
                return null;
            }
        });
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRemoveInvalidLayerOneProperty() {

        propertyObject.remove("invalid");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRemoveInvalidLayerTwoProperty() {

        propertyObject.remove(ONE + ".invalid");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRemoveInvalidLayerThreeProperty() {

        propertyObject.remove(ONE_TWO + ".invalid");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRemoveInvalidMultiLayerProperty() {

        propertyObject.remove("invalid1.invalid2.invalid3");
    }

    @Test
    public void testToMap() {

        assertEquals("the returned map should be correct.", MAP, propertyObject.toMap());
        assertEquals("the returned map should be correct.", MAP, copiedPropertyObject.toMap());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testToMapHasNoLeakedReference() {

        final PropertyObject propertyObject = new PropertyObject(referenceMap());

        final Map<String, Object> map = propertyObject.toMap();
        final Map<String, Object> two = referenceMap(map, TWO);
        final Map<String, Object> four = referenceMap(map, FOUR);
        final Map<String, Object> five = referenceMap(four, FIVE);

        two.put(THREE, 4);
        assertEquals("value two.three should be correct after the dependency map is changed.", 3,
                propertyObject.get(TWO_THREE));

        ((Map<String, Object>) four.get(FIVE)).put(SIX, 7);
        assertEquals("value four.five should be correct after the dependency map is changed.", singletonMap(SIX, 6),
                propertyObject.get(FOUR_FIVE));

        five.put(SIX, 8);
        assertEquals("value four.five.six should be correct after the dependency map is changed.", 6,
                propertyObject.get(FOUR_FIVE_SIX));
    }

    @Test
    public void testClear() {

        propertyObject.clear();

        assertEquals("the property object should be empty.", Collections.<String, Object>emptyMap(),
                propertyObject.toMap());
    }

    private static void assertException(Class<? extends Exception> type, Callable<Void> callable) {

        try {
            callable.call();

        } catch (Exception e) {

            assertThat("the exception should be correct.", e, instanceOf(type));
        }
    }

    private static Map<String, Object> referenceMap() {

        final Map<String, Object> two = new HashMap<String, Object>();
        two.put(THREE, 3);

        final Map<String, Object> five = new HashMap<String, Object>();
        five.put(SIX, 6);

        final Map<String, Object> four = new HashMap<String, Object>();
        four.put(FIVE, five);

        final Map<String, Object> map = new HashMap<String, Object>();
        map.put(ONE, 1);
        map.put(TWO, two);
        map.put(FOUR, four);

        return map;
    }

    @SuppressWarnings("unchecked")
    private static Map<String, Object> referenceMap(Map<String, Object> map, String key) {

        return (Map<String, Object>) map.get(key);
    }
}
