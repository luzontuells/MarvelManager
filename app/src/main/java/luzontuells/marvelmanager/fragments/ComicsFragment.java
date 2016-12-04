package luzontuells.marvelmanager.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import luzontuells.marvelmanager.R;
import luzontuells.marvelmanager.activities.SecondActivity;
import luzontuells.marvelmanager.data.Item;
import luzontuells.marvelmanager.data.JSONManager;


public class ComicsFragment extends Fragment implements AdapterView.OnItemClickListener {

    private String jsonUrlComics,id;
    private ArrayList<Item> mListArrayComic = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ListView listView = (ListView) inflater.inflate(
                R.layout.list_view, container, false);

        this.id = getArguments().getString("char_id");

        this.jsonUrlComics = "http://gateway.marvel.com:80/v1/public/characters/" + id + "/comics?ts=1&apikey=94f4341859283f334a8e1316d7b12e42&hash=aca24562b84ef49172856f5e28d1f95a&limit=100"; //&limit=100



        setupDataFromJson(this.jsonUrlComics);


        listView.setOnItemClickListener(this);
        listView.setAdapter(new MyListAdapter(this.getContext(), 0, this.mListArrayComic));


        return listView;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

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





    public void setupDataFromJson(String jsonUrl) {
        JSONObject json;
        try {
            json = new JSONManager.JSONObtainThread().execute(jsonUrl).get();
//            JSONObject data = json.getJSONObject("data");

            if (jsonUrl == this.jsonUrlComics) {

                JSONObject data = json.getJSONObject("data");
                JSONArray results = data.getJSONArray("results");


                for (int i = 0; i < results.length(); i++) {

                    JSONObject fields = results.getJSONObject(i);

                    String imageString = fields.getJSONObject("thumbnail").getString("path") + "." + fields.getJSONObject("thumbnail").getString("extension");
                    String nameString = fields.getString("name");
                    String descriptionString = fields.getString("description");
                    String idString = fields.getString("id");


                    this.mListArrayComic.add(new Item(imageString,
                            nameString,
                            descriptionString,
                            idString));

                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }




}