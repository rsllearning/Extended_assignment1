package com.example.show_data;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ICartLoadListner {

    static final String URL  =  "https://fakestoreapi.com/products";
    Spinner spinner;
    List <Product> products = new ArrayList<Product>();
    RecyclerView productlist;
    ICartLoadListner iCartLoadListner;
    TextView text2;
    int current = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
         productlist = (RecyclerView) findViewById(R.id.productlist);
        productlist.setLayoutManager(new LinearLayoutManager(this));
         text2 = (TextView) findViewById(R.id.text2);
        spinner = (Spinner) findViewById(R.id.spinner);
       // intit();


        List < String >  list = new ArrayList<String>();
        list.add("all");
        list.add("men's clothing");
        list.add("jewelery");
        list.add("electronics");
        list.add("women's clothing");


        ArrayAdapter < String > arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1,list);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);





        StringRequest request = new StringRequest(URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        GsonBuilder gsonBuilder = new GsonBuilder();
                        Gson gson = gsonBuilder.create();
                        Product[] productsall = gson.fromJson(response,Product[].class);

                        for(int j=0;j<productsall.length;++j)
                        {
                                products.add(productsall[j]);
                         }
                        productlist.setAdapter(new Adapter(MainActivity.this,products,iCartLoadListner));
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                              @Override
                                              public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                                  Toast.makeText(getApplicationContext(),"this"+Integer.toString(i),Toast.LENGTH_SHORT).show();
                                                  current =i;
                                                  getselectedata(i);
                                                  spinner.setSelection(i);

                                              }

                                              @Override
                                              public void onNothingSelected(AdapterView<?> adapterView) {

                                              }
                                          }
        );


        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
    }

    private void intit() {
        iCartLoadListner=this;
    }


    void getselectedata(int i)
    {
            ArrayList <Product> pr = new ArrayList<Product>();

            String [] type =  {"All","men's clothing","jewelery","electronics","women's clothing"};





        for(int j=0;j<products.size();++j)
            {
                String str =products.get(j).getCategory();

                  if(i>0&&(str.equals(type[i])))
                   pr.add(products.get(j));
                  else if(i==0)
                      pr.add(products.get(j));

            }

          Toast.makeText(getApplicationContext(),"YAHI BAAT "+Integer.toString(pr.size()),Toast.LENGTH_SHORT).show();
         productlist.setAdapter(new Adapter(MainActivity.this,pr,iCartLoadListner));
    }


    @Override
    public void onCartLoadSuccess(List<CartModel> cartlist) {

        int cartsum = 0;
        for(CartModel cartModel : cartlist)
        {
            cartsum+=cartModel.getQualtity();

        }
        //text2.setText(String.valueOf(cartsum));
    }

    @Override
    public void onCartLoadFailed(String message) {


    }


    @Override
    protected void onResume() {
        super.onResume();
      //  countcartitems();
    }

    private void countcartitems() {

        List <CartModel> cartModels = new ArrayList<>();
        FirebaseDatabase
                .getInstance().getReference("cart")
                .child("UNIQUE_USER_ID")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        for(DataSnapshot cartSnapshot: dataSnapshot.getChildren())
                        {
                            CartModel cartModel = cartSnapshot.getValue(CartModel.class);
                            cartModel.setKye(cartSnapshot.getKey());
                            cartModels.add(cartModel);
                        }
                        iCartLoadListner.onCartLoadSuccess(cartModels);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                           iCartLoadListner.onCartLoadFailed(databaseError.getMessage());
                    }
                });
    }
}