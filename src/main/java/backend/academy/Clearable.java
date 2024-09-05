package backend.academy;

import java.util.Arrays;

/**
 * Интерфейс для классов, где есть поля, модифицируемые в процессе игры.
 */
public interface Clearable {

    /**
     * Очищает все модифицируемые поля, чтобы сделать перезапуск игры
     */
    void clear();

    /**
     * Метод, который вызывает clear() во всех полях объекта, который наследует интерфейс Clearable
     */
    default void clearClearableFields(){
        Arrays.stream(this.getClass().getDeclaredFields())
            .forEach((field -> {
                try {
                    field.setAccessible(true);
                    Object fieldValue = field.get(this);
                    if (fieldValue instanceof Clearable) {
                        ((Clearable) fieldValue).clear();
                    }
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }));
    }
}
