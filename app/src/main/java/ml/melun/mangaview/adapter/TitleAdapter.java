package ml.melun.mangaview.adapter;
import android.content.Context;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;


import ml.melun.mangaview.Preference;
import ml.melun.mangaview.R;
import ml.melun.mangaview.customViews.WrapContentDraweeView;
import ml.melun.mangaview.mangaview.Title;

public class TitleAdapter extends RecyclerView.Adapter<TitleAdapter.ViewHolder> {

    private ArrayList<Title> mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private Context mainContext;
    Boolean dark = false;
    Boolean save;
    Boolean resume = true;
    Boolean updated = false;
    String path = "";
    Preference p;

    public TitleAdapter(Context context) {
        p = new Preference(context);
        dark = p.getDarkTheme();
        save = p.getDataSave();
        this.mInflater = LayoutInflater.from(context);
        mainContext = context;
        this.mData = new ArrayList<>();
    }
    public void removeAll(){
        int originSize = mData.size();
        mData.clear();
        notifyItemRangeRemoved(0,originSize);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_title, parent, false);
        return new ViewHolder(view);
    }
    public void addData(List<Title> t){
        int oSize = mData.size();
        for(Title d:t){
            d.setBookmark(p.getBookmark(d.getName()));
        }
        mData.addAll(t);
        notifyItemRangeInserted(oSize,t.size());
    }
    public void moveItemToTop(int from){
        mData.add(0, mData.get(from));
        mData.remove(from+1);
        for(int i= from; i>0; i--){
            notifyItemMoved(i,i-1);
        }
    }

    public void remove(int pos){
        mData.remove(pos);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Title data = mData.get(position);
        String title = data.getName();
        String thumb = data.getThumb();
        String author = data.getAuthor();
        String tags ="";
        int bookmark = data.getBookmark();
        for(String s:data.getTags()){
            tags+=s+" ";
        }
        holder.name.setText(title);
        holder.author.setText(author);
        holder.tags.setText(tags);
        if(thumb.length()>1 && !save){
            holder.thumb.setImageURI(thumb);
        }
        else holder.thumb.setImageBitmap(null);
        if(bookmark>0 && resume) holder.resume.setVisibility(View.VISIBLE);
        else holder.resume.setVisibility(View.GONE);

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView name;
        WrapContentDraweeView thumb;
        TextView author;
        TextView tags;
        Button resume;
        CardView card;

        ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.Title);
            thumb = itemView.findViewById(R.id.Thumb);
            author =itemView.findViewById(R.id.TitleAuthor);
            tags = itemView.findViewById(R.id.TitleTag);
            card = itemView.findViewById(R.id.titleCard);
            resume = itemView.findViewById(R.id.epsButton);


            if(dark){
                card.setBackgroundColor(ContextCompat.getColor(mainContext, R.color.colorDarkBackground));
                resume.setBackgroundColor(ContextCompat.getColor(mainContext, R.color.resumeDark));
            }
            card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mClickListener.onItemClick(getAdapterPosition());
                }
            });
            card.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mClickListener.onLongClick(v, getAdapterPosition());
                    return true;
                }
            });
            resume.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mClickListener.onResumeClick(getAdapterPosition(), p.getBookmark(mData.get(getAdapterPosition()).getName()));
                }
            });
        }
    }

    public void noResume(){
        resume = false;
    }
    public Title getItem(int id) {
        return mData.get(id);
    }

    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onItemClick(int position);
        void onLongClick(View view, int position);
        void onResumeClick(int position, int id);
    }
}