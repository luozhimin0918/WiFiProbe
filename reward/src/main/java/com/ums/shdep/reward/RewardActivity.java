package com.ums.shdep.reward;

import android.app.Activity;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class RewardActivity extends Activity implements View.OnClickListener {
    private List<RewardAmountEntity> amountEntityList;
    private GridViewAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private EditText etAmount;
    private ImageView ivBack;
    private Button btnReward;
    private TextView tvDes;
    private int[] amounts;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        setContentView(R.layout.activity_reward);
        mRecyclerView = (RecyclerView) findViewById(R.id.reward_recyclerView);
        etAmount = (EditText) findViewById(R.id.reward_et_amount_custom);
        tvDes = (TextView) findViewById(R.id.reward_tv_des);
        ivBack = (ImageView) findViewById(R.id.reward_iv_back);
        btnReward = (Button) findViewById(R.id.reward_btn);
        ivBack.setOnClickListener(this);
        btnReward.setOnClickListener(this);
        amountEntityList = new ArrayList<>();
        amounts = getResources().getIntArray(R.array.reward_amount_array);
        for (int i = 0; i < amounts.length; i++) {
            amountEntityList.add(new RewardAmountEntity(amounts[i]));
        }
        String[] dess = getResources().getStringArray(R.array.reward_des_array);
        int index = new Random().nextInt(dess.length);
        String des = dess[index];
        tvDes.setText(des);
        mAdapter = new GridViewAdapter(amountEntityList);
        mRecyclerView.setAdapter(mAdapter);
        final GridLayoutManager layoutManager = new GridLayoutManager(this, 3);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.addItemDecoration(new SpaceItemDecoration());
        mAdapter.setOnSelectPositionChangeListener(new OnAmountSelectChangeListener() {
            @Override
            public void onAmountSelectChanged(int position) {
                etAmount.setText("");
                etAmount.clearFocus();
                String sAmount = mAdapter.getRewardAmountEntity(position).getShowAmount();
                pay(sAmount);

            }
        });
        etAmount.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    //在这个方法中处理你需要处理的事件
                    etAmount.clearFocus();
                    return true;
                }
                return false;
            }
        });
        etAmount.addTextChangedListener(new MyTextWatcher(5, 2, new MyTextWatcher.OnAfterTextChangeListener() {
            @Override
            public void afterTextChanged(Editable editable) {
                String mAmount = editable.toString();
                if(!TextUtils.isEmpty(mAmount)&&Double.valueOf(mAmount)>0){
                    btnReward.setBackgroundResource(R.drawable.btn_bg_round_normal);
                    btnReward.setClickable(true);
                }else{
                    btnReward.setBackgroundResource(R.drawable.btn_bg_round_disable);
                    btnReward.setClickable(false);
                }
            }
        }));

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.reward_iv_back) {
            finish();

        } else if (i == R.id.reward_btn) {
            String amount = etAmount.getText().toString();
            if (!TextUtils.isEmpty(amount) && Double.valueOf(amount) > 0) {
                pay(amount);
            }

        }
    }

    private void pay(String amount) {
        new RewardPay().pay(this, "客流分析打赏", floorInt(Double.valueOf(amount) * 100) + "", new RewardPay.OnPayResultListener() {
            @Override
            public void onPayResult(int resultCode) {
                if (resultCode == 0) {
                    new ThankBossDialog(RewardActivity.this, new ThankBossDialog.OnDialogCloseListener() {
                        @Override
                        public void onDialogClosed() {
                            setResult(-1);
                            RewardActivity.this.finish();
                        }
                    }).show();
                }
            }
        });
    }

    public static int floorInt(double number) {
        return (int) Math.floor(number);
    }

    class SpaceItemDecoration extends RecyclerView.ItemDecoration {
        int dividerWidth = 39;
        int spanCount = ((GridLayoutManager) mRecyclerView.getLayoutManager()).getSpanCount();
        int eachWidth = dividerWidth * (spanCount - 1) / spanCount;
        int dividerHeight = 25;


        /**
         * Retrieve any offsets for the given item. Each field of <code>outRect</code> specifies
         * the number of pixels that the item view should be inset by, similar to padding or margin.
         * The default implementation sets the bounds of outRect to 0 and returns.
         * <p>
         * <p>
         * If this ItemDecoration does not affect the positioning of item views, it should set
         * all four fields of <code>outRect</code> (left, top, right, bottom) to zero
         * before returning.
         * <p>
         * <p>
         * If you need to access Adapter for additional data, you can call
         * {@link RecyclerView#getChildAdapterPosition(View)} to get the adapter position of the
         * View.
         *
         * @param outRect Rect to receive the output.
         * @param view    The child view to decorate
         * @param parent  RecyclerView this ItemDecoration is decorating
         * @param state   The current state of RecyclerView.
         */
        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            int itemPosition = parent.getChildAdapterPosition(view);
            int left;
            int right;
            int bottom = 0;
            int top = 0;
            left = itemPosition % spanCount * 13;
            right = eachWidth - left;
            if (itemPosition / 3 < amountEntityList.size() / 3 - 1) {
                bottom = dividerHeight;
            }
            outRect.set(left, top, right, bottom);
        }

        public SpaceItemDecoration() {
        }
    }

    class RewardAmountEntity {
        private int amount;
        private String showAmount;

        public RewardAmountEntity(int amount) {
            this.amount = amount;
            this.showAmount = amount + "";
        }

        public int getAmount() {
            return amount;
        }

        public void setAmount(int amount) {
            this.amount = amount;
        }

        public String getShowAmount() {
            return showAmount;
        }

        public void setShowAmount(String showAmount) {
            this.showAmount = showAmount;
        }
    }

    class GridViewAdapter extends RecyclerView.Adapter {
        OnAmountSelectChangeListener listener;
        List<RewardAmountEntity> amountEntities;

        public GridViewAdapter(List<RewardAmountEntity> amountEntities) {
            this.amountEntities = amountEntities;
        }

        public void setOnSelectPositionChangeListener(OnAmountSelectChangeListener listener) {
            this.listener = listener;
        }


        public RewardAmountEntity getRewardAmountEntity(int selectPosition) {
            return amountEntities.get(selectPosition);
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.gridview_item_rewardamount, null);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
            ((MyViewHolder) holder).getTextView().setText(amountEntities.get(position).getShowAmount());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onAmountSelectChanged(position);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return amountEntities.size();
        }


    }

    interface OnAmountSelectChangeListener {
        void onAmountSelectChanged(int position);
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView textView;

        public MyViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.reward_item_amount);
        }

        public TextView getTextView() {
            return textView;
        }
    }
}
