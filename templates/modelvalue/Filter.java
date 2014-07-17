package ${config.project.packageName}.model.value;

import java.io.Serializable;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
/**
 *
 * @author ${config.project.author}
 */
public interface Filter extends Serializable{

	public <T> Expression<T> prepareValueExpression(CriteriaBuilder criteriaBuilder,T value);
	public <T> Predicate preparePredicate(CriteriaBuilder criteriaBuilder, FilterValue<T> filterValue);
    
}

