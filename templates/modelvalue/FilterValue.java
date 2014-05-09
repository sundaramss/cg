package ${config.project.packageName}.model.value;

import java.io.Serializable;
import javax.persistence.metamodel.Attribute; 

/**
 *
 * @author ${config.project.author}
 */
public class FilterValue<T> implements Serializable{

    private Filter filter;
    private Attribute field;
    
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


    private Expression<T> getValueExpression(CriteriaBuilder criteriaBuilder,Serializable bean) {
       DirectFieldAccessor fieldAccessor = new DirectFieldAccessor(bean);
       T value = (T) fieldAccessor.getPropertyType(field.getName());
       return criteriaBuilder.literal(value);
    }

    private Expression<T> getFieldExpression(Root<?> root){
        String fieldPath = field.getName();
       return root.get(fieldPath);
    }

    public Predicate preparePredicate(CriteriaBuilder criteriaBuilder,Root<?> root,Serializable serializable) {
        Expression<T> fieldExpression = getFieldExpression(root);
        Expression<T> valueExpression = getValueExpression(criteriaBuilder,serializable);
        return filter.preparePredicate(criteriaBuilder,fieldExpression,valueExpression);
    }
}

