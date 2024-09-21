package backend.academy.LocalizationTest.testModel;

import backend.academy.lozalization.Localize;

@Localize("localize.outer")
public class OuterClassToLocalize {

    @Localize
    public String value = null;

    @Localize
    public InnerClassToLocalize innerClassToLocalize = new InnerClassToLocalize();
}
