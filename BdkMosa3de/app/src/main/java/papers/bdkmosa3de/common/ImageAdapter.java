package papers.bdkmosa3de.common;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import papers.bdkmosa3de.R;
import papers.bdkmosa3de.activities.MainActivity_;
import papers.bdkmosa3de.activities.OrderActivity_;
import papers.bdkmosa3de.activities.SplashScreen;


public class ImageAdapter extends BaseAdapter {
    private final int[] desc;
    private int[] images;
    private Context context;


    public ImageAdapter(Context context, int[] images , int[] desc) {
        this.context = context;
        this.images =images;
        this.desc = desc;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View gridView;

        if (convertView == null) {
            gridView = inflater.inflate(R.layout.grid_row, null);
            ImageView imageView = gridView
                    .findViewById(R.id.grid_item_image);


            TextView textView = gridView.findViewById(R.id.desc);
            textView.setText(desc[position]);
            imageView.setImageResource(images[position]);
            imageView.setOnClickListener(v -> {
                Intent i = new Intent(context, OrderActivity_.class);
                i.putExtra("key",position);
                context.startActivity(i);
            });

        } else {
            gridView = convertView;
        }

        return gridView;
    }

    @Override
    public int getCount() {
        return images.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

}
