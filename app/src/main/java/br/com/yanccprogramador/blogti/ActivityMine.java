package br.com.yanccprogramador.blogti;

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

import java.util.ArrayList;
import java.util.List;

import br.com.yanccprogramador.blogti.BD.BancoController;

import static com.android.volley.Request.Method.GET;

public class ActivityMine extends AppCompatActivity {
    private ArrayList<Integer> listaMineId;
    private JsonObjectRequest req;
    private ArrayList<String> lista, listaMine;
    private ArrayAdapter<String>  adpMine;
    private RequestQueue mRequestQueue;
    private ListView lv2;
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
                    startActivity(i);
                    break;
                case R.id.navigation_dashboard:
                    finish();
                    Intent i2= new Intent(ActivityMine.this,ActivityPublish.class);
                    startActivity(i2);
                    break;
                case R.id.navigation_notifications:
                   return false;
                /*case R.id.user:
                    bc= new BancoController(getBaseContext());
                    bc.deleteUser();
                    finish();
                    Intent i3= new Intent(ActivityMine.this,logar.class);
                    startActivity(i3);
                    break;
*/

            }
            return true;
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        listaMineId= new ArrayList<>();
        listaMine= new ArrayList<>();

        setContentView(R.layout.art);
        navigation= (BottomNavigationView) findViewById(R.id.nav3);
        navigation.setSelectedItemId(R.id.navigation_notifications);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        getMyArticles();


    }
    private void getMyArticles() {
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
                            int limite= response.getInt("numLinhas");
                            JSONArray js = response.getJSONArray("rows");
                            for (int i = 0; i < limite; i++) {


                                JSONObject items = js.getJSONObject(i);
                                String titulo = items.getString("titulo");
                                int id=items.getInt("id");

                                listaMine.add(i,titulo);
                                listaMineId.add(i,id);


                            }

                            finalize();
                            Toast.makeText(getApplicationContext(), "Busca Finalizada com sucesso!", Toast.LENGTH_SHORT).show();
                            if(adpMine==null){
                                adpMine= new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1);
                            }

                            adpMine.addAll(listaMine);
                            lv2=(ListView) findViewById(R.id.lv3);
                            lv2.setAdapter(adpMine);
                            lv2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, final int position, long l) {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(ActivityMine.this);
                                    builder.setMessage(R.string.wtd)
                                            .setPositiveButton(R.string.up, new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    // FIRE ZE MISSILES!
                                                }
                                            })
                                            .setNegativeButton(R.string.del, new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    deleteArticle(listaMineId.get(position).toString());
                                                }
                                            });
                                    // Create the AlertDialog object and return it
                                    builder.create();
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
    private void deleteArticle(String id) {

        String url="https://yc-ti-blog.herokuapp.com/";

        req = new JsonObjectRequest(Request.Method.DELETE,url+id, null,
                new Response.Listener<JSONObject>() {
                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            finalize();
                            Toast.makeText(getApplicationContext(), "Succes!", Toast.LENGTH_SHORT).show();
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
