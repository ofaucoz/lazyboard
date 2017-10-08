package com.ofaucoz.lazyboard;

import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.app.FragmentManager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;

/*
 *  GenericFragment for the slide views
 */
public class GridCommandFragment extends Fragment {
    Integer[] imageIDs = {
            R.drawable.youtube,
            R.drawable.google,
            R.drawable.write,
            R.drawable.nav
    };

    String[] stringCommands = {
            "youtube",
            "google",
            "write",
            "nav"
    };

    private LazyboardService mLazyboardService = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_grid_command, container, false);
        final FragmentActivity activity = getActivity();


        GridView gridview = (GridView) rootView.findViewById(R.id.grid_command);
        gridview.setAdapter(new ImageAdapter(activity));

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent,
                                    View v, int position, long id) {
                    DialogFragment newFragment = DialogCommandFragment.newInstance(
                            R.string.alert_dialog_two_buttons, stringCommands[position]);
                    newFragment.show(getActivity().getFragmentManager(), "dialog");
            }
        });

        return rootView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mLazyboardService != null) {
            mLazyboardService.stop();
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        // Performing this check in onResume() covers the case in which BT was
        // not enabled during onStart(), so we were paused to enable it...
        // onResume() will be called when ACTION_REQUEST_ENABLE activity returns.
        if (mLazyboardService != null) {
            // Only if the state is STATE_NONE, do we know that we haven't started already
            if (mLazyboardService.getState() == LazyboardService.STATE_NONE) {

            }
        }
    }

    public class ImageAdapter extends BaseAdapter {
        private Context context;

        public ImageAdapter(Context c) {
            context = c;
        }

        //---returns the number of images---
        public int getCount() {
            return imageIDs.length;
        }

        //---returns the ID of an item---
        public Object getItem(int position) {
            return position;
        }

        public long getItemId(int position) {
            return position;
        }

        //---returns an ImageView view---
        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView imageView;
            if (convertView == null) {
                imageView = new ImageView(context);
                imageView.setLayoutParams(new GridView.LayoutParams(300, 300));
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView.setPadding(5, 5, 5, 5);
            } else {
                imageView = (ImageView) convertView;
            }
            imageView.setImageResource(imageIDs[position]);
            return imageView;
        }
    }
}

