package net.vpc.common.tson.impl.elements;

import net.vpc.common.tson.TsonElement;
import net.vpc.common.tson.TsonDocumentHeader;
import net.vpc.common.tson.TsonDocumentHeaderBuilder;
import net.vpc.common.tson.impl.builders.TsonDocumentHeaderBuilderImpl;
import net.vpc.common.tson.impl.util.TsonUtils;
import net.vpc.common.tson.impl.util.UnmodifiableArrayList;

import java.util.List;

public class TsonDocumentHeaderImpl implements TsonDocumentHeader {
    private String version = null;
    private String encoding = null;
    private UnmodifiableArrayList<TsonElement> params;

    public TsonDocumentHeaderImpl() {
        params = UnmodifiableArrayList.empty();
    }

    public TsonDocumentHeaderImpl(String version, String encoding, TsonElement[] params) {
        this.version = version;
        this.encoding = encoding;
        this.params = UnmodifiableArrayList.ofCopy(params);
    }

    @Override
    public String getVersion() {
        return version;
    }

    @Override
    public String getEncoding() {
        return encoding;
    }

    @Override
    public List<TsonElement> getAll() {
        return params;
    }

    @Override
    public int size() {
        return params.size();
    }

    @Override
    public List<TsonElement> all() {
        return getAll();
    }

    @Override
    public TsonDocumentHeaderBuilder builder() {
        return new TsonDocumentHeaderBuilderImpl().setVersion(version).setEncoding(encoding).addParams(params);
    }

    @Override
    public int compareTo(TsonDocumentHeader o) {
        int i = TsonUtils.compare(version, o.getVersion());
        if (i != 0) {
            return i;
        }
        i = TsonUtils.compare(encoding, o.getEncoding());
        if (i != 0) {
            return i;
        }
        return TsonUtils.compareElementsArray(params, o.getAll());
    }
}
