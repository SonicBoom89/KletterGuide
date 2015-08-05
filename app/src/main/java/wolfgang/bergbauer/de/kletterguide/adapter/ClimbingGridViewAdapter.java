package wolfgang.bergbauer.de.kletterguide.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;

import wolfgang.bergbauer.de.kletterguide.R;
import wolfgang.bergbauer.de.kletterguide.model.ClimbingBase;

/**
 * Created by berg21 on 05.08.2015.
 */
public class ClimbingGridViewAdapter extends BaseAdapter {

    private Context mContext;

    List<ClimbingBase> items;

    public ClimbingGridViewAdapter(Context c, List<ClimbingBase> items) {
        mContext = c;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return items.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View grid;
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {

            ClimbingBase currentItem = items.get(position);

            grid = new View(mContext);
            grid = inflater.inflate(R.layout.climbing_item, null);
            TextView title = (TextView) grid.findViewById(R.id.textView_climbing_item_title);
            TextView subTitle = (TextView) grid.findViewById(R.id.textView_climbing_item_subtitle);
            ImageView imageView = (ImageView)grid.findViewById(R.id.imageView_climbing_item);
            if (currentItem.getDrawableUrl() != null) {
                try {
                    Drawable d = Drawable.createFromStream(mContext.getAssets().open(currentItem.getDrawableUrl()), null);
                    imageView.setImageDrawable(d);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
            title.setText(currentItem.getName());
            subTitle.setText(currentItem.getRanking() + "");

        } else {
            grid = (View) convertView;
        }

        return grid;
    }

}
