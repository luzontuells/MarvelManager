package luzontuells.marvelmanager.data;

import android.os.AsyncTask;

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


public class JSONManager {

    public static class JSONReader {

        private static String readAll(Reader rd) throws IOException {
            StringBuilder stringBuilder = new StringBuilder();
            int copy;
            while ((copy = rd.read()) != -1) {
                stringBuilder.append((char) copy);
            }
            return stringBuilder.toString();
        }

        public static JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
            InputStream inputStream = new URL(url).openStream();
            try {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, Charset.forName("UTF-8")));
                String jsonText = readAll(bufferedReader);
                JSONObject json = new JSONObject(jsonText);
                return json;
            } finally {
                inputStream.close();
            }
        }
    }

    public static class JSONObtainThread extends AsyncTask<String, Void, JSONObject> {

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

}
