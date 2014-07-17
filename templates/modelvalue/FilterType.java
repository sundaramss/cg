package ${config.project.packageName}.model.value;

import org.springframework.util.StringUtils;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;

/**
 *
 * @author ${config.project.author}
 */
public enum FilterType implements Filter{

	EQUAL() {
        public <T> Predicate preparePredicate(CriteriaBuilder criteriaBuilder,FilterValue<T> filterValue){
				return  criteriaBuilder.equal(filterValue.getFieldExpression(), filterValue.getValueExpression());
		}	
	},

    LESS_THAN() {
        public <T> Predicate preparePredicate(CriteriaBuilder criteriaBuilder,FilterValue<T> filterValue){
                Expression<Number> field = filterValue.getFieldExpression();
                Expression<Number> value = filterValue.getValueExpression();
				return  criteriaBuilder.lt(field, value);
		}
    },

	EQUAL_LIKE(){

        public <T> Expression<T> prepareValueExpression(CriteriaBuilder criteriaBuilder,T value) {
            String newValue = StringUtils.replace((String)value, "\\\\", "\\\\\\\\");
            newValue = StringUtils.replace(newValue, "%", "\\\\%");
            newValue = StringUtils.replace(newValue, "_", "\\\\_");
            StringBuilder literalValue = new StringBuilder().append("%%").append(newValue).append("%%");
            return criteriaBuilder.literal((T)literalValue.toString());
        }

        public <T>  Predicate preparePredicate(CriteriaBuilder criteriaBuilder, FilterValue<T> filterValue) {
            Expression<String> field = filterValue.getFieldExpression();
            Expression<String> value = filterValue.getValueExpression();
            return criteriaBuilder.like(field,value);
        }
    };

    public <T> Expression<T> prepareValueExpression(CriteriaBuilder criteriaBuilder,T value) {
        return criteriaBuilder.literal(value);
    }
	//public abstract <T> Predicate preparePredicate(CriteriaBuilder criteriaBuilder, FilterValue<T> filterValue);
    
}

