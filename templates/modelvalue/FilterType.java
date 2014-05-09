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
		public <T> Predicate preparePredicate(CriteriaBuilder criteriaBuilder,Expression<T> fieldExpression,Expression<T> valueExpression){
				return  criteriaBuilder.equal(fieldExpression,valueExpression);
		}	
	};

	public abstract <T> Predicate preparePredicate(CriteriaBuilder criteriaBuilder, Expression<T> fieldExpression,Expression<T> valueExpression);
    
}

