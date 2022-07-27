package com.sovoro.model;

import java.util.ArrayList;

public class Word {
    String englishWord;
    String koreanWord;
    String koreanWordDescription;
    String wordImageURL;
    public Word(Object o, Object o1) {}
    public Word(String testType, String englishWord, String koreanWord) {
        this.englishWord=englishWord;
        this.koreanWord=koreanWord;

        if(testType == "main") {
            ArrayList<Word> mainTest = new ArrayList<Word>();
            mainTest.add(new Word(englishWord, koreanWord));
            //DailyWords main = new DailyWords();
            //main.
        }

        if(testType == "test1") {
            ArrayList<Word> testOne = new ArrayList<Word>();
            testOne.add(new Word(englishWord, koreanWord));
        }

        if(testType == "test2") {
            ArrayList<Word> testTwo = new ArrayList<Word>();
            testTwo.add(new Word(englishWord, koreanWord));
        }

        if(testType == "test3") {
            ArrayList<Word> testThree = new ArrayList<Word>();
            testThree.add(new Word(englishWord, koreanWord));
        }
    }
    public Word(String englishWord, String koreanWord, String koreanWordDescription, String wordImageURL) {
        this.englishWord = englishWord;
        this.koreanWord = koreanWord;
        this.koreanWordDescription = koreanWordDescription;
        this.wordImageURL = wordImageURL;
    }

    public String getEnglishWord() {
        return englishWord;
    }

    public void setEnglishWord(String englishWord) {
        this.englishWord = englishWord;
    }

    public String getKoreanWord() {
        return koreanWord;
    }

    public void setKoreanWord(String koreanWord) {
        this.koreanWord = koreanWord;
    }

    public String getKoreanWordDescription() {
        return koreanWordDescription;
    }

    public void setKoreanWordDescription(String koreanWordDescription) {
        this.koreanWordDescription = koreanWordDescription;
    }

    public String getWordImageURL() {
        return wordImageURL;
    }

    public void setWordImageURL(String wordImageURL) {
        this.wordImageURL = wordImageURL;
    }
}
