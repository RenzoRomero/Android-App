package pe.com.smartvet.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.androidnetworking.widget.ANImageView;

import pe.com.smartvet.R;
import pe.com.smartvet.SmartVetApp;
import pe.com.smartvet.activities.EditUserProfileActivity;
import pe.com.smartvet.models.Vet;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserFragment extends Fragment {
    private FloatingActionButton editUserFloatingActionButton;
    private ANImageView photoANImageView;
    private TextView displayNameTextView;
    private TextView emailTextView;
    private TextView mobilePhoneTextView;
    private TextView genderTextView;
    private TextView addressTextView;

    Vet vet;

    public UserFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user, container, false);

        vet = SmartVetApp.getInstance().getVet();

        photoANImageView = view.findViewById(R.id.photoANImageView);
        displayNameTextView = view.findViewById(R.id.displayNameTextView);
        emailTextView = view.findViewById(R.id.emailTextView);
        mobilePhoneTextView = view.findViewById(R.id.mobilePhoneTextView);
        genderTextView = view.findViewById(R.id.genderTextView);
        addressTextView = view.findViewById(R.id.addressTextView);
        editUserFloatingActionButton = view.findViewById(R.id.editUserFloatingActionButton);
        editUserFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.getContext()
                        .startActivity(new Intent(v.getContext(),
                                EditUserProfileActivity.class));
            }
        });

        userInformation();

        return view;
    }

    public void userInformation() {
        vet = SmartVetApp.getInstance().getVet();
        photoANImageView.setErrorImageResId(R.mipmap.ic_launcher);
        photoANImageView.setDefaultImageResId(R.mipmap.ic_launcher);
        photoANImageView.setImageUrl(vet.getPhoto());
        displayNameTextView.setText(vet.getName() + " " + vet.getLastName());
        emailTextView.setText(vet.getEmail());
        mobilePhoneTextView.setText((vet.getMobilePhone().toString()));
        if (vet.getGender().equals("Man") || vet.getGender().equals("Hombre")) {
            genderTextView.setText(getResources().getString(R.string.man_gender));
        } else {
            if (vet.getGender().equals("Woman") || vet.getGender().equals("Mujer")) {
                genderTextView.setText(getResources().getString(R.string.women_gender));
            } else {
                genderTextView.setText("");
            }
        }
        addressTextView.setText(vet.getAddress());
    }

    @Override
    public void onResume() {
        super.onResume();
        userInformation();
    }

}
