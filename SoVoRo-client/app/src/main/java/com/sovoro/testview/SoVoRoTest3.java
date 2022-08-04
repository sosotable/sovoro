package com.sovoro.testview;

import android.os.Bundle;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import com.sovoro.R;
import com.sovoro.model.DailyWords;
import com.sovoro.model.Word;
import com.sovoro.model.WordOption;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SoVoRoTest3#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SoVoRoTest3 extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Word mainWord;
    private Word testWord1;
    private Word testWord2;
    private Word testWord3;

    private AppCompatTextView main;
    private RadioButton test1;
    private RadioButton test2;
    private RadioButton test3;
    private RadioButton test4;

    public SoVoRoTest3() {
        // Required empty public constructor
        mainWord= DailyWords.dailyWordsMap.get(WordOption.MAINWORD).get(2);
        testWord1=DailyWords.dailyWordsMap.get(WordOption.TESTWORD_1).get(2);
        testWord2=DailyWords.dailyWordsMap.get(WordOption.TESTWORD_2).get(2);
        testWord3=DailyWords.dailyWordsMap.get(WordOption.TESTWORD_3).get(2);
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SoVoRoTest3.
     */
    // TODO: Rename and change types and number of parameters
    public static SoVoRoTest3 newInstance(String param1, String param2) {
        SoVoRoTest3 fragment = new SoVoRoTest3();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_sovoro_test3, container, false);
        main=view.findViewById(R.id.sovoro_question_3);
        test1=view.findViewById(R.id.sovoro_answer_radio_3_1);
        test2=view.findViewById(R.id.sovoro_answer_radio_3_2);
        test3=view.findViewById(R.id.sovoro_answer_radio_3_3);
        test4=view.findViewById(R.id.sovoro_answer_radio_3_4);

        main.setText(mainWord.getEnglishWord());
        test1.setText(mainWord.getKoreanWord());
        test2.setText(testWord1.getKoreanWord());
        test3.setText(testWord2.getKoreanWord());
        test4.setText(testWord3.getKoreanWord());
        return view;
    }
}