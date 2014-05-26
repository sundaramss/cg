package ${config.project.packageName}.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

import ${config.project.packageName}.model.value.ModelValueBean;
import ${config.project.packageName}.model.value.SortOrderValue;
import ${config.project.packageName}.service.AppService;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.TypeUtils;
import org.codehaus.jackson.map.ObjectMapper;
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
        List<SortOrderValue> sortOrderValues = prepareSortOrderList(sortBy);
        List<MB> modelValueList = appService.getAll(sortOrderValues);
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

    private List<SortOrderValue> prepareSortOrderList(String jsonString) {

        Map<String, Object> sortOrderMap = prepareMap(jsonString);
        List<SortOrderValue> sortOrderValues = new ArrayList<SortOrderValue>(sortOrderMap.size());

        for (Map.Entry<String, Object> entry : sortOrderMap.entrySet()) {
            SortOrderValue sortOrderValue = new SortOrderValue();
            String columnName = entry.getKey();
            if (StringUtils.isBlank(columnName)) {
                continue;
            }
            Object value = entry.getValue();
            boolean ascending = false;
            if (TypeUtils.isInstance(value, Boolean.TYPE)) {
                ascending = BooleanUtils.toBooleanDefaultIfNull((Boolean) value, false);
            }
            sortOrderValue.setAsending(ascending);
            sortOrderValue.setColumnName(columnName);
            sortOrderValues.add(sortOrderValue);
        }

        return sortOrderValues;
    }

    private Map<String, Object> prepareMap(String jsonString) {

        if (StringUtils.isBlank(jsonString)) {
            return Collections.emptyMap();
        }

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(jsonString, HashMap.class);
        } catch (IOException e) {
           // e.printStackTrace();
        }
        return Collections.emptyMap();

    }

    protected abstract AppService<MB> getAppService();

}
