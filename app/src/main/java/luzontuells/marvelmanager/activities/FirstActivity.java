package luzontuells.marvelmanager.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
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


    private static final String TAG_FIRST_ACTIVITY = FirstActivity.class.getSimpleName();
    private static final String JSON_URL = "http://gateway.marvel.com/v1/public/characters?ts=1&apikey=94f4341859283f334a8e1316d7b12e42&hash=aca24562b84ef49172856f5e28d1f95a";

    private ArrayList<String> mCharId = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);

        final ListView mListView = (ListView) this.findViewById(R.id.list_view_first);
        mListView.setOnItemClickListener(this);

        final EditText searchEditText = (EditText) findViewById(R.id.name_edit_text);
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() == 0) {
                    //TODO: Busqueda
                }
            }
        });

        JSONObject json;
        Bitmap bmp;
        try {
            json = new JSONObtainThread().execute(JSON_URL).get();
            if (json == null)
                Log.e(FirstActivity.TAG_FIRST_ACTIVITY, "Not read");
            JSONObject data = json.getJSONObject("data");
            JSONArray results = data.getJSONArray("results");

            ArrayList<Item> mListArray = new ArrayList<>();

            for (int i = 0; i < results.length(); i++) {
                JSONObject fields = results.getJSONObject(i);

                bmp = new ImageObtainThread().execute(fields.getJSONObject("thumbnail").getString("path")
                        + "." + fields.getJSONObject("thumbnail").getString("extension")).get();


//                if (bmp == null){
//                    Log.e(FirstActivity.TAG_FIRST_ACTIVITY, "No images loaded");
//                }


                String idString = fields.getString("id");

                this.mCharId.add(idString);


                mListArray.add(new Item(bmp,
                        fields.getString("name"),
                        fields.getString("description"),
                        idString));

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
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        Intent mIntent = new Intent(this, SecondActivity.class);
        Bundle bundle = new Bundle();
        Log.e(FirstActivity.TAG_FIRST_ACTIVITY, "Position " + Integer.toString(position));
        bundle.putString("char_id", this.mCharId.get(position));
        mIntent.putExtras(bundle);
        this.startActivity(mIntent);
        finish();
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
//            mViewHolder.icon_ImgView.setImageResource(this.mContext.getResources().getIdentifier(this.itemList.get(position).getmImage(), "drawable", this.mContext.getPackageName()));
            mViewHolder.icon_ImgView.setImageBitmap(this.itemList.get(position).getmImage());
            mViewHolder.title_TxtView.setText(this.itemList.get(position).getmName());
            mViewHolder.body_TxtView.setText(this.itemList.get(position).getmBody());

            // To check that views are loaded only when they have to be shown
            //Log.i(MainActivity.TAG_FIRST_ACTIVITY, String.valueOf(this.mContext.getResources().getIdentifier(this.itemList.get(position).getmImage(), "drawable", this.mContext.getPackageName())));

            return convertView;
        }
    }

    private class JSONObtainThread extends AsyncTask<String, Void, JSONObject> {

        @Override
        protected JSONObject doInBackground(String[] params) {
            try {
                URL myUrl = new URL(params[0]);
                HttpURLConnection myConnection = (HttpURLConnection) myUrl.openConnection();
                myConnection.setRequestMethod("GET");
                myConnection.setDoInput(true);

                myConnection.connect();

                int respCode = myConnection.getResponseCode();

                if (respCode == HttpURLConnection.HTTP_OK)
//                    Log.e(FirstActivity.TAG_FIRST_ACTIVITY, "HTTP_OK");
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


    private class ImageObtainThread extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(String[] params) {
            try {
                URL myUrl = new URL(params[0]);
                HttpURLConnection myConnection = (HttpURLConnection) myUrl.openConnection();
                myConnection.setRequestMethod("GET");
                myConnection.setDoInput(true);

                myConnection.connect();

                int respCode = myConnection.getResponseCode();

                if (respCode == HttpURLConnection.HTTP_OK) {
                    InputStream myInStream = myConnection.getInputStream();   // Throws 'IOException'

                    Bitmap myBitmap = BitmapFactory.decodeStream(myInStream);

                    myInStream.close();   // Always close the 'InputStream'

                    if (myBitmap == null)
                        Log.e(FirstActivity.TAG_FIRST_ACTIVITY, "No images in Thread");

                    return myBitmap;
                }
            } catch (MalformedURLException e1) {
                e1.printStackTrace();
            } catch (IOException e2) {
                e2.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
//            if (bitmap != null)
//                mImageView.setImageBitmap(bitmap);
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
