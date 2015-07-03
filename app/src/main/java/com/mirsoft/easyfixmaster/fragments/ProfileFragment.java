package com.mirsoft.easyfixmaster.fragments;

import android.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.afollestad.materialdialogs.MaterialDialog;
import com.mirsoft.easyfixmaster.R;
import com.rengwuxian.materialedittext.MaterialEditText;


public class ProfileFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    MaterialDialog dialog;
    MaterialEditText etSpeciality;
    MaterialEditText etLicense;

    private Integer[] list = new Integer[]{2};

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        initSpecialityDialog();

        etSpeciality = (MaterialEditText)view.findViewById(R.id.etSpeciality);
        etSpeciality.setInputType(InputType.TYPE_NULL);

        etSpeciality.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) showSpecialityDialog(true);
                else showSpecialityDialog(false);
            }
        });

        etLicense = (MaterialEditText)view.findViewById(R.id.etLicense);


        return view;
    }

    private void initSpecialityDialog() {
        String[] specs = new String[] {"Slave", "MegaSlave", "HyperSlave"};
    }

    private void showSpecialityDialog(final boolean show) {
        String[] specs = new String[] {"Slave", "MegaSlave", "HyperSlave"};
        if(show) {
            MaterialDialog.Builder builder = new MaterialDialog.Builder(getActivity())
                    .title("Opachki")
                    .items(specs)
                    .itemsCallbackMultiChoice(list, new MaterialDialog.ListCallbackMultiChoice() {
                        @Override
                        public boolean onSelection(MaterialDialog dialog, Integer[] which, CharSequence[] text) {
                            /**
                             * If you use alwaysCallMultiChoiceCallback(), which is discussed below,
                             * returning false here won't allow the newly selected check box to actually be selected.
                             * See the limited multi choice dialog example in the sample project for details.
                             **/
                            list = which;
                            etSpeciality.setText(String.valueOf(which.length));
                            etLicense.requestFocus();
                            return true;
                        }
                    })
                    .positiveText("Choose");
            dialog = builder.build();
            dialog.show();
        } else {
            if (dialog != null)
                dialog.dismiss();
        }


    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
