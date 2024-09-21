package backend.academy.LocalizationTest;

import backend.academy.LocalizationTest.testModel.EnumToLocalize;
import backend.academy.LocalizationTest.testModel.InnerClassToLocalize;
import backend.academy.LocalizationTest.testModel.OuterClassToLocalize;
import backend.academy.lozalization.Localizator;
import backend.academy.lozalization.Localize;
import java.util.Locale;
import java.util.Objects;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public class LocalizatorTest {

    /**
     * Тесты для класса в котором нет комплексных полей(любых полей кроме {@link String}),<br>
     * которые помечены {@link Localize}
     */
    @Nested
    public class InnerClassTest {
        @Test
        public void ruInnerClass_ShouldBeLocalized() {
            Localizator localizator = new Localizator(Locale.of("ru"));
            InnerClassToLocalize innerClassToLocalize = new InnerClassToLocalize();

            localizator.localizate(innerClassToLocalize);

            Assertions.assertEquals(innerClassToLocalize.camelCaseName, "ЛУЧШЕ");
            Assertions.assertEquals(innerClassToLocalize.word, "ХОРОШО");
        }

        @Test
        public void englishInnerClass_ShouldBeLocalized() {
            Localizator localizator = new Localizator(Locale.ENGLISH);
            InnerClassToLocalize innerClassToLocalize = new InnerClassToLocalize();

            localizator.localizate(innerClassToLocalize);

            Assertions.assertEquals(innerClassToLocalize.camelCaseName, "BETTER");
            Assertions.assertEquals(innerClassToLocalize.word, "GOOD");
        }

        @Test
        public void nonExistingValue_ShouldBeNull(){
            Localizator localizator = new Localizator(Locale.ENGLISH);
            InnerClassToLocalize innerClassToLocalize = new InnerClassToLocalize();

            localizator.localizate(innerClassToLocalize);

            Assertions.assertTrue(Objects.isNull(innerClassToLocalize.nonExistingName));
        }
    }

    /**
     * Класс, где есть поле не {@link String}, помеченное {@link Localize}
     */
    @Nested
    public class OuterClassTest {

        @Test
        public void ru_ShouldBeLocalized() {
            Localizator localizator = new Localizator(Locale.of("ru"));
            OuterClassToLocalize outerClassToLocalize = new OuterClassToLocalize();

            localizator.localizate(outerClassToLocalize);
            var innerClass = outerClassToLocalize.innerClassToLocalize;

            Assertions.assertEquals(innerClass.camelCaseName, "ЛУЧШЕ");
            Assertions.assertEquals(innerClass.word, "ХОРОШО");
        }

        @Test
        public void en_ShouldBeLocalized() {
            Localizator localizator = new Localizator(Locale.ENGLISH);
            OuterClassToLocalize outerClassToLocalize = new OuterClassToLocalize();

            localizator.localizate(outerClassToLocalize);
            var innerClass = outerClassToLocalize.innerClassToLocalize;

            Assertions.assertEquals(outerClassToLocalize.value, "VALUE");
            Assertions.assertEquals(innerClass.camelCaseName, "BETTER");
            Assertions.assertEquals(innerClass.word, "GOOD");
        }
    }


    @Nested
    public class EnumLocalizationTest {

        @Test
        public void ru_ShouldBeLocalized() {
            Localizator localizator = new Localizator(Locale.of("ru"));
            EnumToLocalize enumToLocalize = EnumToLocalize.ENUM1;

            localizator.localizate(enumToLocalize);

            Assertions.assertEquals(EnumToLocalize.ENUM1.value,"значение1");
            Assertions.assertEquals(EnumToLocalize.ENUM2.value,"значение2");
            Assertions.assertEquals(EnumToLocalize.ENUM3.value,"значение3");
        }

        @Test
        public void en_ShouldBeLocalized() {
            Localizator localizator = new Localizator(Locale.ENGLISH);
            EnumToLocalize enumToLocalize = EnumToLocalize.ENUM1;

            localizator.localizate(enumToLocalize);

            Assertions.assertEquals(EnumToLocalize.ENUM1.value,"enum1");
            Assertions.assertEquals(EnumToLocalize.ENUM2.value,"enum2");
            Assertions.assertEquals(EnumToLocalize.ENUM3.value,"enum3");
        }

    }


}
