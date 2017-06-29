package br.com.yanccprogramador.blogti;

import android.content.Intent;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Spanned;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import br.com.yanccprogramador.blogti.BD.BancoController;

public class ActivityPublish extends AppCompatActivity {
    private JsonObjectRequest req;
    private Button save;
    private RequestQueue mRequestQueue;
    private ArrayList<Spanned> lista;
    BottomNavigationView navigation;
    private BancoController bc;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    finish();
                    Intent i= new Intent(ActivityPublish.this,MainActivity.class);
                    startActivity(i);
                    break;
                case R.id.navigation_dashboard:
                    setContentView(R.layout.publish);
                    navigation.setSelectedItemId(R.id.navigation_dashboard);
                    final EditText titulo= (EditText) findViewById(R.id.title);
                    final EditText dono= (EditText) findViewById(R.id.dono);
                    final EditText art= (EditText) findViewById(R.id.article);
                    bc=new BancoController(getBaseContext());
                    Cursor c= bc.carregaUser();
                    dono.setText(c.getString(c.getColumnIndex("login")));
                    save=(Button) findViewById(R.id.btPublish);
                    save.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            inserir(titulo.getText().toString(), dono.getText().toString(),art.getText().toString());
                        }
                    });
                    break;
                case R.id.navigation_notifications:
                    finish();
                    Intent i2= new Intent(ActivityPublish.this,ActivityMine.class);
                    startActivity(i2);
                    break;
            }
            return true;
        }
    };




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.publish);
        navigation= (BottomNavigationView) findViewById(R.id.nav2);
        navigation.setSelectedItemId(R.id.navigation_dashboard);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        final EditText titulo = (EditText) findViewById(R.id.title);
        final EditText dono = (EditText) findViewById(R.id.dono);
        final EditText art = (EditText) findViewById(R.id.article);
        bc = new BancoController(getBaseContext());
        Cursor c = bc.carregaUser();
        dono.setText(c.getString(c.getColumnIndex("login")));
        save = (Button) findViewById(R.id.btPublish);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inserir(titulo.getText().toString(), dono.getText().toString(), art.getText().toString());
            }
        });
    }
            public void inserir(String titulo, String dono, String artigo){
                try {
                    req= new JsonObjectRequest(Request.Method.POST, "https://yc-ti-blog.herokuapp.com/", new JSONObject("{\"title\":\"" + titulo + "\",\"dono\":\"" + dono + "\",\"artigo\":\"" + artigo+"\"}"),
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject jsonObject) {
                                    Toast.makeText(ActivityPublish.this,"Inserido",Toast.LENGTH_LONG).show();
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            Toast.makeText(ActivityPublish.this,"Erro: "+volleyError.getMessage(),Toast.LENGTH_LONG).show();
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                addToRequestQueue(req);
            }
            public RequestQueue getRequestQueue() {

                mRequestQueue = Volley.newRequestQueue(getApplicationContext());

                return mRequestQueue;
            }

            public <T> void addToRequestQueue(Request<T> req) {
                req.setTag("Volley");
                getRequestQueue().add(req);
            }

}
