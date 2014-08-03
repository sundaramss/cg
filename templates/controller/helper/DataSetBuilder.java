package ${config.project.packageName}.controller.helper;

import ${config.project.packageName}.model.value.*;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.JsonNode;
import org.springframework.beans.DirectFieldAccessor;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ${config.project.author}.
 */
public abstract class DataSetBuilder {

    private static String defaultOperator ="eq";
	private Enum<?> fieldSet;
    private GroupFilterValue rootGroupFilterValue = new GroupFilterValue();

    protected static Map<String,Filter> filterTypeMap = new LinkedHashMap<String, Filter>();
    protected static Map<Class<?>,Integer> variableType = new LinkedHashMap<Class<?>, Integer>();

    private FilterValue<?> filterValue;

    protected Serializable bean;

    private String field;
     static{
        filterTypeMap.put("eq", FilterType.EQUAL);
        filterTypeMap.put("el", FilterType.EQUAL_LIKE);
        filterTypeMap.put("lt", FilterType.LESS_THAN);
    }
    static {
        variableType.put(String.class,Integer.valueOf(1));
        variableType.put(Integer.class,Integer.valueOf(2));
        variableType.put(Long.class,Integer.valueOf(3));
        variableType.put(Double.class,Integer.valueOf(4));
    }

    private List<SortOrderValue> sortOrderValues=new ArrayList<SortOrderValue>();

    public DataSetBuilder(Enum<?> fieldSet,Serializable bean){
        this.bean = bean;
		this.fieldSet = fieldSet;
    }

    public DataSetBuilder addKey(String field){
        this.field = field;
        filterValue = getFilterValue(field);
        return this;
    }

    public DataSetBuilder next(){
        addFilterValue();
        this.filterValue=null;
        this.field=null;
        return this;
    }

    protected void addFilterValue(){
        if(this.filterValue != null) {
            rootGroupFilterValue.getFilterValues().add(this.filterValue);
        }
    }

    public DataSetBuilder addValue(JsonNode jsonNode){
        Object value = getValue(jsonNode, filterValue.getField().getJavaType());
        DirectFieldAccessor fieldAccessor = new DirectFieldAccessor(bean);
        fieldAccessor.setPropertyValue(field,value);
        return this;
    }

    public DataSetBuilder addOperator(String operator){
        Filter filter = filterTypeMap.get(operator);

        if(filter == null){
            filter = filterTypeMap.get(defaultOperator);
        }
        filterValue.setFilter(filter);
        return this;
    }

    protected Object getValue(JsonNode jsonNode,Class<?> javaType){
        Integer typeId = variableType.get(javaType);

        Object value;

        if(typeId == null){
            typeId = Integer.valueOf(1);
        }

        switch(typeId.intValue()){
            case 2:
                value = jsonNode.asInt(0);
                break;
            case 3:
                value = jsonNode.asLong(0);
                break;
            case 4:
                value = jsonNode.asDouble(0.0);
                break;
            default:
                value = jsonNode.asText();
                break;
        }
        return value;
    }

    public DataSetBuilder addSort(String columnName,String value){

        if (StringUtils.isBlank(columnName) || ! isColumnExist(columnName)) {
            return this;
        }

        SortOrderValue sortOrderValue = new SortOrderValue();
        boolean ascending = Boolean.valueOf(value);
        sortOrderValue.setAsending(ascending);
        sortOrderValue.setColumnName(columnName);
        this.sortOrderValues.add(sortOrderValue);

        return this;
    }


    public DataSet toDataSet(){

        DataSet dataSet = new DataSet();

        if(this.rootGroupFilterValue != null){
            dataSet.setGroupFilterValue(this.rootGroupFilterValue);
        }
        if(this.sortOrderValues != null){
            dataSet.setSortOrderValues(this.sortOrderValues);
        }
        if(this.bean != null){
            dataSet.setBean(this.bean);
        }
		dataSet.setFieldSet(fieldSet);
        
		return dataSet;
    }


    protected FilterValue<?> getFilterValue(String field) {
        Map<String,FilterValue<?>> filterValueMap = getFilterValueMap();
        FilterValue<?> filterValue = filterValueMap.get(field);
        if( filterValue == null) {
            return null;
        }
        try {
            return (FilterValue<?>) BeanUtils.cloneBean(filterValue);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }

    protected abstract Map<String,FilterValue<?>> getFilterValueMap();

    protected abstract boolean isColumnExist(String name);
}
