package com.example.ictproject.fragment_adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ictproject.ChatModel;
import com.example.ictproject.R;
import com.example.ictproject.activity.MessageActivity;
import com.example.ictproject.upload.CompanyUpload;
import com.example.ictproject.upload.Upload;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class FragmentChat extends Fragment {

    public FragmentChat() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_fragment_chat, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.chatFragment_rv);
        recyclerView.setAdapter(new ChatRecyclerViewAdapter(getActivity()));
        recyclerView.setLayoutManager(new LinearLayoutManager(inflater.getContext()));
        return view;
    }

    class ChatRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private List<ChatModel> chatModels = new ArrayList();
        private String uid;
        private ArrayList<String> destinationUsers = new ArrayList<>();
        private Context mContext;

        public ChatRecyclerViewAdapter(Context context) {
            mContext = context;
            uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

            FirebaseDatabase.getInstance().getReference().child("chatRooms").orderByChild("users/" + uid).equalTo(true).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    chatModels.clear();

                    for (DataSnapshot item : dataSnapshot.getChildren()) {
                        chatModels.add(item.getValue(ChatModel.class));
                    }
                    notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat, parent, false);
            return new CustomViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {

            final CustomViewHolder customViewHolder = (CustomViewHolder) holder;
            String destinationUid = null;

            // 채팅방 안의 유저 모두 체크
            for (String user : chatModels.get(position).users.keySet()) {
                if (!user.equals(uid)) {
                    destinationUid = user;
                    destinationUsers.add(destinationUid);
                }
            }

            // destination 지정
            final String finalDestinationUid = destinationUid;
            FirebaseDatabase.getInstance().getReference().child("user").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.child("company").hasChild(finalDestinationUid)) {
                        CompanyUpload cUploads = dataSnapshot.child("company").child(finalDestinationUid).getValue(CompanyUpload.class);
                        customViewHolder.profile.setImageResource(R.drawable.ic_person);
                        customViewHolder.tvTitle.setText(cUploads.getCName());
                    } else if (dataSnapshot.child("employee").hasChild(finalDestinationUid)) {
                        Upload mUploads = dataSnapshot.child("employee").child(finalDestinationUid).getValue(Upload.class);
                        if (mUploads.getName() == null) {
                            customViewHolder.profile.setImageResource(R.drawable.ic_person);
                            customViewHolder.tvTitle.setText(mUploads.getCompanyName());
                        } else {
                            Picasso.with(mContext)
                                    .load(mUploads.getImageUrl())
                                    .fit()
                                    .centerCrop()
                                    .into(customViewHolder.profile);
                            customViewHolder.tvTitle.setText(mUploads.getName());
                        }
                    } else {

                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            // 메시지를 내림차순으로 정렬 후 마지막 메시지를 키값으로 가져오는 코드
            Map<String, ChatModel.Comment> commentMap = new TreeMap<>(Collections.reverseOrder());
            commentMap.putAll(chatModels.get(position).comments); // 채팅에 대한 내용 넣어줌
            String lastMessageKey = (String) commentMap.keySet().toArray()[0]; // 첫번째 값(마지막 메시지)만 받아옴
            customViewHolder.tvLastMessage.setText(chatModels.get(position).comments.get(lastMessageKey).message); // 마지막 메시지를 lastMessageKey 로 받아옴

            // 채팅방 리스트에서 채팅방 클릭 시 채팅 화면으로 전환
            customViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getView().getContext(), MessageActivity.class);
                    intent.putExtra("destinationUid", destinationUsers.get(position)); // 누구랑 대화할지 정해주면 방이 열림
                    startActivity(intent);
                }
            });

        }

        @Override
        public int getItemCount() {
            return chatModels.size();
        }

        private class CustomViewHolder extends RecyclerView.ViewHolder {

            public ImageView profile;
            public TextView tvTitle;
            public TextView tvLastMessage;

            public CustomViewHolder(View view) {
                super(view);
                profile = view.findViewById(R.id.profile);
                tvTitle = view.findViewById(R.id.chatItem_tvTitle);
                tvLastMessage = view.findViewById(R.id.chatItem_tvLastMessage);
            }
        }
    }

}
