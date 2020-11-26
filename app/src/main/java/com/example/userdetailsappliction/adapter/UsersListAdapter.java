package com.example.userdetailsappliction.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.userdetailsappliction.R;
import com.example.userdetailsappliction.activity.UserDetailsActivity;
import com.example.userdetailsappliction.model.UsersListModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class UsersListAdapter extends RecyclerView.Adapter<UsersListAdapter.ViewHolder> implements Filterable {
    private final Context context;
    private final List<UsersListModel> listRecyclerItem;
    private List<UsersListModel> userListFiltered;
    private UserAdapterListener listener;

    public UsersListAdapter(Context context, List<UsersListModel> listRecyclerItem, UserAdapterListener listener) {
        this.context = context;
        this.listRecyclerItem = listRecyclerItem;
        this.listener = listener;
        this.userListFiltered = listRecyclerItem;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final UsersListAdapter.ViewHolder holder, int position) {

        final UsersListModel listItem = userListFiltered.get(position);

        holder.name.setText(listItem.getName());
        holder.name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, UserDetailsActivity.class);
                intent.putExtra("name", (Serializable) holder.name.getText());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return userListFiltered.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    userListFiltered = listRecyclerItem;
                } else {
                    List<UsersListModel> filteredList = new ArrayList<>();
                    for (UsersListModel row : listRecyclerItem) {
                        if (row.getName().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }
                    userListFiltered = filteredList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = userListFiltered;
                return filterResults;
            }
            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                userListFiltered = (ArrayList<UsersListModel>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public interface UserAdapterListener {
        void onUserSelected(UsersListModel user);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView name;

        public ViewHolder(View itemView) {
            super(itemView);

            name = (TextView) itemView.findViewById(R.id.name);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // send selected contact in callback
                    listener.onUserSelected(userListFiltered.get(getAdapterPosition()));
                }
            });

        }
    }


}

