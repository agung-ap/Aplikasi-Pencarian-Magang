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
import id.developer.mahendra.pencarianmagangumb.model.Help;

public class HelpAdapter extends RecyclerView.Adapter<HelpAdapter.ViewHolder> {
    private Context context;
    private ArrayList<Help> helpArrayList;

    public HelpAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_help, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.answer.setText(helpArrayList.get(i).getAnswer());
        viewHolder.question.setText(helpArrayList.get(i).getQuestion());
    }

    @Override
    public int getItemCount() {
        if (null == helpArrayList) return 0;
        return helpArrayList.size();
    }

    public void setHelpData(ArrayList<Help> helpArrayList) {
        this.helpArrayList = helpArrayList;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.question)
        TextView question;
        @BindView(R.id.answer)
        TextView answer;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
