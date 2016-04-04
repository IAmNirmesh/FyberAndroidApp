package nirmesh.fyberandroidapp.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;

import nirmesh.fyberandroidapp.Tasks.DownloadImageTask;
import nirmesh.fyberandroidapp.utils.Offer;

/**
 * Created by asd on 2016-04-04.
 */
public class OfferAdapater extends ArrayAdapter<Offer> {
    Context context;
    List<Offer> offers;
    int layoutResourceId;

    public OfferAdapater(Context context, int resourceId, List<Offer> offers) {
        super(context, resourceId, offers);
        this.context = context;
        this.offers = offers;
        this.layoutResourceId = resourceId;
    }

    @Override
    public int getCount() {
        return offers.size();
    }

    @Override
    public Offer getItem(int position) {
        return offers.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            view = inflater.inflate(layoutResourceId, parent, false);
        }

        Offer offer = offers.get(position);

        TextView text = (TextView) view.findViewById(R.id.item_title);
        text.setText(offer.getTitle());

        text = (TextView) view.findViewById(R.id.item_teaser);
        text.setText(offer.getTeaser());

        text = (TextView) view.findViewById(R.id.item_payout);
        text.setText(offer.getPayout());

        ImageView imageView = (ImageView) view.findViewById(R.id.item_thumbnail);

        new DownloadImageTask(imageView)
                .execute(offer.getThumbnailUrl());

        return view;
    }
}
