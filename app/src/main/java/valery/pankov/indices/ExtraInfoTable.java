package valery.pankov.indices;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Valery on 07.03.2016.
 */
public class ExtraInfoTable extends Fragment {
    public static String LOG_TAG = "my_log";

    public static ExtraInfoTable newInstance(int page, String title) {
       ExtraInfoTable fragmentEIT = new ExtraInfoTable();
       Bundle args = new Bundle();
       args.putInt("someInt", page);
       args.putString("someTitle", title);
       fragmentEIT.setArguments(args);
       return fragmentEIT;
   }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.extrainfotable, container, false);
        SharedPreferences bb = getActivity().getSharedPreferences("my_prefs", 0);

        SharedPreferences.OnSharedPreferenceChangeListener listener = new SharedPreferences.OnSharedPreferenceChangeListener(){
            public void onSharedPreferenceChanged(SharedPreferences bb, String key) {

                //String label = bb.getString("LABEL", "");
                //TextView compName = (TextView) getView().findViewById(R.id.compName);
                //compName.setText(label);
                new ParseTask().execute();
                Log.d("LOG_TAG","Работает");
            }
        };
        bb.registerOnSharedPreferenceChangeListener(listener);
        return view;
    }

    @Override
    public void onAttach(Activity MainActivity) {
        super.onAttach(MainActivity);
        Log.d(LOG_TAG, "ExtraInfoTable onAttach");
    }





    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Log.d(LOG_TAG, "ExtraInfoTable onActivityCreated");
    }

    private class ParseTask extends AsyncTask<Void, Void, String> {

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String resultJson = "";



        @Override
        protected String doInBackground(Void... params) {
            // получаем данные с внешнего ресурса

            try {
                SharedPreferences bb = getActivity().getSharedPreferences("my_prefs", 0);
                String label = bb.getString("LABEL", "");

                URL url = new URL("https://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20csv%20where%20url%3D%27http%3A%2F%2Fdownload.finance.yahoo.com%2Fd%2Fquotes.csv%3Fs%3D"+label+"%26f%3Dsnl1d1t1c1ohgv%26e%3D.csv%27%20and%20columns%3D%27symbol%2Cname%2Cprice%2Cdate%2Ctime%2Cchange%2Ccol1%2Chigh%2Clow%2Ccol2%27&format=json&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys\n");

                //URL url = new URL("http://androiddocs.ru/api/friends.json");

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();

                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }

                resultJson = buffer.toString();

            } catch (Exception e) {
                e.printStackTrace();
            }
            return resultJson;
        }


        @Override
        public void onPostExecute(String strJson) {
            super.onPostExecute(strJson);

            // выводим целиком полученную json-строку
            Log.d(LOG_TAG, strJson);

            JSONObject dataJsonObj = null;
            String strParsedValue = null;
            String firstSymb = "";

            try {
                JSONObject jsonObject = new JSONObject(strJson);
                JSONObject object = jsonObject.getJSONObject("query");
                String count = object.getString("count");
                String date = object.getString("created");
                String lang = object.getString("lang");
                JSONObject results = object.getJSONObject("results");
                JSONObject row = results.getJSONObject("row");

                String h_pr = row.getString("high").toString();
                String l_pr = row.getString("low").toString();
                String col1pr = row.getString("col1").toString();
                String name = row.getString("name").toString();



                TextView cmpName = (TextView) getView().findViewById(R.id.compName);
                cmpName.setText(name);

                TextView hpr = (TextView) getView().findViewById(R.id.hpr);
                hpr.setText(h_pr);
                //hpr.setTextColor(Color.GREEN);

                TextView lpr = (TextView) getView().findViewById(R.id.lpr);
                lpr.setText(l_pr);
                //lpr.setTextColor(Color.GREEN);

                TextView col1 = (TextView) getView().findViewById(R.id.fwhpr);
                col1.setText(col1pr);
                //col1.setTextColor(Color.GREEN);

                //Float f = Float.parseFloat(firstCompCh);
                //if(f>=0){
                //compChange.setTextColor(Color.GREEN);
                // }else{
                //    compChange.setTextColor(Color.RED);
                //}

                //Button btnget = (Button) findViewById(R.id.butGet);



            } catch (JSONException e) {

                e.printStackTrace();
            }
        }
    }

    public void onStart() {

        super.onStart();
        Log.d(LOG_TAG, "ExtraInfoTable onStart");
    }

    public void onResume() {
        new ParseTask().execute();
        super.onResume();
        Log.d(LOG_TAG, "ExtraInfoTable onResume");
    }

    public void onPause() {
        super.onPause();
        Log.d(LOG_TAG, "ExtraInfoTable onPause");
    }

    public void onStop() {
        super.onStop();
        Log.d(LOG_TAG, "ExtraInfo onStop");
    }

    public void onDestroyView() {
        super.onDestroyView();
        Log.d(LOG_TAG, "ExtraInfo onDestroyView");
    }

    public void onDestroy() {
        super.onDestroy();
        Log.d(LOG_TAG, "ExtraInfo onDestroy");
    }

    public void onDetach() {
        super.onDetach();
        Log.d(LOG_TAG, "ExtraInfo onDetach");
    }


}
