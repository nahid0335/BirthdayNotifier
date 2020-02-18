package com.example.birthdaynotifier.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.birthdaynotifier.BirthDate;
import com.example.birthdaynotifier.R;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class BirthDateAdapter extends RecyclerView.Adapter<BirthDateAdapter.BirthDateHolder> {
    private List<BirthDate> birthDates = new ArrayList<>();


    @NonNull
    @Override
    public BirthDateHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_home, parent, false);
        return new BirthDateHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull BirthDateHolder holder, int position) {
        BirthDate currentBirthDate = birthDates.get(position);
        holder.nametxt.setText(currentBirthDate.getName());
        holder.timetxt.setText(currentBirthDate.getTime());
        String date = (currentBirthDate.getDay())+"-"+(currentBirthDate.getMonth());
        holder.datetxt.setText(date);
        if(currentBirthDate.getNotification()){
            holder.notificationimg.setBackgroundResource(R.drawable.ic_notifications_black_24dp);
        }else{
            holder.notificationimg.setBackgroundResource(R.drawable.ic_notifications_off_shadow_24dp);
        }
    }

    @Override
    public int getItemCount() {
        return birthDates.size();
    }

    public void setBirthDates(List<BirthDate>birthDates){
        this.birthDates = birthDates ;
        notifyDataSetChanged();
    }

    class BirthDateHolder extends RecyclerView.ViewHolder{
        private TextView nametxt;
        private TextView timetxt;
        private TextView datetxt;
        private ImageView notificationimg;
        private ImageView firstlatterimg;

        public BirthDateHolder(@NonNull View itemView) {
            super(itemView);
            nametxt = itemView.findViewById(R.id.textView_recyclerViewHome_Name);
            timetxt = itemView.findViewById(R.id.textView_recyclerViewHome_Time);
            datetxt = itemView.findViewById(R.id.textView_recyclerViewHome_Date);
            notificationimg = itemView.findViewById(R.id.imageView_recyclerViewHome_notificationIcon);
            firstlatterimg = itemView.findViewById(R.id.imageView_recyclerViewHome_firstLatter);
        }
    }
}
