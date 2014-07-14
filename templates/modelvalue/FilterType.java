package ${config.project.packageName}.model.value;

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
                Expression<Number> value = filterValue.getFieldExpression();
				return  criteriaBuilder.lt(field, value);
		}
    },

	EQUAL_LIKE(){
        public <T>  Predicate preparePredicate(CriteriaBuilder criteriaBuilder, FilterValue<T> filterValue) {
            Expression<String> field = filterValue.getFieldExpression();
            Expression<String> value = filterValue.getFieldExpression();
            return criteriaBuilder.like(field,value);
        }
    };

	public abstract <T> Predicate preparePredicate(CriteriaBuilder criteriaBuilder, FilterValue<T> filterValue);
    
}

