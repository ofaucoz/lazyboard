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

    private StringBuffer mOutStringBuffer;

    private EditText mOutEditText;

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

        mOutStringBuffer = new StringBuffer("");

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mOutEditText = (EditText) view.findViewById(R.id.argument_text_out);
        mOutEditText.setOnEditorActionListener(mWriteListener);
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

    /**
     * Sends a message.
     *
     * @param message A string of text to send.
     */
    private void sendMessage(String message) throws UnsupportedEncodingException {
        // Check that we're actually connected before trying anything
        if (mLazyboardService.getState() != LazyboardService.STATE_CONNECTED) {
            Toast.makeText(getActivity(), "not connected", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check that there's actually something to send
        if (message.length() > 0) {
            char[] send = message.toCharArray();
            for (char element : send) {
                mLazyboardService.write((int) element);
            }

            // Reset out string buffer to zero and clear the edit text field
            mOutStringBuffer.setLength(0);
            mOutEditText.setText(mOutStringBuffer);
        }
    }

    private TextView.OnEditorActionListener mWriteListener
            = new TextView.OnEditorActionListener() {
        public boolean onEditorAction(TextView view, int actionId, KeyEvent event) {
            // If the action is a key-up event on the return key, send the message
            if (actionId == EditorInfo.IME_NULL && event.getAction() == KeyEvent.ACTION_UP) {
                String message = view.getText().toString();
                try {
                    sendMessage(message + "\n");
                } catch (UnsupportedEncodingException e) {
                    throw new AssertionError("UTF-8 is unknown");
                }
            }
            return true;
        }
    };

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

