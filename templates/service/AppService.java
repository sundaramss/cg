package ${config.project.packageName}.service;

import ${config.project.packageName}.model.value.DataSet;
import java.util.List;

public interface AppService<MB>{

	MB create(MB modelValueBean);

	MB update(MB modelValueBean);

	MB delete(MB modelValueBean);

	MB get(MB modelValueBean);
	
	List<MB> getList(DataSet dataSet);

}
