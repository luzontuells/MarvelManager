package luzontuells.marvelmanager.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

import com.squareup.picasso.Picasso;

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
    private static final String JSON_URL = "http://gateway.marvel.com/v1/public/characters?ts=1&apikey=94f4341859283f334a8e1316d7b12e42&hash=aca24562b84ef49172856f5e28d1f95a&limit=100"; //&limit=100

    private ArrayList<String> mCharId = new ArrayList<>();
    private ArrayList<String> mNames = new ArrayList<>();
    private ArrayList<String> array_sort = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);

        final ListView listView = (ListView) this.findViewById(R.id.list_view_first);
        listView.setOnItemClickListener(this);

//        final RecyclerView mRecyclerView = (RecyclerView) this.findViewById(R.id.list_recycler_view);


        final EditText searchEditText = (EditText) findViewById(R.id.name_edit_text);
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                //TODO: ARREGLAR!
                /*
                int textlength = searchEditText.getText().length();
                array_sort.clear();
                for (String element: mNames)
                {
                    if (textlength <= element.length())
                    {
                        if (searchEditText.getText().toString().equalsIgnoreCase(
                                (String)
                                        element.subSequence(0,
                                                textlength)))
                        {
                            array_sort.add(element);
                        }
                    }
                }
//                lv.setAdapter(new ArrayAdapter<String>
//                        (MainActivity.this,
//                                android.R.layout.simple_list_item_1, array_sort));
                ArrayList<Item> mListArray = new ArrayList<>();
                mListArray.add(new Item(bmp,
                        nameString,
                        fields.getString("description"),
                        idString));
                mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
                mRecyclerView.setAdapter(new MyListAdapterRecycler(this,array_sort));
                */
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() == 0) {
                    //TODO: Busqueda

                }
            }
        });

        JSONObject json;
//        Bitmap bmp;
        try {
            json = new JSONObtainThread().execute(JSON_URL).get();
            if (json == null)
                Log.e(FirstActivity.TAG_FIRST_ACTIVITY, "Not read");
            JSONObject data = json.getJSONObject("data");
            JSONArray results = data.getJSONArray("results");

            ArrayList<Item> mListArray = new ArrayList<>();

            for (int i = 0; i < results.length(); i++) {
                JSONObject fields = results.getJSONObject(i);

//                bmp = new ImageObtainThread().execute(fields.getJSONObject("thumbnail").getString("path")
//                        + "." + fields.getJSONObject("thumbnail").getString("extension")).get();

                String imageString = fields.getJSONObject("thumbnail").getString("path")+ "." + fields.getJSONObject("thumbnail").getString("extension");

                String nameString = fields.getString("name");
                this.mNames.add(nameString);

                String idString = fields.getString("id");
                this.mCharId.add(idString);



                mListArray.add(new Item(imageString,
                        nameString,
                        fields.getString("description"),
                        idString));

            }
            listView.setAdapter(new MyListAdapter(this, 0, mListArray));
//            mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
//            mRecyclerView.setAdapter(new MyListAdapterRecycler(this,mListArray));

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
        //TODO: Arreglar lo del Click
        Intent mIntent = new Intent(this, SecondActivity.class);
        Bundle bundle = new Bundle();
        Log.e(FirstActivity.TAG_FIRST_ACTIVITY, "Position " + Integer.toString(position));
        bundle.putString("char_id", this.mCharId.get(position));
        mIntent.putExtras(bundle);
        this.startActivity(mIntent);
        finish();
    }

//    @Override
//    public void onClick(View view) {
//
//    }






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
//            mViewHolder.icon_ImgView.setImageBitmap(this.itemList.get(position).getmImage());
            mViewHolder.title_TxtView.setText(this.itemList.get(position).getmName());
            mViewHolder.body_TxtView.setText(this.itemList.get(position).getmBody());
            Picasso.with(mContext)
                    .load(this.itemList.get(position).getmImage())
                    .into(mViewHolder.icon_ImgView);

            // To check that views are loaded only when they have to be shown
            //Log.i(MainActivity.TAG_FIRST_ACTIVITY, String.valueOf(this.mContext.getResources().getIdentifier(this.itemList.get(position).getmImage(), "drawable", this.mContext.getPackageName())));

            return convertView;
        }
    }




//
//
//    // This adapter uses the 'RecyclerView.Adapter' for the 'RecyclerView' (which is an optimized 'ListView')
//    private class MyListAdapterRecycler extends RecyclerView.Adapter<MyListAdapterRecycler.ViewHolder>
//    {
//        // Creating a 'ViewHolder' to speed up the performance
//        public class ViewHolder extends RecyclerView.ViewHolder
//        {
//            public ImageView icon_ImgView;
//            public TextView title_TxtView;
//            public TextView body_TxtView;
//
//            public ViewHolder(View itemView)
//            {
//                super(itemView);
//
//                this.icon_ImgView = (ImageView) itemView.findViewById(R.id.icon_item);
//                this.title_TxtView = (TextView) itemView.findViewById(R.id.item_name);
//                this.body_TxtView = (TextView) itemView.findViewById(R.id.item_body);
//            }
//        }
//
//        Context mContext;
//        ArrayList<Item> itemList;
//
//        // Provide a suitable constructor (depends on the kind of dataset)
//        public MyListAdapterRecycler(Context context, ArrayList<Item> objects)
//        {
//            this.mContext = context;
//            this.itemList = objects;
//        }
//
//        // Create new views (invoked by the layout manager)
//        @Override
//        public ViewHolder onCreateViewHolder(ViewGroup parent, final int viewType)
//        {
//            // create a new view
//            View viewRow = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
//            viewRow.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                }
//            });
//            // set the view's size, margins, paddings and layout parameters
//            ViewHolder viewRowHolder = new ViewHolder(viewRow);
//            return viewRowHolder;
//        }
//
//        // Replace the contents of a view (invoked by the layout manager)
//        @Override
//        public void onBindViewHolder(ViewHolder holder, int position)
//        {
//            // get element from your dataset at this position
//            // replace the contents of the view with that element
////            holder.icon_ImgView.setImageBitmap(this.itemList.get(position).getmImage());
//            holder.title_TxtView.setText(this.itemList.get(position).getmName());
//            holder.body_TxtView.setText(this.itemList.get(position).getmBody());
//
//            Picasso.with(mContext)
//                    .load(this.itemList.get(position).getmImage())
//                    .into(holder.icon_ImgView);
//        }
//
//        // Return the size of your dataset (invoked by the layout manager)
//        @Override
//        public int getItemCount()
//        {
//            return this.itemList.size();
//        }
//    }
//
//
//
//







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
