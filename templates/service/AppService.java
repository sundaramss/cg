package ${config.project.packageName}.service;

import java.util.List;

public interface AppService<M,MB>{

	MB create(MB modelValueBean);

	MB update(MB modelValueBean);

	MB delete(MB modelValueBean);
	
	List<MB> getAll();

}
