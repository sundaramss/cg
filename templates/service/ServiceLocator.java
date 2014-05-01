package ${config.project.packageName}.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
<% config.project.subsystems.each { subsystem -> 
	def subsystemName = subsystem[0].toUpperCase() + subsystem[1..-1]%>
import ${config.project.packageName}.${subsystem.toLowerCase()}.service.${subsystemName}Service;<%}%>
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

<% config.project.subsystems.each { subsystem -> 
	def subsystemName = subsystem[0].toUpperCase() + subsystem[1..-1]
	def subsystemNameRef = modelHelper.getVariableRef([name:subsystem])%>
	private ${subsystemName}Service  ${subsystemNameRef}Service;<%}%>
<% config.project.subsystems.each { subsystem -> 
	def subsystemName = subsystem[0].toUpperCase() + subsystem[1..-1]
	def subsystemNameRef = modelHelper.getVariableRef([name:subsystem])%>
	@Autowired
	public void set${subsystemName}Service(${subsystemName}Service  ${subsystemNameRef}Service){
			this.${subsystemNameRef}Service = ${subsystemNameRef}Service;
	}
	<%}%>

	<% modelData.each { mData -> 
	def subsystemNameRef = modelHelper.getVariableRef([name:mData.subsystem])%>
	public ${mData.className}Service  get${mData.className}Service() { 
			return ${subsystemNameRef}Service;
	}<%}%>

}
