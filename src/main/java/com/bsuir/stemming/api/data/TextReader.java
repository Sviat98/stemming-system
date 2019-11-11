package com.bsuir.stemming.api.data;

import com.bsuir.stemming.data.reader.ReadingException;

import java.io.File;

public interface TextReader {
    String read(String link) throws ReadingException;

}