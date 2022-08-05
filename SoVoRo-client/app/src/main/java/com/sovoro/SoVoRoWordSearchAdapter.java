package com.sovoro;

import android.content.Context;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.sovoro.model.Word;

import java.util.ArrayList;

public class SoVoRoWordSearchAdapter extends RecyclerView.Adapter<SoVoRoWordSearchAdapter.ViewHolder> {
    private ArrayList<Word> mData = null ;

    // 아이템 뷰를 저장하는 뷰홀더 클래스.
    public class ViewHolder extends RecyclerView.ViewHolder {
        AppCompatTextView engWord;
        AppCompatTextView korWord;

        ViewHolder(View itemView) {
            super(itemView) ;

            engWord=itemView.findViewById(R.id.engword);
            korWord=itemView.findViewById(R.id.korword);

        }
    }

    // 생성자에서 데이터 리스트 객체를 전달받음.
    SoVoRoWordSearchAdapter(ArrayList<Word> list) {
        mData = list ;
    }

    // onCreateViewHolder() - 아이템 뷰를 위한 뷰홀더 객체 생성하여 리턴.
    @Override
    public SoVoRoWordSearchAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext() ;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) ;

        View view = inflater.inflate(R.layout.sovoro_search_content, parent, false) ;
        SoVoRoWordSearchAdapter.ViewHolder vh = new SoVoRoWordSearchAdapter.ViewHolder(view) ;

        return vh ;
    }

    // onBindViewHolder() - position에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시.
    @Override
    public void onBindViewHolder(SoVoRoWordSearchAdapter.ViewHolder holder, int position) {
        String engword = mData.get(position).getEnglishWord();
        String korword=mData.get(position).getKoreanWord();
        holder.engWord.setText(engword);
        holder.korWord.setText(korword);
    }

    // getItemCount() - 전체 데이터 갯수 리턴.
    @Override
    public int getItemCount() {
        return mData.size() ;
    }

    public void setData(ArrayList<Word> arraylist) {
        mData=arraylist;
    }

}
