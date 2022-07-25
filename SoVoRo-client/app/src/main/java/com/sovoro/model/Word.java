package com.sovoro.model;

public class Word {
    String englishWord;
    String koreanWord;
    String koreanWordDescription;
    String wordImageURL;
    public Word(Object o, Object o1) {}
    public Word(String englishWord, String koreanWord) {
        this.englishWord=englishWord;
        this.koreanWord=koreanWord;
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
