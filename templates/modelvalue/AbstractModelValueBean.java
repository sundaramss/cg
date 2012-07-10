package ${config.project.packageName}.model.value;

import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author ${config.project.author}
 */
public abstract class AbstractModelValueBean implements  ModelValueBean{
	
    private String skGuid;
    
    private Date createdDate;

    @XmlTransient
    private Date modifiedDate;
    
    private String createdBy;
    
    @XmlTransient
    private String modifiedBy;

    public String getSkGuid() {
        return skGuid;
    }

    public void setSkGuid(String skGuid) {
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

   
}
