package cn.nicemorning.mymirror.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import cn.nicemorning.mymirror.R;

/**
 * Created by Nicemorning on 04-Mar-18.
 */

public class PhotoFrameActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    private TextView mBackToMain;
    private GridView mPhotoFrameList;
    private int[] photo_styles;
    private String[] photo_name;
    private Bitmap[] bitmaps;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_frame);
        initView();
        initDatas();
        mBackToMain.setOnClickListener(this);
        PhotoFrameAdapter adapter = new PhotoFrameAdapter();
        mPhotoFrameList.setAdapter(adapter);
        mPhotoFrameList.setOnItemClickListener(this);
    }

    private void initView() {
        mBackToMain = findViewById(R.id.back_to_main);
        mPhotoFrameList = findViewById(R.id.photo_frame_list);
    }

    private void initDatas() {
        photo_styles = new int[]{R.mipmap.mag_0001, R.mipmap.mag_0003, R.mipmap.mag_0005,
                R.mipmap.mag_0006, R.mipmap.mag_0007, R.mipmap.mag_0008, R.mipmap.mag_0009,
                R.mipmap.mag_0011, R.mipmap.mag_0012, R.mipmap.mag_0014};
        photo_name = new String[]{"Beautiful", "Special", "Wishes", "Forever", "Journey",
                "Love", "River", "Wonderful", "Birthday", "Nice"};
        bitmaps = new Bitmap[photo_styles.length];
        for (int i = 0; i < photo_styles.length; i++) {
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), photo_styles[i]);
            bitmaps[i] = bitmap;
        }
    }

    /**
     * Callback method to be invoked when an item in this AdapterView has
     * been clicked.
     * <p>
     * Implementers can call getItemAtPosition(position) if they need
     * to access the data associated with the selected item.
     *
     * @param parent   The AdapterView where the click happened.
     * @param view     The view within the AdapterView that was clicked (this
     *                 will be a view provided by the adapter)
     * @param position The position of the view in the adapter.
     * @param id       The row id of the item that was clicked.
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent();
        intent.putExtra("POSITION", position);
        setResult(RESULT_OK, intent);
        finish();
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_to_main:
                finish();
                break;
            default:
                break;
        }
    }

    class PhotoFrameAdapter extends BaseAdapter {

        /**
         * How many items are in the data set represented by this Adapter.
         *
         * @return Count of items.
         */
        @Override
        public int getCount() {
            return photo_name.length;
        }

        /**
         * Get the data item associated with the specified position in the data set.
         *
         * @param position Position of the item whose data we want within the adapter's
         *                 data set.
         * @return The data at the specified position.
         */
        @Override
        public Object getItem(int position) {
            return photo_name[position];
        }

        /**
         * Get the row id associated with the specified position in the list.
         *
         * @param position The position of the item within the adapter's data set whose row id we want.
         * @return The id of the item at the specified position.
         */
        @Override
        public long getItemId(int position) {
            return position;
        }

        /**
         * Get a View that displays the data at the specified position in the data set. You can either
         * create a View manually or inflate it from an XML layout file. When the View is inflated, the
         * parent View (GridView, ListView...) will apply default layout parameters unless you use
         * {@link LayoutInflater#inflate(int, ViewGroup, boolean)}
         * to specify a root view and to prevent attachment to the root.
         *
         * @param position    The position of the item within the adapter's data set of the item whose view
         *                    we want.
         * @param convertView The old view to reuse, if possible. Note: You should check that this view
         *                    is non-null and of an appropriate type before using. If it is not possible to convert
         *                    this view to display the correct data, this method can create a new view.
         *                    Heterogeneous lists can specify their number of view types, so that this View is
         *                    always of the right type (see {@link #getViewTypeCount()} and
         *                    {@link #getItemViewType(int)}).
         * @param parent      The parent that this view will eventually be attached to
         * @return A View corresponding to the data at the specified position.
         */
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = getLayoutInflater().inflate(R.layout.item_gridview, null);
                viewHolder.imageView = convertView.findViewById(R.id.item_pic);
                viewHolder.textView = convertView.findViewById(R.id.item_txt);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            setData(viewHolder, position);
            return convertView;
        }

        private void setData(ViewHolder holder, int position) {
            holder.imageView.setImageBitmap(bitmaps[position]);
            holder.textView.setText(photo_name[position]);
        }

        class ViewHolder {
            ImageView imageView;
            TextView textView;
        }

    }

}
