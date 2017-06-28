package br.com.yanccprogramador.blogti;

import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.android.volley.Request.Method.GET;

public class MainActivity extends AppCompatActivity {

    private TextView mTextMessage;
    private JsonObjectRequest req;
    private ArrayList<String> lista, listaMine;
    private List<String> dono, articles;
    private ArrayAdapter<String> adp, adpMine;
    private RequestQueue mRequestQueue;
    private Button save;
    private JsonArrayRequest req1;
    private ArrayList<Integer> listaMineId;
    private ListView lv1,lv2;
    private BancoController bc=new BancoController(getBaseContext());
    BottomNavigationView navigation;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    lista = new ArrayList<>();
                    dono= new ArrayList<>();
                    articles= new ArrayList<>();
                    adp= new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1);
                    setContentView(R.layout.activity_main);
                    getAllArticles();
                    lv1 = (ListView) findViewById(R.id.lv1);
                    lv1.setAdapter(adp);
                    break;
                case R.id.navigation_dashboard:
                    setContentView(R.layout.publish);

                    final EditText titulo= (EditText) findViewById(R.id.title);
                    final EditText dono= (EditText) findViewById(R.id.dono);
                    final EditText art= (EditText) findViewById(R.id.article);
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
                    listaMine= new ArrayList<>();
                    adpMine= new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1);
                    setContentView(R.layout.art);
                    lv2=(ListView) findViewById(R.id.lv3);
                    getMyArticles();
                    lv2.setAdapter(adpMine);
                    break;
            }
         return true;
        }
    };


    public MainActivity() {
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        lista = new ArrayList<>();
        dono= new ArrayList<>();
        articles= new ArrayList<>();
        adp= new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1);
        setContentView(R.layout.activity_main);
         navigation= (BottomNavigationView) findViewById(R.id.nav);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        getAllArticles();
        lv1 = (ListView) findViewById(R.id.lv1);

        lv1.setAdapter(adp);
    }
    private void getAllArticles() {


        req = new JsonObjectRequest(GET, "https://yc-ti-blog.herokuapp.com/", null,
                new Response.Listener<JSONObject>() {
                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public void onResponse(JSONObject response) {
                        String login = "";
                        String name = "";
                        try {

                            Log.i("Response", response.toString());
                            int limite= response.getInt("numLinhas");
                            JSONArray js = response.getJSONArray("rows");
                            for (int i = 1; i <= limite; i++) {


                                JSONObject items = js.getJSONObject(i);
                                String titulo = items.getString("titulo");
                                String owner = items.getString("dono");
                                String artigo = items.getString("artigo");

                                lista.add(i,titulo);
                                dono.add(i,owner);
                                articles.add(i,artigo);



                            }

                            finalize();
                            Toast.makeText(getApplicationContext(), "Busca Finalizada com sucesso!", Toast.LENGTH_SHORT).show();
                            adp.addAll(lista);
                            Log.i("Fim", "Sucesso");
                            final String finalLogin = login;
                            final String finalName = name;

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(),
                                    "Error, tente novamente",
                                    Toast.LENGTH_LONG).show();
                        } catch (Throwable throwable) {
                            throwable.printStackTrace();

                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Volley", "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        "Erro tente Novamente", Toast.LENGTH_SHORT).show();

            }
        });
        addToRequestQueue(req);
    }
    private void getMyArticles() {
        Cursor c=bc.carregaUser();

        req = new JsonObjectRequest(GET, "https://yc-ti-blog.herokuapp.com/meu/"+c.getString(c.getColumnIndex("login")), null,
                new Response.Listener<JSONObject>() {
                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public void onResponse(JSONObject response) {
                        String login = "";
                        String name = "";
                        try {

                            Log.i("Response", response.toString());
                            int limite= response.getInt("numLinhas");
                            JSONArray js = response.getJSONArray("rows");
                            for (int i = 0; i < limite; i++) {


                                JSONObject items = js.getJSONObject(i);
                                String titulo = items.getString("titulo");
                                int id=items.getInt("id");

                                listaMine.add(i, titulo);
                                listaMineId.add(i,id);


                            }

                            finalize();
                            Toast.makeText(getApplicationContext(), "Busca Finalizada com sucesso!", Toast.LENGTH_SHORT).show();
                            adp.addAll(lista);
                            Log.i("Fim", "Sucesso");
                            final String finalLogin = login;
                            final String finalName = name;

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(),
                                    "Error, tente novamente",
                                    Toast.LENGTH_LONG).show();
                        } catch (Throwable throwable) {
                            throwable.printStackTrace();

                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Volley", "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        "Erro tente Novamente", Toast.LENGTH_SHORT).show();

            }
        });
        addToRequestQueue(req);
    }
    public void inserir(String titulo, String dono, String artigo){
        try {
            req= new JsonObjectRequest(Request.Method.POST, "https://yc-ti-blog.herokuapp.com/", new JSONObject("{\"title\":\"" + titulo + "\",\"dono\":\"" + dono + "\",\"artigo\":\"" + artigo+"\"}"),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject jsonObject) {
                           Toast.makeText(MainActivity.this,"Inserido",Toast.LENGTH_LONG).show();
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    Toast.makeText(MainActivity.this,"Erro: "+volleyError.getMessage(),Toast.LENGTH_LONG).show();
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public RequestQueue getRequestQueue() {

        mRequestQueue = Volley.newRequestQueue(getApplicationContext());

        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag("Volley");
        getRequestQueue().add(req);
    }
    /*public void popular(){

        BancoController crud = new BancoController(getBaseContext());
        Cursor cursor = crud.carregaDados();
        lista=cursor.get
        adp.addAll(lista);
        lv1 = (ListView)findViewById(R.id.lv1);
        lv1.setAdapter(adp);
    }*/
}
