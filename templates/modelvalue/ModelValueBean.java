package ${config.project.packageName}.model.value;

import java.io.Serializable;
import java.util.List;
import javax.persistence.criteria.*;

import ${config.project.packageName}.model.Model;
/**
 *
 * @author ${config.project.author}
 */
public interface ModelValueBean extends Serializable{

    public Predicate getBusinessKey(CriteriaBuilder criteriaBuilder,Root<? extends Model> root);
    
}
