package com.meghpy.mytodoapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.shimmer.ShimmerFrameLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    String todo,completed,userId;
    private GridView gridView;
    private HashMap<String,String> hashMap;
    private ArrayList<HashMap <String,String>> arrayList = new ArrayList<>();
    private ShimmerFrameLayout shimmerFrameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gridView = findViewById(R.id.gridView);
        data();
    }

    //===================================
    private void data(){
        shimmerFrameLayout = findViewById(R.id.shimmerEffect);
        gridView.setVisibility(View.INVISIBLE);

        String url ="https://dummyjson.com/todos";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {

                    JSONArray jsonArray = response.getJSONArray("todos");
                    for (int i=0; i<jsonArray.length(); i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        userId = jsonObject.getString("userId");
                        todo = jsonObject.getString("todo");
                        completed = jsonObject.getString("completed");



                        hashMap = new HashMap<>();
                        hashMap.put("userId",userId);
                        hashMap.put("todo", todo);
                        hashMap.put("completed", completed);

                        arrayList.add(hashMap);


                    }
                    MyAdapter myAdapter = new MyAdapter();
                    gridView.setAdapter(myAdapter);
                    shimmerFrameLayout.stopShimmer();
                    shimmerFrameLayout.setVisibility(View.GONE);
                    gridView.setVisibility(View.VISIBLE);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        requestQueue.add(jsonObjectRequest);

    }
    //===================================

    public class MyAdapter extends BaseAdapter{
        @Override
        public int getCount() {
            return arrayList.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @SuppressLint("ResourceAsColor")
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View myView = inflater.inflate(R.layout.design,parent,false);


            CardView layItem = myView.findViewById(R.id.layItem);
            TextView todos = myView.findViewById(R.id.todo);
            TextView completeds = myView.findViewById(R.id.completed);
            TextView userIds = myView.findViewById(R.id.userId);

            todos.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                        clipboard.setText(todo);
                        Toast.makeText(MainActivity.this, "Copied", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(MainActivity.this, "Can't copy", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            HashMap<String,String> hashMap = arrayList.get(position);
             String todo = hashMap.get("todo");
             String completed = hashMap.get("completed");
             String userId = hashMap.get("userId");

            Random rnd = new Random();
            int color = Color.argb(100, rnd.nextInt(280), rnd.nextInt(256), rnd.nextInt(300));
            layItem.setBackgroundColor(color);

            todos.setText(todo);
            userIds.setText(userId);

            if (completed.contains("true")){
                completeds.setText("Completed");
            }else {
                completeds.setText("Incomplete");
                completeds.setTextColor(R.color.black);
            }


            return myView;
        }
    }
    //===================================
}