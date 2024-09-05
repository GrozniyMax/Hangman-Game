package backend.academy.lozalization;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.logging.log4j.core.pattern.RegexReplacement;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Locale;
import java.util.ResourceBundle;

@RequiredArgsConstructor
public class Localizator {

    private final Locale locale;
    private ResourceBundle resourceBundle;

    @SneakyThrows
    public void localizate(Object o, Locale locale){
        var loader = this.getClass().getClassLoader();
        Class<?> clazz = o.getClass();
        if (!clazz.isAnnotationPresent(Localize.class)) return;
        var annotation = clazz.getAnnotation(Localize.class);

        this.resourceBundle = getBundle(
            annotation.value().isEmpty()?clazz.getName():annotation.value());

        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            if (!field.isAnnotationPresent(Localize.class)) continue;
            String value = field.getAnnotation(Localize.class).value();
            String fieldValue = null;
            if (!value.isEmpty()) fieldValue = getLocalizedValue(value);
            else fieldValue=getLocalizedValueFromCamelCase(field.getName());
            field.set(this, fieldValue);
        }

    }

    private ResourceBundle getBundle(String path){
        return ResourceBundle.getBundle(path, locale);
    }

    private String getLocalizedValueFromCamelCase(String camelCaseName){
        return getLocalizedValue(processCamelCase(camelCaseName));
    }

    private String getLocalizedValue(String dotSeparatedKey){
        return resourceBundle.getString(dotSeparatedKey);
    }

    private String processCamelCase(String camelCaseKey){
        StringBuilder builder = new StringBuilder();
        for(Character c : camelCaseKey.toCharArray()){
            if(Character.isUpperCase(c)){
                builder.append(".");
            }
            builder.append(c);
        }
        return builder.toString();

    }

}
