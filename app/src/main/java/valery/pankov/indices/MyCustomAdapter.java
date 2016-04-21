package valery.pankov.indices;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Valery on 05.03.2016.
 */
public class MyCustomAdapter extends BaseAdapter implements ListAdapter {
    private ArrayList<String> listsymb = new ArrayList<String>();
    private ArrayList<String> listcurpr = new ArrayList<String>();
    private ArrayList<String> listcurch = new ArrayList<String>();
    private Context context;

    public MyCustomAdapter(ArrayList<String> listsymb, ArrayList<String> listcurpr, ArrayList<String> listcurch, Context context) {
        this.listsymb = listsymb;
        this.listcurpr = listcurpr;
        this.listcurch = listcurch;
        this.context = context;
    }
    @Override
    public int getCount() {
        return listsymb.size();
    }

    @Override
    public Object getItem(int pos) {
        return listsymb.get(pos);
    }

    @Override
    public long getItemId(int pos) {
        return 0;
    }

    //@Override
    //public long getItemId(int pos) {
    //    return list.get(pos).getId();
        //just return 0 if your list items do not have an Id variable.
   // }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.primaryinforow, null);
        }

        //Handle TextView and display string from your list
        TextView symbcomp = (TextView)view.findViewById(R.id.symbcomp);
        symbcomp.setText(listsymb.get(position));

        TextView curprice = (TextView)view.findViewById(R.id.curprice);
        curprice.setText(listcurpr.get(position));

        TextView curchange = (TextView)view.findViewById(R.id.curchange);
        curchange.setText(listcurch.get(position));
        Float f = Float.parseFloat(listcurch.get(position));
        if(f>=0){
            curchange.setTextColor(Color.GREEN);
        }else{
            curchange.setTextColor(Color.RED);
        }





        return view;
    }
}
