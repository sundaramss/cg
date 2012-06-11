package ${config.project.packageName}.${config.project.model};

import java.util.List;
import java.util.Map;

/**
 *
 * @author ${config.project.author}
 */
public interface ModelManager<M extends Model,MB extends ModelValueBean,E extends Enum> {
   
    public M createModel(MB modelBean);
    public M lookupBySurrogateKey(MB modelValue);
    public M lookupByBusinessKey(MB modelValue);
    
    public List<M> lookupByCriteria(MB valueBean,E dataset);
    
    public Map lookupByCriteria(MB valueBean, int pageNumber, int pageSize, E dataSetType, List<SortOrderValue> sortOrderList);
        
    
    public List<M> getAll();
    public void save(M model);
    public void delete(M model);
    
}
