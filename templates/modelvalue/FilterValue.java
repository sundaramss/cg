package ${config.project.packageName}.model.value;

import java.io.Serializable;
import javax.persistence.criteria.*;

/**
 *
 * @author ${config.project.author}
 */
public class FilterValue implements Serializable{

    public enum ConditionType {
        LIKE,EQUAL,EQUAL_IGNORE_CASE
    }

    private String type;
    private String value;
    private  String field;
    private ConditionType conditionType;
    
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public ConditionType getConditionType() {
        return conditionType;
    }

    public void setConditionType(ConditionType conditionType) {
        this.conditionType = conditionType;
    }

    public Predicate preparePredicate(CriteriaBuilder criteriaBuilder,Root<?> root) {

        Predicate predicate = null;
        switch (conditionType) {
            case  LIKE:
                StringBuilder literalValue = new StringBuilder().append("%%").append(this.value).append("%%");
                Expression<String> literal = criteriaBuilder.upper(criteriaBuilder.literal(literalValue.toString()));
                predicate = criteriaBuilder.like(criteriaBuilder.upper(root.<String>get(field)), literal);
                break;
            case  EQUAL_IGNORE_CASE:
                Expression<String> literal = criteriaBuilder.upper(criteriaBuilder.literal(this.value));
                predicate = criteriaBuilder.like(criteriaBuilder.upper(root.<String>get(field)), literal);
                break;
            case  EQUAL:
                Expression<String> literal = criteriaBuilder.literal(this.value);
                predicate = criteriaBuilder.like(criteriaBuilder.upper(root.<String>get(field)), literal);
                break;
        }

        return  predicate;

    }

}
