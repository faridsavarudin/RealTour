package id.dsc.realtour.ui.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import id.dsc.realtour.R;
import id.dsc.realtour.data.model.ParentContentJava;

public class SpotVRAdapter extends RecyclerView.Adapter<SpotVRAdapter.MyViewHolder> {
    private List<ParentContentJava> itemList;
    private Context context;
    private VrListener mListener;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textTitle;
        View view;
        LinearLayout container;

        public MyViewHolder(View view) {
            super(view);
            textTitle = (TextView) view.findViewById(R.id.textTitle);
            this.view = (View) view.findViewById(R.id.view);
            container = (LinearLayout) view.findViewById(R.id.container);
        }
    }


    public SpotVRAdapter(List<ParentContentJava> itemList, Context context, VrListener mListener) {
        this.itemList = itemList;
        this.context = context;
        this.mListener = mListener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_list_vr, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        if (itemList.size()==1){
            holder.container.setVisibility(View.GONE);
        }else {
            holder.container.setVisibility(View.VISIBLE);
        }

        holder.itemView.setOnClickListener(view1 -> mListener.onSelected(position));

        holder.view.setVisibility(View.VISIBLE);
        if (itemList.get(position).getName()!=null){
        holder.textTitle.setText(itemList.get(position).getName());
        }else {

        }

        if ((itemList.size())-1 == position){
            holder.view.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

}
