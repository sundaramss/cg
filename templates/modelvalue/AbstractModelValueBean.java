package ${config.project.packageName}.model.value;

import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author ${config.project.author}
 */
public abstract class AbstractModelValueBean<T extends Enum> implements  ModelValueBean<T>{
	
    private Long skGuid;
    
    private Date createdDate;
    @XmlTransient
    private Date modifiedDate;
    
    private String createdBy;
    
    @XmlTransient
    private String modifiedBy;

    private List<String> filtersBy;
    
    public Long getSkGuid() {
        return skGuid;
    }

    public void setSkGuid(Long skGuid) {
        this.skGuid = skGuid;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public Date getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

	public List<String> getFiltersBy() {
		return filtersBy;
	}

	public void setFiltersBy(List<String> filtersBy) {
		this.filtersBy = filtersBy;
	}
   
}
