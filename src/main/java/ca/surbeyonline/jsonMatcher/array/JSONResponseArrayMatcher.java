package ca.surbeyonline.jsonMatcher.array;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeDiagnosingMatcher;
import org.json.JSONArray;
import org.json.JSONException;
import org.skyscreamer.jsonassert.JSONCompare;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.skyscreamer.jsonassert.JSONCompareResult;
import org.skyscreamer.jsonassert.JSONParser;

import javax.ws.rs.core.Response;


public class JSONResponseArrayMatcher extends JSONArrayMatcher<Response> {


    public JSONResponseArrayMatcher(JSONArray expectedResult, JSONCompareMode jsonCompareMode) {
        super(expectedResult, jsonCompareMode);
    }

    @Override
    protected boolean matchesSafely(Response response, Description description) {

        try {
            JSONArray actualJsonArray = (JSONArray) JSONParser.parseJSON(response.getEntity().toString());
            super.matchesSafely(actualJsonArray, description);
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
