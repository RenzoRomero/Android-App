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
import pe.com.smartvet.activities.AboutPetActivity;
import pe.com.smartvet.models.Pet;

public class PetsAdapter extends RecyclerView.Adapter<PetsAdapter.ViewHolder> {
    private List<Pet> petList;

    public PetsAdapter() {

    }
    public PetsAdapter(List<Pet> petList) {
        this.petList = petList;
    }

    @NonNull
    @Override
    public PetsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new PetsAdapter.ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_pet, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(
            PetsAdapter.ViewHolder holder, @SuppressLint("RecyclerView") final int position) {

        holder.photoPetANImageView.setErrorImageResId(R.mipmap.ic_launcher);
        holder.photoPetANImageView.setDefaultImageResId(R.mipmap.ic_launcher);
        holder.photoPetANImageView.setImageUrl(petList.get(position).getPhoto());
        holder.nameTextView.setText(petList.get(position).getName());
        holder.breedTextView.setText(petList.get(position).getBreed());
        holder.genderTextView.setText(petList.get(position).getGender());
        holder.petCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SmartVetApp.getInstance().setPet(petList.get(position));
                v.getContext()
                        .startActivity(new Intent(v.getContext(),
                                AboutPetActivity.class));
            }
        });
    }

    @Override
    public int getItemCount() {
        return petList.size();
    }

    public PetsAdapter setPets(List<Pet> petList) {
        this.petList = petList;
        return this;
    }

    public void updateList(List<Pet> pets) {
        petList = new ArrayList<>();
        petList.addAll(pets);
        notifyDataSetChanged();
    }

    public class ViewHolder extends  RecyclerView.ViewHolder {
        ANImageView photoPetANImageView;
        TextView nameTextView;
        TextView breedTextView;
        TextView genderTextView;
        CardView petCardView;

        public ViewHolder(View itemView) {
            super(itemView);
            photoPetANImageView = itemView.findViewById(R.id.photoPetANImageView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            breedTextView = itemView.findViewById(R.id.breedTextView);
            genderTextView = itemView.findViewById(R.id.genderTextView);
            petCardView = itemView.findViewById(R.id.petCardView);
        }
    }
}
