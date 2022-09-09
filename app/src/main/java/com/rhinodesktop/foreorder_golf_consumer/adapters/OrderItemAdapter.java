package com.rhinodesktop.foreorder_golf_consumer.adapters;

import android.app.Activity;
import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.rhinodesktop.foreorder_golf_consumer.R;
import com.rhinodesktop.foreorder_golf_consumer.events.RemoveOrderItemEvent;
import com.rhinodesktop.foreorder_golf_consumer.models.Club;
import com.rhinodesktop.foreorder_golf_consumer.models.OptionGroupWithItems;
import com.rhinodesktop.foreorder_golf_consumer.models.OptionItem;
import com.rhinodesktop.foreorder_golf_consumer.models.Order;
import com.rhinodesktop.foreorder_golf_consumer.models.OrderItem;
import com.rhinodesktop.foreorder_golf_consumer.utils.ForeOrderDialogUtils;
import com.rhinodesktop.foreorder_golf_consumer.utils.ForeOrderResourceUtils;
import com.rhinoactive.generalutilities.PriceUtils;
import com.rhinodesktop.foreorder_golf_consumer.utils.ForeOrderSharedPrefUtils;
import com.rhinodesktop.foreorder_golf_consumer.views.OptionItemCheckoutLayout;
import com.rhinoactive.generalutilities.MainThreadRunner;
import com.rhinoactive.generalutilities.ResourceUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.Locale;

import io.realm.Realm;

/**
 * Created by rhinodesktop on 2017-03-28.
 */

public class OrderItemAdapter extends RecyclerView.Adapter<OrderItemViewHolder> {

    private Activity activity;
    private Order order;

    public OrderItemAdapter(Activity activity, Order order) {
        this.activity = activity;
        this.order = order;
    }

    @Override
    public OrderItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.
                from(parent.getContext()).
                inflate(viewType, parent, false);
        return new OrderItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(OrderItemViewHolder holder, int position) {
        if (isLastItem(position)) {
            bindOrderCostViewHolder(holder);
        } else if (position != 0) {
            bindOrderItemViewHolder(holder, position);
        }
    }

    @Override
    public int getItemCount() {
        //Size is the number of order items plus 2 for the header and the footer
        int numOfItems = order.getOrderItems().size();
        return numOfItems + 2;
    }

    @Override
    public int getItemViewType(int position) {
        int layout;
        if (position == 0) {
            layout = R.layout.your_order_view;
        } else if (isLastItem(position)) {
            layout = R.layout.order_cost_view;
        } else {
            layout = R.layout.order_item_view;
        }
        return layout;
    }

    private void bindOrderCostViewHolder(OrderItemViewHolder holder) {
        //TODO: update tax calculation
        float price = order.getPriceTotal();
        holder.getSubtotalTextView().setText(String.format(Locale.ENGLISH, "$%.2f", price));

//        order.setPriceTotalTax(calculateTotalTax(order.getOrderItems()));

//        float hst = order.getPriceTotalTax();
        float hst = PriceUtils.calculateHst(price);
        holder.getHstTextView().setText(String.format(Locale.ENGLISH, "$%.2f", hst));

        float totalPrice = price + hst;

        Realm realm = Realm.getDefaultInstance();
        Club currentClub = realm.where(Club.class).equalTo("clubId", ForeOrderSharedPrefUtils.getCurrentClubId(activity)).findFirst();
        realm.close();

        if (currentClub.getShowTax()) {
            holder.getTotalPriceTextView().setText(String.format(Locale.ENGLISH, "$%.2f", totalPrice));
            holder.getRLayoutSubtotal().setVisibility(View.VISIBLE);
            holder.getRLayoutHST().setVisibility(View.VISIBLE);
        } else {
            holder.getRLayoutSubtotal().setVisibility(View.GONE);
            holder.getRLayoutHST().setVisibility(View.GONE);
            holder.getTotalPriceTextView().setText(String.format(Locale.ENGLISH, "$%.2f", price));
        }
    }

//    private float calculateTotalTax(List<OrderItem> orderItemsList) {
//        float totalTax = 0;
//
//        for (OrderItem orderItem : orderItemsList) {
//
//        }
//
//        return totalTax;
//    }

    private void bindOrderItemViewHolder(OrderItemViewHolder holder, int position) {
        int index = position - 1; //minus 1 to account for the header
        OrderItem orderItem = order.getOrderItems().get(index);
        holder.getItemNameTextView().setText(orderItem.getItemName());
        holder.getNumOfItemsTextView().setText(String.format(Locale.ENGLISH, "%d", orderItem.numOfItems()));
        holder.getPriceOfItemsTextView().setText(String.format(Locale.ENGLISH, "$%.2f", orderItem.getPrice()));
        setSpecialInstructionsView(holder, orderItem);
        setOptionItemViews(holder, orderItem);
        initRemoveItemButton(holder, orderItem);
    }

    private void initRemoveItemButton(final OrderItemViewHolder holder, final OrderItem orderItem) {
        final ResourceUtils resourceUtils = ForeOrderResourceUtils.getInstance();
        final Context context = holder.getRemoveItemButton().getContext();
        holder.getRemoveItemButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ForeOrderDialogUtils.getInstance().buildDialog(context,
                        String.format(Locale.ENGLISH, "%s %s %s", resourceUtils.strRes( R.string.remove ), orderItem.getItemName(), resourceUtils.strRes( R.string.from_cart)))
                        .content(resourceUtils.strRes( R.string.remove_item_message))
                        .negativeText(R.string.cancel)
                        .positiveText(R.string.remove)
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                removeOrderItem(holder, orderItem);
                            }
                        })
                        .show();
            }
        });
    }

    private void removeOrderItem(final OrderItemViewHolder holder, final OrderItem orderItem) {
        boolean removed = order.removeOrderItem(orderItem);
        if (removed) {
            EventBus.getDefault().post(new RemoveOrderItemEvent(orderItem));
            MainThreadRunner.runFromUiThread(new Runnable() {
                @Override
                public void run() {
                    notifyItemChanged(getItemCount());
                    notifyItemRemoved(holder.getAdapterPosition());
                }
            });
        }
        if (order.getTotalNumberOfItemsInOrder() == 0) {
            activity.finish();
        }
    }

    private void setSpecialInstructionsView(OrderItemViewHolder holder, OrderItem orderItem) {
        if (orderItem.hasSpecialInstructions()) {
            holder.getInstructionsTextView().setText(orderItem.getSpecialInstructions());
            holder.getInstructionsTextView().setVisibility(View.VISIBLE);
        } else {
            holder.getInstructionsTextView().setVisibility(View.GONE);
        }
    }

    private void setOptionItemViews(OrderItemViewHolder holder, OrderItem orderItem) {
        if (orderItem.getOptionGroupWithItemsList().size() == 0) {
            return;
        }
        LinearLayout optionItemsLayout = holder.getOptionItemsLayout();
        for (OptionGroupWithItems optionGroupWithItems : orderItem.getOptionGroupWithItemsList()) {
            for (OptionItem optionItem : optionGroupWithItems.getOptionItems()) {
                OptionItemCheckoutLayout optionItemCheckoutLayout = new OptionItemCheckoutLayout(optionItemsLayout.getContext());
                optionItemCheckoutLayout.setOptionItemText(optionItem);
                optionItemsLayout.addView(optionItemCheckoutLayout);
            }
        }
    }

    private boolean isLastItem(int position) {
        return position == order.getOrderItems().size() + 1; // plus 1 for the header
    }
}
