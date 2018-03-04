package cn.nicemorning.mymirror.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.WindowManager;
import android.widget.SeekBar;
import android.widget.TextView;

import cn.nicemorning.mymirror.R;
import cn.nicemorning.mymirror.view.FunctionView;

public class HintActivity extends AppCompatActivity {

    private TextView mIKnow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hint);
        initView();
    }

    private void initView() {
        mIKnow = findViewById(R.id.i_know);
        mIKnow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

}
