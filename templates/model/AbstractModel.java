package ${config.project.packageName}.${config.project.model};

import ${config.project.packageName}.model.value.ModelValueBean;

/**
 *
 * @author ${config.project.author}
 */
public abstract class AbstractModel<MB extends ModelValueBean> implements Model<MB> {
    
    
    protected AbstractModel(){
        
    }
}
