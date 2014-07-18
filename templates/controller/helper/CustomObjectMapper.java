package ${config.project.packageName}.controller.helper;

import org.codehaus.jackson.*;
import org.codehaus.jackson.map.*;
import org.codehaus.jackson.map.deser.std.StdDeserializer;
import org.codehaus.jackson.map.module.SimpleModule;
import org.codehaus.jackson.map.ser.CustomSerializerFactory;
import org.codehaus.jackson.map.util.StdDateFormat;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by ${config.project.author}.
 */

public class CustomObjectMapper extends ObjectMapper {

    public CustomObjectMapper() {
            SimpleModule module = new SimpleModule("JSONModule", new Version(2, 0, 0, null));
            module.addSerializer(Date.class, new DateSerializer());
            module.addDeserializer(Date.class, new DateDeserializer());
            registerModule(module);
    }

}

class DateSerializer extends JsonSerializer<Date> {

    public DateSerializer() {
    }

    @Override
    public void serialize(Date date, JsonGenerator json,
                          SerializerProvider provider) throws IOException,
            JsonGenerationException {
        // The client side will handle presentation, we just want it accurate
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        String out = df.format(date);
        json.writeString(out);
    }

}

class DateDeserializer extends StdDeserializer<Date> {

    public DateDeserializer() {
        super(Date.class);
    }

    @Override
    public Date deserialize(JsonParser json, DeserializationContext context)
            throws IOException, JsonProcessingException {
        try {
            DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
            return df.parse(json.getText());
        } catch (ParseException e) {
            return null;
        }
    }

}
