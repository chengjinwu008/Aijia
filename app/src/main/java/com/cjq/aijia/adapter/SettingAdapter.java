package com.cjq.aijia.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cjq.aijia.R;
import com.cjq.aijia.entity.SettingItem;

import java.util.List;

/**
 * Created by CJQ on 2015/12/1.
 */
public class SettingAdapter extends RecyclerView.Adapter<SettingAdapter.Holder>{

    private List<SettingItem> itemList;
    public static final int ITEM_TYPE_MENU = 0;
    public static final int ITEM_TYPE_BUTTON = 1;

    public SettingAdapter(List<SettingItem> itemList) {
        this.itemList = itemList;
    }

    private ItemClickedInterface itemClickedInterface;

    public ItemClickedInterface getItemClickedInterface() {
        return itemClickedInterface;
    }

    public SettingAdapter setItemClickedInterface(ItemClickedInterface itemClickedInterface) {
        this.itemClickedInterface = itemClickedInterface;
        return this;
    }

    public interface ItemClickedInterface{
        void onItemClicked(int position,Object entity,View childView);
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        SettingItem.TYPE type = SettingItem.TYPE.MENU;
        switch (viewType){
            case ITEM_TYPE_MENU:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_menu,parent,false);
                type = SettingItem.TYPE.MENU;
                break;
            case ITEM_TYPE_BUTTON:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_button,parent,false);
                type = SettingItem.TYPE.BUTTON;
                break;
        }
        return new Holder(view,type);
    }

    @Override
    public void onBindViewHolder(Holder holder, final int position) {
        final SettingItem item = itemList.get(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(itemClickedInterface!=null){
                    itemClickedInterface.onItemClicked(position,item,v);
                }
            }
        });

        switch (item.getType()){
            case BUTTON:
                holder.buttonMSG.setText(item.getMainMSG());
                break;
            case MENU:
                holder.menuMSG.setText(item.getMainMSG());
                holder.menuSubMSG.setText(item.getSubMSG());
                holder.arrow.setVisibility(item.isShowArrow()?View.VISIBLE:View.GONE);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public static class Holder extends RecyclerView.ViewHolder {

//        @InjectView(R.id.recycler_button_main_msg)
        TextView buttonMSG;
//        @InjectView(R.id.recycler_menu_main_msg)
        TextView menuMSG;
//        @InjectView(R.id.recycler_menu_sub_msg)
        TextView menuSubMSG;
//        @InjectView(R.id.recycler_menu_arrow)
        View arrow;

        public Holder(View itemView,SettingItem.TYPE type) {
            super(itemView);
//            ButterKnife.inject(this,itemView);
            switch (type){
                case MENU:
                    menuMSG = (TextView) itemView.findViewById(R.id.recycler_menu_main_msg);
                    menuSubMSG = (TextView) itemView.findViewById(R.id.recycler_menu_sub_msg);
                    arrow = itemView.findViewById(R.id.recycler_menu_arrow);
                    break;
                case BUTTON:
                    buttonMSG = (TextView) itemView.findViewById(R.id.recycler_button_main_msg);
                    break;
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        SettingItem item = itemList.get(position);

        int type = 0;

        switch (item.getType()) {
            case MENU:
                type = ITEM_TYPE_MENU;
                break;
            case BUTTON:
                type = ITEM_TYPE_BUTTON;
                break;
        }
        return type;
    }
}
