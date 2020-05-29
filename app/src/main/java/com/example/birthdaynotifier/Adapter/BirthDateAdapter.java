package com.example.birthdaynotifier.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.birthdaynotifier.BirthDate;
import com.example.birthdaynotifier.R;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

public class BirthDateAdapter extends ListAdapter<BirthDate,BirthDateAdapter.BirthDateHolder> {

    private OnItemClickListener listener;

    public BirthDateAdapter() {
        super(DIFF_CALLBACK);
    }

    private static final DiffUtil.ItemCallback<BirthDate> DIFF_CALLBACK = new DiffUtil.ItemCallback<BirthDate>() {
        @Override
        public boolean areItemsTheSame(@NonNull BirthDate oldItem, @NonNull BirthDate newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull BirthDate oldItem, @NonNull BirthDate newItem) {
            return oldItem.getName().equals(newItem.getName())&&
                    oldItem.getDay() == newItem.getDay()&&
                    oldItem.getMonth() == newItem.getMonth() &&
                    oldItem.getNotification().equals(newItem.getNotification());
        }
    };

    @NonNull
    @Override
    public BirthDateHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_home, parent, false);
        return new BirthDateHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull BirthDateHolder holder, int position) {
        BirthDate currentBirthDate = getItem(position);
        holder.nametxt.setText(currentBirthDate.getName());
        String date = (currentBirthDate.getDay())+"-"+(currentBirthDate.getMonth());
        holder.datetxt.setText(date);
        if(currentBirthDate.getNotification()){
            holder.notificationimg.setBackgroundResource(R.drawable.ic_notifications_black_24dp);
        }else{
            holder.notificationimg.setBackgroundResource(R.drawable.ic_notifications_off_shadow_24dp);
        }
        String name = currentBirthDate.getName().toUpperCase();
        switch (name.charAt(0)){
            case 'A':
                holder.firstlatterimg.setBackgroundResource(R.drawable.ic_a);
                break;
            case 'B':
                holder.firstlatterimg.setBackgroundResource(R.drawable.ic_b);
                break;
            case 'C':
                holder.firstlatterimg.setBackgroundResource(R.drawable.ic_c);
                break;
            case 'D':
                holder.firstlatterimg.setBackgroundResource(R.drawable.ic_d);
                break;
            case 'E':
                holder.firstlatterimg.setBackgroundResource(R.drawable.ic_e);
                break;
            case 'F':
                holder.firstlatterimg.setBackgroundResource(R.drawable.ic_f);
                break;
            case 'G':
                holder.firstlatterimg.setBackgroundResource(R.drawable.ic_g);
                break;
            case 'H':
                holder.firstlatterimg.setBackgroundResource(R.drawable.ic_h);
                break;
            case 'I':
                holder.firstlatterimg.setBackgroundResource(R.drawable.ic_i);
                break;
            case 'J':
                holder.firstlatterimg.setBackgroundResource(R.drawable.ic_j);
                break;
            case 'K':
                holder.firstlatterimg.setBackgroundResource(R.drawable.ic_k);
                break;
            case 'L':
                holder.firstlatterimg.setBackgroundResource(R.drawable.ic_l);
                break;
            case 'M':
                holder.firstlatterimg.setBackgroundResource(R.drawable.ic_m);
                break;
            case 'N':
                holder.firstlatterimg.setBackgroundResource(R.drawable.ic_n);
                break;
            case 'O':
                holder.firstlatterimg.setBackgroundResource(R.drawable.ic_o);
                break;
            case 'P':
                holder.firstlatterimg.setBackgroundResource(R.drawable.ic_p);
                break;
            case 'Q':
                holder.firstlatterimg.setBackgroundResource(R.drawable.ic_q);
                break;
            case 'R':
                holder.firstlatterimg.setBackgroundResource(R.drawable.ic_r);
                break;
            case 'S':
                holder.firstlatterimg.setBackgroundResource(R.drawable.ic_s);
                break;
            case 'T':
                holder.firstlatterimg.setBackgroundResource(R.drawable.ic_t);
                break;
            case 'U':
                holder.firstlatterimg.setBackgroundResource(R.drawable.ic_u);
                break;
            case 'V':
                holder.firstlatterimg.setBackgroundResource(R.drawable.ic_v);
                break;
            case 'W':
                holder.firstlatterimg.setBackgroundResource(R.drawable.ic_w);
                break;
            case 'X':
                holder.firstlatterimg.setBackgroundResource(R.drawable.ic_x);
                break;
            case 'Y':
                holder.firstlatterimg.setBackgroundResource(R.drawable.ic_y);
                break;
            case 'Z':
                holder.firstlatterimg.setBackgroundResource(R.drawable.ic_z);
                break;
        }
    }


    public BirthDate getBirthDateAt(int position) {
        return getItem(position);
    }

    class BirthDateHolder extends RecyclerView.ViewHolder{
        private TextView nametxt;
        private TextView datetxt;
        private ImageView notificationimg;
        private ImageView firstlatterimg;

        public BirthDateHolder(@NonNull View itemView) {
            super(itemView);
            nametxt = itemView.findViewById(R.id.textView_recyclerViewHome_Name);
            datetxt = itemView.findViewById(R.id.textView_recyclerViewHome_Date);
            notificationimg = itemView.findViewById(R.id.imageView_recyclerViewHome_notificationIcon);
            firstlatterimg = itemView.findViewById(R.id.imageView_recyclerViewHome_firstLatter);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if(listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(getItem(position));
                    }
                }
            });
        }
    }


    public interface OnItemClickListener {
        void onItemClick(BirthDate birthDate);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }
}
