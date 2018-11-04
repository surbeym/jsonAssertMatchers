package ca.surbeyonline.jsonMatcher.array;

import ca.surbeyonline.jsonMatcher.JSONMatcher;
import org.hamcrest.Description;
import org.json.JSONArray;
import org.json.JSONException;
import org.skyscreamer.jsonassert.JSONCompare;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.skyscreamer.jsonassert.JSONCompareResult;

public abstract class JSONArrayMatcher<T> extends JSONMatcher<T> {
    private JSONArray expectedResult;

    protected JSONArrayMatcher(JSONArray expectedResult, JSONCompareMode jSONCompareMode) {
        this.expectedResult = expectedResult;
        this.jsonCompareMode = jSONCompareMode;
    }

    @Override
    public <T> T getExpectedResult() {
        return null;
    }


    protected boolean matchesSafely(JSONArray actualJsonArray, Description description) {
        try {
            JSONCompareResult jsonCompareResult = JSONCompare.compareJSON(expectedResult, actualJsonArray, jsonCompareMode);
            if (jsonCompareResult.failed()) {
                this.jsonCompareResult = jsonCompareResult;
//                if (jsonCompareMode.isExtensible() && !jsonCompareResult.isUnexpectedOnField() && !jsonCompareResult.isMissingOnField() && !jsonCompareResult.isFailureOnField()) {
//                    return true;
//                }
                this.jsonCompareResult = jsonCompareResult;
                description.appendText("Actual   Value ").appendValue(actualJsonArray)
                        .appendText(", which did not match \n")
                        .appendText("          Expected Value ")
                        .appendValue(expectedResult);
            }
            return jsonCompareResult.passed();
        } catch (JSONException e) {
            jsonException = e;
        }
        return false;
    }
}
