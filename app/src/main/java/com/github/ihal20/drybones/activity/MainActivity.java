package com.github.ihal20.drybones.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import com.github.ihal20.drybones.AppContainer;
import com.github.ihal20.drybones.DryBonesApp;
import com.github.ihal20.drybones.R;
import com.github.ihal20.drybones.android.AppBarOwner;
import com.github.ihal20.drybones.flowmortar.FlowBundler;
import com.github.ihal20.drybones.flowmortar.HandlesBack;
import com.github.ihal20.drybones.flowmortar.HandlesUp;
import dagger.ObjectGraph;
import flow.Flow;
import flow.PathContainerView;
import javax.inject.Inject;
import mortar.Mortar;
import mortar.MortarActivityScope;
import mortar.MortarScope;
import mortar.dagger1support.Dagger1;

import static android.content.Intent.ACTION_MAIN;
import static android.content.Intent.CATEGORY_LAUNCHER;
import static android.view.MenuItem.SHOW_AS_ACTION_ALWAYS;

public class MainActivity extends ActionBarActivity implements AppBarOwner.Activity, Flow.Dispatcher {

  @Inject AppBarOwner appBarOwner;
  @Inject AppContainer appContainer;

  private MortarActivityScope activityScope;
  private AppBarOwner.MenuAction appBarMenuAction;

  private PathContainerView container;
  private HandlesBack containerAsHandlesBack;
  private HandlesUp containerAsHandlesUp;
  private Flow flow;
  private Toolbar toolBar;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (isWrongInstance()) {
      finish();
      return;
    }
    MortarScope parentScope = Mortar.getScope(getApplication());
    String scopeName = getLocalClassName() + "-task-" + getTaskId();
    activityScope = (MortarActivityScope) parentScope.findChild(scopeName);
    if (activityScope == null) {
      ObjectGraph parentGraph = parentScope.getObjectGraph();
      MainModule activityModule = new MainModule();
      ObjectGraph activityGraph = Dagger1.createSubgraph(parentGraph, activityModule);
      activityScope = Mortar.createActivityScope(parentScope, scopeName, activityGraph);
    }
    Dagger1.inject(this, this);
    activityScope.onCreate(savedInstanceState);
    appBarOwner.takeView(this);
    flow = getFlowBundler().onCreate(savedInstanceState);
    ViewGroup root = appContainer.get(this);
    getLayoutInflater().inflate(R.layout.container_view, root);
    toolBar = (Toolbar) findViewById(R.id.app_bar_view);
    setSupportActionBar(toolBar);
    container = (PathContainerView) findViewById(R.id.container);
    containerAsHandlesBack = (HandlesBack) container;
    containerAsHandlesUp = (HandlesUp) container;
  }

  @Override
  protected void onResume() {
    super.onResume();
    flow.setDispatcher(this);
  }

  @Override protected void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    getFlowBundler().onSaveInstanceState(outState);
    activityScope.onSaveInstanceState(outState);
  }

  @Override
  protected void onPause() {
    super.onPause();
    flow.removeDispatcher(this);
  }

  @Override
  protected void onDestroy() {
    if (appBarOwner != null) {
      appBarOwner.dropView(this);
    }
    //Note activityScope may be null in case isWrongInstance() returned true in onCreate()
    if (isFinishing() && activityScope != null) {
      MortarScope parentScope = Mortar.getScope(getApplication());
      parentScope.destroyChild(activityScope);
      activityScope = null;
    }
    super.onDestroy();
  }

  /** Inform the view about back events. */
  @Override public void onBackPressed() {
    if (!containerAsHandlesBack.onBackPressed()) {
      super.onBackPressed();
    }
  }

  @Override public boolean onCreateOptionsMenu(Menu menu) {
    if (appBarMenuAction != null) {
      menu.add(appBarMenuAction.title).setShowAsActionFlags(SHOW_AS_ACTION_ALWAYS).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
        @Override public boolean onMenuItemClick(MenuItem menuItem) {
          appBarMenuAction.action.call();
          return true;
        }
      });
    }
    return true;
  }

  /** Inform the view about up events. */
  @Override public boolean onOptionsItemSelected(MenuItem item) {
    if (item.getItemId() == android.R.id.home) {
      return containerAsHandlesUp.onUpPressed();
    }
    return super.onOptionsItemSelected(item);
  }

  @Override public Object getSystemService(String name) {
    if (Flow.isFlowSystemService(name)) return flow;
    if (Mortar.isScopeSystemService(name)) return activityScope;
    return super.getSystemService(name);
  }

  @Override
  public void setTitle(CharSequence title) {
    if (getSupportActionBar() == null) {
      return;
    }
    getSupportActionBar().setTitle(title);
  }

  /**
   * Dev tools and the play store (and others?) launch with a different intent, and so lead to a redundant instance of this activity being
   * spawned. <a href="http://stackoverflow.com/questions/17702202/find-out-whether-the-current-activity-will-be-task-root-eventually-after-pendin"
   * >Details</a>.
   */
  private boolean isWrongInstance() {
    if (!isTaskRoot()) {
      Intent intent = getIntent();
      boolean isMainAction = intent.getAction() != null && intent.getAction().equals(ACTION_MAIN);
      return intent.hasCategory(CATEGORY_LAUNCHER) && isMainAction;
    }
    return false;
  }

  private FlowBundler getFlowBundler() {
    return ((DryBonesApp) getApplication()).getFlowBundler();
  }

  @Override
  public void setVisibility(boolean visible) {
    if (toolBar == null) {
      return;
    }
    if (visible) {
      toolBar.setVisibility(View.VISIBLE);
    } else {
      toolBar.setVisibility(View.GONE);
    }
  }

  @Override public void setMenu(AppBarOwner.MenuAction action) {
    if (action != appBarMenuAction) {
      appBarMenuAction = action;
      invalidateOptionsMenu();
    }
  }

  @Override public MortarScope getScope() {
    return activityScope;
  }

  @Override public void dispatch(Flow.Traversal traversal, Flow.TraversalCallback callback) {
    container.dispatch(traversal, callback);
  }

  @Override public void setShowHomeEnabled(boolean enabled) {
    ActionBar actionBar = getSupportActionBar();
    if (actionBar == null) {
      return;
    }
    actionBar.setDisplayShowHomeEnabled(false);
  }

  @Override public void setUpButtonEnabled(boolean enabled) {
    ActionBar actionBar = getSupportActionBar();
    if (actionBar == null) {
      return;
    }
    actionBar.setDisplayHomeAsUpEnabled(enabled);
    actionBar.setHomeButtonEnabled(enabled);
  }

  @Nullable @Override @Deprecated
  public android.app.ActionBar getActionBar() {
    throw new RuntimeException("You should use getSupportActionBar()");
  }
}

