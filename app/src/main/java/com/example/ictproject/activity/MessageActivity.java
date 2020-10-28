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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.ictproject.ChatModel;
import com.example.ictproject.R;
import com.example.ictproject.upload.CompanyUpload;
import com.example.ictproject.upload.Feedback;
import com.example.ictproject.upload.Upload;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MessageActivity extends AppCompatActivity {

    private DatabaseReference DatabaseRef, fDatabaseRef, chatDatabaseRef;
    private String destinationUid, chatRoomUid, uid, comPhone, albaPhone;
    private RelativeLayout relativeLayout;
    private Button button, matchButton;
    private EditText editText, feedbackEditText;
    private RecyclerView recyclerView;
    private TextView textView;
    private CompanyUpload cUploads;
    private Upload mUploads;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        uid = FirebaseAuth.getInstance().getCurrentUser().getUid(); // 채팅 보내는이
        destinationUid = getIntent().getStringExtra("destinationUid"); // 채팅 받는이
        button = findViewById(R.id.messageActivity_btn);
        matchButton = findViewById(R.id.matchButton);
        editText = findViewById(R.id.messageActivity_et);
        feedbackEditText = findViewById(R.id.feedbackEditText);
        recyclerView = findViewById(R.id.messageActivity_rv);
        textView = findViewById(R.id.chatRoomName);
        relativeLayout = findViewById(R.id.writeFeedbackLayout);
        DatabaseRef = FirebaseDatabase.getInstance().getReference("user");
        fDatabaseRef = FirebaseDatabase.getInstance().getReference("feedback");
        chatDatabaseRef = FirebaseDatabase.getInstance().getReference("chatRooms");

        ImageView imageView = findViewById(R.id.backward);
        Button uploadFd = findViewById(R.id.uploadFeedback);
        Button feedback = findViewById(R.id.feedbackButton);

        DatabaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("employee").hasChild(destinationUid)) {
                    Upload upload = dataSnapshot.child("employee").child(destinationUid).getValue(Upload.class);
                    textView.setText(upload.getName());
                } else {
                    CompanyUpload companyUpload = dataSnapshot.child("company").child(destinationUid).getValue(CompanyUpload.class);
                    textView.setText(companyUpload.getCompanyName());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

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
                    chatDatabaseRef.push().setValue(chatModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            checkChatRoom();
                        }
                   });
                } else {
                    ChatModel.Comment comment = new ChatModel.Comment();
                    comment.uid = uid;
                    comment.message = editText.getText().toString();
                    chatDatabaseRef.child(chatRoomUid).child("comments").push().setValue(comment).addOnCompleteListener(new OnCompleteListener<Void>() {
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
                DatabaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.child("company").hasChild(uid)) {
                            mUploads = dataSnapshot.child("employee").child(destinationUid).getValue(Upload.class);
                            cUploads = dataSnapshot.child("company").child(uid).getValue(CompanyUpload.class);
                            albaPhone = mUploads.getPhoneNum().trim();
                            sendMessage(albaPhone, "[ShareAbility] |n 안녕하세요" + cUploads.getCompanyName() + "입니다. 아르바이트 매칭 확인 차 연락을 드립니다.");

                        } else {
                            cUploads = dataSnapshot.child("company").child(destinationUid).getValue(CompanyUpload.class);
                            mUploads = dataSnapshot.child("employee").child(uid).getValue(Upload.class);
                            if (cUploads.getMyPhoneNum() == null) {
                                comPhone = cUploads.getCompanyPhone().trim();
                                sendMessage(comPhone, "[ShareAbility] |n 안녕하세요" + cUploads.getCompanyName() + "에서 아르바이트를 하게 된" + mUploads.getName() + "입니다. 아르바이트 매칭 확인 차 연락을 드립니다.");
                                DatabaseRef.child("match").child(uid).setValue("matching");
                            } else {
                                albaPhone = cUploads.getMyPhoneNum().trim();
                                comPhone = cUploads.getCompanyPhone().trim();
                                sendMessage(albaPhone, "[ShareAbility] |n 안녕하세요" + cUploads.getCompanyName() + "에서" +  cUploads.getName() + "님 대신 아르바이트를 하게 된" + mUploads.getName() + "입니다. 아르바이트 매칭 확인 차 연락을 드립니다.");
                                sendMessage(comPhone, "[ShareAbility] |n 안녕하세요" + cUploads.getCompanyName() + "에서" +  cUploads.getName() + "님 대신 아르바이트를 하게 된" + mUploads.getName() + "입니다. 아르바이트 매칭 확인 차 연락을 드립니다.");
                                DatabaseRef.child("match").child(uid).setValue("matching");
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

        feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                relativeLayout.setVisibility(View.VISIBLE);
            }
        });

        uploadFd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.child("company").hasChild(uid)) {
                            cUploads = dataSnapshot.child("company").child(uid).getValue(CompanyUpload.class);
                            String companyName = cUploads.getCompanyName().trim();

                            Feedback feedback = new Feedback(companyName, feedbackEditText.getText().toString().trim());
                            fDatabaseRef.child(destinationUid).setValue(feedback);
                            Toast.makeText(MessageActivity.this, "등록 성공", Toast.LENGTH_LONG).show();
                            relativeLayout.setVisibility(View.GONE);
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
        chatDatabaseRef.orderByChild("users/" + uid).equalTo(true).addListenerForSingleValueEvent(new ValueEventListener() {
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

        RecyclerViewAdapter() {
            comments = new ArrayList<>();

            // 회사 구직자 구별 필요?
            DatabaseRef.child(destinationUid).addListenerForSingleValueEvent(new ValueEventListener() {
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
            chatDatabaseRef.child(chatRoomUid).child("comments").addValueEventListener(new ValueEventListener() {
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
            DatabaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    // 대화 상대방이 구직자일 때
                    if (dataSnapshot.child("employee").hasChild(destinationUid)) {
                        mUploads = dataSnapshot.child("employee").child(destinationUid).getValue(Upload.class);
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
            TextView tvName;
            TextView tvMessage;
            LinearLayout llDestination;
            LinearLayout llMain;
            ImageView chatProfile;


            MessageViewHolder(View view) {
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

    void sendMessage(String phoneNum, String message) {
        try {
            SmsManager smgr = SmsManager.getDefault();
            Toast.makeText(MessageActivity.this, "매칭이 완료되었습니다. 상대방에게 매칭 확인 문자를 전송합니다.", Toast.LENGTH_SHORT).show();
            smgr.sendTextMessage(phoneNum, null, message, null, null);
        } catch (Exception e) {
            Toast.makeText(MessageActivity.this, "메시지 전송을 실패하였습니다.", Toast.LENGTH_SHORT).show();
        }
    }

}
