package ca.cliky.cliky.fragments;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import ca.cliky.cliky.R;
import ca.cliky.cliky.adapter.ListViewAdapter;
import ca.cliky.cliky.objects.ListDetail;

/**
 * A simple {@link Fragment} subclass.
 */
public class AboutFragment extends Fragment {

    View rootview;
    private ListView lViews;
    private ListViewAdapter lvAdapter;
    private String TAG = "About";
    RelativeLayout relativeLayout;

    public AboutFragment() {  }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootview = inflater.inflate(R.layout.fragment_about, container, false);
        setHasOptionsMenu(false);

        String[] titles = this.getResources().getStringArray(R.array.about);
        String[] details = this.getResources().getStringArray(R.array.about_detail);
        relativeLayout = (RelativeLayout) rootview.findViewById(R.id.aboutLayout);
        ArrayList<ListDetail> lDetail = new ArrayList<ListDetail>();


        for (int i = 0; i < titles.length; i++) {
            lDetail.add(new ListDetail(titles[i], details[i]));
        }

        lvAdapter = new ListViewAdapter(getActivity().getApplicationContext(), lDetail);
        lViews = (ListView) rootview.findViewById(R.id.about_list);
        lViews.setAdapter(lvAdapter);

        lViews.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch(position) {
                    case 0:
                        Snackbar.make(relativeLayout, "Version 0.1 [early alpha]", Snackbar.LENGTH_LONG).show();
                        break;
                    case 1:
                        String url = "http://www.cliky.ca";
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(url));
                        startActivity(i);
                        break;
                    case 2:
                        Intent email = new Intent(Intent.ACTION_SEND);
                        email.putExtra(Intent.EXTRA_EMAIL, new String[]{"utsavkoju@gmail.com"});
                        email.putExtra(Intent.EXTRA_SUBJECT, "FEEDBACK: ");
                        email.setType("message/rfc822");
                        startActivityForResult(Intent.createChooser(email, "Choose an Email Client"), 1);
                        break;
                    case 3:
                        //Toast.makeText(getApplicationContext(), "Case 3", Toast.LENGTH_SHORT).show();
                        dumpDB();
                        break;
                    default:
                        break;
                }
            }
        });

        return rootview;
    }

    private void dumpDB() {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String DATABASE_NAME = "cliky";
        String databasePath = getActivity().getApplicationContext().getDatabasePath(DATABASE_NAME).getPath();
        String inFileName = databasePath;

        try {
            File dbFile = new File(inFileName);
            FileInputStream fis = new FileInputStream(dbFile);
            File destination = Environment.getExternalStorageDirectory().getAbsoluteFile();
            File Destination = new File(destination+"/Android/data/ca.cliky.cliky/backups/",DATABASE_NAME+"_log_"+timeStamp);

            OutputStream output = new FileOutputStream(Destination);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = fis.read(buffer)) > 0) {
                output.write(buffer, 0, length);
            }
            //Close the streams
            output.flush();
            output.close();
            fis.close();

            Intent email = new Intent(Intent.ACTION_SEND);
            email.putExtra(Intent.EXTRA_EMAIL, new String[]{"utsavkoju@gmail.com"});
            email.putExtra(Intent.EXTRA_SUBJECT, "Having Trouble: ");
            email.setType("text/plain");

            if(!Destination.exists() || !Destination.canRead()) {
                Snackbar.make(relativeLayout, "File doesn't exist or is not readable", Snackbar.LENGTH_LONG).show();
            } else {
                Uri url = Uri.fromFile(Destination);
                email.putExtra(Intent.EXTRA_STREAM, url);
                startActivityForResult(Intent.createChooser(email, "Choose an Email Client"), 1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
