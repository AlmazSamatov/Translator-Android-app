package com.example.einepeople.translator;

/**
 * Created by almaz on 12.04.17.
 */

public class Word {
    private String word;
    private int sourcePosition = -1;
    private int targetPosition = -1;
    private String sourceLanguage;
    private String targetLanguage;
    private String translation;

    Word(String word, String translation, int sourcePosition, int targetPosition) {
        this.word = word;
        this.translation = translation;
        this.sourcePosition = sourcePosition;
        this.targetPosition = targetPosition;
        sourceLanguage = Languages.lang[sourcePosition * 2 + 1].toUpperCase();
        targetLanguage = Languages.lang[targetPosition * 2 + 1].toUpperCase();
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public void setTranslation(String translation) {
        this.translation = translation;
    }

    public String getTranslation() {
        return translation;
    }

    public int getSourcePosition() {
        return sourcePosition;
    }

    public void setSourcePosition(int sourcePosition) {
        this.sourcePosition = sourcePosition;
    }

    public String getSourceLanguage() {
        return sourceLanguage;
    }

    public String getTargetLanguage() {
        return targetLanguage;
    }

    public int getTargetPosition() {
        return targetPosition;
    }

    public void setTargetPosition(String targetLanguage) {
        this.targetPosition = targetPosition;
    }

    public boolean isEmpty() {
        if (word == null && targetPosition == -1 && sourcePosition == -1)
            return true;
        else
            return false;
    }
}
