package ca.surbeyonline.jsonMatcher;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeDiagnosingMatcher;
import org.json.JSONArray;
import org.json.JSONException;
import org.skyscreamer.jsonassert.JSONCompare;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.skyscreamer.jsonassert.JSONCompareResult;
import org.skyscreamer.jsonassert.JSONParser;

import javax.ws.rs.core.Response;


public class JSONResponseArrayMatcher extends TypeSafeDiagnosingMatcher<Response> {
    private JSONArray expectedResult;
    private JSONCompareMode jsonCompareMode;

    private JSONResponseArrayMatcher(JSONArray expectedResult, JSONCompareMode jSONCompareMode) {
        this.expectedResult = expectedResult;
        this.jsonCompareMode = jSONCompareMode;
    }

    @Override
    protected boolean matchesSafely(Response response, Description description) {

        description.appendText("Compared ").appendValue(response.getEntity().toString()).appendText(", which did not match the expected value: \n                    " + expectedResult);
        try {
            JSONArray actualJsonArray = (JSONArray) JSONParser.parseJSON(response.getEntity().toString());
            JSONCompareResult jsonCompareResult = JSONCompare.compareJSON(expectedResult, actualJsonArray, jsonCompareMode);
            return jsonCompareResult.passed();
        } catch (JSONException e) {
            description.appendText(e.getMessage());
        }
        return false;
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("Expected Response json Array to be the same, using JSON Compare Mode: " + jsonCompareMode);
    }

    public static JSONResponseArrayMatcher looselyMatchesArray(String expectedJson) throws JSONException {
        JSONArray expected = (JSONArray) JSONParser.parseJSON(expectedJson);
        return new JSONResponseArrayMatcher(expected, JSONCompareMode.LENIENT);
    }

    public static JSONResponseArrayMatcher strictlyMatchesArray(String expectedJson) throws JSONException {
        JSONArray expected = (JSONArray) JSONParser.parseJSON(expectedJson);
        return new JSONResponseArrayMatcher(expected, JSONCompareMode.STRICT);
    }

}
