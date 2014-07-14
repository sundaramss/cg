package ${config.project.packageName}.controller.helper;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.util.*;

/**
 * Created by ${config.project.author}.
 */
public class RequestParamParser {


    public List<RequestFilterValue> parseFilterBy(String fb){
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            JsonNode jsonRootNode = objectMapper.readTree(fb);
            return prepareJsonNodes(jsonRootNode);
        } catch (IOException e) {
            e.printStackTrace();
            //TODO put the error in logger
        }
        return Collections.emptyList();
    }



    private List<RequestFilterValue> prepareJsonNodes(JsonNode jsonNode){
        List<RequestFilterValue> requestFilterValues = new ArrayList<RequestFilterValue>();
        Iterator<JsonNode> iterator= jsonNode.getElements();
        while(iterator.hasNext()){
            JsonNode jsonNode1 = iterator.next();
            Iterator<Map.Entry<String,JsonNode>> fields =  jsonNode1.getFields();
            RequestFilterValue requestFilterValue = null;
            while(fields.hasNext()) {
                Map.Entry<String, JsonNode> entry = fields.next();
                if(StringUtils.equalsIgnoreCase(entry.getKey(),"opt") && requestFilterValue != null){
                    requestFilterValue.setOperator(entry.getValue().asText());
                }else{
                    requestFilterValue = new RequestFilterValue();
                    requestFilterValue.setField(entry.getKey());
                    requestFilterValue.setValue(entry.getValue());
                }
                requestFilterValues.add(requestFilterValue);
            }
        }
        return requestFilterValues;
    }

    public Map<String,Object> prepareSortByParamMap(String sortBy) {

        Map<String,Object> sortMap = Collections.emptyMap();

        if (StringUtils.isBlank(sortBy)) {
            return sortMap;
        }

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            sortMap = objectMapper.readValue(sortBy, LinkedHashMap.class);
        } catch (IOException e) {
            // e.printStackTrace();
        }
        return sortMap;

    }

    public static void main(String[] args) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        //JsonNode  jsonNode = objectMapper.readTree("{\"or\":[{\"role\":\"clerk\",\"oper\":\"eq\"}]}");
        JsonNode  jsonNode = objectMapper.readTree("[{\"role\":\"clerk\",\"oper\":\"eq\"}]");
        //System.out.println("Array:"+ jsonNode.findValue("or"));
        System.out.println("Array:"+ jsonNode.isArray());
        Iterator<JsonNode> iterator= jsonNode.getElements();
        while(iterator.hasNext()){
            JsonNode jsonNode1 = iterator.next();
            Iterator<Map.Entry<String,JsonNode>> fileds =  jsonNode1.getFields();
            while(fileds.hasNext()) {
                Map.Entry<String, JsonNode> entry = fileds.next();
                System.out.println(entry.getKey());
                System.out.println(entry.getValue());
            }
        }
    }
}
