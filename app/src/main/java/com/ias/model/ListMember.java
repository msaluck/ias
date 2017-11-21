package com.ias.model;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ias.activity.HomeActivity;
import com.ias.fragment.MemberDetailFragment;
import com.ias.fragment.MemberFragment;
import com.ias.iaslogin.R;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Belal on 9/14/2017.
 */

//we need to extend the ArrayAdapter class as we are building an adapter
public class ListMember extends BaseAdapter implements Filterable{

    ArrayList<Member> memberListOriginal;
    ArrayList<Member> memberListFilter;
    MemberDetailFragment memberDetailFragment;
    LayoutInflater inflater;
    Context context;

    public ListMember(Context context, ArrayList<Member> mProductArrayList) {
        this.memberListOriginal = mProductArrayList;
        this.memberListFilter = mProductArrayList;
        this.context=context;
    }

    @Override
    public int getCount() {
        return memberListFilter.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class ViewHolder {
        RelativeLayout RLcontainer;
        TextView userID,fullname,chapterName,stnkNumber;
        CircleImageView imageProfile;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

       ViewHolder holder;
        if (inflater == null)
            inflater = (LayoutInflater)
                    context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.member, null);
            holder.RLcontainer = (RelativeLayout) convertView.findViewById(R.id.line1);
            holder.userID = (TextView) convertView.findViewById(R.id.textViewID);
            holder.fullname = (TextView) convertView.findViewById(R.id.textViewName);
            holder.chapterName = (TextView) convertView.findViewById(R.id.textViewChapter);
            holder.stnkNumber = (TextView) convertView.findViewById(R.id.textViewNoStnk);
            holder.imageProfile = (CircleImageView) convertView.findViewById(R.id.imageView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.userID.setText(memberListFilter.get(position).getUserName());
        holder.fullname.setText(memberListFilter.get(position).getFullName());
        holder.chapterName.setText(memberListFilter.get(position).getChapterName());
        holder.stnkNumber.setText(memberListFilter.get(position).getStnkNumber());
        holder.imageProfile.setImageResource(R.drawable.ic_menu_camera);
        if (null!=memberListFilter.get(position).getImageProfile()) {
            holder.imageProfile.setImageBitmap(getImageBitmap(memberListFilter.get(position).getImageProfile()));

        }

        holder.RLcontainer.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                MemberDetailFragment memberDetailFragment= new MemberDetailFragment();
                Bundle args = new Bundle();
                args.putString("userID", memberListFilter.get(position).getUserName());
                args.putString("fullName", memberListFilter.get(position).getFullName());
                args.putString("stnkNumber", memberListFilter.get(position).getStnkNumber());
                args.putString("chapterName", memberListFilter.get(position).getChapterName());
                args.putString("urlImage", memberListFilter.get(position).getImageProfile());
                args.putString("branchName", memberListFilter.get(position).getBranchName());
                args.putString("brandName", memberListFilter.get(position).getBrandName());
                args.putString("carTypeName", memberListFilter.get(position).getCarTypeName());
                args.putString("communityAdminName", memberListFilter.get(position).getCommunityAdminName());
                args.putString("iasAdminName", memberListFilter.get(position).getIasAdminName());
                args.putString("noSim", memberListFilter.get(position).getNoSim());
                args.putString("regionName", memberListFilter.get(position).getRegionName());
                args.putString("year", memberListFilter.get(position).getYear());
                args.putString("hp", memberListFilter.get(position).getHp());



                memberDetailFragment.setArguments(args);
                ((HomeActivity) context).getSupportFragmentManager().beginTransaction().replace(R.id.content_frame,memberDetailFragment).addToBackStack(null).commit();
            }
        });

        return convertView;
    }


    public Bitmap getImageBitmap(String image){
        byte[] decodedBytes = Base64.decode(image, 0);
        Bitmap bmp = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
        return bmp;
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint,FilterResults results) {

                memberListFilter = (ArrayList<Member>) results.values; // has the filtered values
                notifyDataSetChanged();  // notifies the data with new filtered values
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();        // Holds the results of a filtering operation in values
                ArrayList<Member> FilteredArrList = new ArrayList<Member>();

                if (memberListOriginal == null) {
                    memberListOriginal = new ArrayList<Member>(memberListFilter); // saves the original data in mOriginalValues
                }

                /********
                 *
                 *  If constraint(CharSequence that is received) is null returns the mOriginalValues(Original) values
                 *  else does the Filtering and returns FilteredArrList(Filtered)
                 *
                 ********/
                if (constraint == null || constraint.length() == 0) {

                    // set the Original result to return
                    results.count = memberListOriginal.size();
                    results.values = memberListOriginal;
                } else {
                    constraint = constraint.toString().toLowerCase();
                    for (Member m : memberListOriginal) {
                       if (m.getFullName().toLowerCase().contains(constraint.toString())||m.getUserName().toLowerCase().contains(constraint.toString())||m.getChapterName().toLowerCase().contains(constraint.toString())||m.getStnkNumber().toLowerCase().contains(constraint.toString())) {
                            FilteredArrList.add(m);
                        }
                    }
                    // set the Filtered result to return
                    results.count = FilteredArrList.size();
                    results.values = FilteredArrList;
                }
                return results;
            }
        };
        return filter;
    }

}
