package com.example.stackoverflow;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class QuestionListActivity extends AppCompatActivity {
    ArrayList<String> userTagList = new ArrayList<>();
    RecyclerView recyclerView;
    LinearLayout linearLayout;
    RecyclerAdapter mRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_list);

        Bundle bundle = getIntent().getExtras();
        userTagList = (ArrayList<String>) bundle.getStringArrayList("array_list");

        QuestionListActivity.HttpRequestPost httpRequestPost = new HttpRequestPost();
        httpRequestPost.execute();
        //RecyclerView
        recyclerView = (RecyclerView) findViewById(R.id.post_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //Linear layout
        linearLayout = findViewById(R.id.post_linear_layout);

        for (int i = 0; i < userTagList.size(); i++) {
            final TextView textView = new TextView(getApplicationContext());
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(10, 10, 10, 20);
            textView.setLayoutParams(params);
            //textView.setBackgroundResource(R.drawable.round_shape);
            textView.setBackgroundColor(getResources().getColor(R.color.active));
            textView.setPadding(20, 10, 20, 10);
            textView.setTextSize(20);
            textView.setTextColor(getResources().getColor(R.color.white));
            textView.setText(userTagList.get(i) + " x");
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    String tag = textView.getText().toString().split(" ")[0];
                    if (userTagList.contains(tag)) {
                        if (userTagList.size() == 1) {
                            Toast.makeText(getApplicationContext(), "Can not remove all tags", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        userTagList.remove(tag);
                        view.setBackgroundColor(getResources().getColor(R.color.inactive));
                    } else {
                        userTagList.add(tag);
                        view.setBackgroundColor(getResources().getColor(R.color.active));
                    }
                    if (mRecyclerAdapter != null) {
                        QuestionListActivity.HttpRequestPost httpRequestPost = new HttpRequestPost();
                        httpRequestPost.execute();
                    }
                }
            });
            if (linearLayout != null) {
                linearLayout.addView(textView);
            }
        }
    }

    public class HttpRequestPost extends AsyncTask<Void, Integer, ArrayList<String>> {

        @Override
        protected ArrayList<String> doInBackground(Void... voids) {
            HttpHandler httpHandler = new HttpHandler();
            try {
                ArrayList<String> list_post = httpHandler.getPosts(userTagList);
                return list_post;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(ArrayList<String> strings) {
            super.onPostExecute(strings);
            if (strings == null)
                Toast.makeText(getApplicationContext(), "Error fetching Posts", Toast.LENGTH_SHORT).show();
            if (strings.size() == 0) {
                Toast.makeText(getApplicationContext(), "No question for this Combination of Tags", Toast.LENGTH_SHORT).show();
                mRecyclerAdapter = new RecyclerAdapter(getApplicationContext(), strings, new RecyclerAdapter.OnViewClickListener() {
                    @Override
                    public void onViewClick(String tag) {
                    }
                });
                recyclerView.setAdapter(mRecyclerAdapter);
            }
            else {
                //update UI
                mRecyclerAdapter = new RecyclerAdapter(getApplicationContext(), strings, new RecyclerAdapter.OnViewClickListener() {
                    @Override
                    public void onViewClick(String tag) {
                    }
                });
                recyclerView.setAdapter(mRecyclerAdapter);
            }
        }
    }
}
