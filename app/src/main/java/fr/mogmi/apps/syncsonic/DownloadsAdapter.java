package fr.mogmi.apps.syncsonic;

import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

/**
 * Created by mogmi on 25/03/17.
 */

public class DownloadsAdapter extends RecyclerView.Adapter<DownloadsAdapter.DownloadViewHolder> {

    private ArrayList<SyncedItem> items = new ArrayList<>();

    public DownloadsAdapter(ArrayList<SyncedItem> items) {
        this.items = items;
    }

    @Override
    public DownloadViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = View.inflate(parent.getContext(), R.layout.download_item, null);
        return new DownloadViewHolder(v);
    }

    @Override
    public void onBindViewHolder(DownloadViewHolder holder, int position) {
        SyncedItem item = items.get(position);
        holder.title.setText(item.artist);
        holder.text.setText(item.title);
        if (item.isNew) {
            holder.text.setTextColor(Color.parseColor("#43a047"));
            holder.title.setTextColor(Color.parseColor("#43a047"));
            holder.iconOk.setVisibility(View.GONE);
            holder.iconSync.setVisibility(View.VISIBLE);
        } else {
            holder.text.setTextColor(Color.BLACK);
            holder.title.setTextColor(Color.BLACK);
            holder.iconOk.setVisibility(View.VISIBLE);
            holder.iconSync.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class DownloadViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView text;
        ImageView iconOk;
        ImageView iconSync;

        DownloadViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.download_item_title);
            text = (TextView) itemView.findViewById(R.id.download_item_text);
            iconOk = (ImageView) itemView.findViewById(R.id.download_item_icon_done);
            iconSync = (ImageView) itemView.findViewById(R.id.download_item_icon_sync);
        }
    }
}
