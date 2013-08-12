package ${config.project.packageName}.model.value;

import java.io.Serializable;
import javax.persistence.metamodel.Attribute; 

/**
 *
 * @author ${config.project.author}
 */
public class FilterValue implements Serializable{

    private FilterType filterType;
    private Attribute field;
    
    public FilterType getFilterType() {
		return filterType;
	}
	
	public void setFilterType(FilterType filterType) {
		this.filterType = filterType;
	}
    
    
	public Attribute getField () {
		return this.field;
	}

	public void setField(Attribute field) {
		 this.field = field;
	}    
	
}
