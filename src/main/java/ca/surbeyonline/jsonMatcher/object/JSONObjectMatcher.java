package ca.surbeyonline.jsonMatcher.object;

import ca.surbeyonline.jsonMatcher.JSONMatcher;
import org.hamcrest.Description;
import org.json.JSONException;
import org.json.JSONObject;
import org.skyscreamer.jsonassert.JSONCompare;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.skyscreamer.jsonassert.JSONCompareResult;

public abstract class JSONObjectMatcher<T> extends JSONMatcher<T> {
    private JSONObject expectedResult;

    protected JSONObjectMatcher(JSONObject expectedResult, JSONCompareMode jSONCompareMode) {
        this.expectedResult = expectedResult;
        this.jsonCompareMode = jSONCompareMode;
    }

    protected boolean matchesSafely(JSONObject actualJsonArray, Description description) {

//        description.appendText("Actual   Value ").appendValue(actualJsonArray);
        try {
            JSONCompareResult jsonCompareResult = JSONCompare.compareJSON(getExpectedResult(), actualJsonArray, jsonCompareMode);
            if (jsonCompareResult.failed()) {
                this.jsonCompareResult = jsonCompareResult;
                description.appendText("Actual   Value ").appendValue(actualJsonArray)
                        .appendText(", which did not match \n")
                        .appendText("          Expected Value ")
                        .appendValue(getExpectedResult());
            }
            return jsonCompareResult.passed();
        } catch (JSONException e) {
        }
        return false;
    }

    @Override
    public <T> T getExpectedResult() {
        return (T) expectedResult;
    }
}
