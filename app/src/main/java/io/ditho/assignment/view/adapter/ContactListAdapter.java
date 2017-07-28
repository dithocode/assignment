package io.ditho.assignment.view.adapter;

import android.content.Context;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.FitCenter;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.ditho.assignment.R;
import io.ditho.assignment.repository.entity.ContactEntity;

public class ContactListAdapter extends RecyclerView.Adapter<ContactListAdapter.ViewHolder> {

    private ArrayList<ContactEntity> contactEntityList = new ArrayList<>();

    private Context context;
    private LayoutInflater inflater;

    public ContactListAdapter(Context context) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.list_item_marged_contact, null);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        int listSize = contactEntityList.size();
        if (position >= 0 && position < listSize) {
            ContactEntity contactEntity = contactEntityList.get(position);
            holder.textAccountValue.setText(contactEntity.getAccount());
            holder.textBusinessPhoneValue.setText(contactEntity.getBusinessPhone());
            holder.textFirstNameValue.setText(contactEntity.getFirstName());
            holder.textMiddleNameValue.setText(contactEntity.getMiddleName());
            holder.textLastNameValue.setText(contactEntity.getLastName());
            holder.textFullNameValue.setText(contactEntity.getFullName());
            holder.textGenre.setText(contactEntity.getGender());
            holder.textEmailValue.setText(contactEntity.getEmail());
            holder.textMobileValue.setText(contactEntity.getMobile());
            holder.textNotesValue.setText(contactEntity.getNotes());
            holder.textPhoneValue.setText(contactEntity.getPhone());
            holder.textBusinessEmailValue.setText(contactEntity.getBusinessEmail());
            holder.textBusinessMobileValue.setText(contactEntity.getBusinessMobile());
            holder.textJobTitleValue.setText(contactEntity.getJobTitleDescription());
            holder.imageContact.setContentDescription(contactEntity.getPictureThumbnailUrl());

            if (contactEntity.isExpand()) {
                holder.layoutExpandable.setVisibility(View.VISIBLE);
            } else {
                holder.layoutExpandable.setVisibility(View.GONE);
            }

            Glide.clear(holder.imageContact);

            TextDrawable textDrawable = null;
            try {
                String textChar = "?";
                if (!TextUtils.isEmpty(contactEntity.getFirstName())) {
                    textChar = String.valueOf(contactEntity.getFirstName().charAt(0)).toUpperCase();
                }
                textDrawable = TextDrawable.builder()
                    .buildRect(
                            textChar,
                            ResourcesCompat.getColor(
                                    context.getResources(),
                                    R.color.colorPrimary, null));
            } catch (Exception ex) {
                Log.e(getClass().getName(), ex.getMessage(), ex);
            }

            if (!TextUtils.isEmpty(contactEntity.getPictureThumbnailUrl())) {
                Glide.with(context)
                        .load(contactEntity.getPictureThumbnailUrl())
                        .diskCacheStrategy(DiskCacheStrategy.RESULT)
                        .priority(Priority.HIGH)
                        .bitmapTransform(new FitCenter(context))
                        .placeholder(textDrawable)
                        .crossFade()
                        .error(textDrawable)
                        .into(holder.imageContact);
            } else {
                if (textDrawable != null) {
                    holder.imageContact.setImageDrawable(textDrawable);
                }
            }

            final LinearLayout expandableLayour = holder.layoutExpandable;
            final Button toggleButton = holder.buttonToggleView;
            final int itemPos = position;

            holder.buttonToggleView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (expandableLayour.getVisibility() == View.VISIBLE) {
                        toggleButton.setText(R.string.expand);
                        contactEntityList.get(itemPos).setExpand(false);
                    } else {
                        toggleButton.setText(R.string.collapse);
                        contactEntityList.get(itemPos).setExpand(true);
                    }
                    ContactListAdapter.this.notifyDataSetChanged();
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return contactEntityList.size();
    }

    public void setList(ArrayList<ContactEntity> list) {
        contactEntityList = list;
    }
    public ArrayList<ContactEntity> getList() {
        return contactEntityList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.image_picture)
        ImageView imageContact;

        @Bind(R.id.text_account_value)
        TextView textAccountValue;

        @Bind(R.id.text_first_name_value)
        TextView textFirstNameValue;

        @Bind(R.id.text_middle_name_value)
        TextView textMiddleNameValue;

        @Bind(R.id.text_last_name_value)
        TextView textLastNameValue;

        @Bind(R.id.text_full_name_value)
        TextView textFullNameValue;

        @Bind(R.id.text_email_value)
        TextView textEmailValue;

        @Bind(R.id.text_genre_value)
        TextView textGenre;

        @Bind(R.id.text_mobile_value)
        TextView textMobileValue;

        @Bind(R.id.text_phone_value)
        TextView textPhoneValue;

        @Bind(R.id.text_business_email_value)
        TextView textBusinessEmailValue;

        @Bind(R.id.text_business_mobile_value)
        TextView textBusinessMobileValue;

        @Bind(R.id.text_business_phone_value)
        TextView textBusinessPhoneValue;

        @Bind(R.id.text_job_title_value)
        TextView textJobTitleValue;

        @Bind(R.id.text_notes_value)
        TextView textNotesValue;

        @Bind(R.id.button_toggle_view)
        Button buttonToggleView;

        @Bind(R.id.layout_expandable)
        LinearLayout layoutExpandable;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


}
