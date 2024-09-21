package backend.academy.LocalizationTest.testModel;

import backend.academy.lozalization.Localize;

@Localize("localize.inner")
public class InnerClassToLocalize {

    @Localize
    public String camelCaseName = null;

    @Localize
    public String nonExistingName = null;

    @Localize("word.key")
    public String word = null;
}
