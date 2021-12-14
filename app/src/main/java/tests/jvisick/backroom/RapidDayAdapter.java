package tests.jvisick.backroom;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.zip.Inflater;

import io.realm.OrderedRealmCollection;
import io.realm.RealmBaseAdapter;
import io.realm.RealmResults;
import tests.jvisick.backroom.BackroomObjects.RapidDay;

/**
 * Created by scott on 3/6/16.
 */
public class RapidDayAdapter extends RealmBaseAdapter<RapidDay> implements ListAdapter {

    OrderedRealmCollection<RapidDay> results;
    LayoutInflater inflater;

    private static class ViewHolder {
        TextView rapidDay;
    }

    public RapidDayAdapter(Context context, int resId, OrderedRealmCollection<RapidDay> realmResults, boolean automaticUpdate) {
        super(realmResults);
        results = realmResults;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.rapidDay = (TextView) convertView.findViewById(android.R.id.text1);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        RapidDay item = results.get(position);
        viewHolder.rapidDay.setText(item.getDate());
        return convertView;
    }

    public OrderedRealmCollection<RapidDay> getRealmResults() {
        return results;
    }
}
