package com.example.show_data;

import android.content.Context;
import android.graphics.drawable.LayerDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Adapter extends  RecyclerView.Adapter< Adapter.VIewHolder> {

    Context context;
    List <Product> data = new ArrayList<Product>();
    ICartLoadListner iCartLoadListner;
     int cnt=0;

    public Adapter(Context context, List<Product> data, ICartLoadListner iCartLoadListner) {
        this.context = context;
        this.data = data;
        this.iCartLoadListner = iCartLoadListner;
    }

    @NonNull
    @Override
    public VIewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.product_layout,parent,false);
        return new VIewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VIewHolder holder, int position) {
        Product product = data.get(position);
        holder.textView.setText("Category:-  "+product.getCategory());
        holder.countview.setText("Count is "+Integer.toString(cnt));

        Glide.with(holder.imageView.getContext()).load(product.getImage()).into(holder.imageView);

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context,"Price is "+product.getPrice(),Toast.LENGTH_SHORT).show();
            }
        });



        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               cnt+=1;
               holder.countview.setText("Count is "+Integer.toString(cnt));
              // addtocart(product);

            }
        });


    }

    void addtocart(Product product)
    {

        DatabaseReference usercart = FirebaseDatabase
                .getInstance()
                .getReference("cart")
                .child("UNIQUE_USER_ID");

        usercart.child(Integer.toString(product.getId()))
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if(dataSnapshot.exists())
                        {
                              CartModel cartModel = dataSnapshot.getValue(CartModel.class);
                              Map<String,Object> updatedata = new HashMap<>();
                              updatedata.put("quantity",cartModel.getQualtity()+1);
                              updatedata.put("totalprice",cartModel.getQualtity()*cartModel.getPrice());

                              usercart.child(Integer.toString(product.getId()))
                                      .updateChildren(updatedata)
                                      .addOnSuccessListener(unused -> {
                                              iCartLoadListner.onCartLoadFailed("This is too Good");
                                      })
                                      .addOnFailureListener(e -> iCartLoadListner.onCartLoadFailed(e.getMessage()));

                        }
                        else
                        {

                            //Float val = (Float) product.getPrice();
                            CartModel cartModel = new CartModel();
                            cartModel.setName(product.getTitle());
                            cartModel.setImage(product.getImage());
                            cartModel.setPrice(product.getPrice());
                            cartModel.setQualtity(1);
                            cartModel.setKye(String.valueOf(product.getId()));
                            cartModel.setTotalprice((Float) product.getPrice());

                            usercart.child(Integer.toString(product.getId()))
                                    .setValue(cartModel)
                                    .addOnSuccessListener(unused -> {
                                        iCartLoadListner.onCartLoadFailed("This is too Good");
                                    })
                                    .addOnFailureListener(e -> iCartLoadListner.onCartLoadFailed(e.getMessage()));
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        iCartLoadListner.onCartLoadFailed(databaseError.getMessage());
                    }
                });
    }



    @Override
    public int getItemCount() {
        return data.size();
    }

    class VIewHolder extends RecyclerView.ViewHolder
    {
        ImageView imageView;
        TextView textView,countview;
        Button button;

        public VIewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = (ImageView) itemView.findViewById(R.id.imageView);
            textView = (TextView) itemView.findViewById(R.id.textView);
           // countview = (TextView) itemView.findViewById(R.id.imag)
            countview = (TextView) itemView.findViewById(R.id.textView2);
            button = (Button)  itemView.findViewById(R.id.button);



        }
    }

}
