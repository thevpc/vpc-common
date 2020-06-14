package net.vpc.common.jeep;

import java.util.List;

public interface JListWithSeparators<T> {

    default int size(){
        return getItems().size();
    }

    List<T> getItems();

    JToken getStartToken();

    JToken getEndToken();

    List<JToken> getSeparatorTokens();
}
