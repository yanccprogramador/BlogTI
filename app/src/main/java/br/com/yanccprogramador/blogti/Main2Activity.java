package br.com.yanccprogramador.blogti;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Spanned;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Main2Activity extends AppCompatActivity {
    private JsonObjectRequest req;
    private Button save;
    private ArrayList<Spanned> lista;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    Intent intent = new Intent(Main2Activity.this,MainActivity.class);
                    startActivity(intent);
                    finish();
                    return true;
                case R.id.navigation_dashboard:
                    setContentView(R.layout.publish);
                    //navigation.setSelectedItemId(R.id.navigation_dashboard);
                    final EditText titulo= (EditText) findViewById(R.id.title);
                    final EditText dono= (EditText) findViewById(R.id.dono);
                    final EditText art= (EditText) findViewById(R.id.article);
                    save=(Button) findViewById(R.id.btPublish);
                    save.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            inserir(titulo.getText().toString(), dono.getText().toString(),art.getText().toString());
                        }
                    });
                    return true;
                case R.id.navigation_notifications:
                    /*setContentView(R.layout.art);
                    BottomNavigationView nav3 = (BottomNavigationView) findViewById(R.id.nav3);
                    nav3.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
                    nav3.setSelectedItemId(R.id.navigation_notifications);
                    return true;*/
            }
            return false;
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.publish);
       // BottomNavigationView nav2 = (BottomNavigationView) findViewById(R.id.nav2);
        ///nav2.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        //nav2.setSelectedItemId(R.id.navigation_dashboard);
        final EditText titulo= (EditText) findViewById(R.id.title);
        final EditText dono= (EditText) findViewById(R.id.dono);
        final EditText art= (EditText) findViewById(R.id.article);
        save=(Button) findViewById(R.id.btPublish);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inserir(titulo.getText().toString(), dono.getText().toString(),art.getText().toString());
            }
        });
    }
    public void inserir(String titulo, String dono, String artigo){
        try {
            req= new JsonObjectRequest(Request.Method.POST, "https://yc-ti-blog.herokuapp.com/", new JSONObject("{\"title\":\"" + titulo + "\",\"dono\":\"" + dono + "\",\"artigo\":\"" + artigo+"\"}"),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject jsonObject) {
                            Toast.makeText(Main2Activity.this,"Inserido",Toast.LENGTH_LONG).show();
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    Toast.makeText(Main2Activity.this,"Erro: "+volleyError.getMessage(),Toast.LENGTH_LONG).show();
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
