package ${config.project.packageName}.model.value;

import java.io.Serializable;
import java.util.List;

/**
 * Created by ${config.project.author}.
 */
public class DataSet  {

    private GroupFilterValue groupFilterValue;
    private List<SortOrderValue> sortOrderValues;
	private Serializable bean;
	private Enum<?> fieldSet;
	
    public GroupFilterValue getGroupFilterValue() {
        return groupFilterValue;
    }

    public void setGroupFilterValue(GroupFilterValue groupFilterValue) {
        this.groupFilterValue = groupFilterValue;
    }

    public List<SortOrderValue> getSortOrderValues() {
        return sortOrderValues;
    }

    public void setSortOrderValues(List<SortOrderValue> sortOrderValues) {
        this.sortOrderValues = sortOrderValues;
    }    public Serializable getBean() {
        return bean;
    }

    public void setBean(Serializable bean) {
        this.bean = bean;
    }
	
    public Enum<?> getFieldSet() {
        return fieldSet;
    }

    public void setFieldSet(Enum<?> fieldSet) {
        this.fieldSet = fieldSet;
    }

}
