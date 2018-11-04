package ca.surbeyonline.jsonMatcher;

import org.hamcrest.Description;
import org.hamcrest.StringDescription;
import org.hamcrest.TypeSafeDiagnosingMatcher;
import org.json.JSONException;
import org.json.JSONObject;
import org.skyscreamer.jsonassert.FieldComparisonFailure;
import org.skyscreamer.jsonassert.JSONCompare;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.skyscreamer.jsonassert.JSONCompareResult;

import java.util.List;
import java.util.function.BiConsumer;

public abstract class JSONMatcher<T> extends TypeSafeDiagnosingMatcher<T> {

    public final static String ROOT = "{BASE}";
    protected JSONCompareMode jsonCompareMode;
    protected JSONCompareResult jsonCompareResult;
    protected JSONException jsonException;

    public abstract <T> T getExpectedResult();

    @Override
    public void describeTo(Description description) {
        if (jsonCompareResult != null) {
            if (jsonCompareResult.isMissingOnField()) {
                missingFields.accept(jsonCompareResult, description);
            }
            if (jsonCompareResult.isUnexpectedOnField()) {
                unexpectedFields.accept(jsonCompareResult, description);
            } if (!jsonCompareResult.isMissingOnField() && !jsonCompareResult.isUnexpectedOnField()) {
                description.appendText(jsonCompareResult.getMessage());
            }
        } else {
            description.appendText(jsonException.getMessage().substring(9));
        }
    }

    protected BiConsumer<JSONCompareResult, Description> missingFields = (compareResult, description) -> {
        List<FieldComparisonFailure> missingFields = compareResult.getFieldMissing();
        missingFields.forEach(missingField -> {
            if (missingField != missingFields.get(0)) {
                description.appendText("          ");
            }
            String field = missingField.getField();
            if ("".equals(field)){
                field = ROOT;
            }

            description.appendText(missingField.getExpected() + " in the structure of " + field);
            if (missingField != missingFields.get(missingFields.size() - 1)) {
                description.appendText("\n");
            }
        });
    };

    protected BiConsumer<JSONCompareResult, Description> unexpectedFields = (compareResult, description) -> {
        List<FieldComparisonFailure> unexpectedFields = compareResult.getFieldUnexpected();
        unexpectedFields.forEach(unexpectedField -> {
            if (unexpectedField != unexpectedFields.get(0)) {
                description.appendText("          ");
            }
            String field = unexpectedField.getField();
            if ("".equals(field)){
                field = ROOT;
            }
            Object value = unexpectedField.getActual();
            description.appendText(field + " structure should not contain " + value);
            if (unexpectedField != unexpectedFields.get(unexpectedFields.size() - 1)) {
                description.appendText("\n");
            }
        });
    };
}
