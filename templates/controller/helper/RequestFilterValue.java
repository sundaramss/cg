package ${config.project.packageName}.controller.helper;

import org.codehaus.jackson.JsonNode;

/**
 * Created by ${config.project.author}.
 */
public class RequestFilterValue {

    private String field;
    private JsonNode value;
    private String operator;

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public JsonNode getValue() {
        return value;
    }

    public void setValue(JsonNode value) {
        this.value = value;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }
}
