package com.projects.notdefterim;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> implements Filterable {

    private ArrayList<Not> notList;
    private ArrayList<Not> filteredNotList;
    private Context context;
    private CustomItemClickListener customItemClickListener;

    private CustomItemLongClickListener customItemLongClickListener;///

    public CustomAdapter(Context context, ArrayList<Not> notArrayList,
                         CustomItemClickListener customItemClickListener, CustomItemLongClickListener customItemLongClickListener) {
        this.context = context;
        this.notList = notArrayList;
        this.filteredNotList = notArrayList;
        this.customItemClickListener = customItemClickListener;
        this.customItemLongClickListener = customItemLongClickListener;///
    }

    @Override
    public CustomAdapter.MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.not_list_item, viewGroup, false);
        final MyViewHolder myViewHolder = new MyViewHolder(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // for click item listener
                customItemClickListener.onItemClick(filteredNotList.get(myViewHolder.getAdapterPosition()), myViewHolder.getAdapterPosition());
            }
        });

        view.setOnLongClickListener(new View.OnLongClickListener() {///
            @Override
            public boolean onLongClick(View view) {
                customItemLongClickListener.onItemLongClick(filteredNotList.get(myViewHolder.getAdapterPosition()), myViewHolder.getAdapterPosition());
                return false;
            }
        });

        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(CustomAdapter.MyViewHolder viewHolder, int position) {
        viewHolder.baslik.setText(filteredNotList.get(position).getBaslik());
        viewHolder.icerik.setText(filteredNotList.get(position).getIcerik());
        viewHolder.tarih.setText(filteredNotList.get(position).getTarih());
        viewHolder.saat.setText(filteredNotList.get(position).getSaat());
    }

    @Override
    public int getItemCount() {
        return filteredNotList.size();
    }

    public void setNotList(ArrayList<Not> notList) {
        this.filteredNotList = notList;
        this.notList = notList;
        notifyDataSetChanged();
    }

    @Override
    public Filter getFilter() {

        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {

                String searchString = charSequence.toString();

                if (searchString.isEmpty()) {
                    filteredNotList = notList;
                } else {
                    ArrayList<Not> tempFilteredList = new ArrayList<>();

                    for (Not not : notList) {
                        // search for user name
                        if (not.getBaslik().toLowerCase().contains(searchString)) {
                            tempFilteredList.add(not);
                        }
                    }

                    filteredNotList = tempFilteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredNotList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                filteredNotList = (ArrayList<Not>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
        private TextView baslik;
        private TextView icerik;
        private TextView tarih;
        private TextView saat;

        public MyViewHolder(View view) {
            super(view);
            baslik = (TextView) view.findViewById(R.id.baslik);
            icerik = (TextView) view.findViewById(R.id.icerik);
            tarih = (TextView) view.findViewById(R.id.tarih);
            saat = (TextView) view.findViewById(R.id.saat);
            view.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            //menu.setHeaderTitle("Kelimeye Uygula");
            menu.add(0, v.getId(), 0, "Sil");//groupId, itemId, order, title
            menu.add(0, v.getId(), 0, "Kopyala");
            menu.add(0, v.getId(), 0, "Paylaş");
        }
    }
}
