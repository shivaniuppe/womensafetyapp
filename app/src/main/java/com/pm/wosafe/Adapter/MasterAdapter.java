package com.pm.wosafe.Adapter;


import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import com.pm.wosafe.Activity.EmergencyContactNumber;
import com.pm.wosafe.Activity.RegisterActivity;
import com.pm.wosafe.Model.MasterModel;
import com.pm.wosafe.R;


public class MasterAdapter extends RecyclerView.Adapter<MasterAdapter.ViewHolder>{
    private List<MasterModel> shops;
    private Context mContext;

    public MasterAdapter(List<MasterModel> lists, Context mContext) {
        this.shops = lists;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.promoters_list,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MasterModel sp=shops.get(position);
        holder.promoter_bid.setText(sp.getFamilyType());
        holder.promoter_name.setText(sp.getName());
        holder.promoter_address.setText(sp.getNumber());


        if (sp.isImportant()){
            holder.important.setVisibility(View.VISIBLE);
        }

        /*holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(mContext, Dashboard.class);
                *//*intent.putExtra("shopId", sp.getShopId());
                intent.putExtra("shopName", sp.getShopName());
                intent.putExtra("shopType", sp.getShopType());
                intent.putExtra("shopNumber", sp.getShopNumber());
                intent.putExtra("shopLocation", sp.getShopLocation());
                intent.putExtra("shopImage", sp.getShopPhoto());

                intent.putExtra("shopLatitude", sp.getLatitude());
                intent.putExtra("shopLangitude", sp.getLongitude());
                intent.putExtra("shopAbout", sp.getShopAbout());*//*
                mContext.startActivity(intent);
            }
        });*/
        holder.Promoter_more.setOnClickListener(v-> {
            SharedPreferences sharedPreferences = mContext.getSharedPreferences(RegisterActivity.MyPREFERENCES, Context.MODE_PRIVATE);
            String userObject = sharedPreferences.getString(RegisterActivity.NUm, null);

            boolean aj= true;
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Contacts");
            ref.child(userObject).orderByChild("number").equalTo(sp.getNumber())
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                        {
                            for (DataSnapshot ds : dataSnapshot.getChildren())
                            {
                                ds.getRef().removeValue();

                                if (ds.hasChild("important")) {
                                    SharedPreferences sharedPreferences = mContext.getSharedPreferences(EmergencyContactNumber.MyPREFERENCES, Context.MODE_PRIVATE);
                                    sharedPreferences.edit().remove(EmergencyContactNumber.NumberContacts).commit();
                                    Toast.makeText(mContext, "Deleted", Toast.LENGTH_SHORT).show();
                                }else {
                                    Toast.makeText(mContext, "Imporatant is available", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError)
                        {
                            Toast.makeText(mContext, "Failed to delete", Toast.LENGTH_SHORT).show();
                        }
                    });
        });
        holder.CallEmergency.setOnClickListener(v-> {

            Toast.makeText(mContext, sp.getNumber(), Toast.LENGTH_SHORT).show();
        });

    }

    @Override
    public int getItemCount() {
        return shops.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView promoter_bid,promoter_name, promoter_address, important;
        private ImageView CallEmergency, Promoter_more;

        public ViewHolder(View itemView) {
            super(itemView);

            promoter_name=(TextView)itemView.findViewById(R.id.promoter_name);
            promoter_address=(TextView)itemView.findViewById(R.id.promoter_address);
            promoter_bid=(TextView)itemView.findViewById(R.id.promoter_bid);
            important=(TextView)itemView.findViewById(R.id.important);


            CallEmergency=(ImageView)itemView.findViewById(R.id.calls);
            Promoter_more=(ImageView)itemView.findViewById(R.id.more);
        }
    }
}
