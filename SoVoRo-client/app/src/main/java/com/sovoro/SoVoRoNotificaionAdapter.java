package com.sovoro;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class SoVoRoNotificaionAdapter extends RecyclerView.Adapter<SoVoRoNotificaionAdapter.ViewHolder>{
    private ArrayList<String> commentNotificationData = null ;
    public interface OnItemClickListener {
        void onItemClick(View v, int position) ;
    }
    private SoVoRoNotificaionAdapter.OnItemClickListener mListener = null ;

    // OnItemClickListener 리스너 객체 참조를 어댑터에 전달하는 메서드
    public void setOnItemClickListener(SoVoRoNotificaionAdapter.OnItemClickListener listener) {
        this.mListener = listener ;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        AppCompatTextView contentView;

        ViewHolder(View itemView) {
            super(itemView) ;
            // 뷰 객체에 대한 참조. (hold strong reference)
            contentView=itemView.findViewById(R.id.notificationContent);

            contentView.setOnClickListener(v->{
                int pos = getAdapterPosition() ;
                if (pos != RecyclerView.NO_POSITION) {
                    // 리스너 객체의 메서드 호출.
                    if (mListener != null) {
                        mListener.onItemClick(v, pos) ;
                    }
                }
            });
        }
    }
    public SoVoRoNotificaionAdapter(ArrayList<String> _commentNotificationData) {
        commentNotificationData=_commentNotificationData;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext() ;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) ;

        View view = inflater.inflate(R.layout.sovoro_notificaion_content, parent, false) ;
        SoVoRoNotificaionAdapter.ViewHolder vh = new SoVoRoNotificaionAdapter.ViewHolder(view) ;

        return vh;
    }

    // onBindViewHolder() - position에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시.
    @Override
    public void onBindViewHolder(SoVoRoNotificaionAdapter.ViewHolder holder, int position) {
        String notificationContent=commentNotificationData.get(position);
        holder.contentView.setText(notificationContent);
    }

    // getItemCount() - 전체 데이터 갯수 리턴.
    @Override
    public int getItemCount() {
        return commentNotificationData.size() ;
    }

    public void addItem(String item) {
        String fmt="%s님이 댓글에 좋아요를 눌렀어요";
        commentNotificationData.add(String.format(fmt,item));
        notifyDataSetChanged();
    }
}
