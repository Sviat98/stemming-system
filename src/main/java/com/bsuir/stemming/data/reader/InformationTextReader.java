package com.bsuir.stemming.data.reader;

import com.bsuir.stemming.api.data.TextReader;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.File;
import java.io.IOException;

public class InformationTextReader implements TextReader {

    @Override
    public String read(String path) throws ReadingException {

        try  {

            File file = new File(path);
            Document doc = Jsoup.parse(file, "UTF-8");;

            return doc.text();

        } catch (IOException e) {
            throw new ReadingException("Reading problems", e);
        }

    }
}
