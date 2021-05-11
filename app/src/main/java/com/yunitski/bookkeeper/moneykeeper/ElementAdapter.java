package com.yunitski.bookkeeper.moneykeeper;

import android.graphics.Color;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ElementAdapter extends RecyclerView.Adapter<ElementAdapter.ViewHolder>  {

    private final LayoutInflater inflater;
    private final List<Element> elementList;

    //экземпляр position и 2 метода используются для получения конкретной позиции
    private int position;

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }


    public ElementAdapter(LayoutInflater inflater, List<Element> elementList) {
        this.inflater = inflater;
        this.elementList = elementList;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Element element = elementList.get(position);
        holder.valueView.setText(element.getValue());
        holder.totalValueView.setText(element.getTotalValue());
        holder.dateView.setText(element.getDate());
        holder.imageView.setImageResource(element.getOperation());
        holder.sumCur.setText(element.getCurrency());
        holder.balCur.setText(element.getCurrency());
        holder.categ.setText(element.getCategory());
        holder.constraintLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                setPosition(holder.getAdapterPosition());
                return false;
            }
        });

    }

    @Override
    public int getItemCount() {
        return elementList.size();
    }

    //метод нужен для очистки адаптера, чтобы его можно было перезаписать
    //иначе recyclerview будет разрастаться при каждом onCreate
    public void clear() {
        int size = elementList.size();
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                elementList.remove(0);
            }

            notifyItemRangeRemoved(0, size);
        }
    }
    public void addAll(ArrayList<Element> list){
        elementList.addAll(list);
        notifyDataSetChanged();
    }
    @Override
    public void onViewRecycled(ViewHolder holder) {
        holder.itemView.setOnLongClickListener(null);
        super.onViewRecycled(holder);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
        final TextView valueView, totalValueView, dateView, sumCur, balCur, categ;
        final ImageView imageView;
        final ConstraintLayout constraintLayout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            valueView = itemView.findViewById(R.id.tv_value);
            totalValueView = itemView.findViewById(R.id.tv_total_value);
            dateView = itemView.findViewById(R.id.tv_date);
            imageView = itemView.findViewById(R.id.imageView);
            sumCur = itemView.findViewById(R.id.sum_cur);
            balCur = itemView.findViewById(R.id.bal_cur);
            categ = itemView.findViewById(R.id.tv_category);
            constraintLayout = itemView.findViewById(R.id.item);
            itemView.setOnCreateContextMenuListener(this);

        }
        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.add(0, 1, getAdapterPosition(), "action 1");
            //установка бэкграунда контекстного меню
            int positionOfMenuItem = 0;
            MenuItem item = menu.getItem(positionOfMenuItem);
            SpannableString s = new SpannableString("Удалить");
            s.setSpan(new ForegroundColorSpan(Color.BLACK), 0, s.length(), 0);
            item.setTitle(s);
        }
    }
}
