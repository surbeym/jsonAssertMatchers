package ca.surbeyonline.jsonMatcher;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeDiagnosingMatcher;
import org.json.JSONException;
import org.json.JSONObject;
import org.skyscreamer.jsonassert.*;

import java.util.List;

public class JSONObjectMatcher extends TypeSafeDiagnosingMatcher<String> {
    private JSONObject expectedResult;
    private JSONCompareMode jsonCompareMode;
    private JSONCompareResult jsonCompareResult;

    private JSONObjectMatcher(JSONObject expectedResult, JSONCompareMode jSONCompareMode) {
        this.expectedResult = expectedResult;
        this.jsonCompareMode = jSONCompareMode;
    }

    @Override
    protected boolean matchesSafely(String actual, Description description) {

        try {
            JSONObject actualJsonArray = (JSONObject) JSONParser.parseJSON(actual);
            JSONCompareResult jsonCompareResult = JSONCompare.compareJSON(expectedResult, actualJsonArray, jsonCompareMode);
            if (jsonCompareResult.failed()) {
                this.jsonCompareResult = jsonCompareResult;
                description.appendText("Actual   Value ").appendValue(actualJsonArray)
                           .appendText(", which did not match \n").appendText("          Expected Value ")
                           .appendValue(expectedResult);
            }
            return jsonCompareResult.passed();
        } catch (JSONException e) {
            description.appendText(e.getMessage());
        }
        return false;
    }

    @Override
    public void describeTo(Description description) {
        if (jsonCompareResult.isMissingOnField()) {
            List<FieldComparisonFailure> missingFields = jsonCompareResult.getFieldMissing();
            for (FieldComparisonFailure missingField : missingFields) {
                if (missingField != missingFields.get(0)) {
                    description.appendText("          ");
                }
                description.appendText(missingField.getExpected() + " in the structure of " + missingField.getField());
                if (missingField != missingFields.get(missingFields.size() - 1)) {
                    description.appendText("\n");
                }
            }
        }
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
