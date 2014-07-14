package ${config.project.packageName}.model.value;

import java.io.Serializable;
import java.util.List;

import ${config.project.packageName}.model.Model;
/**
 *
 * @author ${config.project.author}
 */
public interface ModelValueBean extends Serializable{

	public String getSkGuid();
	
    public void setSkGuid(String skGuid);
    public GroupFilterValue getBusinessKeys();
    
}
