package com.ums.wifiprobe.ui.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.hardware.input.InputManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ums.wifiprobe.R;
import com.ums.wifiprobe.app.WPApplication;
import com.ums.wifiprobe.service.greendao.MacWhiteList;
import com.ums.wifiprobe.service.greendao.MacWhiteListDao;
import com.ums.wifiprobe.ui.customview.swipemenulistview.BaseSwipListAdapter;
import com.ums.wifiprobe.ui.customview.swipemenulistview.SwipeMenu;
import com.ums.wifiprobe.ui.customview.swipemenulistview.SwipeMenuCreator;
import com.ums.wifiprobe.ui.customview.swipemenulistview.SwipeMenuItem;
import com.ums.wifiprobe.ui.customview.swipemenulistview.SwipeMenuListView;
import com.ums.wifiprobe.utils.DialogUtil;
import com.ums.wifiprobe.utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Handler;

import butterknife.BindView;

/**
 * Created by chenzhy on 2017/11/10.
 */

public class WhiteListActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.head_back)
    ImageButton back;
    @BindView(R.id.detail_button)
    Button detailButton;
    @BindView(R.id.whitelist_listview)
    SwipeMenuListView listView;
    @BindView(R.id.whitelist_layout_add)
    LinearLayout layoutAdd;

    private List<MacWhiteList> list = new ArrayList<>();
    private CustomerAdapter adapter;

    @Override
    public void initView() {
        back.setOnClickListener(this);
        detailButton.setOnClickListener(this);
        layoutAdd.setOnClickListener(this);
        adapter = new CustomerAdapter();

        listView.setAdapter(adapter);
        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                // create "delete" item
                SwipeMenuItem deleteItem = new SwipeMenuItem(
                        getApplicationContext());
                // set item background
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
                        0x3F, 0x25)));
                // set item width
                deleteItem.setWidth(Utils.dp2px(WhiteListActivity.this, 90));
                // set a icon
                deleteItem.setIcon(R.mipmap.ic_delete);
                // add to menu
                menu.addMenuItem(deleteItem);
            }
        };
        listView.setMenuCreator(creator);
        listView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:
                        WPApplication.gMacWhiteListDao.delete(list.get(position));
                        list.remove(position);
                        adapter.notifyDataSetChanged();
                }
                return false;
            }
        });

    }

    @Override
    public void initData() {
        list = WPApplication.gMacWhiteListDao.queryBuilder().list();

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_whitelist;
    }

    @Override
    public void CacheClearComplete() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.head_back:
                onBackPressed();
                break;

            case R.id.whitelist_layout_add:
                showEditDialog();
                break;
            case R.id.detail_button:
                DialogUtil.showAlertDialogWhiteList(this, "白名单说明", "白名单里的MAC将不计入客流，建议将店员的手机、Pad等使用WiFi的设备MAC地址加入白名单。\n\n安卓设备查看MAC：设置——关于手机——状态——WLAN MAC地址\n\n苹果设备查看MAC：设置——通用——关于本机——无线局域网地址", null, null);
                break;

        }
    }

    class CustomerAdapter extends BaseSwipListAdapter {

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = View.inflate(getApplicationContext(),
                        R.layout.listview_item_whitelist, null);
                holder = new ViewHolder(convertView);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            final MacWhiteList entity = list.get(position);
            holder.tv_mac.setText(entity.getMac());

            return convertView;
        }

        class ViewHolder {
            TextView tv_mac;

            public ViewHolder(View view) {
                tv_mac = (TextView) view.findViewById(R.id.whitelist_item_tv_mac);
                view.setTag(this);
            }
        }
    }

    private void showEditDialog() {

        View view = View.inflate(this, R.layout.whitelist_dialog, null);
        final EditText et = (EditText) view.findViewById(R.id.edit_whitenumber);
        //        et.setHint("    00:1D:FA:2A:B5:36");
        final AlertDialog.Builder builder = new AlertDialog.Builder(this).setTitle("添加MAC白名单").setView(view);
        final AlertDialog dialog = builder.create();
        final TextView tvConfirm = (TextView) view.findViewById(R.id.confirm);
        final TextView tvCancel = (TextView) view.findViewById(R.id.cancel);
        tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String input = et.getText().toString();
                if (TextUtils.isEmpty(input)) {
                    Toast.makeText(getApplicationContext(), "Mac地址不能为空！", Toast.LENGTH_LONG).show();
                } else if (!stringIsMac(input)) {
                    Toast.makeText(getApplicationContext(), "Mac地址格式错误！", Toast.LENGTH_LONG).show();
                } else {
                    MacWhiteList macWhiteList = WPApplication.gMacWhiteListDao.queryBuilder().where(MacWhiteListDao.Properties.Mac.eq(input.toUpperCase())).unique();
                    if (macWhiteList == null) {
                        macWhiteList = new MacWhiteList();
                        macWhiteList.setMac(input.toUpperCase());
                        WPApplication.gMacWhiteListDao.insert(macWhiteList);
                        list.add(macWhiteList);
                        adapter.notifyDataSetChanged();
                        Toast.makeText(getApplicationContext(), "已保存", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "该Mac已在白名单中！", Toast.LENGTH_LONG).show();
                    }
                }
                dialog.dismiss();
            }
        });
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
//        new AlertDialog.Builder(this).setTitle("添加MAC白名单")
//                .setView(view)
//                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        String input = et.getText().toString();
//                        if (TextUtils.isEmpty(input)) {
//                            Toast.makeText(getApplicationContext(), "Mac地址不能为空！", Toast.LENGTH_LONG).show();
//                        } else if (!stringIsMac(input)) {
//                            Toast.makeText(getApplicationContext(), "Mac地址格式错误！", Toast.LENGTH_LONG).show();
//                        } else {
//                            MacWhiteList macWhiteList = WPApplication.gMacWhiteListDao.queryBuilder().where(MacWhiteListDao.Properties.Mac.eq(input.toUpperCase())).unique();
//                            if (macWhiteList == null) {
//                                macWhiteList = new MacWhiteList();
//                                macWhiteList.setMac(input.toUpperCase());
//                                WPApplication.gMacWhiteListDao.insert(macWhiteList);
//                                list.add(macWhiteList);
//                                adapter.notifyDataSetChanged();
//                                Toast.makeText(getApplicationContext(), "已保存", Toast.LENGTH_LONG).show();
//                            } else {
//                                Toast.makeText(getApplicationContext(), "该Mac已在白名单中！", Toast.LENGTH_LONG).show();
//                            }
//                        }
//                    }
//                })
//                .setNegativeButton("取消", null)
//                .show();
        new android.os.Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                showInputMethod();
            }
        }, 100);

    }

    private void showInputMethod() {
        //自动弹出键盘
        InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    private boolean stringIsMac(String val) {
        String trueMacAddress = "([A-Fa-f0-9]{2}:){5}[A-Fa-f0-9]{2}";
        // 这是真正的MAV地址；正则表达式；
        if (val.matches(trueMacAddress)) {
            return true;
        } else {
            return false;
        }
    }
}
