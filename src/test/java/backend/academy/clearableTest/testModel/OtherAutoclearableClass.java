package backend.academy.clearableTest.testModel;

import backend.academy.clearable.AutoClearable;
import backend.academy.clearable.Clearable;

public class OtherAutoclearableClass implements AutoClearable {

    public ClerableClass clerableClass = new ClerableClass();

    @Override
    public void clear() {

    }
}
