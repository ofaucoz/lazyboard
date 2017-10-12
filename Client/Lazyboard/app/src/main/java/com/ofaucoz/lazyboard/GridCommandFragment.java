package com.ofaucoz.lazyboard;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.app.FragmentManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

/*
 *  GenericFragment for the slide views
 */
public class GridCommandFragment extends Fragment {
    TypedArray stringCommands = null;
    private LazyboardService mLazyboardService = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_grid_command, container, false);
        final FragmentActivity activity = getActivity();


        GridView gridview = (GridView) rootView.findViewById(R.id.grid_command);
        gridview.setAdapter(new ImageAdapter(activity, R.layout.gridview_items, getData()));

        stringCommands = getResources().obtainTypedArray(R.array.image_string);
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent,
                                    View v, int position, long id) {
                DialogFragment newFragment = DialogCommandFragment.newInstance(
                        R.string.alert_dialog_two_buttons, stringCommands.getString(position));
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

    public class ImageAdapter extends ArrayAdapter {
        private Context context;
        private int layoutResourceId;
        private ArrayList data = new ArrayList();

        public ImageAdapter(Context context, int layoutResourceId, ArrayList data) {
            super(context, layoutResourceId, data);
            this.layoutResourceId = layoutResourceId;
            this.context = context;
            this.data = data;
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
            View row = convertView;
            ViewHolder holder = null;
            if (row == null) {
                LayoutInflater inflater = ((Activity) context).getLayoutInflater();
                row = inflater.inflate(layoutResourceId, parent, false);
                holder = new ViewHolder();
                holder.imageTitle = (TextView) row.findViewById(R.id.grid_text);
                holder.image = (ImageView) row.findViewById(R.id.grid_image);
                row.setTag(holder);
            } else {
                holder = (ViewHolder) row.getTag();
            }

            ImageItem item = (ImageItem) data.get(position);
            holder.imageTitle.setText(item.getTitle());
            holder.image.setImageBitmap(item.getImage());
            return row;
        }

        class ViewHolder {
            TextView imageTitle;
            ImageView image;
        }
    }

    private ArrayList<ImageItem> getData() {
        final ArrayList<ImageItem> imageItems = new ArrayList<>();
        TypedArray imgs = getResources().obtainTypedArray(R.array.image_ids);
        stringCommands = getResources().obtainTypedArray(R.array.image_string);
        for (int i = 0; i < imgs.length(); i++) {
            Log.d("getData" , "current = " + i);
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), imgs.getResourceId(i, -1));
            imageItems.add(new ImageItem(bitmap, stringCommands.getString(i)));
        }
        return imageItems;
    }
}

