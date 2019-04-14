package com.example.stackoverflow;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class UserInterestActivity extends AppCompatActivity {

    ArrayList<String> userTagList = new ArrayList<>();
    RecyclerView recyclerView;
    LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_interest);
        HttpRequestTag httpRequestTag = new HttpRequestTag();
        httpRequestTag.execute();

        //RecyclerView
        recyclerView = (RecyclerView) findViewById(R.id.user_interest_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //Linear layout
        linearLayout = findViewById(R.id.user_interest_linear_layout);

        //submit button
        Button submitButton= findViewById(R.id.submit_tags);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(userTagList.size()!=4){
                    Toast.makeText(getApplicationContext(), "Select 4 tags", Toast.LENGTH_SHORT).show();
                }else{
                    Intent i = new Intent(getApplicationContext(),QuestionListActivity.class);
                    i.putExtra("array_list",userTagList);
                    startActivity(i);
                }
            }
        });
    }

    public class HttpRequestTag extends AsyncTask<Void, Integer, ArrayList<String>> {

        @Override
        protected ArrayList<String> doInBackground(Void... voids) {
            HttpHandler httpHandler = new HttpHandler();
            try {
                ArrayList<String> list_tags = httpHandler.getTags();
                return list_tags;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(ArrayList<String> strings) {
            super.onPostExecute(strings);
            if (strings == null)
                Toast.makeText(getApplicationContext(), "Error fetching Tags", Toast.LENGTH_SHORT).show();
            else {
                //update UI
                recyclerView.setAdapter(new RecyclerAdapter(getApplicationContext(), strings, new RecyclerAdapter.OnViewClickListener() {
                    @Override
                    public void onViewClick(String tag) {
                        if (userTagList.size() < 4 && !userTagList.contains(tag)) {
                            userTagList.add(tag);

                            final TextView textView = new TextView(getApplicationContext());
                            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                            params.setMargins(10, 10, 10, 20);
                            textView.setLayoutParams(params);
                            textView.setBackgroundResource(R.drawable.round_shape);
                            textView.setPadding(20, 10, 20, 10);
                            textView.setTextSize(20);
                            textView.setTextColor(getResources().getColor(R.color.white));
                            textView.setText(tag+" x");
                            textView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    String tag =textView.getText().toString().split(" ")[0];
                                    if (userTagList.contains(tag)){
                                        userTagList.remove(tag);
                                        view.setVisibility(View.GONE);
                                    }
                                }
                            });
                            if (linearLayout != null) {
                                linearLayout.addView(textView);
                            }

                        } else {
                            Toast.makeText(getApplicationContext(), "Can't add more tags", Toast.LENGTH_SHORT).show();
                        }
                    }
                }));
            }
        }
    }

}
