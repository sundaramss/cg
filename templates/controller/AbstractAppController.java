package ${config.project.packageName}.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

import ${config.project.packageName}.controller.helper.DataSetBuilder;
import ${config.project.packageName}.controller.helper.RequestFilterValue;
import ${config.project.packageName}.controller.helper.RequestParamParser;
import ${config.project.packageName}.model.value.DataSet;
import ${config.project.packageName}.model.value.ModelValueBean;
import ${config.project.packageName}.service.AppService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import ${config.project.packageName}.service.ServiceLocator;

/**
 *
 * @author ${config.project.author}
 */
public abstract class AbstractAppController<MB extends ModelValueBean> implements AppController<MB>{
 
     protected RequestParamParser requestParamParser=new RequestParamParser();

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
        return new ResponseEntity<MB>(createValue, HttpStatus.OK);
    }

    @Override
    @RequestMapping(method = RequestMethod.PUT,value = "/{skGuid}")
    public ResponseEntity<MB> update(@RequestBody MB modelValue,@PathVariable String skGuid) {
        AppService<MB> appService = getAppService();
	modelValue.setSkGuid(skGuid);
        MB modifiedValue = appService.update(modelValue);
        return new ResponseEntity<MB>(modifiedValue, HttpStatus.OK);
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
    public ResponseEntity<List<MB>> getAll(@RequestParam(value = "sb",required = false) String sortBy) {
        AppService<MB> appService = getAppService();
        DataSet dataSet = prepareDataSet(null,sortBy);
        List<MB> modelValueList = appService.getList(dataSet);
        return new ResponseEntity<List<MB>>(modelValueList, HttpStatus.OK);
    }

    @Override
    @RequestMapping(method = RequestMethod.GET,value = "/{skGuid}")
    public ResponseEntity<MB> get(MB criteriaValue,@PathVariable String skGuid) {
        AppService<MB> appService = getAppService();
        criteriaValue.setSkGuid(skGuid);
        MB modelValue = appService.get(criteriaValue);
        return new ResponseEntity<MB>(modelValue, HttpStatus.OK);

    }
  

    private DataSet prepareDataSet(String filterByJson,String sortByJson){

        DataSetBuilder dataSetBuilder = getDataSetBuilder();

        if(StringUtils.isNotBlank(sortByJson)){
            Map<String, Object> sortOrderMap = requestParamParser.prepareSortByParamMap(sortByJson);
            for (Map.Entry<String, Object> entry : sortOrderMap.entrySet()) {
                dataSetBuilder.addSort(entry.getKey(),entry.getValue());
            }
        }

        if(StringUtils.isNotBlank(filterByJson)){
           List<RequestFilterValue> filterByList = requestParamParser.parseFilterBy(filterByJson);
           for(RequestFilterValue requestFilterValue:filterByList){
               dataSetBuilder.addKey(requestFilterValue.getField())
                       .addValue(requestFilterValue.getValue())
                       .addOperator(requestFilterValue.getOperator())
                       .next();
           }
        }
        return dataSetBuilder.toDataSet();
    }

    protected abstract AppService<MB> getAppService();

    protected abstract DataSetBuilder getDataSetBuilder();

}
