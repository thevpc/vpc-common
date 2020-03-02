package net.vpc.common.tson;

import net.vpc.common.tson.Tson;
import net.vpc.common.tson.TsonElement;
import net.vpc.common.tson.TsonObjectContext;

public interface TsonSerializable {
    default TsonElement toTsonElement() {
        return toTsonElement(Tson.serializer().context());
    }

    default TsonElement toTsonElement(TsonObjectContext context) {
        throw new UnsupportedOperationException("Unsupported toNode(TsonSerializer serializer) in " + getClass().getName());
    }
}
