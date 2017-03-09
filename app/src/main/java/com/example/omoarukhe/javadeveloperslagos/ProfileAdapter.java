package com.example.omoarukhe.javadeveloperslagos;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * Created by Omoarukhe on 09/03/2017.
 */

public class ProfileAdapter extends BaseAdapter {

    private Context mContext;
    private Profile[] mProfile;

    public ProfileAdapter(Context context, Profile[] profiles){
        mContext = context;
        mProfile = profiles;
    }

    @Override
    public int getCount() {
        return mProfile.length;
    }

    @Override
    public Object getItem(int position) {
        return mProfile[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null){

            convertView = LayoutInflater.from(mContext).inflate(R.layout.profile_layout, null);
            holder = new ViewHolder();
            holder.profileImage = (ImageView) convertView.findViewById(R.id.roundImage);
            holder.javaImage = (ImageView) convertView.findViewById(R.id.javaImage);
            holder.userTextName = (TextView) convertView.findViewById(R.id.userName);

            convertView.setTag(holder);

        }else {

            holder = (ViewHolder) convertView.getTag();

        }

        Profile profile = mProfile[position];
        holder.userTextName.setText(profile.getUserName());
        Picasso.with(mContext).load(profile.getUserImage()).into(holder.profileImage);
        Picasso.with(mContext).load(R.drawable.javaimage).into(holder.javaImage);

        return convertView;
    }

    public static class ViewHolder{
        ImageView profileImage;
        TextView userTextName;
        ImageView javaImage;
    }
}
