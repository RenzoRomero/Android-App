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
import pe.com.smartvet.activities.AboutClinicHistoryActivity;
import pe.com.smartvet.models.ClinicHistory;

public class ClinicalHistoriesAdapter extends RecyclerView.Adapter<ClinicalHistoriesAdapter.ViewHolder> {
    private List<ClinicHistory> clinicalHistoriesList;

    public ClinicalHistoriesAdapter() {

    }
    public ClinicalHistoriesAdapter(List<ClinicHistory> clinicalHistoriesList) {
        this.clinicalHistoriesList = clinicalHistoriesList;
    }

    @NonNull
    @Override
    public ClinicalHistoriesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ClinicalHistoriesAdapter.ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_owner, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(
            ClinicalHistoriesAdapter.ViewHolder holder, @SuppressLint("RecyclerView") final int position) {

        holder.photoClinicHistoryANImageView.setErrorImageResId(R.mipmap.ic_launcher);
        holder.photoClinicHistoryANImageView.setDefaultImageResId(R.mipmap.ic_launcher);
        holder.photoClinicHistoryANImageView.setImageUrl(clinicalHistoriesList.get(position).getPet().getPhoto());
        holder.detailsTextView.setText(clinicalHistoriesList.get(position).getDetails());
        holder.photoClinicHistoryANImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SmartVetApp.getInstance().setClinicHistory(clinicalHistoriesList.get(position));
                v.getContext()
                        .startActivity(new Intent(v.getContext(),
                                AboutClinicHistoryActivity.class));
            }
        });
    }

    @Override
    public int getItemCount() {
        return clinicalHistoriesList.size();
    }

    public ClinicalHistoriesAdapter setClinicalHistories(List<ClinicHistory> clinicalHistoriesList) {
        this.clinicalHistoriesList = clinicalHistoriesList;
        return this;
    }

    public void updateList(List<ClinicHistory> owners) {
        clinicalHistoriesList = new ArrayList<>();
        clinicalHistoriesList.addAll(owners);
        notifyDataSetChanged();
    }

    public class ViewHolder extends  RecyclerView.ViewHolder {
        ANImageView photoClinicHistoryANImageView;
        TextView detailsTextView;
        CardView clinicHistoryCardView;

        public ViewHolder(View itemView) {
            super(itemView);
            photoClinicHistoryANImageView = itemView.findViewById(R.id.photoClinicHistoryANImageView);
            detailsTextView = itemView.findViewById(R.id.detailsTextView);
            clinicHistoryCardView = itemView.findViewById(R.id.clinicHistoryCardView);
        }
    }
}
