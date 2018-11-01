package ca.surbeyonline.jsonMatcher

import org.junit.After
import org.junit.Test
import spock.lang.Specification
import spock.lang.Unroll

import static ca.surbeyonline.jsonMatcher.JSONObjectMatcher.jsonStringMatchesLooselyTo
import static ca.surbeyonline.jsonMatcher.JSONObjectMatcher.jsonStringMatchesStrictlyTo
import static org.junit.Assert.assertThat;

class JSONObjectMatcherGroovyTest extends Specification {

    @Test
    @Unroll
    void "Json string matches loosely to"(String actual, String expected) {
        expect:
        assertThat(actual, jsonStringMatchesLooselyTo(expected))

        where:
        actual               | expected
        "{}"                 | "{}"
        "{obj: {}}"          | "{}"
        "{obj: {obj: {}}}"   | "{}"
        "{obj: {}}"          | "{obj: {}}"
        "{obj: {id:1}, " +
                " obj: {}}"  | "{obj: {}}"
        "{obj: {id:1}, " +
                " obj2: {}}" | "{obj: {id:1}}"
    }

    @Test
    @Unroll
    void "Json string matches STRICTLY To"() {
        expect:
        assertThat(actual, jsonStringMatchesStrictlyTo(expected))

        where:
        actual          | expected
        "{}"            | "{}"
        "{obj: {}}"     | "{obj: {}}"
        "{obj: {id:1}}" | "{obj: {id:1}}"
    }

    @Test
    @Unroll
    void "json string DOES NOT Matches Strictly To"() {
        when:
        assertThat(actual, jsonStringMatchesStrictlyTo(expected))

        then:
        AssertionError assertionError = thrown();
        assertionErrorMessage == assertionError.message;

        where:
        actual          | expected                                 | assertionErrorMessage
        "{obj: {id:1}}" | '{obj: {id:1, title:"test"}}"'           | '\nExpected: title in the structure of obj\n     but: Actual   Value <{"obj":{"id":1}}>, which did not match \n          Expected Value <{"obj":{"id":1,"title":"test"}}>'
        "{obj: {id:2}}" | '{obj: {id:2, title:"test", pages:423}}' | '\nExpected: pages in the structure of obj\n          title in the structure of obj\n     but: Actual   Value <{"obj":{"id":2}}>, which did not match \n          Expected Value <{"obj":{"pages":423,"id":2,"title":"test"}}>'
    }
}