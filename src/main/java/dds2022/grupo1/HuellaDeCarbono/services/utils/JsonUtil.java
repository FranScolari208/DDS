package dds2022.grupo1.HuellaDeCarbono.services.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import spark.ResponseTransformer;

public class JsonUtil {
    public static String toJson(Object object) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        return mapper.writeValueAsString(object);
        // return new Gson().toJson(object);
    }

    public static String toJsonCustom(Object obj){
        return obj.toString();
    }



    public static ResponseTransformer json() {
        return JsonUtil::toJsonCustom;
    }
}
