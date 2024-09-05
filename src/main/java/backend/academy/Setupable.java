package backend.academy;

/**
 * Интерфейс для всех настраиваемых объектов
 */
public interface Setupable<T> {

    void setup(T t);
}
