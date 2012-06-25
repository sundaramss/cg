package ${config.project.packageName}.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
<% config.project.subsystems.each { subsystem -> 
	def subsystemName = subsystem[0].toUpperCase() + subsystem[1..-1]%>
import ${config.project.packageName}.${subsystem}.${subsystemName}Service;<%}%>
<% modelData.each { mData -> %>
import ${config.project.packageName}.${mData.subsystem}.${mData.className}Service;<%}%>
/**
 * 
 * @author ${config.project.author}
 */
@Service
public class ServiceLocator {

<% config.project.subsystems.each { subsystem -> 
	def subsystemName = subsystem[0].toUpperCase() + subsystem[1..-1]%>
	private ${subsystemName}Service  ${subsystem}Service;<%}%>
<% config.project.subsystems.each { subsystem -> 
	def subsystemName = subsystem[0].toUpperCase() + subsystem[1..-1]%>
	@Autowired
	public void set${subsystemName}Service(${subsystemName}Service  ${subsystem}Service);<%}%>

	<% modelData.each { mData -> %>
	public ${mData.className}Service  get${mData.className}Service() { 
			return ${mData.subsystem}Service;
	}<%}%>

}
