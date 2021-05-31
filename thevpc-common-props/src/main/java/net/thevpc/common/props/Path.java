package net.thevpc.common.props;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

public class Path {

    private static Pattern S = Pattern.compile("[/\\\\]+");

    private String[] items;

    private Path(String... items) {
        List<String> a = new ArrayList<>();
        if (items != null) {
            for (String item : items) {
                if (item != null) {
                    for (String s : S.split(item)) {
                        if (!s.isEmpty()) {
                            a.add(s);
                        }
                    }
                }
            }
        }
        this.items = a.toArray(new String[0]);
    }

    private Path(List<String> items) {
//        List<String> a = new ArrayList<>();
//        if (items != null) {
//            for (String item : items) {
//                if (item != null) {
//                    for (String s : S.split(item)) {
//                        if (!s.isEmpty()) {
//                            a.add(item);
//                        }
//                    }
//                }
//            }
//        }
        this.items = items.toArray(new String[0]);
    }

    public static Path root() {
        return of();
    }

    public static Path of(String... items) {
        return new Path(items);
    }

    public String get(int index) {
        return items[index];
    }

    public Path child(Path path) {
        return path == null ? this : append(path.items);
    }

    public boolean startsWith(Path child) {
        if (size() >= child.size()) {
            for (int i = 0; i < child.size(); i++) {
                String s = this.get(i);
                String o = child.get(i);
                if (!Objects.equals(s, o)) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    public Path append(String... items) {
        if (items == null || items.length == 0) {
            return this;
        }
        return append(new Path(items));
    }

    public Path append(Path items) {
        if (items == null || items.size() == 0) {
            return this;
        }
        List<String> a = new ArrayList<>(Arrays.asList(this.items));
        a.addAll(Arrays.asList(items.items));
        return new Path(a);
    }

    public Path parent() {
        if (items.length == 0) {
            return this;
        }
        return new Path(Arrays.copyOf(items, items.length - 1));
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(items);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Path path = (Path) o;
        return Arrays.equals(items, path.items);
    }

    public String toString() {
        if (items.length == 0) {
            return "/";
        }
        StringBuilder sb = new StringBuilder();
        for (String item : items) {
            sb.append("/").append(item);
        }
        return sb.toString();
    }

    public boolean isEmpty() {
        return items.length == 0;
    }

    public int size() {
        return items.length;
    }

    public String name() {
        return items.length == 0 ? null : items[items.length - 1];
    }

    public String last() {
        return name();
    }

    public String first() {
        return items.length == 0 ? null : items[0];
    }

    public Path skipLast() {
        return items.length == 0 ? this : of(Arrays.copyOfRange(items, 0, items.length - 1));
    }

    public Path skipFirst() {
        return items.length == 0 ? this : of(Arrays.copyOfRange(items, 1, items.length));
    }

    public Path subPath(int from) {
        return Path.of(
                Arrays.copyOfRange(items, from, items.length)
        );
    }

    public Path subPath(int from, int to) {
        return Path.of(
                Arrays.copyOfRange(items, from, to)
        );
    }

    public Path tail(int size) {
        return Path.of(
                Arrays.copyOfRange(items, items.length - size, items.length)
        );
    }

    public Path head(int size) {
        return Path.of(
                Arrays.copyOfRange(items, 0, size)
        );
    }
}
