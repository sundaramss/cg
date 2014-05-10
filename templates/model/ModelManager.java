package ${config.project.packageName}.${config.project.model};

import java.util.List;
import java.util.Map;
import java.io.Serializable;
import ${config.project.packageName}.model.value.FilterValue;
import ${config.project.packageName}.model.value.ModelValueBean;
import ${config.project.packageName}.model.value.Page;
import ${config.project.packageName}.model.value.SortOrderValue;

/**
 *
 * @author ${config.project.author}
 */
public interface ModelManager<M,MB> {
   
    public M createModel(MB modelBean);

    public boolean delete(MB modelBean);
    
    public M lookupBySurrogateKey(MB modelValue);
    public M lookupByBusinessKey(MB modelValue);
    
        
    public List<M> getAll();
    
    
}
