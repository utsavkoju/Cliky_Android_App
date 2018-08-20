package ca.cliky.cliky.fragments;


import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

import ca.cliky.cliky.R;
import ca.cliky.cliky.database.Retailers;
import ca.cliky.cliky.essentials.Constant;
import ca.cliky.cliky.services.NetworkUtils;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    String url;
    ProgressBar progressBar;
    TextView errorMessage_tv;
    WebView webView;
    Fragment fragment;
    public HomeFragment() { }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        setHasOptionsMenu(false);

        webView = (WebView)rootView.findViewById(R.id.offerView);
        webView.setVisibility(View.GONE);
        progressBar = (ProgressBar) rootView.findViewById(R.id.progress);
        errorMessage_tv = (TextView) rootView.findViewById(R.id.errorMessage_tv);
        progressBar.setVisibility(View.VISIBLE);

        webViewSettings();
        loadWebPage();

        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                webView.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
            }
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                if(request.getUrl().toString().contains("mobile/offers")){
                    String url = request.getUrl().toString();
                    String[] url_split = url.split("/");
                    String retailer = url_split[url_split.length-1];
                    fragment = new OfferFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("retailer", retailer);
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.container_body, fragment);
                    fragment.setArguments(bundle);
                } else {
                    Intent intent = new Intent(Intent.ACTION_VIEW, request.getUrl());
                    view.getContext().startActivity(intent);
                    return true;
                }
                return false;
            }
        });

        getActivity().invalidateOptionsMenu();

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.findItem(R.id.action_search).setVisible(false);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_search) {
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

    private void loadWebPage() {
        if (NetworkUtils.checkConnection(getActivity().getApplicationContext())) {
            url = Constant.DASH_URL;
            webView.stopLoading();
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
                loadWebPage();
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
    }

}
