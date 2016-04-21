package valery.pankov.indices;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.ListFragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
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
import java.util.logging.Handler;

/**
 * Created by Valery on 05.03.2016.
 */
public class PrimaryInfo extends Fragment {
    final String LOG_TAG = "myLogs";
    private int mInterval = 1000;
    private Handler mHandler;
    private SwipeRefreshLayout swipeRefreshLayout;
    //private OnFragmentInteractionListener mListener;

    public PrimaryInfo(){

    }




    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(LOG_TAG, "PrimaryInfo onCreate");
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(LOG_TAG, "PrimaryInfo onCreateView");

        //View view = inflater.inflate(R.layout.primaryinfo_frag, container, false);
        return inflater.inflate(R.layout.primaryinfo_frag, null);
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //Button getst = (Button) getView().findViewById(R.id.getst);
        new ParseTask().execute();


        //getst.setOnClickListener(new View.OnClickListener(){
            //@Override
            //public void onClick(View view){
                //PrimaryInfo ldf = new PrimaryInfo ();

                //Bundle args = new Bundle();
                //args.putString("rowid", "value");
                //ldf.setArguments(args);

            //}
        //});
        Log.d(LOG_TAG, "PrimaryInfo onActivityCreated");
    }

    private class ParseTask extends AsyncTask<Void, Void, String> {

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String resultJson = "";
        int rid=1;


        @Override
        protected String doInBackground(Void... params) {
            // получаем данные с внешнего ресурса

            try {
                //String cmplst="ALL";
                String cmplst="YHOO%2CNFLX%2CFB%2CGOOG%2CINTC%2CTWTR%2CAMZN%2CLNKD%2CAAPL";
                URL url = new URL("https://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20csv%20where%20url%3D%27http%3A%2F%2Fdownload.finance.yahoo.com%2Fd%2Fquotes.csv%3Fs%3D"+cmplst+"%26f%3Dsl1d1t1c1ohgv%26e%3D.csv%27%20and%20columns%3D%27symbol%2Cprice%2Cdate%2Ctime%2Cchange%2Ccol1%2Chigh%2Clow%2Ccol2%27&format=json&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys\n");

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
                JSONArray row = results.getJSONArray("row");
                Log.d(LOG_TAG, "date: " + date);
                Log.d(LOG_TAG, "count: " + count);
                Log.d(LOG_TAG, "lang: " + lang);

                for (int i = 0; i < row.length(); i++) {
                    strParsedValue+="\n"+row.getJSONObject(i).getString("symbol").toString();


                }

                ArrayList<String> listsymb = new ArrayList<String>();
                for (int i = 0; i<row.length(); i++){
                    listsymb.add(row.getJSONObject(i).getString("symbol").toString());
                }
                //listsymb.add(row.getJSONObject(0).getString("symbol").toString());
                //listsymb.add(row.getJSONObject(1).getString("symbol").toString());
                //listsymb.add(row.getJSONObject(2).getString("symbol").toString());
                //listsymb.add(row.getJSONObject(3).getString("symbol").toString());
                //listsymb.add(row.getJSONObject(4).getString("symbol").toString());
                //listsymb.add(row.getJSONObject(5).getString("symbol").toString());
                //listsymb.add(row.getJSONObject(6).getString("symbol").toString());
                //listsymb.add(row.getJSONObject(7).getString("symbol").toString());
                //listsymb.add(row.getJSONObject(8).getString("symbol").toString());


                ArrayList<String> listcurpr = new ArrayList<String>();
                for (int i = 0; i<row.length(); i++){
                    listcurpr.add(row.getJSONObject(i).getString("price").toString());
                }


                ArrayList<String> listcurch = new ArrayList<String>();
                for (int i = 0; i<row.length(); i++){
                    listcurch.add(row.getJSONObject(i).getString("change").toString());
                }






                Log.d(LOG_TAG, "list: " + listcurch);


                //instantiate custom adapter
                MyCustomAdapter adapter = new MyCustomAdapter(listsymb, listcurpr, listcurch, getActivity());

                //handle listview and assign adapter
                ListView lView = (ListView) getView().findViewById(R.id.mainListView);
                lView.setAdapter(adapter);
                lView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapter, View view, final int position, long id) {
                        String value = (String) adapter.getItemAtPosition(position).toString();
                        long rowid = (long) adapter.getItemIdAtPosition(position);
                        final int rid = position;
                        //Here define all your sharedpreferences code with key and value
                        SharedPreferences prefs = getActivity().getSharedPreferences("my_prefs", Context.MODE_PRIVATE);
                        SharedPreferences.Editor edit = prefs.edit();
                        edit.putString("LABEL", value);
                        edit.apply();

                        String rid1 = String.valueOf(rid);


                        Log.d(LOG_TAG, "rowid1: " + rid1);
                        Log.d(LOG_TAG, "value1: " + value);

                    }


                });






                String firstCompName = row.getJSONObject(rid).getString("symbol").toString();
                final String firstCompPrice = row.getJSONObject(rid).getString("price").toString();
                final String firstCompCh = row.getJSONObject(rid).getString("change").toString();

                TextView compName = (TextView) getView().findViewById(R.id.company_name);
                //compName.setText(firstCompName);

                TextView compPrice = (TextView) getView().findViewById(R.id.cur_price);
                //compPrice.setText(firstCompPrice);

                TextView compChange = (TextView) getView().findViewById(R.id.cur_change);
                //compChange.setText(firstCompCh);

                //Float f = Float.parseFloat(firstCompCh);
                //if(f>=0){
                   //compChange.setTextColor(Color.GREEN);
               // }else{
                //    compChange.setTextColor(Color.RED);
                //}


                final String finalSecondName = firstSymb;
                View.OnClickListener oclbtnget = new View.OnClickListener(){
                    @Override
                    public void onClick(View v){

                        //final TextView cmpnm = (TextView) findViewById(R.id.company_name);
                        //final TextView indnm = (TextView) findViewById(R.id.ind_name);
                        // final TextView curch = (TextView) findViewById(R.id.cur_change);
                        //final Spinner spinner_ind = (Spinner) findViewById(R.id.indices);
                        //final Spinner spinner_comp=(Spinner) findViewById(R.id.companies);
                        //TextView curpr = (TextView) findViewById(R.id.cur_price);

                        //String txt_cmp = spinner_comp.getSelectedItem().toString();
                        //String txt_ind = spinner_ind.getSelectedItem().toString();
                        Float f = Float.parseFloat(firstCompCh);
                        if(f>=0){
                            //curch.setTextColor(Color.GREEN);
                        }else{
                            //curch.setTextColor(Color.RED);
                        }
                        //indnm.setText(txt_ind);
                        //cmpnm.setText(firstCompName);
                        //cmpnm.setTextColor(Color.RED);
                        //curch.setText(firstCompCh);
                        //curpr.setText(firstCompPrice);
                    }
                };
                //btnget.setOnClickListener(oclbtnget);
            } catch (JSONException e) {

                e.printStackTrace();
            }
        }
    }

    @Override
    public void onAttach(Activity MainActivity) {
        super.onAttach(MainActivity);
        Log.d(LOG_TAG, "PrimaryInfo onAttach");
        //try {
        // mListener = (OnFragmentInteractionListener)MainActivity;
        //} catch (ClassCastException e) {
        //   throw new ClassCastException(MainActivity.toString()
        //         + " must implement OnFragmentInteractionListener");
        //}
    }

    public void onStart() {
        super.onStart();
        Log.d(LOG_TAG, "PrimaryInfo onStart");
    }

    public void onResume() {
        super.onResume();
        Log.d(LOG_TAG, "PrimaryInfo onResume");
    }



    public void onPause() {
        super.onPause();
        Log.d(LOG_TAG, "PrimaryInfo onPause");
    }

    public void onStop() {
        super.onStop();
        Log.d(LOG_TAG, "PrimaryInfo onStop");
    }

    public void onDestroyView() {
        super.onDestroyView();
        Log.d(LOG_TAG, "PrimaryInfo onDestroyView");
    }

    public void onDestroy() {
        super.onDestroy();
        Log.d(LOG_TAG, "PrimaryInfo onDestroy");
    }

    public void onDetach() {
        super.onDetach();
        //mListener=null;
        Log.d(LOG_TAG, "PrimaryInfo onDetach");
    }

    //public interface OnFragmentInteractionListener{
   //     public void onFragmentInteraction(int rowid);
    //}
}
