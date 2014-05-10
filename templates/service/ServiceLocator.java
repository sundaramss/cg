package ${config.project.packageName}.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
<% modelData.each { mData -> %>
import ${config.project.packageName}.${mData.subsystem.toLowerCase()}.service.${mData.className}Service;<%}%>
/**
 * 
 * @author ${config.project.author}
 */
public class ServiceLocator {

	private static ServiceLocator instance;
	
	private ServiceLocator(){
	}
	
	public static ServiceLocator getInstance(){
		if(instance == null){
			instance = new ServiceLocator();
		}
		return instance;
	}


	<% modelData.each { mData -> 
	def modelNameRef = modelHelper.getVariableRef([name:mData.className])%>

	private ${mData.className}Service  ${modelNameRef}Service;

	public ${mData.className}Service  get${mData.className}Service() { 
			return ${modelNameRef}Service;
	}

	@Autowired
	public void set${mData.className}Service(${mData.className}Service ${modelNameRef}Service) { 
			this.${modelNameRef}Service=${modelNameRef}Service;
	}
	<%}%>


}
