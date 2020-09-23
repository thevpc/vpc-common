/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.commons.ljson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.hjson.JsonObject;
import org.hjson.JsonValue;

public class LJSON {

    public static final LJSON NULL = new LJSON(null);

    private JsonValue value;

    public static LJSON of(String json) {
        try {
            if (json == null) {
                return NULL;
            }
            if (json.trim().isEmpty()) {
                return NULL;
            }
            JsonValue u = JsonValue.readHjson(json);
            return new LJSON(u);
        } catch (Exception ex) {
            return NULL;
        }
    }

    public LJSON(JsonValue value) {
        this.value = value;
    }

    public boolean isUndefined() {
        return (this.value == null);
    }

    public Double getDouble(String name) {
        return get(name).asDouble();
    }

    public Member[] objectMembers() {
        if (isObject()) {
            List<Member> members = new ArrayList<>();
            for (JsonObject.Member member : this.value.asObject()) {
                members.add(new Member(member.getName(), new LJSON(member.getValue())));
            }
            return members.<Member>toArray(new Member[members.size()]);
        }
        return new Member[0];
    }

    public LJSON[] arrayMembers() {
        if (isArray()) {
            List<LJSON> members = new ArrayList<>();
            for (JsonValue member : this.value.asArray()) {
                members.add(new LJSON(member));
            }
            return members.<LJSON>toArray(new LJSON[members.size()]);
        }
        return new LJSON[0];
    }

    public LJSON get(String name) {
        name=name.replace('.', '/');
        if (this.value == null) {
            return NULL;
        }
        if (this.value.isObject()) {
            boolean updated;
            JsonValue v = this.value;
            do {
                updated = false;
                if (name.startsWith("/")) {
                    name = name.substring(1);
                    updated = true;
                }
                if (!name.endsWith("/")) {
                    continue;
                }
                name = name.substring(0, name.length() - 1);
                updated = true;
            } while (updated);
            if (name.isEmpty()) {
                return this;
            }
            int x = name.indexOf('/');
            if (x < 0) {
                if (this.value instanceof JsonObject) {
                    return new LJSON(this.value.asObject().get(name));
                }
                if (this.value instanceof org.hjson.JsonArray) {
                    Integer y = parseInt(name);
                    if (y != null) {
                        return get(y.intValue());
                    }
                    return NULL;
                }
                return NULL;
            }
            String n = name.substring(0, x);
            String extra = name.substring(x + 1);
            return get(n).get(extra);
        }
        return NULL;
    }

    public LJSON get(int index) {
        if (this.value == null) {
            return NULL;
        }
        if (this.value.isArray()) {
            if (index < 0) {
                index = this.value.asArray().size() + index;
            }
            try {
                return new LJSON(this.value.asArray().get(index));
            } catch (Exception e) {
                return NULL;
            }
        }
        return NULL;
    }

    public String[] asStringArray() {
        if (this.value == null) {
            return null;
        }
        if (this.value.isNull()) {
            return null;
        }
        if (this.value.isString()) {
            return new String[]{this.value.asString()};
        }
        if (this.value.isArray()) {
            return (String[]) Arrays.<LJSON>stream(arrayMembers()).map(x -> x.asString()).toArray(x$0 -> new String[x$0]);
        }
        return new String[0];
    }

    public String asString() {
        if (this.value == null) {
            return null;
        }
        if (this.value.isNull()) {
            return null;
        }
        if (this.value.isString()) {
            return this.value.asString();
        }
        return this.value.asString();
    }

    public double asDouble(double defaultValue) {
        Double d = asDouble();
        if (d == null) {
            return defaultValue;
        }
        return defaultValue;
    }

    public Double asDouble() {
        if (this.value == null) {
            return null;
        }
        if (this.value.isNull()) {
            return null;
        }
        if (this.value.isNumber()) {
            return Double.valueOf(this.value.asDouble());
        }
        if (this.value.isString())
      try {
            return Double.valueOf(this.value.asString());
        } catch (Exception e) {
            return null;
        }
        return null;
    }

    public int asInt(int defaultValue) {
        Integer d = asInt();
        if (d == null) {
            return defaultValue;
        }
        return defaultValue;
    }

    public Integer asInt() {
        if (this.value == null) {
            return null;
        }
        if (this.value.isNull()) {
            return null;
        }
        if (this.value.isNumber()) {
            return Integer.valueOf(this.value.asInt());
        }
        if (this.value.isString())
      try {
            return Integer.valueOf(this.value.asString());
        } catch (Exception e) {
            return null;
        }
        return null;
    }

    public LJSON asObject() {
        if (this.value.isObject()) {
            return this;
        }
        return NULL;
    }

    private static Integer parseInt(String s) {
        try {
            return Integer.valueOf(Integer.parseInt(s));
        } catch (Exception ex) {
            return null;
        }
    }

    public boolean isObject() {
        return (this.value != null && this.value.isObject());
    }

    public boolean isArray() {
        return (this.value != null && this.value.isArray());
    }

    public boolean isString() {
        return (this.value != null && this.value.isString());
    }

    public boolean isNull() {
        return (this.value == null || this.value.isNull());
    }

    public static class Member {

        private String name;

        private LJSON value;

        public Member(String name, LJSON value) {
            this.name = name;
            this.value = value;
        }

        public String getName() {
            return this.name;
        }

        public LJSON getValue() {
            return this.value;
        }
    }

    @Override
    public String toString() {
        if(value==null){
            return "<NULL>";
        }
        return value.toString();
    }
    
}
