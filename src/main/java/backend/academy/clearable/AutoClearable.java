package backend.academy.clearable;

import java.util.Arrays;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Интерфейс для классов, где есть поля, модифицируемые в процессе игры.
 */
public interface AutoClearable extends Clearable {

    Logger LOGGER = LogManager.getLogger();

    /**
     * Метод, который вызывает clear() во всех полях объекта, которые реализуют интерфейс Clearable
     */

    default void clearClearableFields() {
        Arrays.stream(this.getClass().getDeclaredFields())
            .forEach((field -> {
                try {
                    field.setAccessible(true);
                    Object fieldValue = field.get(this);
                    if (fieldValue instanceof AutoClearable) {
                        ((AutoClearable) fieldValue).fullClear();
                        LOGGER.info("Field {} was auto-cleared", field.getName());
                    } else if (fieldValue instanceof Clearable) {
                        LOGGER.info("Field {} was cleared", field.getName());
                        ((Clearable) fieldValue).clear();
                    }
                } catch (IllegalAccessException e) {
                    LOGGER.error("Error clearing field {}: {}", field.getName(), e);
                }
            }));
    }

    /**
     * Метод для полной очистки объекта
     */
    default void fullClear() {
        clearClearableFields();
        clear();
    }
}
