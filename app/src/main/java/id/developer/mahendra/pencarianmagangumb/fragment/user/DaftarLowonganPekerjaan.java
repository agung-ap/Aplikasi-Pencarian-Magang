package id.developer.mahendra.pencarianmagangumb.fragment.user;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import id.developer.mahendra.pencarianmagangumb.EditProfilUser;
import id.developer.mahendra.pencarianmagangumb.MagangDetailUserActivity;
import id.developer.mahendra.pencarianmagangumb.MagangPost;
import id.developer.mahendra.pencarianmagangumb.UserActivity;
import id.developer.mahendra.pencarianmagangumb.R;
import id.developer.mahendra.pencarianmagangumb.adapter.MagangListAdapter;
import id.developer.mahendra.pencarianmagangumb.data.model.Magang;
import id.developer.mahendra.pencarianmagangumb.util.Constant;

public class DaftarLowonganPekerjaan extends Fragment implements MagangListAdapter.DataListener{
    private static final String TAG = DaftarLowonganPekerjaan.class.getSimpleName();
    private FirebaseAuth auth;
    private DatabaseReference databaseReference;

    private TextView emptyMessage;
    private RecyclerView recyclerView;
    private MagangListAdapter magangListAdapter;
    private ArrayList<Magang> magangArrayList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_daftar_lowongan_kerja, container, false);

        ((UserActivity)getActivity()).getSupportActionBar().setTitle("Home");
        setHasOptionsMenu(true);

        auth = FirebaseAuth.getInstance();

        emptyMessage = (TextView)view.findViewById(R.id.empty_message_user);
        recyclerView = (RecyclerView)view.findViewById(R.id.magang_list_user);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);

        magangArrayList = new ArrayList<>();
        magangListAdapter = new MagangListAdapter(getActivity(), this);

        getData();
        //add Adapter to RecyclerView
        recyclerView.setAdapter(magangListAdapter);

        return view;
    }

    private void searchMagangPost(Menu menu){
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.search_magang));
        searchView.setQueryHint("Yuk Cari Magang");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                searchMagangByTitle(query);
                Toast.makeText(getActivity(), query, Toast.LENGTH_SHORT).show();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        MenuItem menuItem = menu.findItem(R.id.search_magang);
        menuItem.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem menuItem) {
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem menuItem) {
                magangArrayList.clear();
                getData();
                //add Adapter to RecyclerView
                recyclerView.setAdapter(magangListAdapter);
                return true;
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.user, menu);

        searchMagangPost(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.add_magang_info) {
            Intent intent = new Intent(getActivity(), MagangPost.class);
            startActivityForResult(intent, EditProfilUser.REQUEST_ADD);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void getData(){
        databaseReference = FirebaseDatabase.getInstance().getReference(Constant.MAGANG_POSTING);
        databaseReference
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        magangArrayList.clear();

                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            //Mapping data pada DataSnapshot ke dalam objek mahasiswa
                            Magang posting = snapshot.getValue(Magang.class);

                            posting.setKey(snapshot.getKey());
                            magangArrayList.add(posting);
                        }
                        if (magangArrayList.size() == 0) {
                            emptyMessage.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.GONE);
                        } else {
                            emptyMessage.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);
                            //init list data to adapter
                            magangListAdapter.setMagangData(magangArrayList);
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    @Override
    public void onClick(Magang dataPosition) {
        Bundle bundle = new Bundle();

        ArrayList<Magang> magangModel = new ArrayList<>();
        magangModel.add(dataPosition);
        //put parcelable
        bundle.putParcelableArrayList(getString(R.string.GET_SELECTED_ITEM), magangModel);
        //send data via intent
        Intent intent = new Intent(this.getActivity(), MagangDetailUserActivity.class);
        intent.putExtras(bundle);
        //intent.putExtra("user status", );
        startActivity(intent);
    }

    private void searchMagangByTitle(final String query){
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance()
                .getReference(Constant.MAGANG_POSTING);

        Query queryData = databaseReference.orderByChild("title")
                .startAt(query.toUpperCase())
                .endAt(query.toLowerCase()+"\uf8ff");

        queryData.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()){
                    magangArrayList.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                        Magang magang = snapshot.getValue(Magang.class);
                        magangArrayList.add(magang);
                    }

                    magangListAdapter.setMagangData(magangArrayList);
                    recyclerView.setAdapter(magangListAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
