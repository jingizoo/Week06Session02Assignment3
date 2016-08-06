package com.jalaj.firstapp.fragmentlayoutimplementation1;

import android.app.Fragment;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class FragmentsLayout extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toast.makeText(this, "FragmentLayout: OnCreate()", Toast.LENGTH_SHORT)
                .show();
        setContentView(R.layout.activity_fragments_layout);
    }
    public static class DetailsActivity extends AppCompatActivity {


        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            Toast.makeText(this, "DetailsActivity", Toast.LENGTH_SHORT).show();

            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                // If the screen is now in landscape mode, we can show the
                // dialog in-line with the list so we don't need this activity.
                finish();
                return;
            }

            if (savedInstanceState == null) {
                // During initial setup, plug in the details fragment.

                // create fragment
                FragmentsLayout.DetailsFragment details = new FragmentsLayout.DetailsFragment();

                // get and set the position input by user (i.e., "index")
                // which is the construction arguments for this fragment
                details.setArguments(getIntent().getExtras());

                //
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.add(android.R.id.content,details);
                ft.commit();
            }
        }

    }
    public static class TitlesFragment extends ListFragment{
         boolean mDualPane;
        int mCurCheckPosition = 0;
        @Override
        public void onActivityCreated(@Nullable Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
            Toast.makeText(getActivity(), "TitlesFragment:onActivityCreated", Toast.LENGTH_LONG).show();

            setListAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_activated_1, Shakespeare.TITLES));
            View detailsFrame = getActivity().findViewById(R.id.details);
            Toast.makeText(getActivity(), "detailsFrame " + detailsFrame, Toast.LENGTH_LONG).show();
            mDualPane = detailsFrame != null && detailsFrame.getVisibility() == View.VISIBLE;
            Log.d("MDualPane",mDualPane+"");
            if (savedInstanceState != null) {
                mCurCheckPosition = savedInstanceState.getInt("curChoice", 0);
            }

            if (mDualPane) {
                getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);

                showDetails(mCurCheckPosition);
            } else {
                getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
                getListView().setItemChecked(mCurCheckPosition, true);
            }
        }

        @Override
        public void onSaveInstanceState(Bundle outState) {
            super.onSaveInstanceState(outState);
            Toast.makeText(getActivity(), "onSaveInstanceState",
                    Toast.LENGTH_LONG).show();

            outState.putInt("curChoice", mCurCheckPosition);
        }

        // If the user clicks on an item in the list (e.g., Henry V then the
        // onListItemClick() method is called. It calls a helper function in
        // this case.

        @Override
        public void onListItemClick(ListView l, View v, int position, long id) {

            Toast.makeText(getActivity(),
                    "onListItemClick position is" + position, Toast.LENGTH_LONG)
                    .show();

            showDetails(position);
        }

        void showDetails(int index) {
            mCurCheckPosition = index;


            if (mDualPane) {
                getListView().setItemChecked(index, true);
                 DetailsFragment details = (DetailsFragment) getFragmentManager().findFragmentById(R.id.details);
                if (details == null || details.getShownIndex() != index) {
                    details = DetailsFragment.newInstance(index);

                    Toast.makeText(getActivity(),
                            "showDetails dual-pane: create and relplace fragment",
                            Toast.LENGTH_LONG).show();
                            FragmentTransaction ft = getFragmentManager()
                            .beginTransaction();
                    ft.replace(R.id.details,details);
                   // ft.setTransition(FragmentTransaction.TRANSIT_NONE);
                    ft.commit();
                }

            } else {
                // Otherwise we need to launch a new activity to display
                // the dialog fragment with selected text.
                // That is: if this is a single-pane (e.g., portrait mode on a
                // phone) then fire DetailsActivity to display the details
                // fragment

                // Create an intent for starting the DetailsActivity
                Intent intent = new Intent();

                // explicitly set the activity context and class
                // associated with the intent (context, class)
               intent.setClass(getActivity(), FragmentsLayout.DetailsActivity.class);

                // pass the current position
                intent.putExtra("index", index);

                startActivity(intent);
            }
        }
    }
    public static class DetailsFragment extends android.support.v4.app.Fragment {

        // Create a new instance of DetailsFragment, initialized to show the
        // text at 'index'.

        public static DetailsFragment newInstance(int index) {
            DetailsFragment f = new DetailsFragment();

            // Supply index input as an argument.
            Bundle args = new Bundle();
            args.putInt("index", index);
            f.setArguments(args);

            return f;
        }

        public int getShownIndex() {
            return getArguments().getInt("index", 0);
        }

        // The system calls this when it's time for the fragment to draw its
        // user interface for the first time. To draw a UI for your fragment,
        // you must return a View from this method that is the root of your
        // fragment's layout. You can return null if the fragment does not
        // provide a UI.

        // We create the UI with a scrollview and text and return a reference to
        // the scoller which is then drawn to the screen

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            Toast.makeText(getActivity(), "DetailsFragment:onCreateView",
                    Toast.LENGTH_LONG).show();
            //
            if (container == null) {
            // // We have different layouts, and in one of them this
            // // fragment's containing frame doesn't exist. The fragment
            // // may still be created from its saved state, but there is
            // // no reason to try to create its view hierarchy because it
            // // won't be displayed. Note this is not needed -- we could
            // // just run the code below, where we would create and return
            // // the view hierarchy; it would just never be used.
            return null;
             }

            // If non-null, this is the parent view that the fragment's UI
            // should be attached to. The fragment should not add the view
            // itself, but this can be used to generate the LayoutParams of
            // the view.
            //

            // programmatically create a scrollview and texview for the text in
            // the container/fragment layout. Set up the properties and add the
            // view.

            ScrollView scroller = new ScrollView(getActivity());
            TextView text = new TextView(getActivity());
            int padding = (int) TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP, 4, getActivity()
                            .getResources().getDisplayMetrics());
            text.setPadding(padding, padding, padding, padding);
            scroller.addView(text);
            text.setText(Shakespeare.DIALOGUE[getShownIndex()]);
            return scroller;
        }
    }

    }




