package ${config.project.packageName}.model.value;

import java.io.Serializable;
import java.util.List;


/**
 *
 * @author ${config.project.author}
 */
public class Page<T> implements Serializable{

	private List<T> modelValueList;
	
	private int total;

    public List<T> getModelValueList() {
        return modelValueList;
    }

    public void setModelValueList(List<T> modelValueList) {
        this.modelValueList = modelValueList;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }		
		
}
