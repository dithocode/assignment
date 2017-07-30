package io.ditho.assignment.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.ditho.assignment.R;

public class EditContactListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int TYPE_HEADER = 0;
    public static final int TYPE_ITEM = 1;
    private final Context context;
    private final LayoutInflater inflater;

    private ArrayList<Integer> itemTypeList = new ArrayList<>();
    private ArrayList<String> textList = new ArrayList<>();
    private ArrayList<Boolean> checkedList = new ArrayList<>();

    public EditContactListAdapter(Context context) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        if (viewType == TYPE_ITEM) {
            View view = inflater.inflate(R.layout.list_item_field_value, null);
            viewHolder = new FieldValueViewHolder(view);
        } else {
            View view = inflater.inflate(R.layout.list_item_field_title, null);
            viewHolder = new FieldTitleViewHolder(view);
        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int type = getItemViewType(position);
        int listSize = itemTypeList.size();
        if (position >= 0 && position < listSize)
        if (type == TYPE_ITEM) {
            FieldValueViewHolder viewHolder = (FieldValueViewHolder) holder;
            viewHolder.textFieldValue.setText(textList.get(position));
            if (checkedList.get(position)) {
                viewHolder.imageCheck.setImageResource(android.R.drawable.checkbox_on_background);
            } else {
                viewHolder.imageCheck.setImageResource(android.R.drawable.checkbox_off_background);
            }
            viewHolder.itemPosition = position;
        } else {
            FieldTitleViewHolder viewHolder = (FieldTitleViewHolder) holder;
            viewHolder.textFieldTitle.setText(textList.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return itemTypeList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return itemTypeList.get(position);
    }

    public void setListData(List<String> fieldTitleList,
                             List<ArrayList<String>> fieldValueList) {

        if (fieldTitleList != null &&
            fieldValueList != null) {

            int fieldTitleListSize = fieldTitleList.size();
            int fieldValueListSize = fieldValueList.size();

            if (fieldTitleListSize == fieldValueListSize) {

                this.itemTypeList.clear();
                this.textList.clear();
                this.checkedList.clear();

                for (int titleCounter = 0; titleCounter < fieldTitleListSize; titleCounter++) {
                    this.itemTypeList.add(TYPE_HEADER);
                    this.textList.add(fieldTitleList.get(titleCounter));
                    this.checkedList.add(false);

                    ArrayList<String> valueList = fieldValueList.get(titleCounter);
                    if (valueList != null) {
                        int valueListSize = valueList.size();
                        for (int valueCounter = 0; valueCounter < valueListSize; valueCounter++) {
                            itemTypeList.add(TYPE_ITEM);
                            this.textList.add(valueList.get(valueCounter));
                            this.checkedList.add(false);
                        }
                    }
                }
            }
        }
    }

    public HashMap<String, String> getSelectedData() {
        int listSize = itemTypeList.size();
        HashMap<String, String> result = new HashMap<>();

        String fieldName = "";
        for (int counter = 0; counter < listSize; counter++) {
            if (itemTypeList.get(counter) == TYPE_HEADER) {
                // Initial Set Value
                fieldName = textList.get(counter);
                result.put(fieldName, "");
            } else {
                if (checkedList.get(counter)) {
                    // Set Selected Value
                    result.put(
                        fieldName,
                        textList.get(counter));
                }
            }
        }

        return result;
    }

    public class FieldTitleViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.text_field_title)
        TextView textFieldTitle;

        public FieldTitleViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }

    public class FieldValueViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.text_field_value)
        TextView textFieldValue;

        @Bind(R.id.image_check)
        ImageView imageCheck;

        int itemPosition;

        public FieldValueViewHolder(final View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int reverseCounter = itemPosition - 1;
                    while (reverseCounter >= 0 &&
                            itemTypeList.get(reverseCounter) == TYPE_ITEM) {
                        checkedList.set(reverseCounter, false);
                        reverseCounter--;
                    }
                    int forwardCounter = itemPosition + 1;
                    while (forwardCounter < itemTypeList.size() &&
                            itemTypeList.get(forwardCounter) == TYPE_ITEM) {
                        checkedList.set(forwardCounter, false);
                        forwardCounter++;
                    }
                    checkedList.set(itemPosition, true);
                    notifyDataSetChanged();
                }
            });
        }
    }
}
