package backend.academy.clearableTest.testModel;

import backend.academy.clearable.AutoClearable;
import backend.academy.clearable.Clearable;

public class AutoClearableClass implements AutoClearable {

    public ClerableClass clearable = new ClerableClass();
    public OtherAutoclearableClass autoClearable = new OtherAutoclearableClass();

    public String clerableValue="VALUE";

    @Override
    public void clear() {
        clerableValue="";
    }
}
