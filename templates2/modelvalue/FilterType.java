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
		public <T> Predicate prepareSimplePredicate(CriteriaBuilder criteriaBuilder,Expression fieldExpression,T value){
				Expression<T> valueExpression = criteriaBuilder.literal(value);
				return  criteriaBuilder.equal(fieldExpression,valueExpression);
		}	
	};

	public abstract <T> Predicate prepareSimplePredicate(CriteriaBuilder criteriaBuilder,Expression fieldExpression,T value);
    
}
