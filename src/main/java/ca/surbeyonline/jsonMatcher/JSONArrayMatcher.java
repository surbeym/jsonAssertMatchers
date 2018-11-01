package ca.surbeyonline.jsonMatcher;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeDiagnosingMatcher;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import org.skyscreamer.jsonassert.JSONCompare;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.skyscreamer.jsonassert.JSONCompareResult;
import org.skyscreamer.jsonassert.JSONParser;

public class JSONArrayMatcher extends TypeSafeDiagnosingMatcher<String> {

    private JSONArray expectedResult;
    private JSONCompareMode jsonCompareMode;

    public JSONArrayMatcher(JSONArray expectedResult, JSONCompareMode jSONCompareMode) {
        this.expectedResult = expectedResult;
        this.jsonCompareMode = jSONCompareMode;
    }

    @Override
    protected boolean matchesSafely(String actual, Description description) {
        description.appendText("Compared ").appendValue(actual).appendText(", which did not match the expected value: \n                    " + expectedResult);
        try {
            JSONArray actualJsonArray = (JSONArray) JSONParser.parseJSON(actual);
            JSONCompareResult jsonCompareResult = JSONCompare.compareJSON(expectedResult, actualJsonArray, jsonCompareMode);
            return jsonCompareResult.passed();
        } catch (JSONException e) {
            description.appendText(e.getMessage());
        }
        return false;
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("Expected the json Array to be the same, using JSON Compare Mode: " + jsonCompareMode);
    }

    public static JSONArrayMatcher looselyMatchesArray(String expectedJson) throws JSONException {
        JSONArray expected = (JSONArray) JSONParser.parseJSON(expectedJson);
        return new JSONArrayMatcher(expected, JSONCompareMode.LENIENT);
    }

    public static JSONArrayMatcher strictlyMatchesArray(String expectedJson) throws JSONException {
        JSONArray expected = (JSONArray) JSONParser.parseJSON(expectedJson);
        return new JSONArrayMatcher(expected, JSONCompareMode.STRICT);
    }

}
