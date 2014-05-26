package ${config.project.packageName}.model.value;

import java.io.Serializable;
import java.util.Map;

import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Root;

import ${config.project.packageName}.model.Model;
/**
 *
 * @author ${config.project.author}
 */
public class SortOrderValue implements Order,Serializable{

    private String columnName;
    private boolean asending;

    private Expression expression;
    

    public void setAsending(boolean asending) {
        this.asending = asending;
    }

    @Override
    public boolean isAscending() {
        return this.asending;
    }

    
    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    @Override
    public Order reverse() {
        return this;
    }


    @Override
    public Expression<?> getExpression() {
        return this.expression;
    }
    
    public void prepareExpression(Root root,Map<String,Join<? extends Model,? extends Model>> map){
    	String name = this.columnName;
    	if(name == null) return;

    	String[] columns = name.split("\\\\.");
    	if( columns.length == 2 && map != null) {
    		Join<? extends Model,? extends Model> j = map.get(columns[0]);
    		if(j == null) {
    			j = root.join(columns[0]);
    			map.put(columns[0], j);
    		}
    		this.expression = j.get(columns[1]);
    	}else{
    		this.expression = root.get(this.columnName);
    	}
    } 
    
}
