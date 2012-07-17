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

    @PersistenceContext(unitName="${config.project.persistenceUnitName}")
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }


    @Override
    public MB createModel(MB modelBean) {

        M model = initializeModel(modelBean);
        entityManager.persist(model);

        return (MB) model.getValue();
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
    

    @Transactional(readOnly = true)
    public M lookupBySurrogateKey(MB modelValue) {
        
        if (modelValue == null || modelValue.getSkGuid() == null) {
            return null;
        } else {
            String skGUID = modelValue.getSkGuid();
            Class<M> type = getEntityType();
            M m = entityManager.find(type, skGUID);
            Assert.notNull(m,"Invalid SkGUID");
            return m;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public M lookupByBusinessKey(MB modelValue) {
        Class<M> type = getEntityType();
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<M>  criteriaQuery = criteriaBuilder.createQuery(type);
        Root<M> mainRoot = criteriaQuery.from(type);
        FilterValue[] filters = modelValue.getBusinessKeys();
        Predicate[] businessKeyPredicate = preparePredicates(modelValue,criteriaBuilder,mainRoot,filters);
        try{
            Predicate andPredicate = criteriaBuilder.and(businessKeyPredicate);
            criteriaQuery.where(andPredicate);
            M model = entityManager.createQuery(criteriaQuery).getSingleResult();
            return model;
        }catch(NoResultException ignore) {

        }
        return null;
    }



    public List<MB> getAll(Enum... datasets) {
        
        Class<M> type = getEntityType();
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<M> criteriaQuery = criteriaBuilder.createQuery(type);
        Root<M> from = criteriaQuery.from(type);
        CriteriaQuery<M> select = criteriaQuery.select(from);
        TypedQuery<M> typedQuery = entityManager.createQuery(select);
        List<M> resultList = typedQuery.getResultList();
        //TODO: iterate and apply the datasets
        List<MB> resultValueList = new ArrayList<MB>(resultList.size());
        for(M m: resultList ) {
            resultValueList.add((MB)m.getValue());
        }
        return resultValueList;
        
    }

    protected List<M> lookupByCriteria(Serializable value,List<FilterValue> filterValueList){
        Class<M> type = getEntityType();
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<M>  criteriaQuery = criteriaBuilder.createQuery(type);
        Root<M> mainRoot = criteriaQuery.from(type);

        FilterValue[] filters = filterValueList.toArray(new FilterValue[0]);
        Predicate[] predicates = preparePredicates(value,criteriaBuilder,mainRoot,filters);
        //TODO needs to improve join, or,group conditions
        if( predicates.length > 0 ) {
            Predicate andPredicate = criteriaBuilder.and(predicates);
            criteriaQuery.where(andPredicate);
        }

        List<M> resultList = entityManager.createQuery(criteriaQuery).getResultList();
        return  resultList;
    }
    
    @Override
    public List<MB> lookupByCriteria(Serializable value,List<FilterValue> filterValueList,List<Enum> datasets){


        List<M> resultList = lookupByCriteria(value,filterValueList);
        //TODO: iterate and apply the datasets
        List<MB> resultValueList = new ArrayList<MB>(resultList.size());
        for(M m: resultList ) {
            resultValueList.add((MB)m.getValue());
        }

        return  resultValueList;
    }

    protected Page<M> lookupByCriteria(Serializable value, int pageNumber, int pageSize, List<FilterValue> filterValueList, List<SortOrderValue> sortOrderList) {

        Class<M> type = getEntityType();
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> criteriaCountQuery = criteriaBuilder.createQuery(Long.class);
        Root<M> root = criteriaCountQuery.from(type);
        criteriaCountQuery.select(criteriaBuilder.count(root));

        FilterValue[] filters = filterValueList.toArray(new FilterValue[0]);
        Predicate[] predicates = preparePredicates(value,criteriaBuilder,root,filters);

        if(predicates != null ) {
            Predicate andPredicate = criteriaBuilder.and(predicates);
            criteriaCountQuery.where(andPredicate);
        }

        Page<M> page = new Page<M>();
        int totalRecords =  entityManager.createQuery(criteriaCountQuery).getSingleResult().intValue();
        page.setTotal(totalRecords);
        if(totalRecords == 0) {
            page.setModelValueList(Collections.<M>emptyList());
            return page;
        }

        validateSortColumns(sortOrderList);


        CriteriaQuery<M>  criteriaQuery = criteriaBuilder.createQuery(type);
        Root<M> mainRoot = criteriaQuery.from(type);


        if(! sortOrderList.isEmpty()) {
            //TODO: handle the join map
            prepareOrderList(mainRoot,null,sortOrderList);
            criteriaQuery.orderBy(sortOrderList.toArray(new Order[0]));
        }

        predicates = preparePredicates(value,criteriaBuilder,mainRoot,filters);

        if(predicates!=null  ){
            Predicate andPredicate = criteriaBuilder.and(predicates);
            criteriaQuery.where(andPredicate);
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

    @Override
    public Page<MB> lookupByCriteria(Serializable value, int pageNumber, int pageSize, List<FilterValue> filterValueList,List<Enum> datasets, List<SortOrderValue> sortOrderList){

        Page<M> page = lookupByCriteria(value,pageNumber,pageSize,filterValueList,sortOrderList);
        Page<MB> pageValue = new Page<MB>();

        List<M>  modelList = page.getModelValueList();
        List<MB> valueList = new ArrayList<MB>(modelList.size());

        pageValue.setTotal(page.getTotal());
        pageValue.setModelValueList(valueList);

        for(M m:modelList) {
            //TODO: Should be implement dataset implementations
            valueList.add((MB) m.getValue());
        }

        return  pageValue;

    }

    protected void validateSortColumns(List<SortOrderValue> sortOrderList) {
		
	}

	private void prepareOrderList(Root<M> root, Map<String,Join<? extends Model,? extends Model>> map, List<SortOrderValue> sortOrderList) {
    	 
        for(SortOrderValue sortOrderValue: sortOrderList){
        	sortOrderValue.prepareExpression(root,map);
        }
        
    }

    private Predicate[] preparePredicates(Serializable serializable,CriteriaBuilder criteriaBuilder,Root<M> root,FilterValue... filters) {

        List<Predicate> predicateList = new ArrayList<Predicate>(filters.length);

        for(FilterValue filterValue:filters) {
            //TODO: refactor the below step by Proper Attribute type
            //TODO: NullPointer should be handle
            String valuePath = filterValue.getField().getName();
            Expression expression = root.get(valuePath);
            Object value = getValue(serializable,valuePath);
            FilterType filterType = filterValue.getFilterType();
            Predicate predicate = filterType.prepareSimplePredicate(criteriaBuilder,expression,value);
            predicateList.add(predicate);
        }

        return predicateList.toArray(new Predicate[0]);  //To change body of created methods use File | Settings | File Templates.
    }




    private Object getValue(Serializable serializable,String valuePath) {

        Object value=null;
        try {
            value =  PropertyUtils.getSimpleProperty(serializable,valuePath);
            return value;
        } catch (IllegalAccessException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (InvocationTargetException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (NoSuchMethodException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        return null;
    }
}
