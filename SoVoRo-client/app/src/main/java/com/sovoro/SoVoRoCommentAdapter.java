package com.sovoro;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class SoVoRoCommentAdapter extends RecyclerView.Adapter<SoVoRoCommentAdapter.ViewHolder> {
    private ArrayList<SoVoRoCommentInfo> commentViewData = null ;
    public interface OnItemClickListener {
        void onItemClick(View v, int position) ;
    }
    private OnItemClickListener mListener = null ;

    // OnItemClickListener 리스너 객체 참조를 어댑터에 전달하는 메서드
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener ;
    }

    // 아이템 뷰를 저장하는 뷰홀더 클래스.
    public class ViewHolder extends RecyclerView.ViewHolder {
        AppCompatImageView userImage;
        AppCompatTextView userNickname;
        AppCompatTextView userComment;
        AppCompatButton userCommentLike;
        AppCompatTextView userCommentLikesCount;

        ViewHolder(View itemView) {
            super(itemView) ;
            // 뷰 객체에 대한 참조. (hold strong reference)
            userImage=itemView.findViewById(R.id.sovoro_comment_userimage);
            userNickname=itemView.findViewById(R.id.sovoro_comment_username);
            userComment=itemView.findViewById(R.id.sovoro_comment_usercomment);
            userCommentLike=itemView.findViewById(R.id.sovoro_comment_likes);
            userCommentLikesCount=itemView.findViewById(R.id.sovoro_comment_likes_count);

            userCommentLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition() ;
                    if (pos != RecyclerView.NO_POSITION) {
                        // 리스너 객체의 메서드 호출.
                        if (mListener != null) {
                            mListener.onItemClick(v, pos) ;
                        }
                    }
                    userCommentLikesCount.setText(commentViewData.get(pos).userCommentLikesCount);
                }
            });

//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    int pos = getAdapterPosition() ;
//                    if (pos != RecyclerView.NO_POSITION) {
//                        // 리스너 객체의 메서드 호출.
//                        if (mListener != null) {
//                            mListener.onItemClick(v, pos) ;
//                        }
//                    }
//                }
//            });
        }
    }
    // 생성자에서 데이터 리스트 객체를 전달받음.
    public SoVoRoCommentAdapter(ArrayList<SoVoRoCommentInfo> _commentViewData) {
        commentViewData=_commentViewData;
    }

    // onCreateViewHolder() - 아이템 뷰를 위한 뷰홀더 객체 생성하여 리턴.
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext() ;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) ;

        View view = inflater.inflate(R.layout.sovoro_comment_view, parent, false) ;
        ViewHolder vh = new ViewHolder(view) ;

        return vh;
    }
    // onBindViewHolder() - position에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시.
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        SoVoRoCommentInfo soVoRoCommentInfo = commentViewData.get(position);
        String userImage=soVoRoCommentInfo.userImage;
        String userNickname=soVoRoCommentInfo.userNickname;
        String userComment=soVoRoCommentInfo.userComment;
        String userCommentLikesCount=Integer.toString(soVoRoCommentInfo.userCommentLikesCount);
        // Glide사용 이미지 지정
        // holder.userImage.setImageIcon(userImage);

        holder.userNickname.setText(userNickname);
        holder.userComment.setText(userComment);
        holder.userCommentLikesCount.setText(userCommentLikesCount);
    }

    // getItemCount() - 전체 데이터 갯수 리턴.
    @Override
    public int getItemCount() {
        return commentViewData.size() ;
    }
    public void setList(ArrayList<SoVoRoCommentInfo> alist) {
        this.commentViewData=alist;
        notifyItemInserted(commentViewData.size());
        notifyDataSetChanged();
    }
    public void addItem(SoVoRoCommentInfo soVoRoCommentInfo) {
        commentViewData.add(soVoRoCommentInfo);
        notifyDataSetChanged();
    }

}
