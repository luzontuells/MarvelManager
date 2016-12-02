package luzontuells.marvelmanager.activities;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import android.widget.Toast;

import java.util.ArrayList;
import luzontuells.marvelmanager.R;
import luzontuells.marvelmanager.data.Item;


public class FirstActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {


    private static final String TAG_FIRST_ACTIVITY = "First Activity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);

        final ListView mListView = (ListView) this.findViewById(R.id.list_view_first);
        mListView.setOnItemClickListener(this);

        // This 'String[]' data could be retrieved from a file, for instance
        String[] mListValues = {"test"};

        //TODO: Obtain Name values from Marvel API

        // Using a custom adapter for the 'ListView' -----------------------------------------------
        // 'ArrayList' with image references (DO NOT INCLUDE FILE EXTENSIONS!)
        String[] mListImages = {"androidicon"};

        //TODO: Obtain Image Values from Marvel API


        String[] mListBodies = {"androidicon"};

        //TODO: Obtain Body Values from Marvel API

        ArrayList<Item> mListArray = new ArrayList<>();
        // Just populating the 'ArrayList' with the 'Item[]'
        for (int idx = 0; idx < mListValues.length; idx++)
            mListArray.add(new Item(mListImages[idx], mListValues[idx], mListBodies[idx]));

        mListView.setAdapter(new MyListAdapter(this, 0, mListArray));
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        //TODO: When clicking one element, launch an activity with the needed values of the hero
//        Intent intent = new Intent(this,SecondActivity.class);
//        startActivity(intent);

        //    {
        // 'ConnectivityManager' answers queries about the state of network connectivity
        ConnectivityManager mConnectionManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        // 'NetworkInfo' describes the status of a network interface
        NetworkInfo mNetworkInfo = mConnectionManager.getActiveNetworkInfo();
        Log.i(FirstActivity.TAG_FIRST_ACTIVITY, mNetworkInfo.getTypeName() + " " + mNetworkInfo.getState().toString());

        // 'isConnected()' handles cases like flaky mobile networks, airplane mode, and restricted background data
        if (mNetworkInfo != null && mNetworkInfo.isConnected())
        {

        }
        else
        {
            Log.i(FirstActivity.TAG_FIRST_ACTIVITY, "Connection  not available");
            Toast.makeText(this, "Connection  not available", Toast.LENGTH_SHORT).show();
        }
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
}



//    @Override
//    public void onClick(View whichView)
//    {
//        // 'ConnectivityManager' answers queries about the state of network connectivity
//        ConnectivityManager mConnectionManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
//        // 'NetworkInfo' describes the status of a network interface
//        NetworkInfo mNetworkInfo = mConnectionManager.getActiveNetworkInfo();
//        Log.i(MainActivity.TAG_FIRST_ACTIVITY, mNetworkInfo.getTypeName() + " " + mNetworkInfo.getState().toString());
//
//        // 'isConnected()' handles cases like flaky mobile networks, airplane mode, and restricted background data
//        if (mNetworkInfo != null && mNetworkInfo.isConnected())
//        {
//            if (whichView.getId() == R.id.imgBtnDownload)
//            {
//                // Do whatever you need
//                final String urlString = "http://www.tutorialspoint.com/green/images/logo.png";
//                // Querying the 'URL' from an alternative thread
//                new MyAlternativeThread().execute(urlString);
//            }
//            else if (whichView.getId() == R.id.btnReadFeed)
//            {
//                // The feed to be accessed
//                final String urlFeed = "http://www.theguardian.com/international/rss";
//                //final String urlFeed = "http://www.revistalatahona.com/feed";
//                // Querying the feed 'URL' from an alternative thread
//                new MyParsingThread(this).execute(urlFeed);
//            }
//        }
//        else
//        {
//            Log.i(MainActivity.TAG_FIRST_ACTIVITY, "Connection  not available");
//            Toast.makeText(this, "Connection  not available", Toast.LENGTH_SHORT).show();
//        }
//    }
//
//// AsyncTask<TypeOfVarArgParams, ProgressValue, ResultValue>
//private class MyAlternativeThread extends AsyncTask<String, Void, Bitmap>
//{
//    @Override
//    protected Bitmap doInBackground(String[] params)
//    {
//        try
//        {
//            // The input arguments are fetched in order
//            URL myUrl = new URL(params[0]);   // Throws 'MalformedURLException'
//            HttpURLConnection myConnection = (HttpURLConnection) myUrl.openConnection();   // Throws 'IOException'
//            myConnection.setRequestMethod("GET");
//            myConnection.setDoInput(true);
//            // Starting the query
//            myConnection.connect();   // Throws 'IOException'
//            int respCode = myConnection.getResponseCode();   // Throws 'IOException'
//            Log.i(MainActivity.TAG_FIRST_ACTIVITY, "The response is: " + respCode);
//            if (respCode == HttpURLConnection.HTTP_OK)
//            {
//                InputStream myInStream = myConnection.getInputStream();   // Throws 'IOException'
//
//                Bitmap myBitmap = BitmapFactory.decodeStream(myInStream);
//
//                myInStream.close();   // Always close the 'InputStream'
//
//                return myBitmap;
//            }
//        }
//        catch (MalformedURLException e1) { e1.printStackTrace(); }
//        catch (IOException e2) { e2.printStackTrace(); }
//
//        return null;
//    }
//
//    @Override
//    protected void onPostExecute(Bitmap bitmap)
//    {
//        super.onPostExecute(bitmap);
//
//        if (bitmap != null)
//            mImageView.setImageBitmap(bitmap);
//        else
//            Log.i(MainActivity.TAG_FIRST_ACTIVITY, "No image to be loaded");
//    }
//}
//
//private class MyParsingThread extends AsyncTask<String, Void, String>
//{
//    private Context threadContext;
//
//    public MyParsingThread(Context mContext) { this.threadContext = mContext; }
//
//    @Override
//    protected String doInBackground(String[] params)
//    {
//        try
//        {
//            // The input arguments are fetched in order
//            URL myUrl = new URL(params[0]);   // Throws 'MalformedURLException'
//            HttpURLConnection myConnection = (HttpURLConnection) myUrl.openConnection();   // Throws 'IOException'
//            myConnection.setRequestMethod("GET");
//            myConnection.setDoInput(true);
//            // Starting the query
//            myConnection.connect();   // Throws 'IOException'
//            int respCode = myConnection.getResponseCode();   // Throws 'IOException'
//            Log.i(MainActivity.TAG_FIRST_ACTIVITY, "The response is: " + respCode);
//            if (respCode == HttpURLConnection.HTTP_OK)
//            {
//                InputStream myInStream = myConnection.getInputStream();   // Throws 'IOException'
//
//                XmlPullParser myXmlParser = Xml.newPullParser();
//                //myXmlParser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);   // Throws 'XmlPullParserException'
//                myXmlParser.setInput(myInStream, null);   // Throws 'XmlPullParserException'
//
//                String fullTitleString = "";
//                int event = myXmlParser.nextTag();
//                // This parsing process is ONLY ensured to be valid for the specific feed accessed
//                while (myXmlParser.getEventType() != XmlPullParser.END_DOCUMENT)
//                {
//                    switch (event)
//                    {
//                        case XmlPullParser.START_TAG:
//                            if (myXmlParser.getName().equals("item"))
//                            {
//                                myXmlParser.nextTag();
//                                myXmlParser.next();
//                                fullTitleString += "- " + myXmlParser.getText() + "\n";
//                            }
//                            break;
//                    }
//
//                    event = myXmlParser.next();
//                }
//                Log.i(MainActivity.TAG_FIRST_ACTIVITY, fullTitleString);
//
//                myInStream.close();   // Always close the 'InputStream'
//
//                return fullTitleString;
//            }
//        }
//        catch (MalformedURLException e1) { e1.printStackTrace(); }
//        catch (IOException e2) { e2.printStackTrace(); }
//        catch (XmlPullParserException e3) { e3.printStackTrace(); }
//
//        return null;
//    }
//
//    @Override
//    protected void onPostExecute(String inString)
//    {
//        super.onPostExecute(inString);
//
//        if (inString != null)
//            Toast.makeText(this.threadContext, inString, Toast.LENGTH_LONG).show();
//        else
//            Log.i(MainActivity.TAG_FIRST_ACTIVITY, "No feed to be loaded");
//    }
//}







