package luzontuells.marvelmanager.activities;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import luzontuells.marvelmanager.R;
import luzontuells.marvelmanager.data.Item;


public class FirstActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {


    private static final String TAG_FIRST_ACTIVITY = "First Activity";
    private static final String JSON_URL = "http://gateway.marvel.com/v1/public/characters?ts=1&apikey=94f4341859283f334a8e1316d7b12e42&hash=aca24562b84ef49172856f5e28d1f95a";

    private String[] mListNames;
    private String[] mListDescriptors;
    private String[] mListImages;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);

        final ListView mListView = (ListView) this.findViewById(R.id.list_view_first);
        mListView.setOnItemClickListener(this);

        JSONObject json = null;
        try {
            json = new MyAlternativeThread().execute(JSON_URL).get();
            JSONObject data = json.getJSONObject("data");
            JSONArray results = data.getJSONArray("results");
//                    Log.e(FirstActivity.TAG_FIRST_ACTIVITY, "We obtain: "+json2.get("results"));
            ArrayList<Item> mListArray = new ArrayList<>();
            String[] names = null, images = null, descriptions = null;
            for (int i = 0; i < results.length(); i++) {
                JSONObject c = results.getJSONObject(i);

                //TODO: De las URL a Imágenes!
                mListArray.add(new Item(c.getJSONObject("thumbnail").getString("path"),
                        c.getString("name"), c.getString("description")));
            }
            mListView.setAdapter(new MyListAdapter(this, 0, mListArray));


        } catch (JSONException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        //TODO: When clicking one element, launch an activity with the needed values of the hero
//        Intent intent = new Intent(this,SecondActivity.class);
//        startActivity(intent);


//        //    {
//        // 'ConnectivityManager' answers queries about the state of network connectivity
//        ConnectivityManager mConnectionManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
//        // 'NetworkInfo' describes the status of a network interface
//        NetworkInfo mNetworkInfo = mConnectionManager.getActiveNetworkInfo();
//        Log.i(FirstActivity.TAG_FIRST_ACTIVITY, mNetworkInfo.getTypeName() + " " + mNetworkInfo.getState().toString());
//
//        // 'isConnected()' handles cases like flaky mobile networks, airplane mode, and restricted background data
//        if (mNetworkInfo != null && mNetworkInfo.isConnected())
//        {
//
//        }
//        else
//        {
//            Log.i(FirstActivity.TAG_FIRST_ACTIVITY, "Connection  not available");
//            Toast.makeText(this, "Connection  not available", Toast.LENGTH_SHORT).show();
//        }
    }


    private class MyListAdapter extends ArrayAdapter<Item> {
        // Creating a ViewHolder to speed up the performance
        private class ViewHolder {
            public ImageView icon_ImgView;
            public TextView title_TxtView;
            public TextView body_TxtView;
        }

        Context mContext;
        ArrayList<Item> itemList;

        public MyListAdapter(Context context, int resource, ArrayList<Item> objects) {
            super(context, resource, objects);

            this.mContext = context;
            this.itemList = objects;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // 'convertView' represents the old view to be reused
            // It is convenient to check whether it is non-null or of an appropriate type before using it
            ViewHolder mViewHolder;
            if (convertView == null) {
                LayoutInflater mInflater = (LayoutInflater) this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = mInflater.inflate(R.layout.list_item, null);

                // Configure a 'ViewHolder'
                mViewHolder = new ViewHolder();
                mViewHolder.icon_ImgView = (ImageView) convertView.findViewById(R.id.icon_item);
                mViewHolder.title_TxtView = (TextView) convertView.findViewById(R.id.item_name);
                mViewHolder.body_TxtView = (TextView) convertView.findViewById(R.id.item_body);
                convertView.setTag(mViewHolder);
            } else
                mViewHolder = (ViewHolder) convertView.getTag();

            // Once we are sure the 'ViewHolder' object is attach to 'convertView', we can populate the view
            mViewHolder.icon_ImgView.setImageResource(this.mContext.getResources().getIdentifier(this.itemList.get(position).getmImageRef(), "drawable", this.mContext.getPackageName()));
            mViewHolder.title_TxtView.setText(this.itemList.get(position).getmTitle());
            mViewHolder.body_TxtView.setText(this.itemList.get(position).getmBody());

            // To check that views are loaded only when they have to be shown
            //Log.i(MainActivity.TAG_FIRST_ACTIVITY, String.valueOf(this.mContext.getResources().getIdentifier(this.itemList.get(position).getmImageRef(), "drawable", this.mContext.getPackageName())));

            return convertView;
        }
    }

    private class MyAlternativeThread extends AsyncTask<String, Void, JSONObject> {

        @Override
        protected JSONObject doInBackground(String[] params) {
            try {
                // The input arguments are fetched in order
                URL myUrl = new URL(params[0]);   // Throws 'MalformedURLException'
                HttpURLConnection myConnection = (HttpURLConnection) myUrl.openConnection();   // Throws 'IOException'
                myConnection.setRequestMethod("GET");
                myConnection.setDoInput(true);
                // Starting the query
                myConnection.connect();   // Throws 'IOException'
                int respCode = myConnection.getResponseCode();   // Throws 'IOException'
                Log.e(FirstActivity.TAG_FIRST_ACTIVITY, "The response is: " + respCode);
                if (respCode == HttpURLConnection.HTTP_OK)
                    Log.e(FirstActivity.TAG_FIRST_ACTIVITY, "HTTP_OK");
                {
                    JSONObject json = JSONReader.readJsonFromUrl(JSON_URL);
                    return json;
                }
            } catch (MalformedURLException e1) {
                e1.printStackTrace();
            } catch (IOException e2) {
                e2.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(JSONObject json) {
            super.onPostExecute(json);
        }
    }

    public static class JSONReader {

        private static String readAll(Reader rd) throws IOException {
            StringBuilder sb = new StringBuilder();
            int cp;
            while ((cp = rd.read()) != -1) {
                sb.append((char) cp);
            }
            return sb.toString();
        }

        public static JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
            InputStream is = new URL(url).openStream();
            try {
                BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
                String jsonText = readAll(rd);
                JSONObject json = new JSONObject(jsonText);
                return json;
            } finally {
                is.close();
            }
        }
    }
}
