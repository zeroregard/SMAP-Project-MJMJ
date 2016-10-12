package examproject.group22.roominator.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import examproject.group22.roominator.DetailListProvider;
import examproject.group22.roominator.Models.GroceryItem;
import examproject.group22.roominator.R;


/**
 * Created by Maria Dam on 10-10-2016.
 */

public class DetailListAdapter extends ArrayAdapter<GroceryItem>
{
    ArrayList<GroceryItem> groceryItems;
    public DetailListAdapter(Context context, ArrayList<GroceryItem> groceryItems)
    {
        super(context, 0, groceryItems);
        this.groceryItems = groceryItems;
    }

    DateFormat dateOnly = new SimpleDateFormat("yyyy/MM/dd");
    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        final GroceryItem grocery = getItem(position);
        if(convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.custom_details, parent, false);
        if(grocery != null) {
            TextView txtName = (TextView) convertView.findViewById(R.id.detail_name);
            TextView txtPrice = (TextView) convertView.findViewById(R.id.detail_price);
            TextView txtDate = (TextView) convertView.findViewById(R.id.detail_buyDate);
            txtName.setText(grocery.name);
            txtPrice.setText(Integer.toString(grocery.price));
            txtDate.setText(dateOnly.format(grocery.boughtStamp));
        }
        return convertView;
    }
}
