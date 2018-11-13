package id.developer.mahendra.pencarianmagangumb.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.developer.mahendra.pencarianmagangumb.R;
import id.developer.mahendra.pencarianmagangumb.data.model.Magang;

public class MagangListAdapter extends RecyclerView.Adapter<MagangListAdapter.ViewHolder> {
    private Context context;
    private ArrayList<Magang> listMagang;

    private DataListener listener;

    public MagangListAdapter(Context context, DataListener listener) {
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_magang_post, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.titleList.setText(listMagang.get(i).getTitle());
        viewHolder.companyNameList.setText(listMagang.get(i).getCompanyName());
        viewHolder.cityList.setText(listMagang.get(i).getCity());
        viewHolder.salaryList.setText(listMagang.get(i).getSalary());
        viewHolder.requirementList.setText(listMagang.get(i).getRequirement());
    }

    @Override
    public int getItemCount() {
        if (null == listMagang) return 0;
        return listMagang.size();
    }

    public void setMagangData(ArrayList<Magang> data) {
        listMagang = data;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.title_list)
        TextView titleList;
        @BindView(R.id.company_name_list)
        TextView companyNameList;
        @BindView(R.id.city_list)
        TextView cityList;
        @BindView(R.id.salary_list)
        TextView salaryList;
        @BindView(R.id.requirement_list)
        TextView requirementList;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            //set item to click
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            listener.onClick(listMagang.get(position));
        }
    }

    //Membuat Interface
    public interface DataListener {
        void onClick(Magang dataPosition);
    }
}
