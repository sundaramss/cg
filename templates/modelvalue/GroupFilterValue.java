package ${config.project.packageName}.model.value;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ${config.project.author}.
 */
public class GroupFilterValue implements GroupFilter {

    private GroupType groupType=GroupType.AND;
    private GroupType filterListGroupType=GroupType.AND;

    private List<FilterValue<?>> filterValues=new ArrayList<FilterValue<?>>();

    private GroupFilterValue groupFilterValue;

    public List<FilterValue<?>> getFilterValues() {
        return filterValues;
    }

    public GroupType getGroupType() {
        return groupType;
    }

    public void setGroupType(GroupType groupType) {
        this.groupType = groupType;
    }

    public GroupType getFilterListGroupType() {
        return filterListGroupType;
    }

    public void setFilterListGroupType(GroupType filterListGroupType) {
        this.filterListGroupType = filterListGroupType;
    }

    public void setFilterValues(List<FilterValue<?>> filterValues) {
        this.filterValues = filterValues;
    }

    public GroupFilterValue getGroupFilterValue() {
        return groupFilterValue;
    }

    public void setGroupFilterValue(GroupFilterValue groupFilterValue) {
        this.groupFilterValue = groupFilterValue;
    }
}
