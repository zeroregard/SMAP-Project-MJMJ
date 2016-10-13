package examproject.group22.roominator.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import examproject.group22.roominator.Models.UserModel;
import examproject.group22.roominator.R;

/**
 * Created by Maria Dam on 02-10-2016.
 */

//https://github.com/codepath/android_guides/wiki/Using-an-ArrayAdapter-with-ListView

//public class UserInfoAdapter extends ArrayAdapter<UserInfo> {
public class UserInfoAdapter extends ArrayAdapter<UserModel> {

    ArrayList<UserModel> users;
    ArrayList<Integer> totals;
    public UserInfoAdapter(Context context, ArrayList<UserModel> users, ArrayList<Integer> totals)
    {
        super(context, 0, users);
        this.users = users;
        this.totals = totals;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {
        final UserModel user = getItem(position);
        if(convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.custom_users, parent, false);

        TextView txtName= (TextView) convertView.findViewById(R.id.customUser_txtName);
        TextView txtTotal= (TextView) convertView.findViewById(R.id.customUser_txtTotal);
        ImageView imgUser = (ImageView) convertView.findViewById(R.id.imgUser);

        txtName.setText(user.name);
        txtTotal.setText("Betalt: " + totals.get(position).toString());

        if(user.image !=null) {
            imgUser.setImageBitmap(user.image);
        }else{
            imgUser.setImageResource(R.drawable.img_placeholder);
        }

        return convertView;
    }


}
