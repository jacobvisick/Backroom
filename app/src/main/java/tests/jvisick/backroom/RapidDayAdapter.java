package tests.jvisick.backroom;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.TextView;

import io.realm.RealmBaseAdapter;
import io.realm.RealmResults;
import tests.jvisick.backroom.BackroomObjects.RapidDay;

/**
 * Created by scott on 3/6/16.
 */
public class RapidDayAdapter extends RealmBaseAdapter<RapidDay> implements ListAdapter {

    private static class ViewHolder {
        TextView rapidDay;
    }

    public RapidDayAdapter(Context context, int resId, RealmResults<RapidDay> realmResults, boolean automaticUpdate) {
        super(context, realmResults, automaticUpdate);
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

        RapidDay item = realmResults.get(position);
        viewHolder.rapidDay.setText(item.getDate());
        return convertView;
    }

    public RealmResults<RapidDay> getRealmResults() {
        return realmResults;
    }
}
