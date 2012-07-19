package ${config.project.packageName}.${config.project.model};

import java.io.Serializable;
import ${config.project.packageName}.model.value.ModelValueBean;

/**
 *
 * @author ${config.project.author}
 */
public interface Model<MB extends ModelValueBean> extends Serializable {

     public void saveValue(MB modelBean);
     
     public void updateValue(MB modelBean);
     
     public MB getValue();
     
     public MB getInitValue();

     public MB populateValue(MB modelBean,Enum dataset);
}
