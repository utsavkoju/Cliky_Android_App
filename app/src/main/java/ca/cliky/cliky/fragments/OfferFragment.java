package ca.cliky.cliky.fragments;


import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.Animatable;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import ca.cliky.cliky.HomeActivity;
import ca.cliky.cliky.R;
import ca.cliky.cliky.database.Departments;
import ca.cliky.cliky.database.Retailers;
import ca.cliky.cliky.database.Types;
import ca.cliky.cliky.essentials.Constant;
import ca.cliky.cliky.objects.Department;
import ca.cliky.cliky.objects.Retailer;
import ca.cliky.cliky.objects.Type;
import ca.cliky.cliky.services.NetworkUtils;

/**
 * A simple {@link Fragment} subclass.
 */
public class OfferFragment extends Fragment {

    ProgressBar progressBar;
    String url;
    TextView errorMessage_tv;
    WebView webView;
    private ViewGroup hiddenPanel;
    private boolean isPanelShown;
    private Button btnFilter, btnDefault;
    Spinner Spin_retailer, Spin_department, Spin_Type, Spin_discount, Spin_price;
    String[] orderBy = {"","Low to High", "High to Low"};
    private ImageButton close;

    public OfferFragment() { }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true);
        View rootView = inflater.inflate(R.layout.fragment_offer, container, false);

        webView = (WebView)rootView.findViewById(R.id.offerView);
        webView.setVisibility(View.GONE);
        progressBar = (ProgressBar) rootView.findViewById(R.id.progress);
        errorMessage_tv = (TextView) rootView.findViewById(R.id.errorMessage_tv);
        Spin_department = (Spinner) rootView.findViewById(R.id.input_department);
        Spin_retailer = (Spinner) rootView.findViewById(R.id.input_retailer);
        Spin_Type = (Spinner) rootView.findViewById(R.id.input_updateType);
        Spin_discount = (Spinner) rootView.findViewById(R.id.input_discount);
        Spin_price = (Spinner) rootView.findViewById(R.id.input_price);
        btnDefault = (Button) rootView.findViewById(R.id.btn_default);
        btnFilter = (Button) rootView.findViewById(R.id.btn_filter);
        hiddenPanel = (ViewGroup) rootView.findViewById(R.id.hidden_panel);
        close = (ImageButton) rootView.findViewById(R.id.sliderdown);
        hiddenPanel.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);

        Retailers retailers = new Retailers(getActivity().getApplicationContext());
        Departments departments = new Departments(getActivity().getApplicationContext());
        Types types = new Types(getActivity().getApplicationContext());

        List<Retailer> rtls = retailers.getRetails();
        ArrayAdapter<Retailer> retailAdapter = new ArrayAdapter<Retailer>(getActivity(), android.R.layout.simple_spinner_item, rtls);
        retailAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spin_retailer.setAdapter(retailAdapter);

        List<Department> dpts = departments.getDeparts();
        ArrayAdapter<Department> dptAdapter = new ArrayAdapter<Department>(getActivity(), android.R.layout.simple_spinner_item, dpts);
        dptAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spin_department.setAdapter(dptAdapter);

        List<Type> typs = types.getTyps();
        ArrayAdapter<Type> typAdapter = new ArrayAdapter<Type>(getActivity(), android.R.layout.simple_spinner_item, typs);
        typAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spin_Type.setAdapter(typAdapter);

        ArrayAdapter<String> orderAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, orderBy);
        orderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spin_price.setAdapter(orderAdapter);
        Spin_discount.setAdapter(orderAdapter);

        close.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Animation bottomDown = AnimationUtils.loadAnimation(getActivity(), R.anim.bottom_down);
                hiddenPanel.startAnimation(bottomDown);
                hiddenPanel.setVisibility(View.INVISIBLE);
                isPanelShown = false;
            }
        });

        btnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String retailer, department, type, price, discount;
                retailer = checkVal(((Retailer)Spin_retailer.getSelectedItem()).getId());
                department = checkVal(((Department)Spin_department.getSelectedItem()).getId());
                type = checkVal(((Type)Spin_Type.getSelectedItem()).getId());
                price = orderingMapper(String.valueOf(Spin_price.getSelectedItem()));
                discount = orderingMapper(String.valueOf(Spin_discount.getSelectedItem()));
                loadWebPage(retailer, department, type, price, discount);
                Animation bottomDown = AnimationUtils.loadAnimation(getActivity(), R.anim.bottom_down);
                hiddenPanel.startAnimation(bottomDown);
                hiddenPanel.setVisibility(View.INVISIBLE);
                isPanelShown = false;
            }
        });

        isPanelShown = false;

        webViewSettings();

        Bundle argument = getArguments();
        if(argument != null && argument.containsKey("retailer")){
            String retailer = argument.getString("retailer");
            Log.e("retailer", retailer);
            url = Constant.OFFER_URL+"/"+retailer;
            Log.e("url", url);
            loadWebPage(retailer, null, null, null, null);
        } else {
            url = Constant.OFFER_URL;
            Log.e("url", url);
            loadWebPage(null, null, null, null, null);
        }

        webView.setWebViewClient(new WebViewClient(){
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                Intent intent = new Intent(Intent.ACTION_VIEW, request.getUrl());
                view.getContext().startActivity(intent);
                return true;
            }
        });

        getActivity().invalidateOptionsMenu();

        return rootView;
    }



    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.findItem(R.id.action_search).setVisible(true);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_search) {
            Animation bottomUp = AnimationUtils.loadAnimation(getActivity(), R.anim.bottom_up);
            hiddenPanel.startAnimation(bottomUp);
            hiddenPanel.setVisibility(View.VISIBLE);
        }
        return super.onOptionsItemSelected(item);
    }

    private void improveWebViewPerformance() {
        webView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // chromium, enable hardware acceleration
            webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        } else {
            // older android version, disable hardware acceleration
            webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
    }

    private void webViewSettings() {
        improveWebViewPerformance();
        webView.getSettings().setBuiltInZoomControls(false);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                progressBar.setVisibility(View.GONE);
            }
        });
        webView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                if (progress == 100 && progressBar.getVisibility() == View.VISIBLE)
                    progressBar.setVisibility(View.GONE);

                progressBar.setProgress(progress);
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                if (!TextUtils.isEmpty(title)) {
                    //  pageTitle = title;
                    //      parent.getSupportActionBar().setTitle(title);
                }
            }
        });
    }

    private void loadWebPage(String retailer, String department, String type, String price, String discount) {
        if (NetworkUtils.checkConnection(getActivity().getApplicationContext())) {
            webView.stopLoading();
            url = Constant.OFFER_URL+"/"+retailer+"/"+department+"/"+type+"/"+price+"/"+discount;
            Log.e("OFFER URL: ", url);
            webView.loadUrl(url);
            webView.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.VISIBLE);
            progressBar.setProgress(0);
        } else {
            onNetworkError();
        }
    }

    private void onNetworkError() {
        errorMessage_tv.setVisibility(View.VISIBLE);
        webView.setVisibility(View.GONE);
        errorMessage_tv.setText(getString(R.string.message_network_problem));
        errorMessage_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadWebPage(null, null, null, null, null);
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        webView.onPause();
        if (getActivity() != null)
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);

    }

    @Override
    public void onResume() {
        super.onResume();
        webView.onResume();
        if (getActivity() != null)
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        ((HomeActivity) getActivity()).setActionBarTitle("Offer");
    }

    private String checkVal(String val) {
        if(val.isEmpty()) return null;
        return val;
    }

    private String orderingMapper(String val) {
        if(val.equals("Low to High")) return "ASC";
        else if(val.equals("High to Low")) return "DESC";
        return "null";
    }

}
