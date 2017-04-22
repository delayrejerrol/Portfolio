package com.android.jerroldelayre.portfolio.taskwatcher;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.jerroldelayre.portfolio.R;
import com.android.jerroldelayre.portfolio.adapter.RecyclerViewCursorAdapter;
import com.android.jerroldelayre.portfolio.taskwatcher.databases.Database;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TaskActivity extends AppCompatActivity {

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    @BindView(R.id.container) RecyclerView rvTaskContainer;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.edittext_task_list_name) EditText mEditTextTaskListName;

    String projectId;

    TaskListAdapter taskListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        rvTaskContainer.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        Bundle bundle = getIntent().getExtras();
        projectId = bundle.getString(Database._ID);

        Database database = new Database(this);
        taskListAdapter = new TaskListAdapter(database.getTaskList());
        rvTaskContainer.setAdapter(taskListAdapter);
        database.close();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_task, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_new_task) {

        }
        else if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.btn_save_task_list)
    public void saveTaskList(View view) {
        Database db = new Database(this);
        if(db.insertTaskList(projectId, mEditTextTaskListName.getText().toString()) > 0 ) {
            taskListAdapter.swapCursor(db.getTaskList());
        }
        db.close();
    }

    class TaskListAdapter extends RecyclerViewCursorAdapter<TaskListAdapter.ViewHolder> {

        TaskListAdapter(Cursor cursor) {
            super(cursor);
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_task_list, parent, false);

            return new ViewHolder(view);
        }

        @Override
        protected void onBindViewHolder(ViewHolder holder, Cursor cursor) {
            //holder.mToolbar.setTitle(cursor.getString(cursor.getColumnIndex(Database.TBL_TASK_LIST.NAME)));
        }

        class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            @BindView(R.id.tv_task_name) TextView mTextViewTaskName;
            @BindView(R.id.iv_show_more)
            ImageView mImageViewShowMore;

            ViewHolder (View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
                itemView.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {
                Log.i("TaskListAdapter", "onClick");
            }
        }
    }
}
