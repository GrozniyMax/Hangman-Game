package backend.academy.LocalizationTest.testModel;

import backend.academy.lozalization.Localize;

@Localize("localize.enum")
public enum EnumToLocalize {

    ENUM1(null),
    ENUM2(null),
    ENUM3(null);

    @Localize
    public String value;

    EnumToLocalize(String value) {
        this.value = value;
    }


}
