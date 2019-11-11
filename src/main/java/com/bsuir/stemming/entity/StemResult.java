package com.bsuir.stemming.entity;

public class StemResult {
    private String wordValue;
    private int frequency;

    public StemResult(String wordValue, int frequency) {
        this.wordValue = wordValue;
        this.frequency = frequency;
    }

    public String getWordValue() {
        return wordValue;
    }


    public int getFrequency() {
        return frequency;
    }

}
