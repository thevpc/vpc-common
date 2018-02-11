package net.vpc.common.io;

import java.util.*;

public class NoDuplicatesExceptionTextListener implements ExceptionTextListener {

    private static ExceptionTextEqObj EE = new ExceptionTextEqObj(1);
    private Map<ComprableObj<ExceptionText>, Long> exceptions = new HashMap<>();

    @Override
    public final void onExceptionText(ExceptionTextEvent ex) {
        ComprableObj<ExceptionText> value = new ComprableObj<>(ex.getExceptionText(), EE);
        if (!exceptions.containsKey(value)) {
            exceptions.put(value, 1L);
            onNonDuplicatedException(ex.getExceptionText());
        } else {
            exceptions.put(value, exceptions.get(value) + 1L);
//            System.out.println("Duplicate : "+exceptions.get(ex.getExceptionText()));
        }
    }

    public List<ExceptionTextExt> getExceptions() {
        List<ExceptionTextExt> all=new ArrayList<>();
        for (Map.Entry<ComprableObj<ExceptionText>, Long> e : exceptions.entrySet()) {
            all.add(new ExceptionTextExt(e.getKey().obj,e.getValue()));
        }
        Collections.sort(all, new Comparator<ExceptionTextExt>() {
            @Override
            public int compare(ExceptionTextExt o1, ExceptionTextExt o2) {
                return Long.compare(o1.text.getLineNumber(),o2.text.getLineNumber());
            }
        });
        return all;
    }

    public List<ExceptionTextExt> getExceptionsOrderByCountDesc() {
        List<ExceptionTextExt> all=new ArrayList<>();
        for (Map.Entry<ComprableObj<ExceptionText>, Long> e : exceptions.entrySet()) {
            all.add(new ExceptionTextExt(e.getKey().obj,e.getValue()));
        }
        Collections.sort(all, new Comparator<ExceptionTextExt>() {
            @Override
            public int compare(ExceptionTextExt o1, ExceptionTextExt o2) {
                return Long.compare(o2.getCount(),o1.getCount());
            }
        });
        return all;
    }

    public void onNonDuplicatedException(ExceptionText ex){}

    private static class ExceptionTextEqObj implements EqObj {
        private int lines;

        public ExceptionTextEqObj(int lines) {
            this.lines = lines;
        }

        private String conv(ExceptionText o1) {

            StringBuilder sb = new StringBuilder();
            int i = 0;
            for (String line : o1.getLines()) {
                if (i == 0) {
                    sb.append(line);
                } else if (i < lines) {
                    sb.append("\n");
                    sb.append(line);
                } else {
                    break;
                }
                i++;
            }
            return sb.toString();
        }

        @Override
        public boolean equals(Object o1, Object o2) {
            if (o2 == null || !(o2 instanceof ExceptionText)) {
                return false;
            }
            String a = conv((ExceptionText) o1);
            String b = conv((ExceptionText) o2);
            return a.equals(b);
        }

        @Override
        public int hashCode(Object o) {
            return conv((ExceptionText) o).hashCode();
        }

    }

    private interface EqObj {

        boolean equals(Object o1, Object o2);

        int hashCode(Object o);
    }

    public static class ComprableObj<T> {

        EqObj comparator;
        T obj;

        public ComprableObj(T obj, EqObj comparator) {
            this.comparator = comparator;
            this.obj = obj;
        }

        public T getObj() {
            return obj;
        }

        @Override
        public boolean equals(Object obj) {
            return comparator.equals(this.obj, ((ComprableObj) obj).obj);
        }

        @Override
        public int hashCode() {
            return comparator.hashCode(this.obj);
        }

    }
    public class ExceptionTextExt {
        private ExceptionText text;
        private long count;

        public ExceptionTextExt(ExceptionText text, long count) {
            this.text = text;
            this.count = count;
        }

        public long getCount() {
            return count;
        }

        public int getLineNumber() {
            return text.getLineNumber();
        }

        public List<String> getLines() {
            return text.getLines();
        }

        public String getMessage() {
            return text.getMessage();
        }
    }
}
