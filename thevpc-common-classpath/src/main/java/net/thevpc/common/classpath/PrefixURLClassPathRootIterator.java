package net.thevpc.common.classpath;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

class PrefixURLClassPathRootIterator extends URLClassPathRootIterator {
    private final String prefix;
    ClassPathResource old;

    public PrefixURLClassPathRootIterator(URL url, String prefix) throws IOException {
        super(url);
        if (prefix.startsWith("/")) {
            prefix = prefix.substring(1);
        }
        this.prefix = prefix;
        old = null;
    }

    @Override
    public boolean hasNext() {
        while (true) {
            boolean z = super.hasNext();
            if (z) {
                ClassPathResource old2 = super.next();
                String path = old2.getPath();
                if (path.startsWith(prefix)) {
                    String pp = path.substring(prefix.length() + 1);
                    if (pp.isEmpty()) {
                        pp = "/";
                    }
                    String finalPp = pp;
                    this.old = new ClassPathResource() {
                        @Override
                        public String getPath() {
                            return finalPp;
                        }

                        @Override
                        public InputStream open() throws IOException {
                            return old2.open();
                        }
                    };
                    return true;
                }
            } else {
                return false;
            }
        }
    }

    @Override
    public ClassPathResource next() {
        return old;
    }
}
