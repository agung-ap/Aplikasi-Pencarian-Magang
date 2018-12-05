package id.developer.mahendra.pencarianmagangumb.fragment.user;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import id.developer.mahendra.pencarianmagangumb.R;
import id.developer.mahendra.pencarianmagangumb.UserActivity;
import id.developer.mahendra.pencarianmagangumb.adapter.HelpAdapter;
import id.developer.mahendra.pencarianmagangumb.model.Help;

public class HelpFragment extends Fragment {
    private RecyclerView helpList;
    private ArrayList<Help> helpArrayList;
    private HelpAdapter helpAdapterter;
    private Help help;
    private String [] question, answer;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_help, container, false);
        //set actionbar title
        ((UserActivity)getActivity()).getSupportActionBar().setTitle("Help");
        //allow fragment to use menu in action bar
        setHasOptionsMenu(true);
        //init recyclerview
        helpList = (RecyclerView)view.findViewById(R.id.help_list);
        helpList.setLayoutManager(new LinearLayoutManager(getContext()));
        helpList.setHasFixedSize(true);
        //init array list
        helpArrayList = new ArrayList<>();
        //init adapter
        helpAdapterter = new HelpAdapter(getContext());
        //tampilkan help list ke recyclerView
        data();
        //set recyclerview adapter
        helpList.setAdapter(helpAdapterter);

        return view;
    }

    private void data() {
        question =  getResources().getStringArray(R.array.question);
        answer = getResources().getStringArray(R.array.answer);

        for (int i =0; i < question.length; i++){
            //mapping data to data model
            help = new Help(question[i], answer[i]);
            helpArrayList.add(help);
        }

        helpAdapterter.setHelpData(helpArrayList);
    }

}
