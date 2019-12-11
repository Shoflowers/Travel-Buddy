package com.example.travelbuddy;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.example.travelbuddy.Objects.Forum;
import com.example.travelbuddy.Objects.ForumQuestion;
import com.example.travelbuddy.Objects.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;


import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class ForumActivity extends AppCompatActivity {

    Forum forum;
    User currUser;
    private RecyclerView qListView;
    private TextView countryNameTextView;
    private ImageView countryImgView;
    private List<ForumQuestion> qList;
    private QuestionRecyclerAdapter questionRecyclerAdapter;
    private LinearLayout bottomSheet;
    private BottomSheetBehavior sheetBehavior;
    private Button cancelBtn;
    private Button sendBtn;
    private Button addBtn;

    private EditText searchBar;
    private ImageView searchButton;
    private ImageView searchIcon;
    private ImageView cancelSearchButton;

    private DatabaseHandler dbHandler;
    private FirebaseFirestore dbInstance;
    private FirebaseAuth dbAuth;
    private RequestQueue requestQueue;


    //default data for testing
    private String defaultCountryName = "Japan";
    private List<ForumQuestion> defaultQList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forum);

        Intent intent = getIntent();
        forum = (Forum) intent.getSerializableExtra("Forum");
        currUser = ((TravelBuddyApplication)getApplication()).getCurUser();

        qListView = findViewById(R.id.question_list_view);
        countryNameTextView = findViewById(R.id.countryNameTextView);
        countryImgView = findViewById(R.id.countryImageView);
        bottomSheet = findViewById(R.id.new_question_bottom_sheet);
        sheetBehavior = BottomSheetBehavior.from(bottomSheet);
        addBtn = findViewById(R.id.newQuestionBtn);
        cancelBtn = findViewById(R.id.cancelButton);
        sendBtn = findViewById(R.id.sendButton);

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
                    sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                    sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
            }
        });

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

                if (sheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                    sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

                    EditText qTitle = findViewById(R.id.qTitleEditText);
                    EditText qBody = findViewById(R.id.qBodyEditText);
                    //String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

                    //add new question to questions collection
                    DocumentReference addedDocRef = dbInstance.collection("questions").document();
                    ForumQuestion newQuestion = new ForumQuestion(
                            addedDocRef.getId(), qTitle.getText().toString(), qBody.getText().toString(),
                            currUser.getUserId(), new ArrayList<String>(), new ArrayList<String>(), 0,
                            new Date(System.currentTimeMillis()), forum.getForumId(), 0);
                    addedDocRef.set(newQuestion);

                    //update forum and users
                    dbInstance.collection("forums")
                            .document(forum.getForumId())
                            .update("questionIds", FieldValue.arrayUnion(addedDocRef.getId()));

                    dbInstance.collection("users")
                            .document(currUser.getUserId())
                            .update("questionIds", FieldValue.arrayUnion(addedDocRef.getId()));

                    //clear text
                    qTitle.setText("");
                    qBody.setText("");
                    qTitle.setHint("Title");
                    qBody.setHint("Write your question ...");


                }
            }
        });


        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.bottom_navigation_forum);
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_favorites:
                        Intent a = new Intent(ForumActivity.this, HomeActivity.class)
                                .addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(a);
                        break;
                    case R.id.action_explore:
                        Intent b = new Intent(ForumActivity.this, ExploreActivity.class)
                                .addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(b);
                        break;
                    case R.id.action_profile:
                        Intent c = new Intent(ForumActivity.this, ProfileActivity.class)
                                .addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(c);

                        break;
                }
                return false;
            }
        });

        requestQueue = Volley.newRequestQueue(this.getApplicationContext());
        dbInstance = FirebaseFirestore.getInstance();

        String countryName =  forum.getCountryName();
        countryName = countryName.substring(0,1).toUpperCase() + countryName.substring(1).toLowerCase();

        countryNameTextView.setText(countryName);

        loadImage(forum.getPhotoUrl());

        loadData(forum);

        searchBar = findViewById(R.id.searchEditText3);
        searchBar.setVisibility(View.INVISIBLE);
        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                questionRecyclerAdapter.getFilter().filter(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) { }
        });

        searchIcon = findViewById(R.id.searchIcon3);
        searchIcon.setVisibility(View.INVISIBLE);

        cancelSearchButton = findViewById(R.id.cancelIcon3);
        cancelSearchButton.setVisibility(View.INVISIBLE);
        cancelSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchBar.setText("");
                searchBar.setVisibility(View.INVISIBLE);
                searchIcon.setVisibility(View.INVISIBLE);
                searchButton.setVisibility(View.VISIBLE);
                cancelSearchButton.setVisibility(View.INVISIBLE);
            }
        });
        searchButton = findViewById(R.id.searchButton3);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchBar.setVisibility(View.VISIBLE);
                searchIcon.setVisibility(View.VISIBLE);
                searchButton.setVisibility(View.INVISIBLE);
                cancelSearchButton.setVisibility(View.VISIBLE);
            }
        });

    }

    private void loadData(Forum forum) {
        //countryNameTextView.setText(defaultCountryName);
        //countryImgView.setImageDrawable();
        Log.d("DEBUG", "Loading from " + forum.getForumId() );
        Log.d("DEBUG", "qIDs: " + forum.getQuestionIds().size());

        final Context currContext = this;


        dbInstance.collection("questions")
                .whereEqualTo("forumId", forum.getForumId())
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.d("DEBUG","Listen failed");
                            return;
                        }

                        qList = new LinkedList<>();

                        for (DocumentSnapshot doc : queryDocumentSnapshots) {
                            qList.add(doc.toObject(ForumQuestion.class));
                        }

                        questionRecyclerAdapter = new QuestionRecyclerAdapter(qList);
                        qListView.setLayoutManager(new LinearLayoutManager(currContext));
                        qListView.setAdapter(questionRecyclerAdapter);
                    }
                });

    }

    private void loadImage(String url) {
        if (url != null && !url.isEmpty()) {
            Picasso.get().load(url).into(countryImgView);
        }
    }

    private void getDefaultData() {
        ForumQuestion q1 = new ForumQuestion("q1",
                "Would it be correct to say, \"Aus diesem Grund möchte ich sie bitten, meinen Vertrag schnell wie möglich zu kündigen\"?",
                "empty",
                "user1",
                Arrays.asList("food", "language"),
                Arrays.asList("ans11", "ans12"),
                100, new Date(System.currentTimeMillis()), "Germany", 100);

        ForumQuestion q2 = new ForumQuestion("q2",
                "What my experience was like living in Hamburg for 8 months.",
                "empty",
                "user2",
                Arrays.asList("food", "language"),
                Arrays.asList("ans21", "ans22"),
                100, new Date(System.currentTimeMillis()),"Germany", 100);


        ForumQuestion q3 = new ForumQuestion("q3",
                "What is the difference between Northern people and Southern people of Germany?",
                "empty",
                "user3",
                Arrays.asList("food", "language"),
                Arrays.asList("ans31", "ans32"),
                100, new Date(System.currentTimeMillis()),"Germany", 100);

        defaultQList = new ArrayList<>(3);
        defaultQList.add(q1);
        defaultQList.add(q2);
        defaultQList.add(q3);
    }
}
