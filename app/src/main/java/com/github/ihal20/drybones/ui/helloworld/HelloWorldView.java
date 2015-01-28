package com.github.ihal20.drybones.ui.helloworld;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.github.ihal20.drybones.R;
import javax.inject.Inject;
import mortar.dagger1support.Dagger1;

public class HelloWorldView extends RelativeLayout {

  @InjectView(R.id.hello_world_view_text) TextView textView;
  @Inject HelloWorldScreen.Presenter presenter;

  public HelloWorldView(Context context, AttributeSet attrs) {
    super(context, attrs);
    Dagger1.inject(context, this);
  }

  @Override
  protected void onFinishInflate() {
    super.onFinishInflate();
    ButterKnife.inject(this);
    presenter.takeView(this);
  }

  @Override
  protected void onDetachedFromWindow() {
    super.onDetachedFromWindow();
    presenter.dropView(this);
  }

  public void setText(String s) {
    textView.setText(s);
  }
}
