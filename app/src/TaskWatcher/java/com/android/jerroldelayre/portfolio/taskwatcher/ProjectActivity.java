package com.android.jerroldelayre.portfolio.taskwatcher;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.jerroldelayre.portfolio.R;
import com.android.jerroldelayre.portfolio.adapter.RecyclerViewCursorAdapter;
import com.android.jerroldelayre.portfolio.taskwatcher.databases.Database;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ProjectActivity extends AppCompatActivity {

    @BindView(R.id.rv_project_container) RecyclerView rvProjectContainer;
    @BindView(R.id.layout_empty_container) LinearLayout mLayoutEmptyContainer;
    private ProjectAdapter projectAdapter;
    private Database db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        rvProjectContainer.setLayoutManager(new LinearLayoutManager(this));
        Database db = new Database(this);
        Cursor cursor = db.getProjectList();
        if(cursor.getCount() > 0) {
            rvProjectContainer.setVisibility(View.VISIBLE);
            mLayoutEmptyContainer.setVisibility(View.GONE);
        }
        projectAdapter = new ProjectAdapter(db.getProjectList());
        rvProjectContainer.setAdapter(projectAdapter);
        db.close();
    }

    @OnClick(R.id.fab_create_new_project)
    public void createNewProject(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View mDialogView = getLayoutInflater().inflate(R.layout.dialog_new_project, null);
        final EditText mEditTextProjectTitle = (EditText) mDialogView.findViewById(R.id.edittext_project_title);
        final EditText mEditTextProjectDescription = (EditText) mDialogView.findViewById(R.id.edittext_project_description);
        builder.setView(mDialogView)
        .setPositiveButton(android.R.string.ok, (dialog, which) -> {
            Database db = new Database(ProjectActivity.this);
            if(db.insertProject(mEditTextProjectTitle.getText().toString(), mEditTextProjectDescription.getText().toString()) > 0) {
                Toast.makeText(ProjectActivity.this, "New Project has been created", Toast.LENGTH_LONG).show();
                Cursor cursor = db.getProjectList();
                if(cursor.getCount() > 0) {
                    rvProjectContainer.setVisibility(View.VISIBLE);
                    mLayoutEmptyContainer.setVisibility(View.GONE);
                }
                projectAdapter.swapCursor(cursor);
            }
            db.close();
            dialog.dismiss();
        })
        .setNegativeButton(android.R.string.cancel, (dialog, which) -> {
            dialog.dismiss();
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public class ProjectAdapter extends RecyclerViewCursorAdapter<ProjectAdapter.ViewHolder> {

        public ProjectAdapter(Cursor cursor) {
            super(cursor);
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_project_item, parent, false);

            return new ViewHolder(view);
        }

        @Override
        protected void onBindViewHolder(ViewHolder holder, Cursor cursor) {
            holder.mCardToolbar.setTitle(cursor.getString(cursor.getColumnIndex(Database.TBL_PROJECT.TITLE)));
            holder.mProjectId = cursor.getString(cursor.getColumnIndex(Database._ID));
            holder.mTextViewProjectName.setText(cursor.getString(cursor.getColumnIndex(Database.TBL_PROJECT.TITLE)));
            holder.mTextViewProjectDescription.setText(cursor.getString(cursor.getColumnIndex(Database.TBL_PROJECT.DESCRIPTION)));
        }

        class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            String mProjectId;
            @BindView(R.id.tv_project_name) TextView mTextViewProjectName;
            @BindView(R.id.tv_project_description) TextView mTextViewProjectDescription;
            @BindView(R.id.card_toolbar) Toolbar mCardToolbar;

            ViewHolder (View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
                itemView.setOnClickListener(this);

                mCardToolbar.inflateMenu(R.menu.menu_main);
                mCardToolbar.setOnMenuItemClickListener(item -> {
                    int id = item.getItemId();

                    if(id == R.id.action_settings) {
                        Log.i("ProjectActivity", "menu clicked");
                    }

                    return true;
                });
            }

            @Override
            public void onClick(View v) {
                Log.i("ProjectActivity", "onClick");
            }
        }
    }
}