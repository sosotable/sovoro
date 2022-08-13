package com.sovoro.testview;

import android.os.Bundle;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.sovoro.R;
import com.sovoro.model.DailyWords;
import com.sovoro.model.TestResult;
import com.sovoro.model.UserInfo;
import com.sovoro.model.Word;
import com.sovoro.model.WordOption;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Random;

import io.socket.client.IO;
import io.socket.client.Socket;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SoVoRoTest6#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SoVoRoTest6 extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private Word mainWord;
    private Word[] testWords=new Word[4];
    private ArrayList<Integer> randList=new ArrayList<>();

    private AppCompatTextView main;
    private RadioButton test1;
    private RadioButton test2;
    private RadioButton test3;
    private RadioButton test4;
    private RadioGroup randomGroup;

    private AppCompatTextView tv;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Socket socket;

    public SoVoRoTest6() {
        {
            try {
                socket = IO.socket("http://13.58.48.132:3000");
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }
        socket.connect();
        // Required empty public constructor
        Random random = new Random();
        while(true) {
            int v=random.nextInt(4);
            if(!randList.contains(v)) randList.add(v);
            if(randList.size()==4) break;
        }
        for(int i=0;i<randList.size();i++) {
            switch(randList.get(i)) {
                case 0:
                    testWords[i]=DailyWords.dailyWordsMap.get(WordOption.MAINWORD).get(5);
                    mainWord = DailyWords.dailyWordsMap.get(WordOption.MAINWORD).get(5);
                    break;
                case 1:
                    testWords[i]=DailyWords.dailyWordsMap.get(WordOption.TESTWORD_1).get(5);
                    break;
                case 2:
                    testWords[i]=DailyWords.dailyWordsMap.get(WordOption.TESTWORD_2).get(5);
                    break;
                case 3:
                    testWords[i]=DailyWords.dailyWordsMap.get(WordOption.TESTWORD_3).get(5);
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SoVoRoTest6.
     */
    // TODO: Rename and change types and number of parameters
    public static SoVoRoTest6 newInstance(String param1, String param2) {
        SoVoRoTest6 fragment = new SoVoRoTest6();
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
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_sovoro_test1, container, false);


        main=view.findViewById(R.id.sovoro_question_1);
        test1=view.findViewById(R.id.sovoro_answer_radio_1_1);
        test2=view.findViewById(R.id.sovoro_answer_radio_1_2);
        test3=view.findViewById(R.id.sovoro_answer_radio_1_3);
        test4=view.findViewById(R.id.sovoro_answer_radio_1_4);
        randomGroup=view.findViewById(R.id.sovoro_test_radio_group_1);


        main.setText(mainWord.getEnglishWord());

        test1.setText(testWords[0].getKoreanWord());
        test2.setText(testWords[1].getKoreanWord());
        test3.setText(testWords[2].getKoreanWord());
        test4.setText(testWords[3].getKoreanWord());

        tv=view.findViewById(R.id.sovoro_test_result_1);

        randomGroup.setOnCheckedChangeListener(((radioGroup, id) -> {
            switch(id){
                case R.id.sovoro_answer_radio_1_1:
                    if(testWords[0].getKoreanWord().equals(mainWord.getKoreanWord())) {
                        tv.setText("CORRECT");
                        TestResult.correct++;
                    }
                    else {
                        tv.setText("WRONG");
                    }
                    TestResult.solved++;
                    test1.setEnabled(false);
                    test2.setEnabled(false);
                    test3.setEnabled(false);
                    test4.setEnabled(false);
                    break;
                case R.id.sovoro_answer_radio_1_2:
                    if(testWords[1].getKoreanWord().equals(mainWord.getKoreanWord())) {
                        tv.setText("CORRECT");
                        TestResult.correct++;
                    }
                    else {
                        tv.setText("WRONG");
                    }
                    TestResult.solved++;
                    test1.setEnabled(false);
                    test2.setEnabled(false);
                    test3.setEnabled(false);
                    test4.setEnabled(false);
                    break;
                case R.id.sovoro_answer_radio_1_3:
                    if(testWords[2].getKoreanWord().equals(mainWord.getKoreanWord())) {
                        tv.setText("CORRECT");
                        TestResult.correct++;
                    }
                    else {
                        tv.setText("WRONG");
                    }
                    TestResult.solved++;
                    test1.setEnabled(false);
                    test2.setEnabled(false);
                    test3.setEnabled(false);
                    test4.setEnabled(false);
                    break;
                case R.id.sovoro_answer_radio_1_4:
                    if(testWords[3].getKoreanWord().equals(mainWord.getKoreanWord())) {
                        tv.setText("CORRECT");
                        TestResult.correct++;
                    }
                    else {
                        tv.setText("WRONG");
                    }
                    TestResult.solved++;
                    test1.setEnabled(false);
                    test2.setEnabled(false);
                    test3.setEnabled(false);
                    test4.setEnabled(false);
                    break;
            }
            Log.d("AAA",Integer.toString(TestResult.solved));
            if(TestResult.solved==10) {
                JSONObject testResult=new JSONObject();
                try {
                    testResult.put("correct",TestResult.correct);
                    testResult.put("solved",TestResult.solved);
                    testResult.put("userId", UserInfo.userid);
                    testResult.put("dayCookie",DailyWords.dayCookie);
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
                socket.emit("all solved",testResult);
            }
        }));

        return view;
    }
}