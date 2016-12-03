package luzontuells.marvelmanager.activities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

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


public class SecondActivity extends AppCompatActivity {

    private static final String TAG_SECOND_ACTIVITY = SecondActivity.class.getSimpleName();
    private static final String JSON_URL = "http://gateway.marvel.com/v1/public/characters?ts=1&apikey=94f4341859283f334a8e1316d7b12e42&hash=aca24562b84ef49172856f5e28d1f95a";

    private String jsonUrl, mNombre, mDescripcion;
    private Bitmap bmp;

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

        this.jsonUrl = "http://gateway.marvel.com:80/v1/public/characters/" + id + "?ts=1&apikey=94f4341859283f334a8e1316d7b12e42&hash=aca24562b84ef49172856f5e28d1f95a";

        JSONObject json;
        try {
            json = new JSONObtainThread().execute(this.jsonUrl).get();
//            JSONObject data = json.getJSONObject("data");
            if (json == null)
                Log.e(SecondActivity.TAG_SECOND_ACTIVITY, "ERROR");
            JSONObject data = json.getJSONObject("data");
            JSONArray results = data.getJSONArray("results");
            JSONObject fields = results.getJSONObject(0);

            this.mNombre = fields.getString("name");
            this.mDescripcion = fields.getString("description");

            this.bmp = new ImageObtainThread().execute(fields.getJSONObject("thumbnail").getString("path")
                    + "." + fields.getJSONObject("thumbnail").getString("extension")).get();

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        TextView Nombre = (TextView) findViewById(R.id.second_activity_item_name);
        Nombre.setText(this.mNombre);

        TextView Descripcion = (TextView) findViewById(R.id.second_activity_item_body);
        Descripcion.setText(this.mDescripcion);

        ImageView Icon = (ImageView) findViewById(R.id.second_activity_icon_item);
        Icon.setImageBitmap(this.bmp);

        //TODO: Add Number of COMICS and EVENTOS before inflating the tabs
        TabLayout tabs = (TabLayout) findViewById(R.id.second_activity_recursos_tabs);

        int numComics = 0;
        int numEventos = 0;

        String comics = "(" + String.valueOf(numComics) + ") Comics";
        String eventos = "(" + String.valueOf(numEventos) + ") Eventos";

        tabs.addTab(tabs.newTab().setText(comics));
        tabs.addTab(tabs.newTab().setText(eventos));
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
                    JSONObject json = JSONReader.readJsonFromUrl(params[0]);
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
                        Log.e(SecondActivity.TAG_SECOND_ACTIVITY, "No images in Thread");

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
