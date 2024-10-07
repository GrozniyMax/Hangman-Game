package backend.academy.clearableTest;

import backend.academy.clearable.AutoClearable;
import backend.academy.clearable.Clearable;
import backend.academy.clearableTest.testModel.AutoClearableClass;
import backend.academy.clearableTest.testModel.ClerableClass;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public class ClearableInterfacesTest {

    @Nested
    public class ClearableTest {
        @Test
        public void clearableField_ShouldBeCleared() {
            ClerableClass object = new ClerableClass();

            object.clear();

            Assertions.assertThat(object.valueToClear).isEmpty();
        }

        @Test
        public void nonClearableField_ShouldNotBeCleared() {
            ClerableClass object = new ClerableClass();

            object.clear();

            Assertions.assertThat(object.nonClerableValue).isEqualTo("VALUE");
        }
    }

    @Nested
    public class AutoClerableTest {

        /**
         * Проверяет вызывается ли метод {@link Clearable#clear()} внутри {@link AutoClearable#fullClear()}
         */
        @Test
        public void clearableValue_ShouldBeCleared() {
            AutoClearableClass object = new AutoClearableClass();

            object.fullClear();

            Assertions.assertThat(object.clerableValue).isEmpty();
        }

        /**
         * Проверяет вызывается ли метод {@link Clearable#clear()} на полях внутри класса, реализующий {@code Clearable}
         */
        @Test
        public void clearableField_ShouldBeCleared() {
            AutoClearableClass object = new AutoClearableClass();

            object.fullClear();

            Assertions.assertThat(object.clearable.nonClerableValue).isEqualTo("VALUE");
            Assertions.assertThat(object.clearable.valueToClear).isEmpty();
        }

        /**
         * Проверяет вызывается ли метод {@link AutoClearable#fullClear()} на полях,
         * реализующих интерфейс {@code AutoClearable}
         */
        @Test
        public void autoClerableFields_ShouldBeCleared() {
            AutoClearableClass autoClearableClass = new AutoClearableClass();

            autoClearableClass.fullClear();
            var innerAutoClearable = autoClearableClass.autoClearable;

            Assertions.assertThat(innerAutoClearable.clerableClass.nonClerableValue).isEqualTo("VALUE");
            Assertions.assertThat(innerAutoClearable.clerableClass.valueToClear).isEmpty();
        }
    }

}
