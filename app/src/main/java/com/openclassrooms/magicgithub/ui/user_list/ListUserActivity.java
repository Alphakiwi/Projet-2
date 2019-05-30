package com.openclassrooms.magicgithub.ui.user_list;

import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.openclassrooms.magicgithub.api.ApiService;
import com.openclassrooms.magicgithub.base.BaseActivity;
import com.openclassrooms.magicgithub.R;
import com.openclassrooms.magicgithub.model.User;

import java.util.Random;


public class ListUserActivity extends BaseActivity implements UserListAdapter.Listener {



    // FOR DESIGN ---
    RecyclerView recyclerView;
    FloatingActionButton fab;
    FloatingActionButton fab2;
    String avatar ;
    int id;
    Random rand;
    String login;


    // FOR DATA ---
    private UserListAdapter adapter;

    // OVERRIDE ---

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_user);
        configureFab();
        configureFab2();
        configureRecyclerView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }

    // CONFIGURATION ---

    private void configureRecyclerView() {
        recyclerView = findViewById(R.id.activity_list_user_rv);
        adapter = new UserListAdapter(this);
        recyclerView.setAdapter(adapter);
    }

    private void configureFab() {
        fab = findViewById(R.id.activity_list_user_fab);
        fab.setOnClickListener(view -> {
            getUserRepository().generateRandomUser();
            loadData();
        });
    }

    private void configureFab2() {

        fab2 = findViewById(R.id.activity_list_user_fab2);


        fab2.setOnClickListener(view -> {
            login = "Denis";
            rand = new Random();
            id = rand.nextInt(100); // Gives n such that 0 <= n < 20
            avatar = "https://api.adorable.io/AVATARS/512/" + id + ".png";



            new AlertDialog.Builder(view.getContext())
                    .setView(R.layout.dialog_choix_sujet)
                    .setPositiveButton("Valider", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            EditText etSujet = (EditText) ((AlertDialog) dialog).findViewById(R.id.sujet);
                            if (etSujet.getText().toString().length()>0) {
                                login = etSujet.getText().toString();
                            }
                            getUserRepository().addUser(new User(String.valueOf(id), login, avatar));
                            loadData();



                        }
                    })
                    .setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    })
                    .show();



        });
    }

    private void loadData() {


        adapter.updateList(getUserRepository().getUsers());

    }

    // ACTIONS ---

    @Override
    public void onClickDelete(User user) {
        Log.d(ListUserActivity.class.getName(), "User tries to delete a item.");
        getUserRepository().deleteUser(user);
        loadData();
    }
}
