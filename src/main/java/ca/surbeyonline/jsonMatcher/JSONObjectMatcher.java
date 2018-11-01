package ca.surbeyonline.jsonMatcher;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeDiagnosingMatcher;
import org.json.JSONException;
import org.json.JSONObject;
import org.skyscreamer.jsonassert.JSONCompare;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.skyscreamer.jsonassert.JSONCompareResult;
import org.skyscreamer.jsonassert.JSONParser;

public class JSONObjectMatcher extends TypeSafeDiagnosingMatcher<String> {
    private JSONObject expectedResult;
    private JSONCompareMode jsonCompareMode;

    public JSONObjectMatcher(JSONObject expectedResult, JSONCompareMode jSONCompareMode) {
        this.expectedResult = expectedResult;
        this.jsonCompareMode = jSONCompareMode;
    }

    @Override
    protected boolean matchesSafely(String actual, Description description) {

        description.appendText("Compared ").appendValue(actual).appendText(", which did not match the expected value: \n                    " + expectedResult);
        try {
            JSONObject actualJsonArray = (JSONObject) JSONParser.parseJSON(actual);
            JSONCompareResult jsonCompareResult = JSONCompare.compareJSON(expectedResult, actualJsonArray, jsonCompareMode);
            return jsonCompareResult.passed();
        } catch (JSONException e) {
            description.appendText(e.getMessage());
        }
        return false;
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("Expected the json Object to be the same, using JSON Compare Mode: " + jsonCompareMode);
    }

    public static JSONObjectMatcher jsonStringMatchesLooselyTo(String expectedJson) throws JSONException {
        JSONObject expected = (JSONObject) JSONParser.parseJSON(expectedJson);
        return new JSONObjectMatcher(expected, JSONCompareMode.LENIENT);
    }

    public static JSONObjectMatcher jsonStringMatchesStrictlyTo(String expectedJson) throws JSONException {
        JSONObject expected = (JSONObject) JSONParser.parseJSON(expectedJson);
        return new JSONObjectMatcher(expected, JSONCompareMode.STRICT);
    }

}
