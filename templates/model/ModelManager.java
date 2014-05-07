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
   
    public MB createModel(MB modelBean);

    public boolean delete(MB modelBean);
    
    public M lookupBySurrogateKey(MB modelValue);
    public M lookupByBusinessKey(MB modelValue);
    
	public MB lookupValueBySurrogateKey(MB modelValue,Enum... datasets);
    public MB lookupValueByBusinessKey(MB modelValue,Enum... datasets);
        
    public List<MB> lookupByCriteria(Serializable value,List<FilterValue> filterValueList,Enum... datasets);
    public Page<MB> lookupByCriteria(Serializable value, int pageNumber, int pageSize, List<FilterValue> filterValueList, List<SortOrderValue> sortOrderList,Enum... datasets);
    
    public List<MB> getAll(Enum... datasets);
    
    
}
