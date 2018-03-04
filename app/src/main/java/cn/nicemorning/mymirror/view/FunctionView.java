package cn.nicemorning.mymirror.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.view.View;

import cn.nicemorning.mymirror.R;

/**
 * Created by Nicemorning on 04-Mar-18.
 */

public class FunctionView extends LinearLayout implements View.OnClickListener {
    private LayoutInflater inflater;

    private OnFunctionViewItemClickListener listener;

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
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {

    }
}
