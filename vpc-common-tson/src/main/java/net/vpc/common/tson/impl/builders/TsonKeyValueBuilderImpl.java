package net.vpc.common.tson.impl.builders;

import net.vpc.common.tson.*;
import net.vpc.common.tson.impl.elements.TsonNullImpl;
import net.vpc.common.tson.impl.elements.TsonPairImpl;
import net.vpc.common.tson.impl.util.TsonUtils;

public class TsonKeyValueBuilderImpl extends AbstractTsonElementBuilder<TsonKeyValueBuilder> implements TsonKeyValueBuilder {
    private TsonElement key;
    private TsonElement value;

    @Override
    public TsonElementType getType() {
        return TsonElementType.PAIR;
    }

    @Override
    public TsonKeyValueBuilder merge(TsonPair other) {
        key = other.getKey();
        value = other.getValue();
        return this;
    }

    @Override
    public TsonKeyValueBuilder reset() {
        key = null;
        value = null;
        return this;
    }

    @Override
    public TsonElement getKey() {
        return key;
    }

    @Override
    public TsonKeyValueBuilder setKey(TsonElementBase key) {
        this.key = Tson.elem(key);
        return this;
    }


    @Override
    public TsonElement getValue() {
        return value;
    }

    @Override
    public TsonKeyValueBuilder setValue(TsonElementBase value) {
        this.value = Tson.elem(value);
        return this;
    }


    @Override
    public TsonElement build() {
        return TsonUtils.decorate(
                new TsonPairImpl(
                        key == null ? TsonNullImpl.INSTANCE : key,
                        value == null ? TsonNullImpl.INSTANCE : value
                ), getComments(), getAnnotations());
    }


}
