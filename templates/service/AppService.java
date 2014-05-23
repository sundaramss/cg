package ${config.project.packageName}.service;

import java.util.List;

public interface AppService<MB>{

	MB create(MB modelValueBean);

	MB update(MB modelValueBean);

	MB delete(MB modelValueBean);

	MB get(MB modelValueBean);
	
	List<MB> getAll();

}
