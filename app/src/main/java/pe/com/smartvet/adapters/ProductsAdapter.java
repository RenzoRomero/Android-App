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
import pe.com.smartvet.models.Product;

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ViewHolder> {
    private List<Product> productList;

    public ProductsAdapter() {

    }
    public ProductsAdapter(List<Product> productList) {
        this.productList = productList;
    }

    @NonNull
    @Override
    public ProductsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_product, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(
            ProductsAdapter.ViewHolder holder, @SuppressLint("RecyclerView") final int position) {

        holder.photoProductANImageView.setErrorImageResId(R.mipmap.ic_launcher);
        holder.photoProductANImageView.setDefaultImageResId(R.mipmap.ic_launcher);
        holder.photoProductANImageView.setImageUrl(productList.get(position).getPhoto());
        holder.nameTextView.setText(productList.get(position).getName());
        holder.descriptionTextView.setText(productList.get(position).getDescription());
        holder.priceTextView.setText("S/." + productList.get(position).getPrice().toString());
        /*holder.productCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SmartVetApp.getInstance().setCurrentPet(pets.get(position));
                v.getContext()
                        .startActivity(new Intent(v.getContext(),
                                AboutPetActivity.class));
            }
        });*/
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public ProductsAdapter setProducts(List<Product> productList) {
        this.productList = productList;
        return this;
    }

    public void updateList(List<Product> products) {
        productList = new ArrayList<>();
        productList.addAll(products);
        notifyDataSetChanged();
    }

    public class ViewHolder extends  RecyclerView.ViewHolder {
        ANImageView photoProductANImageView;
        TextView nameTextView;
        TextView descriptionTextView;
        TextView priceTextView;
        CardView productCardView;

        public ViewHolder(View itemView) {
            super(itemView);
            photoProductANImageView = itemView.findViewById(R.id.photoProductANImageView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            descriptionTextView = itemView.findViewById(R.id.descriptionTextView);
            priceTextView = itemView.findViewById(R.id.priceTextView);
            productCardView = itemView.findViewById(R.id.productCardView);
        }
    }
}
