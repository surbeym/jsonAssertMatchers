package ca.surbeyonline.jsonMatcher.array;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeDiagnosingMatcher;
import org.json.JSONArray;
import org.json.JSONException;

import org.skyscreamer.jsonassert.JSONCompare;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.skyscreamer.jsonassert.JSONCompareResult;
import org.skyscreamer.jsonassert.JSONParser;

public class JSONStringArrayMatcher extends JSONArrayMatcher<String> {

    public JSONStringArrayMatcher(JSONArray expectedResult, JSONCompareMode jSONCompareMode) {
        super(expectedResult, jSONCompareMode);
    }

    @Override
    protected boolean matchesSafely(String actual, Description description) {
        description.appendText("Compared ").appendValue(actual).appendText(", which did not match the expected value: \n                    " + getExpectedResult());
        try {
            JSONArray actualJsonArray = (JSONArray) JSONParser.parseJSON(actual);
            return super.matchesSafely(actualJsonArray,description);
        } catch (JSONException e) {
            description.appendText(e.getMessage());
        }
        return false;
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("Expected the json Array to be the same, using JSON Compare Mode: " + jsonCompareMode);
    }

    public static JSONStringArrayMatcher looselyMatchesArray(String expectedJson) throws JSONException {
        JSONArray expected = (JSONArray) JSONParser.parseJSON(expectedJson);
        return new JSONStringArrayMatcher(expected, JSONCompareMode.LENIENT);
    }

    public static JSONStringArrayMatcher strictlyMatchesArray(String expectedJson) throws JSONException {
        JSONArray expected = (JSONArray) JSONParser.parseJSON(expectedJson);
        return new JSONStringArrayMatcher(expected, JSONCompareMode.STRICT);
    }

}
