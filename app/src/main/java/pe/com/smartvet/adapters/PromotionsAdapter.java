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

import pe.com.smartvet.models.Promotion;

public class PromotionsAdapter extends RecyclerView.Adapter<PromotionsAdapter.ViewHolder> {
    private List<Promotion> promotionList;

    public PromotionsAdapter() {

    }
    public PromotionsAdapter(List<Promotion> promotionList) {
        this.promotionList = promotionList;
    }

    @NonNull
    @Override
    public PromotionsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new PromotionsAdapter.ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_promotion, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(
            PromotionsAdapter.ViewHolder holder, @SuppressLint("RecyclerView") final int position) {

        holder.photoPromotionANImageView.setErrorImageResId(R.mipmap.ic_launcher);
        holder.photoPromotionANImageView.setDefaultImageResId(R.mipmap.ic_launcher);
        holder.photoPromotionANImageView.setImageUrl(promotionList.get(position).getPhoto());
        holder.nameTextView.setText(promotionList.get(position).getName());
        holder.descriptionTextView.setText(promotionList.get(position).getDescription());
        holder.startDateTextView.setText(promotionList.get(position).getStartDate());
        holder.endingDateTextView.setText(promotionList.get(position).getEndingDate());
    }

    @Override
    public int getItemCount() {
        return promotionList.size();
    }

    public PromotionsAdapter setPromotions(List<Promotion> promotionList) {
        this.promotionList = promotionList;
        return this;
    }

    public void updateList(List<Promotion> promotions) {
        promotionList = new ArrayList<>();
        promotionList.addAll(promotions);
        notifyDataSetChanged();
    }

    public class ViewHolder extends  RecyclerView.ViewHolder {
        ANImageView photoPromotionANImageView;
        TextView nameTextView;
        TextView descriptionTextView;
        TextView startDateTextView;
        TextView endingDateTextView;
        CardView promotionCardView;

        public ViewHolder(View itemView) {
            super(itemView);
            photoPromotionANImageView = itemView.findViewById(R.id.photoPromotionANImageView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            descriptionTextView = itemView.findViewById(R.id.descriptionTextView);
            startDateTextView = itemView.findViewById(R.id.startDateTextView);
            endingDateTextView = itemView.findViewById(R.id.endingDateTextView);
            promotionCardView = itemView.findViewById(R.id.promotionCardView);
        }
    }
}
