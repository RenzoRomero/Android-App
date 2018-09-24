package pe.com.smartvet.adapters;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.androidnetworking.widget.ANImageView;

import java.util.ArrayList;
import java.util.List;

import pe.com.smartvet.R;
import pe.com.smartvet.SmartVetApp;
import pe.com.smartvet.activities.AboutOwnerActivity;
import pe.com.smartvet.models.Owner;

public class OwnersAdapter extends RecyclerView.Adapter<OwnersAdapter.ViewHolder> {
    private List<Owner> ownerList;

    public OwnersAdapter() {

    }
    public OwnersAdapter(List<Owner> ownerList) {
        this.ownerList = ownerList;
    }

    @NonNull
    @Override
    public OwnersAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new OwnersAdapter.ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_owner, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(
            OwnersAdapter.ViewHolder holder, @SuppressLint("RecyclerView") final int position) {

        holder.photoOwnerANImageView.setErrorImageResId(R.mipmap.ic_launcher);
        holder.photoOwnerANImageView.setDefaultImageResId(R.mipmap.ic_launcher);
        holder.photoOwnerANImageView.setImageUrl(ownerList.get(position).getPhoto());
        holder.nameTextView.setText(ownerList.get(position).getName());
        holder.mobilePhoneTextView.setText(ownerList.get(position).getMobilePhone().toString());
        holder.emailTextView.setText(ownerList.get(position).getEmail());
        holder.ownerCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SmartVetApp.getInstance().setOwner(ownerList.get(position));
                v.getContext()
                        .startActivity(new Intent(v.getContext(),
                                AboutOwnerActivity.class));
            }
        });
    }

    @Override
    public int getItemCount() {
        return ownerList.size();
    }

    public OwnersAdapter setOwners(List<Owner> ownerList) {
        this.ownerList = ownerList;
        return this;
    }

    public void updateList(List<Owner> owners) {
        ownerList = new ArrayList<>();
        ownerList.addAll(owners);
        notifyDataSetChanged();
    }

    public class ViewHolder extends  RecyclerView.ViewHolder {
        ANImageView photoOwnerANImageView;
        TextView nameTextView;
        TextView mobilePhoneTextView;
        TextView emailTextView;
        CardView ownerCardView;

        public ViewHolder(View itemView) {
            super(itemView);
            photoOwnerANImageView = itemView.findViewById(R.id.photoOwnerANImageView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            mobilePhoneTextView = itemView.findViewById(R.id.mobilePhoneTextView);
            emailTextView = itemView.findViewById(R.id.emailTextView);
            ownerCardView = itemView.findViewById(R.id.ownerCardView);
        }
    }
}
