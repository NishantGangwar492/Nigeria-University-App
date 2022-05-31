package com.iotait.schoolapp.ui.homepage.ui.premium;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.iotait.schoolapp.R;
import com.iotait.schoolapp.application.AppController;
import com.iotait.schoolapp.databinding.FragmentPremiumProcessBinding;
import com.iotait.schoolapp.helper.CustomMessage;
import com.iotait.schoolapp.helper.FragmentHelper;
import com.iotait.schoolapp.helper.NetworkHelper;

/**
 * A simple {@link Fragment} subclass.
 */
public class PremiumProcessFragment extends Fragment implements View.OnClickListener {

    private FragmentPremiumProcessBinding premiumProcessBinding;

    private View dialogView;
    private BottomSheetDialog dialog;
    private String state="";
    private int amount=0;

    View view;
    public PremiumProcessFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        premiumProcessBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_premium_process, container, false);

        view = premiumProcessBinding.getRoot();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        premiumProcessBinding.includeToolbar.toolbarTitle.setText("GET PREMIUM");
        ((AppCompatActivity) getActivity()).setSupportActionBar(premiumProcessBinding.includeToolbar.toolbar);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        premiumProcessBinding.includeToolbar.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                FragmentHelper.changeFragmet(premiumProcessBinding.getRoot(), R.id.action_premiumProcessFragment_to_nav_home, bundle);
            }
        });

        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                Bundle bundle = new Bundle();
                FragmentHelper.changeFragmet(premiumProcessBinding.getRoot(), R.id.action_premiumProcessFragment_to_nav_home, bundle);
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);

        dialogView = getLayoutInflater().inflate(R.layout.bottom_sheet_dialog, null);
        LinearLayout layout_online_payment = dialogView.findViewById(R.id.layout_online_payment);
        LinearLayout layout_banking_system = dialogView.findViewById(R.id.layout_banking_bystem);
        LinearLayout layout_mobile_recharge = dialogView.findViewById(R.id.layout_mobile_recharge);
        dialog = new BottomSheetDialog(getContext(), R.style.AppBottomSheetDialogTheme);
        dialog.setContentView(dialogView);

        premiumProcessBinding.btnBuyP1.setOnClickListener(this::onClick);
        premiumProcessBinding.btnBuyP2.setOnClickListener(this::onClick);
       // premiumProcessBinding.tvlearnmore.setOnClickListener(this::onClick);
        premiumProcessBinding.tvlearnmore2.setOnClickListener(this::onClick);
        layout_online_payment.setOnClickListener(this::onClick);
        layout_banking_system.setOnClickListener(this::onClick);
        layout_mobile_recharge.setOnClickListener(this::onClick);


        getPaymentStatus();


    }
    private boolean pstate=false;
    private void getPaymentStatus() {
        if (NetworkHelper.hasNetworAccess(getContext())){
            final ProgressDialog mDialog = new ProgressDialog(getContext());
            mDialog.setMessage("Check status...");
            mDialog.setCanceledOnTouchOutside(false);
            mDialog.setIndeterminate(false);
            mDialog.show();
            AppController.getFirebaseHelper().getPayment().addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    mDialog.dismiss();
                    if (dataSnapshot.exists()){
                        for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                            String userid=dataSnapshot1.child("payment_userid").getValue(String.class);
                            if (userid.equals(AppController.getFirebaseHelper().getFirebaseAuth().getUid())){
                                pstate=true;
                                break;
                            }
                            else {
                                pstate=false;
                            }
                        }
                    }
                    else {
                        pstate=false;
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    pstate=false;
                }
            });
        }
        else {
            new CustomMessage(getActivity(),"No internet connection");
            pstate=false;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
    }

    @Override
    public void onClick(View view) {
        Bundle bundle=new Bundle();
        int id=view.getId();
        switch (id){
            case R.id.btn_buy_p1:
                if (!pstate){
                    if (dialog!=null){
                        dialog.show();
                        state="p1";
                        amount=1000;
                    }
                }
                else {

                    Snackbar snackbar = Snackbar.make(view, R.string.premiumpendingrequestmessage, Snackbar.LENGTH_LONG);
                    View snackbarView = snackbar.getView();
                    TextView snackTextView = (TextView) snackbarView.findViewById(com.google.android.material.R.id.snackbar_text);
                    snackTextView.setMaxLines(4);
                    snackbar.show();
                    // new CustomMessage(getActivity(), "ur request as soon as we can.");
                }
                break;
            case R.id.btn_buy_p2:
                if (!pstate){
                    if (dialog!=null){
                        dialog.show();
                        state="p2";
                        amount=2000;
                    }
                }
                else {

                    Snackbar snackbar = Snackbar.make(view, R.string.premiumpendingrequestmessage, Snackbar.LENGTH_LONG);
                    View snackbarView = snackbar.getView();
                    TextView snackTextView = (TextView) snackbarView.findViewById(com.google.android.material.R.id.snackbar_text);

                    snackTextView.setMaxLines(4);
                    snackbar.show();

                }
                break;
            case R.id.layout_online_payment:
                bundle.putString("state",state);
                bundle.putInt("amount",amount);
                FragmentHelper.changeFragmet(premiumProcessBinding.getRoot(), R.id.action_premiumProcessFragment_to_onlinePaymentFragment, bundle);
                if (dialog!=null){
                    dialog.dismiss();
                }
                break;
            case R.id.layout_banking_bystem:
                bundle.putString("state",state);
                bundle.putInt("amount",amount);
                FragmentHelper.changeFragmet(premiumProcessBinding.getRoot(), R.id.action_premiumProcessFragment_to_bankingSystemFragment, bundle);
                if (dialog!=null){
                    dialog.dismiss();
                }
                break;
            case R.id.layout_mobile_recharge:
                bundle.putString("state", state);
                bundle.putInt("amount", amount);
                FragmentHelper.changeFragmet(premiumProcessBinding.getRoot(), R.id.action_premiumProcessFragment_to_mobileRechargeFragment, bundle);
                if (dialog != null) {
                    dialog.dismiss();
                }
                break;
//            case R.id.tvlearnmore:
//
//                FragmentHelper.changeFragmet(premiumProcessBinding.getRoot(), R.id.action_premiumProcessFragment_to_learnmoreBesic, bundle);
//                break;
            case R.id.tvlearnmore2:
                FragmentHelper.changeFragmet(premiumProcessBinding.getRoot(), R.id.action_premiumProcessFragment_to_learnmorePremium, bundle);

                break;

        }
    }
}
