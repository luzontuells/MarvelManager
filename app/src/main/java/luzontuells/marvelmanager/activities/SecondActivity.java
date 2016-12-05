package luzontuells.marvelmanager.activities;

import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.widget.Toast;


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


public class SecondActivity extends AppCompatActivity implements View.OnClickListener {

    private String urlDetalle, urlWiki, urlComics;

    private static final String TAG_SECOND_ACTIVITY = SecondActivity.class.getSimpleName();
    private static final String JSON_URL = "http://gateway.marvel.com/v1/public/characters?ts=1&apikey=94f4341859283f334a8e1316d7b12e42&hash=aca24562b84ef49172856f5e28d1f95a&limit=100";

    private String jsonUrlCharacters, jsonUrlComics, jsonUrlEvents, mNombre, mDescripcion;
    private int numComics = 0;
    private int numEventos = 0;
    private String imageString, id;

    private ArrayList<Item> mListArray = new ArrayList<>();
    private ArrayList<Item> mListArrayComic = new ArrayList<>();
    private ArrayList<Item> mListArrayEvent = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);


        Bundle bundle = getIntent().getExtras();
        this.id = "";
        if (bundle != null) {
            id = bundle.getString("char_id");
            Log.e("HOLA", id);
        }

        this.jsonUrlCharacters = "http://gateway.marvel.com:80/v1/public/characters/" + this.id + "?ts=1&apikey=94f4341859283f334a8e1316d7b12e42&hash=aca24562b84ef49172856f5e28d1f95a&limit=100"; //&limit=100
        this.jsonUrlComics = "http://gateway.marvel.com:80/v1/public/characters/" + this.id + "/comics?ts=1&apikey=94f4341859283f334a8e1316d7b12e42&hash=aca24562b84ef49172856f5e28d1f95a&limit=100"; //&limit=100
        this.jsonUrlEvents = "http://gateway.marvel.com:80/v1/public/characters/" + this.id + "/events?ts=1&apikey=94f4341859283f334a8e1316d7b12e42&hash=aca24562b84ef49172856f5e28d1f95a&limit=100"; //&limit=100

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

        Button Detalle = (Button) findViewById(R.id.second_activity_button_detalle);
        Detalle.setOnClickListener(this);

        Button Wiki = (Button) findViewById(R.id.second_activity_button_wiki);
        Wiki.setOnClickListener(this);

        Button Comics = (Button) findViewById(R.id.second_activity_button_comics);
        Comics.setOnClickListener(this);

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        TabLayout tabs = (TabLayout) findViewById(R.id.second_activity_recursos_tabs);
        tabs.setupWithViewPager(viewPager);

    }

    private void setupViewPager(ViewPager viewPager) {
        Adapter adapter = new Adapter(getSupportFragmentManager());
        String comics = "(" + String.valueOf(numComics) + ") Comics";
        String eventos = "(" + String.valueOf(numEventos) + ") Eventos";

        Fragment fragmentComics = new ComicsFragment();
        Bundle bundleComics = new Bundle();
        bundleComics.putString("char_id", this.id);
        fragmentComics.setArguments(bundleComics);
        adapter.addFragment(fragmentComics, comics);

        Fragment fragmentEventos = new EventosFragment();
        Bundle bundleEventos = new Bundle();
        bundleEventos.putString("char_id", this.id);
        fragmentEventos.setArguments(bundleEventos);
        adapter.addFragment(fragmentEventos, eventos);

        viewPager.setAdapter(adapter);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.second_activity_button_detalle:
                Uri uriDet = Uri.parse(this.urlDetalle);
                if (uriDet != null) {
                    Intent browserDetalle = new Intent(Intent.ACTION_VIEW, uriDet);
                    startActivity(browserDetalle);
                } else {
                    Toast.makeText(this, "Sorry, URL not available", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.second_activity_button_wiki:
                Uri uriWiki = Uri.parse(this.urlDetalle);
                if (uriWiki != null) {
                    Intent browserWiki = new Intent(Intent.ACTION_VIEW, uriWiki);
                    startActivity(browserWiki);
                } else {
                    Toast.makeText(this, "Sorry, URL not available", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.second_activity_button_comics:
                Uri uriComic = Uri.parse(this.urlDetalle);
                if (uriComic != null) {
                    Intent browserComics = new Intent(Intent.ACTION_VIEW, uriComic);
                    startActivity(browserComics);
                } else {
                    Toast.makeText(this, "Sorry, URL not available", Toast.LENGTH_SHORT).show();
                }
                break;
        }
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

                this.urlDetalle = fields.getJSONArray("urls").getJSONObject(0).getString("url");
                this.urlWiki = fields.getJSONArray("urls").getJSONObject(1).getString("url");
                this.urlComics = fields.getJSONArray("urls").getJSONObject(2).getString("url");


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
}
