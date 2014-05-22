package ${config.project.packageName}.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import ${config.project.packageName}.model.value.ModelValueBean;
import ${config.project.packageName}.service.AppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import ${config.project.packageName}.service.ServiceLocator;

/**
 *
 * @author sundaramss
 */
public abstract class AbstractAppController<MB extends ModelValueBean> implements AppController<MB>{
 

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, false));
    }


    @Override
    @RequestMapping(method = RequestMethod.POST,value = "/")
    public ResponseEntity<MB> create(@RequestBody MB modelValue) {
        AppService<MB> appService = getAppService();
        MB createValue = appService.create(modelValue);
        return new ResponseEntity<>(createValue, HttpStatus.OK);
    }

    @Override
    @RequestMapping(method = RequestMethod.PUT,value = "/{skGuid}")
    public ResponseEntity<MB> update(@RequestBody MB modelValue,@PathVariable String skGuid) {
        AppService<MB> appService = getAppService();
	modelValue.setSkGuid(skGuid);
        MB modifiedValue = appService.update(modelValue);
        return new ResponseEntity<>(modifiedValue, HttpStatus.OK);
    }

    @Override
    @RequestMapping(method = RequestMethod.DELETE,value = "/{skGuid}")
    public ResponseEntity<MB> delete(MB modelValue,@PathVariable String skGuid) {
        AppService<MB> appService = getAppService();
	modelValue.setSkGuid(skGuid);
        MB deleteValue = appService.delete(modelValue);
        return new ResponseEntity<MB>(deleteValue, HttpStatus.OK);
    }

    @Override
    @RequestMapping(method = RequestMethod.GET,value = "/")
    public ResponseEntity<List<MB>> getAll() {
        AppService<MB> appService = getAppService();
        List<MB> modelValueList = appService.getAll();
        return new ResponseEntity<List<MB>>(modelValueList, HttpStatus.OK);
    }

    protected abstract AppService<MB> getAppService();

}
