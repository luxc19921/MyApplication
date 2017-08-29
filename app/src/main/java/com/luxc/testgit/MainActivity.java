package com.luxc.testgit;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.plv)
    ListView plv;
    @BindView(R.id.header_title)
    TextView headerTitle;
    @BindView(R.id.rl_title)
    RelativeLayout rlTitle;

    private ImageView mIvHeader;
//    private RelativeLayout rlTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        View headerView = View.inflate(getApplicationContext(), R.layout.list_header, null);
        mIvHeader = (ImageView) headerView.findViewById(R.id.header_iv);
        rlTitle = (RelativeLayout) headerView.findViewById(R.id.rl_title);
        // 给ListView加头, 要放在setAdapter之前
        plv.addHeaderView(headerView);


         /*
        * 4.4以后时间栏状态栏透明
        * */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }

        final MyAdapter myAdapter = new MyAdapter();
        plv.setAdapter(myAdapter);

        /*
        * 添加一个滚动 监听
        * 用来实现标题置顶效果
        * */
        plv.setOnScrollListener(new ListView.OnScrollListener() {

            //滑动状态监听
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState) {
                    case AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL://拖动
                    case AbsListView.OnScrollListener.SCROLL_STATE_FLING://惯性滑动
                        // 显示滑动时屏幕可见条目中离标题栏最近的第一行
                        int position = plv.getFirstVisiblePosition();
                        //由于listview添加了一个header，所以position=0代表的是header
                        //这里获取到的position和下面onscroll方法里的firstVisibleItem值一致
                        break;
                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE://手指离开或者惯性滑动停止
                        break;
                }

            }

            /*
            * firstVisibleItem  可见的条目中第一个条目
            * visibleItemCount 一屏幕中可见条目
            * totalItemCount   总条目
            * */
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                Log.e("qcl", "firstVisibleItem   " + firstVisibleItem);
                if (firstVisibleItem > 0) {
                    rlTitle.setVisibility(View.VISIBLE);
                    rlTitle.setBackgroundColor(Color.WHITE);
                    headerTitle.setVisibility(View.VISIBLE);
                    if (firstVisibleItem < 5) {
                        headerTitle.setText(Cheeses.TITLES[0]);
                    } else if (firstVisibleItem >= 5 && firstVisibleItem < 9) {
                        headerTitle.setText(Cheeses.TITLES[1]);
                    } else {
                        headerTitle.setText(Cheeses.TITLES[2]);
                    }
                } else {
                    rlTitle.setVisibility(View.INVISIBLE);
                    rlTitle.setBackgroundColor(getResources().getColor(R.color.transparent));
                    headerTitle.setVisibility(View.INVISIBLE);

                }
            }
        });
    }

    class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return Cheeses.NAMES.length + Cheeses.TITLES.length;
        }


        @Override
        public long getItemId(int position) {
            return 0;
        }


        @Override
        public int getItemViewType(int position) {
            if (position == 0 || position == 4 || position == 8) {
                return 0;
            } else {
                return 1;
            }
        }

        @Override
        public int getViewTypeCount() {
            return 2;
        }

        @Override
        public String getItem(int position) {
            String info = "";
            if (position == 0 || position == 4 || position == 8) {
                info = Cheeses.TITLES[position / 4];
            } else if (position < 4) {
                info = Cheeses.NAMES[position - 1];
            } else if (position > 4 && position < 8) {
                info = Cheeses.NAMES[position - 2];
            } else {
                info = Cheeses.NAMES[position - 3];
            }
            return info;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            int type = getItemViewType(position);
            View view = null;
            if (type == 0) {// 标题
                ViewHolder holder = null;
                if (convertView == null) {
                    holder = new ViewHolder();
                    view = View.inflate(getBaseContext(), R.layout.view_item_app_type, null);
                    holder.titile = (TextView) view.findViewById(R.id.type);
                    view.setTag(holder);
                } else {
                    view = convertView;
                    holder = (ViewHolder) view.getTag();
                }
                holder.titile.setText(getItem(position));
                return view;
            } else {
                ViewHolder holder = null;
                // 视图优化 start---
                if (convertView == null) {
                    holder = new ViewHolder();
                    view = View.inflate(getBaseContext(), R.layout.view_item_app_info, null);
                    holder.name = (TextView) view.findViewById(R.id.app_name);
                    view.setTag(holder);
                } else {
                    view = convertView;
                    holder = (ViewHolder) view.getTag();
                }
                holder.name.setText(getItem(position));
                return view;

            }
        }
    }

    class ViewHolder {
        TextView titile;
        TextView name;
    }
}
