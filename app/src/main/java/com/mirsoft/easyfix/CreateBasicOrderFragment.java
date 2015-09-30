package com.mirsoft.easyfix;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatRatingBar;
import android.support.v7.widget.AppCompatSpinner;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mirsoft.easyfix.common.Constants;
import com.mirsoft.easyfix.common.OrderType;
import com.mirsoft.easyfix.fragments.LoginFragment;
import com.mirsoft.easyfix.common.OrderType;
import com.mirsoft.easyfix.fragments.MasterListFragment;
import com.mirsoft.easyfix.fragments.MyMasterHistoryCommentFragment;
import com.mirsoft.easyfix.models.Order;
import com.mirsoft.easyfix.models.PendingContractor;
import com.mirsoft.easyfix.models.Specialty;
import com.mirsoft.easyfix.models.User;
import com.mirsoft.easyfix.networking.RestClient;
import com.mirsoft.easyfix.networking.models.CommonOrder;
import com.mirsoft.easyfix.utils.Singleton;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class CreateBasicOrderFragment extends Fragment {

    Singleton dc;
    Settings settings;

    public AppCompatSpinner servicesSpinner;
    public AppCompatRatingBar ratingBar;
    public EditText orderAddress;
    public EditText orderPhone;
    public EditText orderDescription;
    public TextView mastersRequests;
    public TextView orderNotification;
    public Button orderBtnChange;
    public Button orderBtnCancel;
    public Button orderBtnLocate;

    private double orderLat;
    private double orderLng;

    public TextInputLayout tilOrderAddress;
    public TextInputLayout tilOrderPhone;
    public TextInputLayout tilOrderDescription;

    ProgressDialog mDialog;

    public final String CREATE_MODE  = "createOrder";
    public final String CHECK_MODE   = "checkOrder";
    public final String FINISH_MODE  = "finishOrder";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_basic_order, container, false);
        dc = Singleton.getInstance(getActivity());
        settings = new Settings(getActivity());

        servicesSpinner = (AppCompatSpinner)view.findViewById(R.id.spinner);
        ratingBar = (AppCompatRatingBar)view.findViewById(R.id.llratingbar);
        orderAddress = (EditText)view.findViewById(R.id.order_address);

        orderLat = dc.curLat;
        orderLng = dc.curLng;

        orderAddress.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if(event.getAction() == MotionEvent.ACTION_UP) {
                    if(event.getRawX() >= (orderAddress.getRight() - orderAddress.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        Intent callIntent = new Intent(getActivity(), MapsActivity.class);
                        startActivityForResult(callIntent, Constants.MAPS_REQUEST_CODE);
                        return true;
                    }
                }
                return false;
            }
        });
        orderPhone = (EditText)view.findViewById(R.id.order_phone);
        orderDescription = (EditText)view.findViewById(R.id.order_description);
        orderBtnLocate = (Button)view.findViewById(R.id.btnLocate);

        tilOrderAddress = (TextInputLayout)view.findViewById(R.id.tilAddress);
        tilOrderPhone = (TextInputLayout)view.findViewById(R.id.tilPhone);
        tilOrderDescription = (TextInputLayout)view.findViewById(R.id.tilDescription);

        mastersRequests = (TextView)view.findViewById(R.id.request_from_masters);
        orderNotification = (TextView)view.findViewById(R.id.order_notification);
        orderBtnChange = (Button)view.findViewById(R.id.btnChange);
        orderBtnCancel = (Button)view.findViewById(R.id.btnCancel);

        Drawable progress = ratingBar.getProgressDrawable();
        DrawableCompat.setTint(progress, Color.BLACK);

        ArrayAdapter<Specialty> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, dc.specialtyList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        servicesSpinner.setAdapter(adapter);

        setActivityState(getActivity().getIntent().getStringExtra("activityMode"));

        orderBtnLocate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!validateOrder()) return;
                mDialog = ProgressDialog.show(getActivity(), "Подождите...", "Отправляются данные", true);
                locateOrder(initNewCommonOrder());
            }
        });

        orderBtnChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!validateOrder()) return;
                mDialog = ProgressDialog.show(getActivity(), "Подождите...", "Обновляются данные", true);
                updateOrder(initNewCommonOrder());
            }
        });

        orderBtnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder deleteDialog = new AlertDialog.Builder(getActivity());
                deleteDialog.setTitle("Отмена заказа")
                        .setMessage("Вы хотите отменить заказ ?")
                        .setPositiveButton("Да", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                mDialog = ProgressDialog.show(getActivity(), "Подождите...", "Отменяется заказ", true);
                                cancelOrder(initNewCommonOrder());
                            }
                        })
                        .setNeutralButton("Нет", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        })
                        .show();
            }
        });

        ((ClientOrderDetailsActivity)getActivity()).create_order_toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                ((ClientOrderDetailsActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                ((ClientOrderDetailsActivity) getActivity()).onBackButtonClicked();
            }
        });

      //  ((ClientOrderDetailsActivity)getActivity()).create_order_toolbar.setTitle(getResources().getString(R.string.my_order_history));

        return view;
    }

    private boolean validateOrder() {
        cleanViewsFromError();
        String address = orderAddress.getText().toString();
        String phone = orderPhone.getText().toString();
        String description = orderDescription.getText().toString();

        if (address.length() == 0) {
            tilOrderAddress.setError(getActivity().getResources().getString(R.string.error_address_empty));
            return false;
        }

        if (phone.length() == 0) {
            tilOrderPhone.setError(getActivity().getResources().getString(R.string.error_phone_empty));
            return false;
        }

        if (description.length() == 0) {
            tilOrderDescription.setError(getActivity().getResources().getString(R.string.error_description_empty));
            return false;
        }

        return true;
    }

    private void cleanViewsFromError() {
        tilOrderAddress.setError(null);
        tilOrderPhone.setError(null);
        tilOrderDescription.setError(null);
    }

    public CommonOrder initNewCommonOrder(){
            CommonOrder order = new CommonOrder();
            order.setAddress(orderAddress.getText().toString());
            order.setDescription(orderDescription.getText().toString());
            order.setLatitude(orderLat);
            order.setLongitude(orderLng);
            order.setSpecialty(dc.specialtyList.get(servicesSpinner.getSelectedItemPosition()).getId());
            order.setRating(ratingBar.getRating());
        return order;
    }

    public void locateOrder(CommonOrder order){
        RestClient.getOrderService(false).createCommonOrder(order, settings.getUserId(), new Callback<Order>() {
            @Override
            public void success(Order order, Response response) {
                mDialog.dismiss();
                Toast.makeText(getActivity(), "Success", Toast.LENGTH_SHORT).show();
                ((ClientOrderDetailsActivity) getActivity()).onBackButtonClicked();
                dc.fromCreateBasicOrderFragment = true;
            }

            @Override
            public void failure(RetrofitError error) {
                mDialog.dismiss();
                Toast.makeText(getActivity(), "Failure", Toast.LENGTH_SHORT).show();
                error.printStackTrace();
            }
        });
    }

    public void updateOrder(CommonOrder order){
        RestClient.getOrderService(false).updateOrder(order, settings.getUserId(), dc.clientSelectedOrder.getId(), new Callback<Order>() {
            @Override
            public void success(Order order, Response response) {
                mDialog.dismiss();
                Toast.makeText(getActivity(), "Success updating", Toast.LENGTH_SHORT).show();
                ((ClientOrderDetailsActivity) getActivity()).onBackButtonClicked();
            }

            @Override
            public void failure(RetrofitError error) {
                mDialog.dismiss();
                Toast.makeText(getActivity(), "Failure updating", Toast.LENGTH_SHORT).show();
                error.printStackTrace();
            }
        });
    }

    public  void cancelOrder(CommonOrder order){

        order.setStatus(OrderType.CANCELED);
        RestClient.getOrderService(false).cancelOrder(order, settings.getUserId(), dc.clientSelectedOrder.getId(), new Callback<Order>() {
            @Override
            public void success(Order order, Response response) {
                mDialog.dismiss();
                Toast.makeText(getActivity(), "Заказ отменен", Toast.LENGTH_SHORT).show();
                ((ClientOrderDetailsActivity) getActivity()).onBackButtonClicked();
            }

            @Override
            public void failure(RetrofitError error) {
                mDialog.dismiss();
                Toast.makeText(getActivity(), "Failure :cancel order", Toast.LENGTH_SHORT).show();
                error.printStackTrace();
            }
        });
    }

    public void getPendingOrders(){
        RestClient.getOrderService(false).getContractorRequests(settings.getUserId(), dc.clientSelectedOrder.getId(),
                new Callback<ArrayList<PendingContractor>>() {
                    @Override
                    public void success(final ArrayList<PendingContractor> pendingUsers, Response response) {
                        mastersRequests.setText(getActivity().getResources().getString(R.string.master_request) + " : " + pendingUsers.size());
                        mastersRequests.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (pendingUsers.size() > 0) {
                                    String backStateName = getActivity().getFragmentManager().getClass().getName();
                                    getActivity().getSupportFragmentManager().beginTransaction()
                                            .replace(R.id.container, MasterListFragment.newInstance(getUserListFromPendingContractors(pendingUsers), Constants.PENDING_MASTERS_LIST))
                                            .addToBackStack(backStateName)
                                            .commit();
                                }
                            }
                        });
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Toast.makeText(getActivity(), "Failure : pending orders", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private ArrayList<User> getUserListFromPendingContractors(ArrayList<PendingContractor> plist) {
        ArrayList<User> userList = new ArrayList<>();
        for (int i = 0; i < plist.size(); ++i) {
            userList.add(plist.get(i).getContractor());
        }
        return userList;
    }

    public void setActivityState(String mode) {
        switch (mode) {
            case CREATE_MODE:
                mastersRequests.setVisibility(View.GONE);
                orderNotification.setVisibility(View.GONE);
                orderBtnChange.setVisibility(View.GONE);
                orderBtnCancel.setVisibility(View.GONE);
                ((ClientOrderDetailsActivity)getActivity()).create_order_toolbar.setTitle(getResources().getString(R.string.make_order));
                break;
            case CHECK_MODE:
                Toast.makeText(getActivity(), String.valueOf(dc.clientSelectedOrder.getId()), Toast.LENGTH_SHORT).show();
                mastersRequests.setVisibility(View.VISIBLE);
                orderNotification.setVisibility(View.VISIBLE);
                orderBtnChange.setVisibility(View.VISIBLE);
                orderBtnCancel.setVisibility(View.VISIBLE);
                orderBtnLocate.setVisibility(View.GONE);

                orderAddress.setText(dc.clientSelectedOrder.getAddress());
                orderPhone.setText(dc.clientSelectedOrder.getPhone());
                orderDescription.setText(dc.clientSelectedOrder.getDescription());
                //orderAddress.setText(dc.selectedMaster.getId());
                ratingBar.setRating(1);
                servicesSpinner.setSelection(dc.getPosition(dc.clientSelectedOrder.getSpecialty().getId()));
                if(dc.clientSelectedOrder.getStatus() == OrderType.NEW) {
                    getPendingOrders();
                }
                ((ClientOrderDetailsActivity)getActivity()).create_order_toolbar.setTitle(  getResources().getString(R.string.my_order));
                break;
            case FINISH_MODE:
                mastersRequests.setVisibility(View.VISIBLE);
                orderNotification.setVisibility(View.GONE);
                orderBtnChange.setVisibility(View.GONE);
                orderBtnCancel.setVisibility(View.GONE);
                orderBtnLocate.setVisibility(View.GONE);

                orderAddress.setText(dc.clientSelectedOrder.getAddress());
                orderPhone.setText(dc.clientSelectedOrder.getPhone());
                orderDescription.setText(dc.clientSelectedOrder.getDescription());
                ratingBar.setRating(1);
                servicesSpinner.setSelection(dc.getPosition(dc.clientSelectedOrder.getSpecialty().getId()));
                getUpdatedOrder();

                orderAddress.setEnabled(false);
                orderPhone.setEnabled(false);
                orderDescription.setEnabled(false);
                ratingBar.setIsIndicator(true);
                servicesSpinner.setClickable(false);

                mastersRequests.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String backStateName = getActivity().getFragmentManager().getClass().getName();
                        getActivity().getSupportFragmentManager().beginTransaction()
                                .replace(R.id.container, new MyMasterHistoryCommentFragment())
                                .addToBackStack(backStateName)
                                .commit();
                    }
                });
                ((ClientOrderDetailsActivity)getActivity()).create_order_toolbar.setTitle(getResources().getString(R.string.my_order_history));

                break;
        }
    }


    private void getUpdatedOrder(){
        RestClient.getOrderService(false).getById(settings.getUserId(),dc.clientSelectedOrder.getId(), new Callback<Order>() {
            @Override
            public void success(Order order, Response response) {
                dc.clientSelectedOrder = order;
                mastersRequests.setText(order.getContractor().getFullName());
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(getActivity(), "Error:loading updated information", Toast.LENGTH_SHORT);
                error.printStackTrace();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if( requestCode == Constants.MAPS_REQUEST_CODE && resultCode == Constants.OK_RESULT_CODE ) {
            orderAddress.setText(data.getStringExtra("address"));
            orderLat = data.getDoubleExtra("lat", dc.curLat);
            orderLng = data.getDoubleExtra("lng", dc.curLng);
        }
    }

}

