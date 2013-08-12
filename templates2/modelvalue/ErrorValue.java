package ${config.project.packageName}.model.value;

import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author ${config.project.author}
 */
@XmlRootElement(name="errorValue")
public class ErrorValue {
	
	private String message;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
}
