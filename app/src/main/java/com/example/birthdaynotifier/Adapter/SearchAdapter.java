package com.example.birthdaynotifier.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.birthdaynotifier.BirthDateSQL;
import com.example.birthdaynotifier.R;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchViewHolder> implements Filterable {
    private List<BirthDateSQL> birthDateList;
    private List<BirthDateSQL> birthDateListFull;


    class SearchViewHolder extends RecyclerView.ViewHolder {
        private TextView nametxt;
        private TextView datetxt;
        private ImageView notificationimg;
        private ImageView firstlatterimg;
        SearchViewHolder(View itemView) {
            super(itemView);

            nametxt = itemView.findViewById(R.id.textView_recyclerViewHome_Name);
            datetxt = itemView.findViewById(R.id.textView_recyclerViewHome_Date);
            notificationimg = itemView.findViewById(R.id.imageView_recyclerViewHome_notificationIcon);
            firstlatterimg = itemView.findViewById(R.id.imageView_recyclerViewHome_firstLatter);
        }
    }



    public SearchAdapter(List<BirthDateSQL> birthDates) {
        this.birthDateList = birthDates;
        birthDateListFull = new ArrayList<>(birthDates);
    }



    @Override
    public Filter getFilter() {
        return searchFilter;
    }

    private Filter searchFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<BirthDateSQL> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(birthDateListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (BirthDateSQL birthdate : birthDateListFull) {
                    if (birthdate.getName().toLowerCase().contains(filterPattern)) {
                        filteredList.add(birthdate);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            birthDateList.clear();
            birthDateList.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };







    @NonNull
    @Override
    public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_home, parent, false);
        return new SearchViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchAdapter.SearchViewHolder holder, int position) {
        BirthDateSQL currentBirthDate = birthDateList.get(position);
        holder.nametxt.setText(currentBirthDate.getName());


        int day = currentBirthDate.getDay();
        int month = currentBirthDate.getMonth();
        long id = currentBirthDate.getId();
        holder.itemView.setTag(id);

        //-------------------------------get the current year---------------------------------------
        int year;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            LocalDate currentDate = LocalDate.now();
            year = currentDate.getYear();
        }else{
            Date today = new Date();
            Calendar cal = Calendar.getInstance();
            cal.setTime(today);
            year = cal.get(Calendar.YEAR);
        }

        String modifyDay ;
        String modifyMonth ;
        if(day<10){
            modifyDay = "0"+day;
        }else{
            modifyDay = Integer.toString(day);
        }

        if(month<10){
            modifyMonth = "0"+month;
        }else{
            modifyMonth = Integer.toString(month);
        }

        String date = modifyDay+"-"+modifyMonth+"-"+year;

        holder.datetxt.setText(date);
        if(currentBirthDate.getNotification()==1){
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

    @Override
    public int getItemCount() {
        return birthDateList.size();
    }
}
