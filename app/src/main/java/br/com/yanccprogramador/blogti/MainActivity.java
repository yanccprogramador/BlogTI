package br.com.yanccprogramador.blogti;

import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import br.com.yanccprogramador.blogti.BD.BancoController;

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
    private BancoController bc;
    BottomNavigationView navigation;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                   return false;
                case R.id.navigation_dashboard:
                    finish();
                    Intent i= new Intent(MainActivity.this,ActivityPublish.class);
                    startActivity(i);
                    break;
                case R.id.navigation_notifications:
                    finish();
                    Intent i2= new Intent(MainActivity.this,ActivityMine.class);
                    startActivity(i2);
                    break;
               /* case R.id.user:
                    bc= new BancoController(getBaseContext());
                    bc.deleteUser();
                    finish();
                    Intent i3= new Intent(MainActivity.this,logar.class);
                    startActivity(i3);
                    break;*/
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
        setContentView(R.layout.activity_main);
         navigation= (BottomNavigationView) findViewById(R.id.nav);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        getAllArticles();

    }
    private void getAllArticles() {


        req = new JsonObjectRequest(GET, "https://yc-ti-blog.herokuapp.com/", null,
                new Response.Listener<JSONObject>() {
                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public void onResponse(JSONObject response) {

                        try {

                            Log.i("Response", response.toString());
                            int limite= response.getInt("numLinhas");
                            JSONArray js = response.getJSONArray("rows");
                            for (int i = 0; i < limite; i++) {


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
                            if(adp==null){
                                adp= new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1);
                            }
                              adp.addAll(lista);
                            lv1 = (ListView) findViewById(R.id.lv1);
                            lv1.setAdapter(adp);
                            lv1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    setContentView(R.layout.viewarticle);
                                    TextView tvT=(TextView) findViewById(R.id.tvTi);
                                    TextView tvD=(TextView) findViewById(R.id.tvDono);
                                    TextView tvA=(TextView) findViewById(R.id.tvArt);
                                    tvT.setText(lista.get(i));
                                    tvD.setText(dono.get(i));
                                    tvA.setText(articles.get(i));
                                }
                            });

                            Log.i("Fim", "Sucesso");

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


    public RequestQueue getRequestQueue() {

        mRequestQueue = Volley.newRequestQueue(getApplicationContext());

        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag("Volley");
        getRequestQueue().add(req);
    }

}
