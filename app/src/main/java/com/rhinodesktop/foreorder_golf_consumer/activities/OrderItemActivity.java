package com.rhinodesktop.foreorder_golf_consumer.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.rhinoactive.foreorder_library_android.utils.Constants;
import com.rhinoactive.generalutilities.SizeConverter;
import com.rhinoactive.generalutilities.ViewUtils;
import com.rhinodesktop.activityanimatorutility.fragmentutils.SupportFragmentAnimateManager;
import com.rhinodesktop.foreorder_golf_consumer.R;
import com.rhinodesktop.foreorder_golf_consumer.events.MultiOptionCheckedEvent;
import com.rhinodesktop.foreorder_golf_consumer.events.SingleOptionCheckedEvent;
import com.rhinodesktop.foreorder_golf_consumer.fragments.ChoiceFragment;
import com.rhinodesktop.foreorder_golf_consumer.fragments.MultiChoiceFragment;
import com.rhinodesktop.foreorder_golf_consumer.fragments.SingleChoiceFragment;
import com.rhinodesktop.foreorder_golf_consumer.managers.CurrentOrder;
import com.rhinodesktop.foreorder_golf_consumer.models.MenuItem;
import com.rhinodesktop.foreorder_golf_consumer.models.OptionGroup;
import com.rhinodesktop.foreorder_golf_consumer.models.OptionGroupWithItems;
import com.rhinodesktop.foreorder_golf_consumer.models.OptionItem;
import com.rhinodesktop.foreorder_golf_consumer.models.Order;
import com.rhinodesktop.foreorder_golf_consumer.utils.ForeOrderResourceUtils;
import com.rhinodesktop.foreorder_golf_consumer.utils.ForeOrderToastUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class OrderItemActivity extends OrderItemAppBarActivity {

    public static final String ORDER_MENU_ITEM_KEY = "order_menu_item_key";

    private MenuItem menuItem;
    private EditText specialInstructionEditText;
    private TextView numOfItemTextView;
    private TextView addToCartTextView;
    private TextView totalPriceTextView;
    private Integer numOfItems = 1;

    private Map<ChoiceFragment, OptionGroupWithItems> singleOptionItemForFragments = new HashMap<>();
    private Map<ChoiceFragment, OptionGroupWithItems> multiOptionItemsForFragment = new HashMap<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_item);
        EventBus.getDefault().register(this);
        Intent intent = getIntent();
        String menuItemJson = intent.getStringExtra(ORDER_MENU_ITEM_KEY);
        menuItem = new Gson().fromJson(menuItemJson, MenuItem.class);
        initToolbar(menuItem);
        initViews();
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Subscribe
    public void onSingleOptionCheckedEvent(SingleOptionCheckedEvent event) {
        ChoiceFragment choiceFragment = event.getChoiceFragment();
        OptionItem optionItem = event.getOptionItem();
        OptionGroupWithItems optionGroupWithItems = singleOptionItemForFragments.get(choiceFragment);
        ;
        optionGroupWithItems.getOptionItems().clear();
        optionGroupWithItems.getOptionItems().add(optionItem);
        setTotalItemPriceTexts();
    }

    @Subscribe
    public void onMultiOptionCheckedEvent(MultiOptionCheckedEvent event) {
        ChoiceFragment choiceFragment = event.getChoiceFragment();
        OptionItem optionItem = event.getOptionItem();
        OptionGroupWithItems optionGroupWithItems = multiOptionItemsForFragment.get(choiceFragment);
        if (event.isChecked()) {
            optionGroupWithItems.getOptionItems().add(optionItem);
        } else {
            optionGroupWithItems.getOptionItems().remove(optionItem);
        }
        setTotalItemPriceTexts();
    }

    private void initViews() {
        TextView itemNameTextView = findViewById(R.id.textview_menu_item_name);
        itemNameTextView.setText(menuItem.getName());
        TextView itemDescriptionTextView = findViewById(R.id.textview_menu_item_description);
        itemDescriptionTextView.setText(menuItem.getDesc());
        specialInstructionEditText = findViewById(R.id.edittext_special_instructions);
        numOfItemTextView = findViewById(R.id.textview_number_of_items);
        addToCartTextView = findViewById(R.id.textview_add_to_cart);
        totalPriceTextView = findViewById(R.id.textview_total_price);
        setTotalItemPriceTexts();
        setButtonClickListeners();
        createChoiceItemViews();
    }

    private void setButtonClickListeners() {
        ImageButton minusButton = findViewById(R.id.button_minus);
        ImageButton plusButton = findViewById(R.id.button_plus);
        minusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (numOfItems > 1) {
                    numOfItems--;
                    setTotalItemPriceTexts();
                }
            }
        });
        plusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                numOfItems++;
                setTotalItemPriceTexts();
            }
        });
        setAddToCartClickListener();
    }

    private void setAddToCartClickListener() {
        final Order order = CurrentOrder.getInstance().getOrder();
        RelativeLayout addToCartLayout = findViewById(R.id.rlayout_add_to_cart);
        addToCartLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (numOfItems == 0) {
                    return;
                }
                if (!requiredItemsSelected()) {
                    ForeOrderToastUtils.getInstance().displayToastFromMainThreadLong(Constants.MISSING_ITEMS);
                    return;
                }
                String specialInstructions = specialInstructionEditText.getText().toString();
                List<MenuItem> menuItems = new ArrayList<>();
                for (int itemsAdded = 0; itemsAdded < numOfItems; itemsAdded++) {
                    menuItems.add(menuItem);
                }
                List<OptionGroupWithItems> selectedOptions = createListOfSelectedItems();
                order.addMenuItems(menuItems, selectedOptions, specialInstructions);
                OrderItemActivity.super.onBackPressed();
            }
        });
    }

    private boolean requiredItemsSelected() {
        boolean requiredItemsSelected = requiredItemsSelectedForOptionType(singleOptionItemForFragments);
        if (requiredItemsSelected) {
            requiredItemsSelected = requiredItemsSelectedForOptionType(multiOptionItemsForFragment);
        }
        return requiredItemsSelected;
    }

    private boolean requiredItemsSelectedForOptionType(Map<ChoiceFragment, OptionGroupWithItems> optionItemForFragments) {
        boolean requiredItemsSelected = true;
        for (OptionGroupWithItems optionGroupWithItems : optionItemForFragments.values()) {
            if (optionGroupWithItems.getOptionGroup().getRequired()) {
                if (optionGroupWithItems.getOptionItems().size() == 0) {
                    requiredItemsSelected = false;
                    break;
                }
            }
        }
        return requiredItemsSelected;
    }

    private List<OptionGroupWithItems> createListOfSelectedItems() {
        List<OptionGroupWithItems> selectedOptionItems = new ArrayList<>();
        selectedOptionItems.addAll(createListOfSelectedItemsForOptionType(singleOptionItemForFragments));
        selectedOptionItems.addAll(createListOfSelectedItemsForOptionType(multiOptionItemsForFragment));
        return selectedOptionItems;
    }

    private List<OptionGroupWithItems> createListOfSelectedItemsForOptionType(Map<ChoiceFragment, OptionGroupWithItems> optionItemForFragments) {
        List<OptionGroupWithItems> selectedOptionItems = new ArrayList<>();
        for (OptionGroupWithItems optionGroupWithItems : optionItemForFragments.values()) {
            selectedOptionItems.add(optionGroupWithItems);
        }
        return selectedOptionItems;
    }

    private void setTotalItemPriceTexts() {
        ForeOrderResourceUtils resourceUtils = ForeOrderResourceUtils.getInstance();
        float optionItemPrice = calculateOptionItemPrice();
        float totalPrice = (menuItem.getPrice() + optionItemPrice) * numOfItems;
        numOfItemTextView.setText(String.format(Locale.ENGLISH, "%d", numOfItems));
        totalPriceTextView.setText(String.format(Locale.ENGLISH, "$%.2f", totalPrice));
        addToCartTextView.setText(String.format(Locale.ENGLISH, "%s %d %s", resourceUtils.strRes(R.string.add),
                numOfItems, resourceUtils.strRes(R.string.to_cart)));
    }

    private float calculateOptionItemPrice() {
        float costOfOptionItems = calculateOptionItemPriceForOptionType(singleOptionItemForFragments);
        costOfOptionItems = costOfOptionItems + calculateOptionItemPriceForOptionType(multiOptionItemsForFragment);
        return costOfOptionItems;
    }

    private float calculateOptionItemPriceForOptionType(Map<ChoiceFragment, OptionGroupWithItems> optionItemForFragments) {
        float costOfOptionItems = 0;
        for (OptionGroupWithItems optionGroupWithItems : optionItemForFragments.values()) {
            for (OptionItem optionItem : optionGroupWithItems.getOptionItems()) {
                costOfOptionItems = costOfOptionItems + optionItem.getPrice();
            }
        }
        return costOfOptionItems;
    }

    private void createChoiceItemViews() {
        List<OptionGroup> optionGroups = menuItem.getOptionGroups();
        for (OptionGroup optionGroup : optionGroups) {
            ChoiceFragment choiceFragment;
            if (optionGroup.getSingleChoice()) {
                choiceFragment = SingleChoiceFragment.newInstance(optionGroup);
                singleOptionItemForFragments.put(choiceFragment, new OptionGroupWithItems(optionGroup));
            } else {
                choiceFragment = MultiChoiceFragment.newInstance(optionGroup);
                multiOptionItemsForFragment.put(choiceFragment, new OptionGroupWithItems(optionGroup));
            }
            SupportFragmentAnimateManager.getInstance().addSupportFragment(getFragmentManager(), choiceFragment, R.id.llayout_choice_items_fragment_container);
        }
        adjustMarginOnEditText();
    }

    private void adjustMarginOnEditText() {
        if (menuItem.getOptionGroups().size() > 0) {
            int marginTop = SizeConverter.dpToPx(26, this);
            ViewUtils.addMargins(specialInstructionEditText, 0, marginTop, 0, 0);
        }
    }
}
