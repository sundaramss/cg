package ${config.project.packageName}.${config.project.model};

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import ${config.project.packageName}.model.value.SortOrderValue;
import ${config.project.packageName}.constant.ApplicationConstant;
import ${config.project.packageName}.model.value.ModelValueBean;

/**
 *
 * @author @author ${config.project.author}
 */
public abstract class AbstractModelManager<M extends Model, MB extends ModelValueBean,E extends Enum> implements ModelManager<M, MB,E> {

    
    
    protected EntityManager entityManager;

    protected AbstractModelManager() {
        
    }
    
    protected abstract Class<M> getEntityType();

    @PersistenceContext(unitName="${config.project.persistenceUnitName}")
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
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
    
    public void save(M model) {
        entityManager.persist(model);
    }
    
    public void merge(M model) {
        entityManager.merge(model);
    }
    

    public List<M> getAll() {
        
        Class<M> type = getEntityType();
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<M> criteriaQuery = criteriaBuilder.createQuery(type);
        Root<M> from = criteriaQuery.from(type);
        CriteriaQuery<M> select = criteriaQuery.select(from);
        TypedQuery<M> typedQuery = entityManager.createQuery(select);
        List<M> resultList = typedQuery.getResultList();
        
        return resultList;
        
    }

    
    public void delete(M model) {
        entityManager.remove(model);
    }

    protected abstract void appendCriteria(MB modelBean,CriteriaBuilder criteriaBuilder,CriteriaQuery criteriaQuery,E type,Root<M> root,List<Predicate> andCriteriaList);
    
    protected Map<String,Join<? extends Model,? extends Model>> appendJoinCriteria(MB modelBean,CriteriaBuilder criteriaBuilder,CriteriaQuery criteriaQuery,E type,Root<M> root,List<Predicate> andCriteriaList) {
    	return Collections.EMPTY_MAP;
    }
    
    

    @Override
    @Transactional(readOnly = true)
    public M lookupByBusinessKey(MB modelValue) {
        Class<M> type = getEntityType();
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<M>  criteriaQuery = criteriaBuilder.createQuery(type);
        Root<M> mainRoot = criteriaQuery.from(type);
        Predicate businessKeyPredicate = modelValue.getBusinessKey(criteriaBuilder,mainRoot);
        try{
            Predicate andPredicate = criteriaBuilder.and(businessKeyPredicate);
            criteriaQuery.where(andPredicate);
            M model = entityManager.createQuery(criteriaQuery).getSingleResult();
            return model;
        }catch(NoResultException ignore) {
            
        }
        return null;
    }

    @Override
    @Transactional(readOnly = true)
    public List<M> lookupByCriteria(MB valueBean, E dataset) {
        Class<M> type = getEntityType();
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<M>  criteriaQuery = criteriaBuilder.createQuery(type);
        Root<M> mainRoot = criteriaQuery.from(type);
        List<Predicate> andCriteriaList = new ArrayList<Predicate>();
        appendCriteria(valueBean,criteriaBuilder, criteriaQuery, dataset,mainRoot,andCriteriaList);
        if(! andCriteriaList.isEmpty()) {
        	Predicate andPredicate = criteriaBuilder.and(andCriteriaList.toArray(new Predicate[0]));
        	criteriaQuery.where(andPredicate);
    	}
        List<M> modelList = entityManager.createQuery(criteriaQuery).getResultList();
        return modelList;
    }
    
    
    @Transactional(readOnly = true,propagation=Propagation.REQUIRED)
    public Map lookupByCriteria(MB valueBean, int pageNumber, int pageSize, E dataSetType, List<SortOrderValue> sortOrderList) {
    	
        Class<M> type = getEntityType();
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> criteriaCountQuery = criteriaBuilder.createQuery(Long.class);
        Root<M> root = criteriaCountQuery.from(type);
        
        criteriaCountQuery.select(criteriaBuilder.count(root));
        
        List<Predicate> andCriteriaList = new ArrayList<Predicate>();
        appendCriteria(valueBean,criteriaBuilder, criteriaCountQuery, dataSetType,root,andCriteriaList);
        appendJoinCriteria(valueBean,criteriaBuilder, criteriaCountQuery, dataSetType,root,andCriteriaList);

        if(!andCriteriaList.isEmpty()) {
	        Predicate andPredicate = criteriaBuilder.and(andCriteriaList.toArray(new Predicate[0]));
			criteriaCountQuery.where(andPredicate);
		}
		
        int totalRecords =  entityManager.createQuery(criteriaCountQuery).getSingleResult().intValue();
        
        if(totalRecords == 0) {
            Map map = new HashMap();
            map.put(ApplicationConstant.ENTITY_LIST, Collections.EMPTY_LIST);
            map.put(ApplicationConstant.TOTAL_NO_RECORDS, 0);
            return map;
        }
        
        validateSortColumns(sortOrderList);
        
        CriteriaQuery<M>  criteriaQuery = criteriaBuilder.createQuery(type);
        Root<M> mainRoot = criteriaQuery.from(type);
        
        andCriteriaList = new ArrayList<Predicate>();
        appendCriteria(valueBean,criteriaBuilder, criteriaQuery, dataSetType,mainRoot,andCriteriaList);
        
        Map<String,Join<? extends Model,? extends Model>> joinMap = appendJoinCriteria(valueBean,criteriaBuilder, criteriaQuery, dataSetType,mainRoot,andCriteriaList);
        
        if(! sortOrderList.isEmpty()) {
        	prepareOrderList(mainRoot,joinMap,sortOrderList);
        	criteriaQuery.orderBy(sortOrderList.toArray(new Order[0]));
        }
        
        if(! andCriteriaList.isEmpty()) {
	        Predicate andPredicate = criteriaBuilder.and(andCriteriaList.toArray(new Predicate[0]));
	        criteriaQuery.where(andPredicate);
        }
        
        TypedQuery typedQuery = entityManager.createQuery(criteriaQuery);
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
        Map map = new HashMap();
        map.put(ApplicationConstant.ENTITY_LIST, entityList);
        map.put(ApplicationConstant.TOTAL_NO_RECORDS, totalRecords);
                
        return map;
    }

    protected void validateSortColumns(List<SortOrderValue> sortOrderList) {
		
	}

	private void prepareOrderList(Root<M> root, Map<String,Join<? extends Model,? extends Model>> map, List<SortOrderValue> sortOrderList) {
    	 
        for(SortOrderValue sortOrderValue: sortOrderList){
        	sortOrderValue.prepareExpression(root,map);
        }
        
    }

}
