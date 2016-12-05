package luzontuells.marvelmanager.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import luzontuells.marvelmanager.R;
import luzontuells.marvelmanager.data.Item;
import luzontuells.marvelmanager.data.JSONManager;


public class FirstActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private static final String JSON_URL = "http://gateway.marvel.com/v1/public/characters?ts=1&apikey=94f4341859283f334a8e1316d7b12e42&hash=aca24562b84ef49172856f5e28d1f95a&limit=100"; //&limit=100
    private static final int MIN_LETTERS_TO_TEXT_SEARCH = 3;

    private ArrayList<String> mCharId = new ArrayList<>();
    private ArrayList<String> mUpdatedCharId = new ArrayList<>();
    private ArrayList<String> mNames = new ArrayList<>();
    private ArrayList<Item> array_sort = new ArrayList<>();
    private ArrayList<Item> mListArray = new ArrayList<>();
    private ListView listView;
    private EditText searchEditText;
    private String mJsonUrl = JSON_URL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);

        this.listView = (ListView) this.findViewById(R.id.list_view_first);

        this.mUpdatedCharId = this.mCharId;

        setupDataFromJson(JSON_URL);

        this.listView.setOnItemClickListener(this);
        this.listView.setAdapter(new MyListAdapter(this, 0, this.mListArray));

        this.searchEditText = (EditText) this.findViewById(R.id.name_edit_text);
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                int textlength = searchEditText.getText().length();
                array_sort.clear();
                mUpdatedCharId.clear();
                for (Item element : mListArray) {

                    if (textlength <= element.getmName().length() && textlength >= MIN_LETTERS_TO_TEXT_SEARCH) {
                        mJsonUrl = "http://gateway.marvel.com/v1/public/characters?ts=1&apikey=94f4341859283f334a8e1316d7b12e42&hash=aca24562b84ef49172856f5e28d1f95a&nameStartsWith="
                                + searchEditText.getText().toString();
                        if (searchEditText.getText().toString().equalsIgnoreCase(
                                (String) element.getmName().subSequence(0,
                                        textlength))) {
                            array_sort.add(element);
                            mUpdatedCharId.add(element.getmId());
                        }
                    }
                }
                if (array_sort != null) {
                    setupDataFromJson(mJsonUrl);
                    listView.setAdapter(new MyListAdapter(FirstActivity.this, 0, array_sort));
                }
                if (textlength == 0) {
                    setupDataFromJson(JSON_URL);
                    listView.setAdapter(new MyListAdapter(FirstActivity.this, 0, mListArray));
                    mUpdatedCharId = mCharId;
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        Intent mIntent = new Intent(this, SecondActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("char_id", this.mUpdatedCharId.get(position));
        mIntent.putExtras(bundle);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        this.startActivity(mIntent);
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

    public void setupDataFromJson(String jsonUrl) {

        JSONObject json;

        try {
            json = new JSONManager.JSONObtainThread().execute(jsonUrl).get();
            JSONObject data = json.getJSONObject("data");
            JSONArray results = data.getJSONArray("results");

            for (int i = 0; i < results.length(); i++) {
                JSONObject fields = results.getJSONObject(i);

                String imageString = fields.getJSONObject("thumbnail").getString("path") + "." + fields.getJSONObject("thumbnail").getString("extension");

                String nameString = fields.getString("name");
                this.mNames.add(nameString);

                String idString = fields.getString("id");
                this.mCharId.add(idString);

                this.mListArray.add(new Item(imageString,
                        nameString,
                        fields.getString("description"),
                        idString));
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
