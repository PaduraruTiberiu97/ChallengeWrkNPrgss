package com.example.challengewrknprgs.ui.alllocations;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.challengewrknprgs.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class LocationsRecyclerViewAdapter extends RecyclerView.Adapter<LocationsRecyclerViewAdapter.LocationViewHolder> implements Filterable {
    private ArrayList<Locations> locationsArrayList;
    private ArrayList<Locations> locations;

    public static class LocationViewHolder extends RecyclerView.ViewHolder {
        TextView txtLocation;
        TextView txtLat;
        TextView txtLng;
        ImageView imgLocation;

        public LocationViewHolder(@NonNull View itemView) {
            super(itemView);
            txtLocation = itemView.findViewById(R.id.txt_name_all_locations);
            txtLat = itemView.findViewById(R.id.txt_lat_all_locations);
            txtLng = itemView.findViewById(R.id.txt_long_all_locations);
            imgLocation = itemView.findViewById(R.id.img_all_locations);
        }
    }

    public LocationsRecyclerViewAdapter(ArrayList<Locations> locationsArrayList) {
        this.locationsArrayList = locationsArrayList;
        locations = new ArrayList<>(locationsArrayList);
    }

    @NonNull
    @Override
    public LocationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_item, parent, false);
        return new LocationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LocationViewHolder holder, int position) {
        final Locations currentLocation = locationsArrayList.get(position);
        holder.txtLocation.setText(currentLocation.getLabel() + " - " + currentLocation.getAddress());
        Picasso.get().load(currentLocation.getImage()).into(holder.imgLocation);
        holder.txtLat.setText("Latitude: " + currentLocation.getLat());
        holder.txtLng.setText("Longitude: " + currentLocation.getLng());

    }

    @Override
    public int getItemCount() {
        return locationsArrayList.size();
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    private Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<Locations> filteredList = new ArrayList<>();

            if (charSequence == null || charSequence.length() == 0) {
                filteredList.addAll(locations);
            } else {
                String filterPattern = charSequence.toString().toLowerCase().trim();

                for (Locations item : locations) {
                    if (item.getLabel().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            locationsArrayList.clear();
            locationsArrayList.addAll((List) filterResults.values);
            notifyDataSetChanged();
        }
    };

}
