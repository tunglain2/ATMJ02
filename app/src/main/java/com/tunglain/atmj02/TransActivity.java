package com.tunglain.atmj02;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class TransActivity extends AppCompatActivity {

    private static final String TAG = TransActivity.class.getSimpleName();
    private List<Transaction> trans;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trans);

        recyclerView = findViewById(R.id.recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

//        new TransTask().execute("http://atm201605.appspot.com/h");

        okHttp3();

    }

    private void okHttp3() {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("http://atm201605.appspot.com/h")
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                final String json = response.body().string();
                Log.d(TAG, "onResponse: "+ json);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
//                        parsJson(json);
                        parsGson(json);
                    }
                });

            }
        });
    }

    private void parsGson(String json) {
        Gson gson = new Gson();
        trans = gson.fromJson(json,new TypeToken<ArrayList<Transaction>>(){}.getType());
        TransAdapter adapter = new TransAdapter();
        recyclerView.setAdapter(adapter);
    }

    private void parsJson(String json) {
        trans = new ArrayList<>();
        try {
            JSONArray array = new JSONArray(json);
            for (int i = 0; i < array.length(); i++) {
                JSONObject jsonObject = array.getJSONObject(i);
                Transaction transaction = new Transaction(jsonObject);
                trans.add(transaction);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        TransAdapter adapter = new TransAdapter();
        recyclerView.setAdapter(adapter);

    }

    public class TransAdapter extends RecyclerView.Adapter<TransAdapter.TransHolder>{
        @NonNull
        @Override
        public TransHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(R.layout.item_transaction,parent,false);
            return new TransHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull TransHolder holder, int position) {
            Transaction transaction = trans.get(position);
            holder.toBind(transaction);
        }

        @Override
        public int getItemCount() {
            return trans.size();
        }

        public class TransHolder extends RecyclerView.ViewHolder {

            TextView dateText;
            TextView amountText;
            TextView typeText;
            public TransHolder(@NonNull View itemView) {
                super(itemView);
                dateText = itemView.findViewById(R.id.item_date);
                amountText = itemView.findViewById(R.id.item_amount);
                typeText = itemView.findViewById(R.id.item_type);
            }

            public void toBind(Transaction transaction) {
                dateText.setText(transaction.getDate());
                amountText.setText(String.valueOf(transaction.getAmount()));
                typeText.setText(String.valueOf(transaction.getType()));
            }
        }
    }

    public class TransTask extends AsyncTask<String,Void,String> {

        @Override
        protected String doInBackground(String... strings) {
            StringBuilder sb = new StringBuilder();
            try {
                URL url = new URL(strings[0]);
                InputStream is = url.openStream();
                BufferedReader in = new BufferedReader(new InputStreamReader(is));

                String line = in.readLine();
                while (line != null) {
                    sb.append(line);
                    line = in.readLine();
                }
                Log.d(TAG, "doInBackground: " + sb.toString());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return sb.toString();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d(TAG, "onPostExecute: " + s.toString());
            Toast.makeText(TransActivity.this, "test", Toast.LENGTH_SHORT).show();
        }
    }

}
