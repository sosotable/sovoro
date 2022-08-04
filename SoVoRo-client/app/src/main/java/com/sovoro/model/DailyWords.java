package com.sovoro.model;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DailyWords {
    public static Map<WordOption,ArrayList<Word>> dailyWordsMap=new HashMap<>();

    public static void setWordMap(WordOption wordOption, ArrayList<Word> arrayList) {
        dailyWordsMap.put(wordOption, arrayList);
    }

    public static ArrayList<Word> getWordList(WordOption wordOption) {
        return dailyWordsMap.get(wordOption);
    }

    public static String check(WordOption wordOption) {
        return "{"+"\"korean\":"+dailyWordsMap.get(wordOption).get(0).getKoreanWord()+","+"\"english\":"+dailyWordsMap.get(wordOption).get(0).getEnglishWord()+"}";
    }
}
