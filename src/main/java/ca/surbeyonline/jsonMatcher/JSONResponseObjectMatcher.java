package ca.surbeyonline.jsonMatcher;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeDiagnosingMatcher;
import org.json.JSONException;
import org.json.JSONObject;
import org.skyscreamer.jsonassert.JSONCompare;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.skyscreamer.jsonassert.JSONCompareResult;
import org.skyscreamer.jsonassert.JSONParser;

import javax.ws.rs.core.Response;

public class JSONResponseObjectMatcher extends TypeSafeDiagnosingMatcher<Response> {
    private JSONObject expectedResult;
    private JSONCompareMode jsonCompareMode;

    public JSONResponseObjectMatcher(JSONObject expectedResult, JSONCompareMode jSONCompareMode) {
        this.expectedResult = expectedResult;
        this.jsonCompareMode = jSONCompareMode;
    }

    @Override
    protected boolean matchesSafely(Response response, Description description) {

        description.appendText("Compared ").appendValue(response.getEntity().toString()).appendText(", which did not match the expected value: \n                    " + expectedResult);
        try {
            JSONObject actualJsonArray = (JSONObject) JSONParser.parseJSON(response.getEntity().toString());
            JSONCompareResult jsonCompareResult = JSONCompare.compareJSON(expectedResult, actualJsonArray, jsonCompareMode);
            return jsonCompareResult.passed();
        } catch (JSONException e) {
            description.appendText(e.getMessage());
        }
        return false;
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("Expected Response json Object to be the same, using JSON Compare Mode: " + jsonCompareMode);
    }

    public static JSONResponseObjectMatcher looselyMatches(String expectedJson) throws JSONException {
        JSONObject expected = (JSONObject) JSONParser.parseJSON(expectedJson);
        return new JSONResponseObjectMatcher(expected, JSONCompareMode.LENIENT);
    }

    public static JSONResponseObjectMatcher strictlyMatches(String expectedJson) throws JSONException {
        JSONObject expected = (JSONObject) JSONParser.parseJSON(expectedJson);
        return new JSONResponseObjectMatcher(expected, JSONCompareMode.STRICT);
    }

}
