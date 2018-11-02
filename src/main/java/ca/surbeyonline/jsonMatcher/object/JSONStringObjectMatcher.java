package ca.surbeyonline.jsonMatcher.object;

import org.hamcrest.Description;
import org.json.JSONException;
import org.json.JSONObject;
import org.skyscreamer.jsonassert.*;

public class JSONStringObjectMatcher extends JSONObjectMatcher<String> {

    public JSONStringObjectMatcher(JSONObject expectedResult, JSONCompareMode jSONCompareMode) {
        super(expectedResult, jSONCompareMode);
    }

    public static JSONStringObjectMatcher jsonStringMatchesLooselyTo(String expectedJson) throws JSONException {
        JSONObject expected = (JSONObject) JSONParser.parseJSON(expectedJson);
        return new JSONStringObjectMatcher(expected, JSONCompareMode.LENIENT);
    }

    public static JSONStringObjectMatcher jsonStringMatchesStrictlyTo(String expectedJson) throws JSONException {
        JSONObject expected = (JSONObject) JSONParser.parseJSON(expectedJson);
        return new JSONStringObjectMatcher(expected, JSONCompareMode.STRICT);
    }

    @Override
    protected boolean matchesSafely(String actualJson, Description mismatchDescription) {
        try {
            JSONObject actual = (JSONObject) JSONParser.parseJSON(actualJson);
            return super.matchesSafely(actual, mismatchDescription);
        } catch (JSONException e) {
            mismatchDescription.appendText("it was missing");
            jsonException = e;
        }
        return false;
    }
}
