package net.thevpc.common.props.test;

import org.junit.jupiter.api.Assertions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TestLogHelper {
    private List<String> messages=new ArrayList<>();
    public void println(String message){
        messages.add(message);
        System.out.println(message);
    }
    public void assertEquals(String ... all){
        Assertions.assertEquals(messages, Arrays.asList(all));
    }
}
