package ca.surbeyonline.jsonMatcher.object;

import org.hamcrest.Description;
import org.json.JSONException;
import org.json.JSONObject;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.skyscreamer.jsonassert.JSONParser;

import javax.ws.rs.core.Response;

public class JSONResponseObjectMatcher extends JSONObjectMatcher<Response> {


    public JSONResponseObjectMatcher(JSONObject expectedResult, JSONCompareMode jsonCompareMode) {
        super(expectedResult, jsonCompareMode);
    }

    public static JSONResponseObjectMatcher looselyMatches(String expectedJson) throws JSONException {
        JSONObject expected = (JSONObject) JSONParser.parseJSON(expectedJson);
        return new JSONResponseObjectMatcher(expected, JSONCompareMode.LENIENT);
    }

    public static JSONResponseObjectMatcher strictlyMatches(String expectedJson) throws JSONException {
        JSONObject expected = (JSONObject) JSONParser.parseJSON(expectedJson);
        return new JSONResponseObjectMatcher(expected, JSONCompareMode.STRICT);
    }

    @Override
    protected boolean matchesSafely(Response expectedJson, Description mismatchDescription) {
        try {
            JSONObject expected = (JSONObject) JSONParser.parseJSON(expectedJson.getEntity().toString());
            return super.matchesSafely(expected, mismatchDescription);
        } catch (JSONException e) {
            return false;
        }
    }

}
