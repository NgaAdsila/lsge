package my.lsge.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.io.IOException;
import java.util.Map;

@Slf4j
public final class JsonUtils {
    private static final ObjectMapper mapper = new ObjectMapper() //
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false) //
            .configure(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL, true);

    private JsonUtils() {
    }

    public static String toJson(Object o) {
        try {
            return mapper.writeValueAsString(o);
        } catch (JsonProcessingException e) {
            log.info(ExceptionUtils.getStackTrace(e));
            return null;
        }
    }

    public static <T> T toPojo(String json, Class<T> cls) {
        try {
            return mapper.readValue(json, cls);
        } catch (IOException e) {
            log.info(ExceptionUtils.getStackTrace(e));
            return null;
        }
    }

    public static <T> T toPojo(String json, TypeReference<T> t) {
        try {
            return mapper.readValue(json, t);
        } catch (IOException e) {
            log.info(ExceptionUtils.getStackTrace(e));
            return null;
        }
    }

    public static <T> T toPojo(Map<String, Object> map, Class<T> t) {
        return mapper.convertValue(map, t);
    }
}
