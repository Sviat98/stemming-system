package com.bsuir.stemming.api.data;

import java.util.List;

public interface DocumentParser {
    List<String> parse(String text);
}
