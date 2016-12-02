package luzontuells.marvelmanager.activities;

import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import luzontuells.marvelmanager.R;


public class SecondActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        //TODO: Add Number of COMICS and EVENTOS before inflating the tabs
        TabLayout tabs = (TabLayout) findViewById(R.id.second_activity_recursos_tabs);

        int numComics = 0;
        int numEventos = 0;

        String comics = "(" + String.valueOf(numComics) + ") Comics";
        String eventos = "(" + String.valueOf(numEventos) + ") Eventos";

        tabs.addTab(tabs.newTab().setText(comics));
        tabs.addTab(tabs.newTab().setText(eventos));
    }
}
