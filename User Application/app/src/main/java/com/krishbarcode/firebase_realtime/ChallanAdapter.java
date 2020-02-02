package com.krishbarcode.firebase_realtime;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;


/**
 * Created by kritesh on 28/3/18.
 */

public class ChallanAdapter extends RecyclerView.Adapter<ChallanAdapter.SingleChallan> {

    private ArrayList<Challan> challanList;
    private ItemClickListener itemClickListener;

    public ChallanAdapter(ArrayList<Challan> data, @NonNull ItemClickListener itemClickListener) {
        this.challanList = data;
        this.itemClickListener = itemClickListener;
    }


    @Override
    public SingleChallan onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.challan_card_view_1, parent, false);
        return SingleChallan.newInstance(view);

    }

    @Override
    public void onBindViewHolder(final SingleChallan challan, int listPosition) {
        final Challan challan1 = challanList.get(listPosition);
        challan.setDate(challan1.getDate());
        challan.setDueDate( "6/4/18");
        challan.setLocation(challan1.getLoc());
        challan.setTime(    challan1.getTime());
        challan.setImageViewIcon(challan1.getIsPaid());
        challan.setCardView(challan1.getIsPaid());
        challan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClickListener.onItemClicked(challan);
            }
        });

    }

    @Override
    public int getItemCount() {
        return challanList.size();
    }

    public interface ItemClickListener {
        void onItemClicked(SingleChallan singleChallan);
    }

    //Challan View
    public static final class SingleChallan extends RecyclerView.ViewHolder {
        private final TextView date;
        private final TextView duedate;
        private final TextView location;
        private final TextView time;
        private final ImageView imageViewIcon;
        CardView cardView;



        public static SingleChallan newInstance(View challan) {
            TextView date = challan.findViewById(R.id.date);
            TextView dueDate = challan.findViewById(R.id.duedate);
            TextView location = challan.findViewById(R.id.location);
            TextView time = challan.findViewById(R.id.time);
            CardView cardView = challan.findViewById(R.id.card_view);
            ImageView imageView = challan.findViewById(R.id.imageView);
            return new SingleChallan(challan, date, dueDate, location, time, imageView,cardView);
        }

        public SingleChallan(View itemView, TextView date, TextView duedate, TextView location, TextView time, ImageView imageView, CardView cardView) {
            super(itemView);
            this.date = date;
            this.duedate = duedate;
            this.location = location;
            this.time = time;
            this.cardView = cardView;
            this.imageViewIcon = imageView;

        }

        public TextView getDate() {
            return date;
        }

        public TextView getDuedate() {
            return duedate;
        }

        public TextView getLocation() {
            return location;
        }

        public TextView getTime() {
            return time;
        }

        public ImageView getImageViewIcon() {
            return imageViewIcon;
        }

        public void setDate(CharSequence text) {
            date.setText(text);
        }

        ;

        public void setDueDate(CharSequence text) {
            duedate.setText(text);
        }

        ;

        public void setLocation(CharSequence text) {
            location.setText(text);
        }

        ;

        public void setTime(CharSequence text) {
            time.setText(text);
        }

        ;

        public void setImageViewIcon(boolean isPaid) {
            if (isPaid) {
                cardView.setCardBackgroundColor(Color.parseColor("#c1f0c1"));
                imageViewIcon.setImageResource(R.mipmap.paid_banner_round);
            } else {
                cardView.setCardBackgroundColor(Color.parseColor("#ff9980"));
                imageViewIcon.setImageResource(R.mipmap.unpaid_banner_round);
            }
        }

        ;

        public void setOnClickListener(View.OnClickListener onClickListener) {
            itemView.setOnClickListener(onClickListener);

        }

        public CardView getCardView() {
            return cardView;
        }

        public void setCardView(Boolean is_paid) {

            if (is_paid) {
                cardView.setCardBackgroundColor(Color.parseColor("#c1f0c1"));
               } else {
                cardView.setCardBackgroundColor(Color.parseColor("#ff9980"));
                }
        }
    }
}