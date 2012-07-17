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

	public <T> Predicate prepareSimplePredicate(CriteriaBuilder criteriaBuilder,Expression fieldExpression,T value);
    
}
