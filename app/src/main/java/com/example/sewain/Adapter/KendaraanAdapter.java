package com.example.sewain.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.sewain.R;
import com.example.sewain.ui.cari_kendaraan.CariKendaraanViewModel;

import java.util.ArrayList;

public class KendaraanAdapter extends RecyclerView.Adapter <KendaraanAdapter.CustomViewHolder> {
    LayoutInflater mInflater;
    ArrayList<CariKendaraanViewModel> kendaraan;
    TextView mNamaKendaraan;
    TextView mHargaSewa;
    TextView mLokasi;
    ImageView mFotoKendaraan;
    CardView cardView;
    Context context;

    public KendaraanAdapter(Context context,
                                  ArrayList<CariKendaraanViewModel> kendaraan) {
        this.mInflater = LayoutInflater.from(context);
        this.kendaraan = kendaraan;
        this.context = context;
    }


    @NonNull
    @Override
    public KendaraanAdapter.CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Create view from layout
        View itemView = mInflater.inflate(
                R.layout.item_kendaraan, parent, false);
        return new CustomViewHolder(itemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull KendaraanAdapter.CustomViewHolder holder, int position) {
// Retrieve the data for that position
        final CariKendaraanViewModel current = kendaraan.get(position);
// Add the data to the view
        Glide.with(context).load(current.getFoto()).into(mFotoKendaraan);
//        mFotoKendaraan.setBackground(ContextCompat.getDrawable(mInflater.getContext(), current.get));
        mNamaKendaraan.setText(current.getNama_kendaraan());
        mHargaSewa.setText(current.getHarga_sewa());
        mLokasi.setText(current.getLokasi());
//        cardView.setVisibility(current.visibility);
// add the Listener to the view of that position if desired
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mInflater.getContext(), current.getNama_kendaraan(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        // Return the number of
// data items to display
        return kendaraan.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {

        private KendaraanAdapter mAdapter;

        public CustomViewHolder(View itemView, KendaraanAdapter adapter) {
            super(itemView);
            // Get the layout
            mNamaKendaraan = (TextView) itemView.findViewById(R.id.txtNamaKendaraan);
            mHargaSewa = (TextView) itemView.findViewById(R.id.txtHargwaSewa);
            mLokasi = (TextView) itemView.findViewById(R.id.txtLokasi);
            mFotoKendaraan = (ImageView) itemView.findViewById(R.id.imgFotoKendaraan);
            cardView = (CardView) itemView.findViewById(R.id.cardView);
// Associate with this adapter
            this.mAdapter = adapter;
// Add click listener, if desired
        }
    }
}