package cl.genesys.appchofer.layout;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cl.genesys.appchofer.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class gmm extends Fragment {


    public gmm() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_gmm, container, false);
    }

}
