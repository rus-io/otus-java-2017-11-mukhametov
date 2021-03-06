package handler.nodes;

import java.util.*;
import handler.JsonHandler;
import handler.recorder.RecordBuilder;

class SetNode extends AbstractNode {

    SetNode(Object object) {
        super(object);
    }

    @Override
    public String write() {
        RecordBuilder record = new RecordBuilder();
        record.createArray();
        for (Object o : (Set) object) {
            record.addElement(JsonHandler.write(o));
        }
        return record.build();
    }

    static boolean isSupported(Object object) {
        return getTypes().contains(object.getClass());
    }

    protected static Set<Class> getTypes() {
        Set<Class> cls = new HashSet<>();
        cls.add(HashSet.class);
        cls.add(SortedSet.class);
        cls.add(TreeSet.class);
        return cls;
    }
}
