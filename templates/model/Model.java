package ${config.project.packageName}.${config.project.model};

import java.io.Serializable;

/**
 *
 * @author ${config.project.author}
 */
public interface Model<MB extends ModelValueBean> extends Serializable {

     public void saveValue(MB modelBean);
     
     public void updateValue(MB modelBean);
     
     public MB getValue();
}
