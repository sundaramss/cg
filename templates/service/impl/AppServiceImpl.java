package ${config.project.packageName}.service.impl;

import ${config.project.packageName}.exception.ApplicationException;
import ${config.project.packageName}.model.Model;
import ${config.project.packageName}.model.ModelManager;
import ${config.project.packageName}.model.value.ModelValueBean;
import ${config.project.packageName}.service.AppService;

import java.util.List;
import java.util.ArrayList;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


/**
 *
 * @author  ${config.project.author}
 */
public abstract class AppServiceImpl<M extends Model,MB extends ModelValueBean> implements AppService<MB> {

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = { ApplicationException.class })
    public MB create(MB criteriaModelValue) {
        ModelManager<M,MB> modelManager = getModelManager();
        M model = modelManager.createModel(criteriaModelValue);
        return (MB) model.getValue();
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = { ApplicationException.class })
    public MB update(MB updateModelValue) {
        ModelManager<M,MB> modelManager = getModelManager();
        M model = modelManager.lookupBySurrogateKey(updateModelValue);
        if( model == null) {
            model = modelManager.lookupByBusinessKey(updateModelValue);
        }
        if(model == null) {
            return null;
        }
        model.updateValue(updateModelValue);
        return (MB) model.getValue();
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = { ApplicationException.class })
    public MB delete(MB modelValue) {
        ModelManager<M,MB> modelManager = getModelManager();
        modelManager.delete(modelValue);
        return modelValue;
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS, rollbackFor = { ApplicationException.class })
    public List<MB> getAll() {
        ModelManager<M,MB> modelManager = getModelManager();
        List<M> modelList = modelManager.getAll();
        List<MB> modelValueBeans = new ArrayList<MB>();
        for(M model:modelList) {
            MB modelValueBean = (MB) model.getValue();
            modelValueBeans.add(modelValueBean);
        }
        return modelValueBeans;    
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS, rollbackFor = { ApplicationException.class })
    public MB get(MB criteriaValue) {

        ModelManager<M,MB> modelManager = getModelManager();

        M model = modelManager.lookupBySurrogateKey(criteriaValue);

        if(model == null) {
            return null;
        }

        return (MB) model.getValue();
    }

    protected abstract ModelManager<M,MB> getModelManager();
}
