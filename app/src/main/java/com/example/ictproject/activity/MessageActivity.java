package com.example.ictproject.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.ictproject.ChatModel;
import com.example.ictproject.R;
import com.example.ictproject.upload.CompanyUpload;
import com.example.ictproject.upload.Upload;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MessageActivity extends AppCompatActivity {

    private String destinationUid, chatRoomUid, uid, phoneNum;
    private Button button, matchButton;
    private EditText editText;
    private RecyclerView recyclerView;
    private TextView textView;
    private CompanyUpload cUploads;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        uid = FirebaseAuth.getInstance().getCurrentUser().getUid(); // 채팅 보내는이
        destinationUid = getIntent().getStringExtra("destinationUid"); // 채팅 받는이
        button = findViewById(R.id.messageActivity_btn);
        matchButton = findViewById(R.id.matchButton);
        editText = findViewById(R.id.messageActivity_et);
        recyclerView = findViewById(R.id.messageActivity_rv);
        ImageView imageView = findViewById(R.id.backward);
        textView = findViewById(R.id.chatRoomName);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MessageActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChatModel chatModel = new ChatModel();
                chatModel.users.put(uid, true);
                chatModel.users.put(destinationUid, true);

                if (chatRoomUid == null) {
                    button.setEnabled(false);
                    FirebaseDatabase.getInstance().getReference().child("chatRooms").push().setValue(chatModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            checkChatRoom();
                        }
                    });
                } else {
                    ChatModel.Comment comment = new ChatModel.Comment();
                    comment.uid = uid;
                    comment.message = editText.getText().toString();
                    FirebaseDatabase.getInstance().getReference().child("chatRooms").child(chatRoomUid).child("comments").push().setValue(comment).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            editText.setText(""); // 입력창 초기화
                        }
                    });
                }
            }
        });
        checkChatRoom();

        matchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase.getInstance().getReference("user").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.child("company").hasChild(uid)) {
                            matchButton.setEnabled(false);
                        } else {
                            if (dataSnapshot.child("company").hasChild(destinationUid)) {
                                cUploads = dataSnapshot.child("company").child(destinationUid).getValue(CompanyUpload.class);
                                phoneNum = cUploads.getCompanyPhone().trim();
                            }

                            try {
                                SmsManager smgr = SmsManager.getDefault();
                                smgr.sendTextMessage(phoneNum, null, "안녕하세요, 이번에 쉐어빌리티를 통해 근무하게 되어 연락드립니다", null, null);
                                Toast.makeText(MessageActivity.this, "메시지를 성공적으로 전송하였습니다.", Toast.LENGTH_SHORT).show();
                            } catch (Exception e) {
                                Toast.makeText(MessageActivity.this, "메시지 전송을 실패하였습니다.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

    }

    void checkChatRoom() {
        FirebaseDatabase.getInstance().getReference().child("chatRooms").orderByChild("users/" + uid).equalTo(true).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot item : dataSnapshot.getChildren()) {
                    ChatModel chatModel = item.getValue(ChatModel.class); // 파이어베이스 chatRooms 하의 db 받아옴
                    if (chatModel.users.containsKey(destinationUid)) {
                        chatRoomUid = item.getKey(); // 채팅방 고유값
                        button.setEnabled(true);

                        recyclerView.setLayoutManager(new LinearLayoutManager(MessageActivity.this));
                        recyclerView.setAdapter(new RecyclerViewAdapter());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        List<ChatModel.Comment> comments;
        Upload mUploads;
        CompanyUpload cUploads;

        public RecyclerViewAdapter() {
            comments = new ArrayList<>();

            // 회사 구직자 구별 필요?
            FirebaseDatabase.getInstance().getReference().child("user").child(destinationUid).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.child("employee").hasChild(destinationUid)) {
                        mUploads = dataSnapshot.getValue(Upload.class);
                        getMessageList();
                    } else {
                        cUploads = dataSnapshot.getValue(CompanyUpload.class);
                        getMessageList();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

        void getMessageList() {
            FirebaseDatabase.getInstance().getReference().child("chatRooms").child(chatRoomUid).child("comments").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    comments.clear();
                    for (DataSnapshot item : dataSnapshot.getChildren()) {
                        comments.add(item.getValue(ChatModel.Comment.class));
                    }
                    notifyDataSetChanged();
                    recyclerView.scrollToPosition(comments.size() - 1);
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message, parent, false);
            return new MessageViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
            final MessageViewHolder messageViewHolder = ((MessageViewHolder) holder);

            // 업체 구직자 구별 필요?
            FirebaseDatabase.getInstance().getReference("user").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    // 대화 상대방이 구직자일 때
                    if (dataSnapshot.child("employee").hasChild(destinationUid)) {
                        mUploads = dataSnapshot.child("employee").child(destinationUid).getValue(Upload.class);
                        textView.setText(mUploads.getName());
                        if (comments.get(position).uid.equals(uid)) {
                            messageViewHolder.chatProfile.setVisibility(View.INVISIBLE);
                            messageViewHolder.tvMessage.setText(comments.get(position).message);
                            messageViewHolder.tvMessage.setBackgroundResource(R.drawable.rightbubble);
                            messageViewHolder.llDestination.setVisibility(View.INVISIBLE); // 내 메시지는 상대방이 볼 필요 없으므로
                            messageViewHolder.llMain.setGravity(Gravity.RIGHT); // 내 메시지 우측 정렬

                        } else {
                            messageViewHolder.tvName.setText(mUploads.getName());
                            messageViewHolder.tvMessage.setBackgroundResource(R.drawable.leftbubble);
                            messageViewHolder.llDestination.setVisibility(View.VISIBLE);
                            messageViewHolder.tvMessage.setText(comments.get(position).message);
                            messageViewHolder.llMain.setGravity(Gravity.LEFT);
                            Glide.with(getApplicationContext())
                                    .load(mUploads.getImageUrl())
                                    .fitCenter()
                                    .into(messageViewHolder.chatProfile);
                        }
                        messageViewHolder.tvMessage.setTextSize(15);
                        // 대화 상대방이 업체일 때
                    } else {
                        cUploads = dataSnapshot.child("company").child(destinationUid).getValue(CompanyUpload.class);
                        textView.setText(cUploads.getCompanyName());
                        if (comments.get(position).uid.equals(uid)) {
                            messageViewHolder.chatProfile.setVisibility(View.INVISIBLE);
                            messageViewHolder.tvMessage.setText(comments.get(position).message);
                            messageViewHolder.tvMessage.setBackgroundResource(R.drawable.rightbubble);
                            messageViewHolder.llDestination.setVisibility(View.INVISIBLE); // 내 메시지는 상대방이 볼 필요 없으므로
                            messageViewHolder.llMain.setGravity(Gravity.RIGHT); // 내 메시지 우측 정렬

                        } else {
                            messageViewHolder.tvName.setText(cUploads.getCompanyName());
                            messageViewHolder.tvMessage.setBackgroundResource(R.drawable.leftbubble);
                            messageViewHolder.llDestination.setVisibility(View.VISIBLE);
                            messageViewHolder.chatProfile.setImageResource(R.drawable.ic_burgerking);
                            messageViewHolder.tvMessage.setText(comments.get(position).message);
                            messageViewHolder.llMain.setGravity(Gravity.LEFT); // 상대 메시지 좌측 정렬
                        }
                        messageViewHolder.tvMessage.setTextSize(15);

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }

        @Override
        public int getItemCount() {
            return comments.size();
        }

        private class MessageViewHolder extends RecyclerView.ViewHolder {
            public TextView tvName;
            public TextView tvMessage;
            public LinearLayout llDestination;
            public LinearLayout llMain;
            public ImageView chatProfile;


            public MessageViewHolder(View view) {
                super(view);
                llDestination = (LinearLayout) view.findViewById(R.id.messageItem_llDestination);
                tvMessage = view.findViewById(R.id.messageItem_tvMessage);
                tvName = view.findViewById(R.id.messageItem_tvName);
                llMain = (LinearLayout) view.findViewById(R.id.messageItem_llMain); // 채팅창 정렬에 쓰임
                chatProfile = view.findViewById(R.id.chatProfile);
            }
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        moveTaskToBack(true);
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(1);
    }
}
