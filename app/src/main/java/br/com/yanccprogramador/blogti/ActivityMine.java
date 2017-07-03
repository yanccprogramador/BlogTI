package br.com.yanccprogramador.blogti;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;

import br.com.yanccprogramador.blogti.BD.BancoController;

import static com.android.volley.Request.Method.GET;

public class ActivityMine extends AppCompatActivity {
    private ArrayList<Integer> listaMineId;
    private JsonObjectRequest req;
    private ArrayList<String> lista, listaMine,artigos;
    private ArrayAdapter<String>  adpMine;
    private RequestQueue mRequestQueue;
    private ListView lv2;
    private View mProgressView;
    private BancoController bc;
    BottomNavigationView navigation;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    finish();
                    Intent i= new Intent(ActivityMine.this,MainActivity.class);
                    i.putExtra("close",false);
                    startActivity(i);
                    break;
                case R.id.navigation_dashboard:
                    finish();
                    Intent i2= new Intent(ActivityMine.this,ActivityPublish.class);
                    startActivity(i2);
                    break;
                case R.id.navigation_notifications:
                   return false;
                case R.id.user:
                    bc= new BancoController(getBaseContext());
                    bc.deleteUser();
                    finish();
                    Intent i3= new Intent(ActivityMine.this,logar.class);
                    startActivity(i3);
                    break;


            }
            return true;
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        listaMineId= new ArrayList<>();
        listaMine= new ArrayList<>();
        artigos= new ArrayList<>();
        setContentView(R.layout.art);
        mProgressView = findViewById(R.id.progress2);
        navigation= (BottomNavigationView) findViewById(R.id.nav3);
        navigation.setSelectedItemId(R.id.navigation_notifications);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        getMyArticles();


    }
    private void getMyArticles() {
        showProgress(true);
        bc=new BancoController(getBaseContext());
        Cursor c=bc.carregaUser();
        String login=c.getString(c.getColumnIndex("login"));
        String url="https://yc-ti-blog.herokuapp.com/meu/yccp";
        url=url.replace("yccp",login);

        req = new JsonObjectRequest(Request.Method.GET,url, null,
                new Response.Listener<JSONObject>() {
                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            Log.i("Response", response.toString());
                            int limite = response.getInt("numLinhas");
                            if (limite == 0) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(ActivityMine.this);
                                builder.setMessage(R.string.nda);
                                // Create the AlertDialog object and return it
                                builder.create().show();
                            } else {
                                JSONArray js = response.getJSONArray("rows");
                                for (int i = 0; i < limite; i++) {


                                    JSONObject items = js.getJSONObject(i);
                                    String titulo = items.getString("titulo");
                                    String artigo = items.getString("artigo");
                                    int id = items.getInt("id");

                                    listaMine.add(i, titulo);
                                    listaMineId.add(i, id);
                                    artigos.add(i, artigo);


                                }

                                finalize();
                                Toast.makeText(getApplicationContext(), R.string.finished, Toast.LENGTH_SHORT).show();
                                if (adpMine == null) {
                                    adpMine = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1);
                                }

                                adpMine.addAll(listaMine);
                                lv2 = (ListView) findViewById(R.id.lv3);
                                lv2.setAdapter(adpMine);
                                lv2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> adapterView, View view, final int position, long l) {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(ActivityMine.this);
                                        builder.setMessage(R.string.wtd)
                                                .setPositiveButton(R.string.up, new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int id) {
                                                        setContentView(R.layout.update);
                                                        final EditText titulo = (EditText) findViewById(R.id.title1);
                                                        final EditText dono = (EditText) findViewById(R.id.dono1);
                                                        final EditText art = (EditText) findViewById(R.id.article1);
                                                        bc = new BancoController(getBaseContext());
                                                        Cursor c = bc.carregaUser();
                                                        dono.setText(c.getString(c.getColumnIndex("login")));
                                                        titulo.setText(listaMine.get(position));
                                                        art.setText(artigos.get(position));
                                                        Button save = (Button) findViewById(R.id.btPublish1);
                                                        save.setOnClickListener(new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View view) {
                                                                update(titulo.getText().toString(), dono.getText().toString(), art.getText().toString(), listaMineId.get(position).toString());
                                                            }
                                                        });
                                                        // FIRE ZE MISSILES!
                                                    }
                                                })
                                                .setNegativeButton(R.string.del, new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int id) {
                                                        deleteArticle(listaMineId.get(position).toString());
                                                    }
                                                });
                                        // Create the AlertDialog object and return it
                                        builder.create().show();
                                    }
                                });
                                showProgress(false);
                                Log.i("Fim", "Sucesso");
                            }

                            } catch(JSONException e){
                                e.printStackTrace();
                                Toast.makeText(getApplicationContext(),
                                        R.string.tente,
                                        Toast.LENGTH_LONG).show();
                            } catch(Throwable throwable){
                                throwable.printStackTrace();

                            }



                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Volley", "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        R.string.tente, Toast.LENGTH_SHORT).show();

            }
        });

        addToRequestQueue(req);
    }
    private void deleteArticle(String id) {

        String url="https://yc-ti-blog.herokuapp.com/";

        req = new JsonObjectRequest(Request.Method.DELETE,url+id, null,
                new Response.Listener<JSONObject>() {
                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            finalize();
                            Toast.makeText(getApplicationContext(), R.string.deleted, Toast.LENGTH_SHORT).show();
                            Intent i=new Intent(ActivityMine.this,ActivityMine.class);
                            finish();
                            startActivity(i);
                            Log.i("Fim", "Sucesso");

                        } catch (JSONException e) {
                            e.printStackTrace();
                            showProgress(false);
                            Toast.makeText(getApplicationContext(),
                                    R.string.tente,
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
                        R.string.tente, Toast.LENGTH_SHORT).show();

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
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);



            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);

        }
    }
    public void update(String titulo, String dono, String artigo, String id){
        if (!titulo.isEmpty() && !dono.isEmpty() && !artigo.isEmpty()) {
            artigo=removerCaracteresEspeciais(artigo);
            try {
                req = new JsonObjectRequest(Request.Method.PUT, "https://yc-ti-blog.herokuapp.com/"+id, new JSONObject("{\"title\":\"" + titulo + "\",\"dono\":\"" + dono + "\",\"artigo\":\"" + artigo + "\"}"),
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject jsonObject) {
                                Toast.makeText(ActivityMine.this, R.string.inserted, Toast.LENGTH_LONG).show();
                                Intent i= new Intent(ActivityMine.this,ActivityMine.class);
                                finish();
                                startActivity(i);
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(ActivityMine.this, "Erro: " + volleyError.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }
            addToRequestQueue(req);
        }else{
            Toast.makeText(getApplicationContext(),R.string.required,Toast.LENGTH_LONG).show();
        }

    }
    @Override
    public void onBackPressed(){
        finish();
        Intent i=new Intent(this,MainActivity.class);
        i.putExtra("close",true);
        startActivity(i);
        Toast.makeText(this,R.string.dbclick,Toast.LENGTH_LONG).show();

        return;
    }
    public String removerCaracteresEspeciais(String string) {
        string = Normalizer.normalize(string, Normalizer.Form.NFD);
        string = string.replaceAll("[^\\p{ASCII}]", "");
        return string;
    }
}
