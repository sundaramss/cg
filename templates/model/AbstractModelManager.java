package ${config.project.packageName}.${config.project.model};


import ${config.project.packageName}.model.value.*;

import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;


/**
 *
 * @author @author ${config.project.author}
 */
public abstract class AbstractModelManager<M extends Model, MB extends ModelValueBean> implements ModelManager<M, MB> {

    
    
    protected EntityManager entityManager;

    protected AbstractModelManager() {
        
    }
    
    protected abstract Class<M> getEntityType();

    @PersistenceContext(unitName="${config.project.name}Unit")
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }


    @Override
    public M createModel(MB modelBean) {

        M model = initializeModel(modelBean);
        entityManager.persist(model);

        return model; 
    }

    protected abstract  M initializeModel(MB modelBean);

    public boolean delete(MB modelValue) {

        M model = lookupBySurrogateKey(modelValue);
        if(model!=null) {
            entityManager.remove(model);
            return true;
        }

        return false;
    }
    

    @Override
    public M lookupBySurrogateKey(MB modelValue) {
        
        if (modelValue == null || modelValue.getSkGuid() == null) {
            return null;
        } else {
            ${config.project.skGuidType} skGUID = modelValue.getSkGuid();
            Class<M> type = getEntityType();
            M m = entityManager.find(type, skGUID);
            return m;
        }
    }

	   
    @Override
    public M lookupByBusinessKey(MB criteriaValue) {
        
        Class<M> type = getEntityType();
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<M>  criteriaQuery = criteriaBuilder.createQuery(type);
        Root<M> mainRoot = criteriaQuery.from(type);
        GroupFilterValue groupFilterValue = criteriaValue.getBusinessKeys();
        Predicate wherePredicate = preparePredicate(groupFilterValue,criteriaBuilder,mainRoot,criteriaValue);
        
        try{
            Predicate andPredicate = criteriaBuilder.and(wherePredicate);
            criteriaQuery.where(andPredicate);
            M model = entityManager.createQuery(criteriaQuery).getSingleResult();
            return model;
        }catch(NoResultException ignore) {
			
        }
        
        return null;
    }

    @Override
    public List<M> lookupByCriteria(Serializable value,DataSet dataSet){
		
        Class<M> type = getEntityType();
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<M>  criteriaQuery = criteriaBuilder.createQuery(type);
        Root<M> mainRoot = criteriaQuery.from(type);

        GroupFilterValue groupFilterValue = dataSet.getGroupFilterValue();

        Predicate predicate = preparePredicate(groupFilterValue,criteriaBuilder,mainRoot,value);
        if( predicate != null ) {
            criteriaQuery.where(predicate);
        }

        List<SortOrderValue> sortOrderValues = dataSet.getSortOrderValues();
        if(! sortOrderValues.isEmpty() ) {
            prepareOrderList(mainRoot,null,sortOrderValues);
            criteriaQuery.orderBy(sortOrderValues.toArray(new Order[sortOrderValues.size()]));
        }

        List<M> resultList = entityManager.createQuery(criteriaQuery).getResultList();
        return  resultList;
    }

    protected Page<M> lookupByCriteria(Serializable value, int pageNumber, int pageSize, DataSet dataSet) {


        Class<M> type = getEntityType();
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> criteriaCountQuery = criteriaBuilder.createQuery(Long.class);
        Root<M> root = criteriaCountQuery.from(type);
        criteriaCountQuery.select(criteriaBuilder.count(root));

        GroupFilterValue groupFilterValue = dataSet.getGroupFilterValue();

        Predicate wherePredicate = preparePredicate(groupFilterValue,criteriaBuilder,root,value);
        if(wherePredicate != null) {
            criteriaCountQuery.where(wherePredicate);
        }

        Page<M> page = new Page<M>();
        int totalRecords =  entityManager.createQuery(criteriaCountQuery).getSingleResult().intValue();
        page.setTotal(totalRecords);

        if(totalRecords == 0) {
            page.setModelValueList(Collections.<M>emptyList());
            return page;
        }

        List<SortOrderValue> sortOrderList = dataSet.getSortOrderValues();
        validateSortColumns(sortOrderList);

        CriteriaQuery<M>  criteriaQuery = criteriaBuilder.createQuery(type);
        Root<M> mainRoot = criteriaQuery.from(type);


        if(! sortOrderList.isEmpty()) {
            //TODO: handle the join map
            prepareOrderList(mainRoot,null,sortOrderList);
            criteriaQuery.orderBy(sortOrderList.toArray(new Order[0]));
        }

        wherePredicate = preparePredicate(groupFilterValue,criteriaBuilder,mainRoot,value);

        if(wherePredicate!=null  ){
            criteriaQuery.where(wherePredicate);
        }

        TypedQuery<M> typedQuery = entityManager.createQuery(criteriaQuery);
        if ( pageSize <= 0 ) {
            pageSize=10;
        }
        if( pageNumber <=0 ){
            pageNumber =1;
        }
        int totalPage = (totalRecords / pageSize);
        if( (totalRecords % pageSize) > 0 ) {
            totalPage++;
        }
        if (Integer.MAX_VALUE == pageNumber){
            pageNumber = totalPage;
        }else if (pageNumber>totalPage){
            pageNumber = totalPage;
        }

        typedQuery.setFirstResult((pageNumber -1) * pageSize);
        typedQuery.setMaxResults(pageSize);

        List<M> entityList = typedQuery.getResultList();
        page.setModelValueList(entityList);

        return page;
    }

    protected void validateSortColumns(List<SortOrderValue> sortOrderList) {
		
	}

	private void prepareOrderList(Root<M> root, Map<String,Join<? extends Model,? extends Model>> map, List<SortOrderValue> sortOrderList) {
    	 
        for(SortOrderValue sortOrderValue: sortOrderList){
        	sortOrderValue.prepareExpression(root,map);
        }
        
    }

    protected Predicate preparePredicate(GroupFilterValue groupFilter,CriteriaBuilder criteriaBuilder,Root<?> root,Serializable bean) {

        if( groupFilter == null || groupFilter.getFilterValues() == null ){
            return null;
        }

        List<FilterValue<?>> filterValues = groupFilter.getFilterValues();

        List<Predicate>  predicateList =  new ArrayList<Predicate>(filterValues.size());

        for(FilterValue<?> filterValue:filterValues){
            predicateList.add(filterValue.preparePredicate(criteriaBuilder,root,bean));
        }

        Predicate filterPredicate = null;
        if(! predicateList.isEmpty()){
            if(groupFilter.getFilterListGroupType() == GroupFilter.GroupType.OR && predicateList.size() > 1) {
                filterPredicate = criteriaBuilder.or(predicateList.toArray(new Predicate[predicateList.size()]));
            }else {
                filterPredicate = criteriaBuilder.and(predicateList.toArray(new Predicate[predicateList.size()]));
            }
        }

        Predicate groupPredicate = preparePredicate(groupFilter.getGroupFilterValue(),criteriaBuilder,root,bean);
        Predicate returnPredicate = filterPredicate;
        if( groupPredicate != null ) {
            if(  groupFilter.getGroupType() == GroupFilter.GroupType.OR) {
                if(filterPredicate !=null){
                    returnPredicate = criteriaBuilder.or(filterPredicate,groupPredicate);
                }else{
                    returnPredicate = criteriaBuilder.or(groupPredicate);
                }
            }else {
                if(filterPredicate !=null){
                    returnPredicate = criteriaBuilder.and(filterPredicate,groupPredicate);
                }else{
                    returnPredicate = criteriaBuilder.and(groupPredicate);
                }
            }
        }

        return returnPredicate;
    }




    
    protected void populateValueByDataset(M model,MB valueBean,Enum... datasets) {
		
		if(model == null || valueBean == null) return;
		
		for(Enum dataset:datasets){
			model.populateValue(valueBean,dataset);	
		}		
		
	}
}
