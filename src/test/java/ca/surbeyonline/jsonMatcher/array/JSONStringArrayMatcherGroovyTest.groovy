package ca.surbeyonline.jsonMatcher.array

import org.junit.Test
import spock.lang.Specification
import spock.lang.Unroll

import static ca.surbeyonline.jsonMatcher.array.JSONStringArrayMatcher.looselyMatchesArray
import static org.junit.Assert.assertThat

class JSONStringArrayMatcherGroovyTest extends Specification {

    @Test
    @Unroll
    void "Json array matches loosely"(String actual, String expected) {
        expect:
        assertThat(actual, looselyMatchesArray(expected))

        where:
        actual                      | expected
        "[]"                        | "[]"
        "[{key:1},{key:2},{key:3}]" | "[{key:1},{key:2}]"
        "[{key:1},{key:2},{key:3}]" | "[{key:1},{key:2},{key:3}]"
        "[{key:1},{key:2,door:3}]"  | "[{key:1},{key:2}]"
    }

    @Test
    @Unroll
    void "Json array DOES NOT match loosely Casting exceptions"() {
        when:
        assertThat(actual, looselyMatchesArray(expected))

        then:
        Exception exception = thrown();
        expectedMessage == exception.message;
        exceptionType == exception.getClass()

        where:
        actual | expected | exceptionType      | expectedMessage
        "[]"   | "{}"     | ClassCastException | "org.json.JSONObject cannot be cast to org.json.JSONArray"
        "[}"   | "{}"     | ClassCastException | "org.json.JSONObject cannot be cast to org.json.JSONArray"
        "{}"   | "[]"     | ClassCastException | "org.json.JSONObject cannot be cast to org.json.JSONArray"
    }

    @Test
    @Unroll
    void "Json array DOES NOT match loosely"() {
        when:
        assertThat(actual, looselyMatchesArray(expected))

        then:
        AssertionError exception = thrown();
        expectedMessage == exception.message;
        exceptionType == exception.getClass()

        where:
        actual                        | expected                    | exceptionType  | expectedMessage
        "[{obj: {id:1}}, {obja: {}}]" | '[{obj: {id:1}}, {quit:3}]' | AssertionError | '\nExpected: [1] Could not find match for element {"quit":3}\n     but: Actual   Value <[{"obj":{"id":1}},{"obja":{}}]>, which did not match \n          Expected Value <[{"obj":{"id":1}},{"quit":3}]>'
//        "[{key:1},{key:2}]"           | "[{key:1},{key:2,door:3}]" | AssertionError     | ""
//        "[obj: {}]"                   | "[]"                       | AssertionError     | ""
    }
}
