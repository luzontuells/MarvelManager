package luzontuells.marvelmanager.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import luzontuells.marvelmanager.R;
import luzontuells.marvelmanager.activities.SecondActivity;
import luzontuells.marvelmanager.data.Item;


public class EventosFragment extends Fragment implements AdapterView.OnItemClickListener {

    private String jsonUrlEventos, id;
    private ArrayList<Item> mListArrayEventos = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ListView listView = (ListView) inflater.inflate(
                R.layout.list_view, container, false);

        this.id = getArguments().getString("char_id");

        this.jsonUrlEventos = "http://gateway.marvel.com:80/v1/public/characters/" + id + "/events?ts=1&apikey=94f4341859283f334a8e1316d7b12e42&hash=aca24562b84ef49172856f5e28d1f95a&limit=100"; //&limit=100

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
}