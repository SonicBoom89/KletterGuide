package wolfgang.bergbauer.de.kletterguide.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.GridLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import wolfgang.bergbauer.de.kletterguide.R;
import wolfgang.bergbauer.de.kletterguide.model.ClimbingRoute;

/**
 * Created by Wolfgang on 05.08.2015.
 */
public class RouteCardViewAdapter extends RecyclerView.Adapter<RouteCardViewAdapter.RouteViewHolder> {

    private List<ClimbingRoute> routeList;
    // Allows to remember the last item shown on screen

    public RouteCardViewAdapter(List<ClimbingRoute> routeList) {
        this.routeList = routeList;
    }

    @Override
    public int getItemCount() {
        return routeList.size();
    }

    @Override
    public void onBindViewHolder(RouteViewHolder routeViewHolder, int position) {
        ClimbingRoute ci = routeList.get(position);
        routeViewHolder.vTitle.setText(ci.getName());
        routeViewHolder.vDifficulty.setText(ci.getUIAARank());
    }


    @Override
    public RouteViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.cardview_route, viewGroup, false);

        return new RouteViewHolder(itemView);
    }

    public class RouteViewHolder extends RecyclerView.ViewHolder {
        protected android.support.v7.widget.GridLayout vContainer;
        protected TextView vTitle;
        protected TextView vDifficulty;

        public RouteViewHolder(View v) {
            super(v);
            vContainer = (android.support.v7.widget.GridLayout) v.findViewById(R.id.container);
            vTitle = (TextView) v.findViewById(R.id.title);
            vDifficulty =  (TextView) v.findViewById(R.id.txtDifficulty);
        }
    }

}