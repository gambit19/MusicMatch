package com.example.winx10.musicmatch;

import android.app.Activity;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SearchFragment extends Fragment {

    public com.google.firebase.auth.FirebaseAuth mAuth;
    public DatabaseReference dbreference;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.search_activity, null);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        TextView textView = getView().findViewById(R.id.textView);

        View v = getView();
        final Activity activity = getActivity();

        mAuth = com.google.firebase.auth.FirebaseAuth.getInstance();
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Geolocations");
        dbreference = FirebaseDatabase.getInstance().getReference().child(user.getUid());



        if (user != null) {

            dbreference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {


                    DatabaseInformation databaseInformation = dataSnapshot.getValue(DatabaseInformation.class);

                    final Double latitude = databaseInformation.getLocation().getLatitude();
                    Double longtiude = databaseInformation.getLocation().getLongitude();

                    GeoFire geoFire = new GeoFire(ref);
                    GeoLocation userLocation = new GeoLocation(latitude,longtiude);

                    geoFire.setLocation(user.getUid(),userLocation);

                    new GeoFire.CompletionListener(){

                        @Override
                        public void onComplete(String key, DatabaseError error) {
                             if(error!=null){
                                Toast.makeText(activity,"Error"+error,Toast.LENGTH_SHORT).show();
                            }else{
                                 Toast.makeText(activity,"GeoLocation hashed and saved!",Toast.LENGTH_SHORT).show();
                            }

                        }
                    };

                    GeoQuery geoQuery = geoFire.queryAtLocation(userLocation,10);

                    geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
                        @Override
                        public void onKeyEntered(String key, GeoLocation location) {
                            Toast.makeText(activity,"User:"+key+"\nLocation:"+location,Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onKeyExited(String key) {

                        }

                        @Override
                        public void onKeyMoved(String key, GeoLocation location) {

                        }

                        @Override
                        public void onGeoQueryReady() {

                        }

                        @Override
                        public void onGeoQueryError(DatabaseError error) {

                        }
                    });

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });


        }
    }
}