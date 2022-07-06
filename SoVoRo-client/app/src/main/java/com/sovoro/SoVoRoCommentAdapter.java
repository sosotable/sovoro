package com.sovoro;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class SoVoRoCommentAdapter extends RecyclerView.Adapter<SoVoRoCommentAdapter.ViewHolder> {
    private ArrayList<SoVoRoCommentInfo> commentViewData = null ;

    // 아이템 뷰를 저장하는 뷰홀더 클래스.
    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView userImage;
        TextView userComment;
        Button userCommentLike;
        TextView userCommentLikesCount;

        ViewHolder(View itemView) {
            super(itemView) ;
            // 뷰 객체에 대한 참조. (hold strong reference)
            userImage=itemView.findViewById(R.id.sovoro_comment_userimage);
            userComment=itemView.findViewById(R.id.sovoro_comment_usercomment);
            userCommentLike=itemView.findViewById(R.id.sovoro_comment_likes);
            userCommentLikesCount=itemView.findViewById(R.id.sovoro_comment_likes_count);

            userCommentLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String likeCount=
                            Integer.toString(
                                    Integer.parseInt(userCommentLikesCount.getText().toString())+1
                            );
                    userCommentLike.setText(likeCount);
                }
            });
        }
    }
    // 생성자에서 데이터 리스트 객체를 전달받음.
    public SoVoRoCommentAdapter(ArrayList<SoVoRoCommentInfo> _commentViewData) {
        commentViewData=_commentViewData;
    }

    // onCreateViewHolder() - 아이템 뷰를 위한 뷰홀더 객체 생성하여 리턴.
    @Override
    public SoVoRoCommentAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext() ;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) ;

        View view = inflater.inflate(R.layout.sovoro_comment_view, parent, false) ;
        SoVoRoCommentAdapter.ViewHolder vh = new SoVoRoCommentAdapter.ViewHolder(view) ;

        return vh;
    }
    // onBindViewHolder() - position에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시.
    @Override
    public void onBindViewHolder(SoVoRoCommentAdapter.ViewHolder holder, int position) {
        SoVoRoCommentInfo soVoRoCommentInfo = commentViewData.get(position);
        String userImage=soVoRoCommentInfo.getUserImage();
        String userComment=soVoRoCommentInfo.getUserComment();
        String userCommentLikesCount=soVoRoCommentInfo.getUserCommentLikesCount();
        // Glide사용 이미지 지정
        // holder.userImage.setImageIcon(userImage);
        holder.userComment.setText(userComment);
        holder.userCommentLikesCount.setText(userCommentLikesCount);
    }

    // getItemCount() - 전체 데이터 갯수 리턴.
    @Override
    public int getItemCount() {
        return commentViewData.size() ;
    }
}
