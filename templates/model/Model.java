package ${config.project.packageName}.${config.project.model};

import java.io.Serializable;
import ${config.project.packageName}.model.value.ModelValueBean;

/**
 *
 * @author ${config.project.author}
 */
public interface Model<MB> extends Serializable {

     public void saveValue(MB modelBean);
     
     public void updateValue(MB modelBean);
     
     public MB getValue();
     
     public MB getBusinessKeyValue();

     public void populateValue(MB modelBean,Enum dataset);
}
