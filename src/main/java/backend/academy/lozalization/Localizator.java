package backend.academy.lozalization;

import backend.academy.clearable.Clearable;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.Set;

@Log4j2
@RequiredArgsConstructor
public class Localizator implements Clearable {

    private final Locale locale;

    private final Set<Object> visitedObjects = new HashSet<>();

    @SneakyThrows
    public void localizate(Object o) {
        log.info("Localizing object into {} locale", locale);

        if (visitedObjects.contains(o)) {
            return;
        } else {
            visitedObjects.add(o);
        }

        Class<?> clazz = o.getClass();
        log.info("Localizing class {}", clazz.getName());

        if (clazz.isEnum()) {
            localizeEnum(clazz);
            return;
        }

        if (!clazz.isAnnotationPresent(Localize.class)) {
            log.info("Class {} is not marked @Localize", clazz.getName());
            return;
        }
        var annotation = clazz.getAnnotation(Localize.class);

        var bundle = getBundle(annotation.value().isEmpty() ? clazz.getName() : annotation.value());

        if (Objects.isNull(bundle)) {
            log.warn("Unable to find resource bundle for class {}", clazz.getName());
            return;
        }

        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);

            Class<?> fieldType = field.getType();
            Object fieldValue = field.get(o);

            if (!field.isAnnotationPresent(Localize.class)) {
                continue;//Все поля не помеченные аннотацией пропускаем
            }

            if (fieldType.equals(String.class)) {
                log.info("Localizing String field {}", field.getName());
                String annotationValue = field.getAnnotation(Localize.class).value();

                String localizedValue = null;
                if (!annotationValue.isEmpty()) {
                    localizedValue = getLocalizedValue(bundle, annotationValue);
                } else {
                    localizedValue = getLocalizedValueFromCamelCase(bundle, field.getName());
                }

                if (Objects.isNull(localizedValue)) {
                    continue;// Не получилось найти значение для поля, значит оставим его таким, какое было по умолчанию
                }
                field.set(o, localizedValue);
                continue;
            } else if (fieldType.isEnum()) {
                localizeEnum(fieldType);
                continue;
            }


            localizate(fieldValue);
        }
    }

    private ResourceBundle getBundle(String path) {
        try {
            return ResourceBundle.getBundle(path, locale);
        } catch (MissingResourceException e) {
            log.info("Cannot find resource bundle for path {} and locale {}", path, locale);
            return null;
        }
    }

    private String getLocalizedValueFromCamelCase(ResourceBundle bundle, String camelCaseName) {
        return getLocalizedValue(bundle, processCamelCase(camelCaseName));
    }

    private String getLocalizedValue(ResourceBundle bundle, String dotSeparatedKey) {
        try {
            return bundle.getString(dotSeparatedKey);
        } catch (MissingResourceException|NullPointerException e) {
            log.warn("Cannot find resource bundle for key {} and locale {}", dotSeparatedKey, locale);
            return null;
        }
    }

    private String processCamelCase(String camelCaseKey) {
        StringBuilder builder = new StringBuilder();
        for (Character c : camelCaseKey.toCharArray()) {
            if (Character.isUpperCase(c)) {
                builder.append(".");
                c = Character.toLowerCase(c);
            }
            builder.append(c);
        }
        return builder.toString();
    }

    @SneakyThrows private void localizeEnum(Class<?> enumClass) {
        log.info("Localizing enum {}", enumClass.getName());
        Method values = enumClass.getDeclaredMethod("values", null);

        Method nameMethod = enumClass.getMethod("name");
        nameMethod.setAccessible(true);

        //Проверки на необходимость локализации этого enum а
        if (!enumClass.isAnnotationPresent(Localize.class)) {
            log.info("Class {} is not marked @Localize", enumClass.getName());
            return;
        }
        var annotation = enumClass.getAnnotation(Localize.class);
        var bundle = getBundle(annotation.value().isEmpty() ? enumClass.getName() : annotation.value());


        List<Field> fieldsToLocalize =
            Arrays.stream(enumClass.getDeclaredFields()).filter(field -> field.isAnnotationPresent(Localize.class))
                .toList();

        for (Object enumInstance : (Object[]) values.invoke(null, null)) {
            for (Field field : fieldsToLocalize) {
                field.setAccessible(true);
                if (!field.getType().equals(String.class)) {
                    localizate(field.get(enumInstance));
                } else {
                    String annotationValue = field.getAnnotation(Localize.class).value();

                    //Получаем название объекта enum и приводим его к нижнему регистру
                    String name = (String) nameMethod.invoke(enumInstance);
                    log.info("Localizing String field {} of enum instance {}", field.getName(), name);
                    name = name.toLowerCase();

                    String localizedValue;
                    if (!annotationValue.isEmpty()) {
                        localizedValue = getLocalizedValue(bundle, name + "." + annotationValue);
                    } else {
                        localizedValue = getLocalizedValue(bundle, name + "." +
                            processCamelCase(field.getName()));
                    }
                    if (Objects.isNull(localizedValue)) {
                        continue;// Не получилось найти значение для поля, значит оставим его таким, какое было по умолчанию
                    }
                    field.set(enumInstance, localizedValue);
                }

            }
        }

    }

    @Override public void clear() {
        visitedObjects.clear();
    }
}
