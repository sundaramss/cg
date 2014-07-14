package ${config.project.packageName}.model.value;

import org.springframework.beans.DirectFieldAccessor;

import java.io.Serializable;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.Attribute;


/**
 *
 * @author ${config.project.author}
 */
public class FilterValue<T> implements Serializable{

    private Filter filter;
    private Attribute field;

    private Expression<T> valueExpression;
    private Expression<T> fieldExpression;

    public Filter getFilter() {
		return filter;
	}
	
	public void setFilter(Filter filter) {
		this.filter = filter;
	}
    
    
	public Attribute getField () {
		return this.field;
	}

	public void setField(Attribute field) {
		 this.field = field;
	}    


     private void prepareValueExpression(CriteriaBuilder criteriaBuilder, Serializable bean) {
       DirectFieldAccessor fieldAccessor = new DirectFieldAccessor(bean);
       T value = (T) fieldAccessor.getPropertyType(field.getName());
       this.valueExpression = criteriaBuilder.literal(value);
    }

    private void prepareFieldExpression(Root<?> root){
        String fieldPath = field.getName();
       this.fieldExpression = root.get(fieldPath);
    }

    public <T> Expression<T> getValueExpression() {
        return (Expression<T>) valueExpression;
    }

    public <T> Expression<T> getFieldExpression() {
        return (Expression<T>) fieldExpression;
    }

    public Predicate preparePredicate(CriteriaBuilder criteriaBuilder,Root<?> root,Serializable serializable) {
        prepareFieldExpression(root);
        prepareValueExpression(criteriaBuilder, serializable);
        return filter.preparePredicate(criteriaBuilder,this);
    }
}

