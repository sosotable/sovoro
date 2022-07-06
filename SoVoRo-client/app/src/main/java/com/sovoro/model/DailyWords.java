package com.sovoro.model;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DailyWords {
    public static Map<WordOption,ArrayList<Word>> dailyWordsMap=new HashMap<>();
    public static void setMainWords(ArrayList<Word> arrayList) {
        dailyWordsMap.put(WordOption.MAINWORD,arrayList);
    }
    public static void setTestWords(ArrayList<Word>[] arrayLists) {
        dailyWordsMap.put(WordOption.TESTWORD_1,arrayLists[0]);
        dailyWordsMap.put(WordOption.TESTWORD_2,arrayLists[1]);
        dailyWordsMap.put(WordOption.TESTWORD_3,arrayLists[2]);
    }
    public static ArrayList<Word> getWordList(WordOption wordOption) {
        return dailyWordsMap.get(wordOption);
    }
}
