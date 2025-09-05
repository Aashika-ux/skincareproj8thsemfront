package com.example.skincare.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.skincare.R;
import com.example.skincare.model.Doctor;

import java.util.ArrayList;
import java.util.List;

public class DoctorAdapter extends RecyclerView.Adapter<DoctorAdapter.DoctorViewHolder> {

    private final Context context;
    private List<Doctor> doctorList = new ArrayList<>();
    private OnItemClickListener onItemClickListener;

    public DoctorAdapter(Context context) {
        this.context = context;
    }

    public void setDoctorList(List<Doctor> doctorList) {
        if (doctorList != null) {
            this.doctorList = doctorList;
            notifyDataSetChanged();
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Doctor doctor);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    @NonNull
    @Override
    public DoctorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_doctor, parent, false);
        return new DoctorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DoctorViewHolder holder, int position) {
        Doctor doctor = doctorList.get(position);

        holder.tvName.setText(doctor.getName() != null ? doctor.getName() : "Unknown");
        holder.tvSpecialization.setText(doctor.getSpecialization() != null ? doctor.getSpecialization() : "Specialist");

        // Load default doctor image
        Glide.with(context)
                .load(R.drawable.profilepicture)
                .circleCrop()
                .placeholder(R.drawable.profilepicture)
                .into(holder.imgDoctor);

        holder.itemView.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(doctor);
            }
        });
    }

    @Override
    public int getItemCount() {
        return doctorList.size();
    }

    static class DoctorViewHolder extends RecyclerView.ViewHolder {
        final ImageView imgDoctor;
        final TextView tvName, tvSpecialization;

        public DoctorViewHolder(@NonNull View itemView) {
            super(itemView);
            imgDoctor = itemView.findViewById(R.id.img_doctor);
            tvName = itemView.findViewById(R.id.tv_doctor_name);
            tvSpecialization = itemView.findViewById(R.id.tv_doctor_specialization);
        }
    }
}
