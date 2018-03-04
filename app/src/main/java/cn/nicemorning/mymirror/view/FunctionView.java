package cn.nicemorning.mymirror.view;

import android.content.Context;
import android.media.Image;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import cn.nicemorning.mymirror.R;

/**
 * Created by Nicemorning on 04-Mar-18.
 */

public class FunctionView extends LinearLayout implements View.OnClickListener {
    private LayoutInflater inflater;
    private OnFunctionViewItemClickListener listener;
    private static final int HINT_ID = R.id.hint;
    private static final int CHOOSE_ID = R.id.choose;
    private static final int DOWN_ID = R.id.light_down;
    private static final int UP_ID = R.id.light_up;
    private ImageView hint;
    private ImageView choose;
    private ImageView down;
    private ImageView up;

    /**
     * Callback interface
     */
    public interface OnFunctionViewItemClickListener {
        void hint();

        void choose();

        void down();

        void up();
    }

    public FunctionView(Context context) {
        this(context, null);
    }

    public FunctionView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FunctionView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inflater = LayoutInflater.from(context);
        init();
    }

    public void init() {
        View view = inflater.inflate(R.layout.view_function, this);
        hint = findViewById(HINT_ID);
        choose = findViewById(CHOOSE_ID);
        down = findViewById(DOWN_ID);
        up = findViewById(UP_ID);
        setView();
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        if (listener != null) {
            switch (v.getId()) {
                case HINT_ID:
                    listener.hint();
                    break;
                case CHOOSE_ID:
                    listener.choose();
                    break;
                case DOWN_ID:
                    listener.down();
                    break;
                case UP_ID:
                    listener.up();
                    break;
                default:
                    break;
            }
        }
    }

    private void setView() {
        hint.setOnClickListener(this);
        choose.setOnClickListener(this);
        down.setOnClickListener(this);
        up.setOnClickListener(this);
    }

    public void setOnFunctionViewItemClickListener(OnFunctionViewItemClickListener onFunctionViewItemClickListener) {
        this.listener = onFunctionViewItemClickListener;
    }

}
