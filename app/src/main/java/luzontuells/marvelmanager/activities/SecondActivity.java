package luzontuells.marvelmanager.activities;

import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;


import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import luzontuells.marvelmanager.R;
import luzontuells.marvelmanager.data.Item;
import luzontuells.marvelmanager.data.JSONManager;
import luzontuells.marvelmanager.fragments.ComicsFragment;
import luzontuells.marvelmanager.fragments.EventosFragment;


public class SecondActivity extends AppCompatActivity {

    private static final String TAG_SECOND_ACTIVITY = SecondActivity.class.getSimpleName();
    private static final String JSON_URL = "http://gateway.marvel.com/v1/public/characters?ts=1&apikey=94f4341859283f334a8e1316d7b12e42&hash=aca24562b84ef49172856f5e28d1f95a&limit=100";


    private String jsonUrlCharacters, jsonUrlComics, jsonUrlEvents, mNombre, mDescripcion;
    private int numComics = 0;
    private int numEventos = 0;
    private String imageString;

    private ArrayList<Item> mListArray = new ArrayList<>();
    private ArrayList<Item> mListArrayComic = new ArrayList<>();
    private ArrayList<Item> mListArrayEvent = new ArrayList<>();

    private ArrayList mImageArray = new ArrayList<>();
    private ArrayList mNameArray = new ArrayList<>();
    private ArrayList mDescriptionArray = new ArrayList<>();
    private ArrayList mIdArray = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);


        Bundle bundle = getIntent().getExtras();
        String id = "";
        if (bundle != null) {
            id = bundle.getString("char_id");
            Log.e("HOLA", id);
        }

        this.jsonUrlCharacters = "http://gateway.marvel.com:80/v1/public/characters/" + id + "?ts=1&apikey=94f4341859283f334a8e1316d7b12e42&hash=aca24562b84ef49172856f5e28d1f95a&limit=100"; //&limit=100
        this.jsonUrlComics = "http://gateway.marvel.com:80/v1/public/characters/" + id + "/comics?ts=1&apikey=94f4341859283f334a8e1316d7b12e42&hash=aca24562b84ef49172856f5e28d1f95a&limit=100"; //&limit=100
        this.jsonUrlEvents = "http://gateway.marvel.com:80/v1/public/characters/" + id + "/events?ts=1&apikey=94f4341859283f334a8e1316d7b12e42&hash=aca24562b84ef49172856f5e28d1f95a&limit=100"; //&limit=100

        setupDataFromJson(this.jsonUrlCharacters);
        setupDataFromJson(this.jsonUrlComics);
        setupDataFromJson(this.jsonUrlEvents);

        TextView Nombre = (TextView) findViewById(R.id.second_activity_item_name);
        Nombre.setText(this.mNombre);

        TextView Descripcion = (TextView) findViewById(R.id.second_activity_item_body);
        Descripcion.setText(this.mDescripcion);

        ImageView Icon = (ImageView) findViewById(R.id.second_activity_icon_item);
        Picasso.with(this)
                .load(imageString)
                .into(Icon);

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        TabLayout tabs = (TabLayout) findViewById(R.id.second_activity_recursos_tabs);
        tabs.setupWithViewPager(viewPager);

    }

    private void setupViewPager(ViewPager viewPager) {
        Adapter adapter = new Adapter(getSupportFragmentManager());
        String comics = "(" + String.valueOf(numComics) + ") Comics";
        String eventos = "(" + String.valueOf(numEventos) + ") Eventos";
        //TODO: If Fragments not used: atomize
        Fragment fragmentComics = new ComicsFragment();
        Fragment fragmentEventos = new EventosFragment();
        adapter.addFragment(fragmentComics, comics);
        adapter.addFragment(fragmentEventos, eventos);
        viewPager.setAdapter(adapter);
    }


    static class Adapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public Adapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }

    }


    public void setupDataFromJson(String jsonUrl) {
        JSONObject json;
        try {
            json = new JSONManager.JSONObtainThread().execute(jsonUrl).get();
//            JSONObject data = json.getJSONObject("data");
            if (json == null)
                Log.e(SecondActivity.TAG_SECOND_ACTIVITY, "ERROR");

            if (jsonUrl == this.jsonUrlCharacters) {

                JSONObject data = json.getJSONObject("data");
                JSONArray results = data.getJSONArray("results");
                JSONObject fields = results.getJSONObject(0);


                this.mNombre = fields.getString("name");
                this.mDescripcion = fields.getString("description");
                this.imageString = fields.getJSONObject("thumbnail").getString("path") + "." + fields.getJSONObject("thumbnail").getString("extension");

            } else if (jsonUrl == this.jsonUrlComics) {

                JSONObject data = json.getJSONObject("data");
                JSONArray results = data.getJSONArray("results");

                this.numComics = data.getInt("total");


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

            } else if (jsonUrl == this.jsonUrlEvents) {

                JSONObject data = json.getJSONObject("data");
                JSONArray results = data.getJSONArray("results");

                this.numEventos = data.getInt("total");


                for (int i = 0; i < results.length(); i++) {

                    JSONObject fields = results.getJSONObject(i);

                    String imageString = fields.getJSONObject("thumbnail").getString("path") + "." + fields.getJSONObject("thumbnail").getString("extension");
                    String nameString = fields.getString("name");
                    String descriptionString = fields.getString("description");
                    String idString = fields.getString("id");

                    this.mListArrayEvent.add(new Item(imageString,
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

    private void fromItemToStringArray(ArrayList<Item> arrayListItem){
        for (int idx=0; idx < arrayListItem.size(); idx++){
            this.mImageArray.add(arrayListItem.get(idx).getmImage());
            this.mNameArray.add(arrayListItem.get(idx).getmImage());
            this.mDescriptionArray.add(arrayListItem.get(idx).getmImage());
            this.mIdArray.add(arrayListItem.get(idx).getmImage());

        }
    }
}
