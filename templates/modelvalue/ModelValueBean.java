package ${config.project.packageName}.model.value;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author ${config.project.author}
 */
public interface ModelValueBean<T extends Enum> extends Serializable{

    public Long getSkGuid();
    
    public T getBusinessKeyDataset();
    
    public List<String> getFiltersBy();
    
}
