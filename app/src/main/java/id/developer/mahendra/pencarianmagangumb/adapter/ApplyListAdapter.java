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
import id.developer.mahendra.pencarianmagangumb.model.UsersApply;


public class ApplyListAdapter extends RecyclerView.Adapter<ApplyListAdapter.ViewHolder> {
    private Context context;
    private ArrayList<UsersApply> usersApplyList;
    private DataListener listener;

    public ApplyListAdapter(Context context, DataListener listener) {
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_magang_notification, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int i) {
        holder.userName.setText(usersApplyList.get(i).getUserName());
        holder.titleJob.setText("Jobs : " + usersApplyList.get(i).getTitle());
        holder.companyJob.setText("Company : " + usersApplyList.get(i).getCompany());
    }

    @Override
    public int getItemCount() {
        if (null == usersApplyList) return 0;
        return usersApplyList.size();
    }

    public void setNotificationData(ArrayList<UsersApply> usersApplyList) {
        this.usersApplyList = usersApplyList;
        notifyDataSetChanged();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.nama_user_apply)
        TextView userName;
        @BindView(R.id.title_apply)
        TextView titleJob;
        @BindView(R.id.company_name_apply)
        TextView companyJob;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            listener.onClick(usersApplyList.get(getAdapterPosition()));
        }
    }

    //Membuat Interface
    public interface DataListener {
        void onClick(UsersApply dataPosition);
    }
}
