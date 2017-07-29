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
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.ditho.assignment.R;
import io.ditho.assignment.model.repository.entity.ContactEntity;

public class ContactListAdapter extends RecyclerView.Adapter<ContactListAdapter.ViewHolder> {

    private ArrayList<ContactEntity> contactEntityList = new ArrayList<>();
    private HashMap<Integer, ItemState> itemStateMap = new HashMap<>();

    private Context context;
    private LayoutInflater inflater;
    private boolean selectionMode;

    public ContactListAdapter(Context context) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.list_item_contact, null);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        int listSize = contactEntityList.size();
        if (position >= 0 && position < listSize) {
            ContactEntity contactEntity = contactEntityList.get(position);

            holder.bindDataPosition = position;
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

            ItemState itemState = itemStateMap.get(position);

            if (itemState.isExpanded) {
                holder.layoutExpandable.setVisibility(View.VISIBLE);
                holder.buttonToggleView.setText(R.string.collapse);
            } else {
                holder.layoutExpandable.setVisibility(View.GONE);
                holder.buttonToggleView.setText(R.string.expand);
            }

            if (itemState.isSelected) {
                holder.layoutRoot.setBackgroundColor(
                    ResourcesCompat.getColor(
                        context.getResources(),
                        android.R.color.darker_gray,
                        null));
            } else {
                holder.layoutRoot.setBackgroundColor(
                    ResourcesCompat.getColor(
                        context.getResources(),
                        android.R.color.white,
                        null));
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
        }
    }

    @Override
    public int getItemCount() {
        return contactEntityList.size();
    }

    public void clear() {
        contactEntityList.clear();
        itemStateMap.clear();
    }

    public void addAll(Collection<? extends ContactEntity> c) {
        contactEntityList.addAll(c);
        int listSize = contactEntityList.size();
        for (int counter = 0; counter < listSize; counter++) {
            if (!itemStateMap.containsKey(counter)) {
                itemStateMap.put(counter, new ItemState());
            }
        }
    }

    public ContactEntity get(int position) {
        ContactEntity contactEntity = null;
        if (position >= 0 && position < contactEntityList.size()) {
            contactEntity = contactEntityList.get(position);
        }
        return  contactEntity;
    }

    public void setSelectionMode(boolean enable) {
        this.selectionMode = enable;
        if (!enable) {
            clearSelection();
        }
    }

    public boolean getSelectionMode() {
        return selectionMode;
    }

    public boolean isSelected(int position) {
        boolean value = false;
        if (itemStateMap.containsKey(position)) {
            ItemState itemState = itemStateMap.get(position);
            value = itemState.isSelected;
        }
        return value;
    }

    public void addSelection(int position) {
        if (getSelectionMode() && itemStateMap.containsKey(position)) {
            ItemState itemState = itemStateMap.get(position);
            itemState.isSelected = true;
            notifyDataSetChanged();
        }
    }

    public void removeSelection(int position) {
        if (getSelectionMode() && itemStateMap.containsKey(position)) {
            ItemState itemState = itemStateMap.get(position);
            itemState.isSelected = false;
            notifyDataSetChanged();
        }
    }

    public List<ContactEntity> getSelection() {
        int listSize = contactEntityList.size();
        ArrayList<ContactEntity> selectionList = new ArrayList<>();

        for (int counter = 0; counter < listSize; counter++) {
            if (itemStateMap.containsKey(counter)) {
                ItemState itemState = itemStateMap.get(counter);
                if (itemState.isSelected) {
                    selectionList.add(contactEntityList.get(counter));
                }
            }
        }

        return selectionList;
    }

    public void clearSelection() {
        Collection<ItemState> values = itemStateMap.values();
        Iterator<ItemState> iterator = values.iterator();
        while (iterator.hasNext()) {
            ItemState itemState = iterator.next();
            itemState.isSelected = false;
        }
        notifyDataSetChanged();
    }

    protected ItemState getItemState(int position) {
        return itemStateMap.get(position);
    }


    protected  class ItemState {
        boolean isExpanded;
        boolean isSelected;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.layout_root)
        View layoutRoot;

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

        int bindDataPosition;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            bindDataPosition = -1;

            buttonToggleView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = bindDataPosition;
                    if (position >= 0 && position < getItemCount()) {
                        ItemState itemState = getItemState(position);
                        if (itemState.isExpanded) {
                            itemState.isExpanded = false;
                        } else {
                            itemState.isExpanded = true;
                        }
                        notifyDataSetChanged();
                    }
                }
            });
        }
    }


}
