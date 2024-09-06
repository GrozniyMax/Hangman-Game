package backend.academy.clearable;

import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.Arrays;

/**
 * Интерфейс для классов, где есть поля, модифицируемые в процессе игры.
 */
public interface AutoClearable  extends Clearable {

    static final Logger log = LogManager.getLogger();
    /**
     * Метод, который вызывает clear() во всех полях объекта, которые реализуют интерфейс Clearable
     */
    default void clearClearableFields(){
        Arrays.stream(this.getClass().getDeclaredFields())
            .forEach((field -> {
                try {
                    field.setAccessible(true);
                    Object fieldValue = field.get(this);
                    if (fieldValue instanceof AutoClearable) {
                        ((AutoClearable) fieldValue).fullClear();
                        log.info("Field {} was auto-cleared", field.getName());
                    } else if (fieldValue instanceof Clearable) {
                        log.info("Field {} was cleared", field.getName());
                        ((Clearable) fieldValue).clear();
                    }
                } catch (IllegalAccessException e) {
                    log.error("Error clearing field {}: {}", field.getName(), e);
                }
            }));
    }

    /**
     * Метод для полной очистки объекта
     */
    default void fullClear(){
        clearClearableFields();
        clear();
    }
}
