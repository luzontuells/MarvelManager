package luzontuells.marvelmanager.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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


public class EventosFragment extends Fragment implements AdapterView.OnItemClickListener {

    private ArrayList<Item> mListArrayEventos = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ListView listView = (ListView) inflater.inflate(
                R.layout.list_view, container, false);


        this.mListArrayEventos = ((SecondActivity) getActivity()).getmListArrayEvent();


        listView.setOnItemClickListener(this);
        listView.setAdapter(new MyListAdapter(this.getContext(), 0, this.mListArrayEventos));


        return listView;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }


    private class MyListAdapter extends ArrayAdapter<Item> {

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

            ViewHolder mViewHolder;
            if (convertView == null) {
                LayoutInflater mInflater = (LayoutInflater) this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = mInflater.inflate(R.layout.list_item, null);

                mViewHolder = new ViewHolder();
                mViewHolder.icon_ImgView = (ImageView) convertView.findViewById(R.id.icon_item);
                mViewHolder.title_TxtView = (TextView) convertView.findViewById(R.id.item_name);
                mViewHolder.body_TxtView = (TextView) convertView.findViewById(R.id.item_body);
                convertView.setTag(mViewHolder);

            } else
                mViewHolder = (ViewHolder) convertView.getTag();

            mViewHolder.title_TxtView.setText(this.itemList.get(position).getmName());
            mViewHolder.body_TxtView.setText(this.itemList.get(position).getmBody());
            Picasso.with(mContext)
                    .load(this.itemList.get(position).getmImage())
                    .into(mViewHolder.icon_ImgView);

            return convertView;
        }
    }




/*
    public void setupDataFromJson(String jsonUrl) {
        JSONObject json;
        try {
            json = new JSONManager.JSONObtainThread().execute(jsonUrl).get();
//            JSONObject data = json.getJSONObject("data");

            if (jsonUrl == this.jsonUrlEventos) {

                JSONObject data = json.getJSONObject("data");
                JSONArray results = data.getJSONArray("results");


                for (int i = 0; i < results.length(); i++) {

                    JSONObject fields = results.getJSONObject(i);

                    String imageString = fields.getJSONObject("thumbnail").getString("path") + "." + fields.getJSONObject("thumbnail").getString("extension");
                    String nameString = fields.getString("name");
                    String descriptionString = fields.getString("description");
                    String idString = fields.getString("id");


                    this.mListArrayEventos.add(new Item(imageString,
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
*/


}