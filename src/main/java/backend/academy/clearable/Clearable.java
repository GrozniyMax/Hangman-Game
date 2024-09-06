package backend.academy.clearable;

public interface Clearable {

    /**
     * Очищает все модифицируемые поля, не реализующие интерфейс Clearable
     */
    void clear();
}
