package backend.academy.clearableTest.testModel;

import backend.academy.clearable.Clearable;

public class ClerableClass implements Clearable {

    public String valueToClear = "VALUE";
    public String nonClerableValue="VALUE";

    @Override
    public void clear() {
        valueToClear = "";
    }
}
