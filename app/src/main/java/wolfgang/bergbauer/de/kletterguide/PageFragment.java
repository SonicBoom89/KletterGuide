package wolfgang.bergbauer.de.kletterguide;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import wolfgang.bergbauer.de.kletterguide.adapter.ClimbingGridViewAdapter;
import wolfgang.bergbauer.de.kletterguide.model.ClimbingAreaType;
import wolfgang.bergbauer.de.kletterguide.model.ClimbingBase;

/**
 * Created by Wolfgang on 04.08.2015.
 */
// In this case, the fragment displays simple text based on the page
public class PageFragment extends Fragment {
    public static final String ARG_PAGE = "ARG_PAGE";

    private int mPage;

    public static PageFragment newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        PageFragment fragment = new PageFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPage = getArguments().getInt(ARG_PAGE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        GridView gridView = (GridView) view.findViewById(R.id.gridView_climbing);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            gridView.setNestedScrollingEnabled(true);
        }
        gridView.setAdapter(new ClimbingGridViewAdapter(getActivity(), createStubs()));
        return view;
    }

    private List<ClimbingBase> createStubs() {
        List<ClimbingBase> items = new ArrayList<>();
        for (int i = 0; i< 100; ++i) {
            ClimbingBase item = new ClimbingBase(ClimbingAreaType.OUTDOOR);
            item.setName("Area " + (i +1));
            item.setRanking((float) Math.random());
            item.setDrawableUrl("bg");
            items.add(item);
        }
        return items;
    }
}
