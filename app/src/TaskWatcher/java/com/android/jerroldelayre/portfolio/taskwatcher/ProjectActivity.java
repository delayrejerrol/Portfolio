package com.android.jerroldelayre.portfolio.taskwatcher;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
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
                projectAdapter.swapCursor(db.getProjectList());
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
            holder.mTextViewProjectName.setText(cursor.getString(cursor.getColumnIndex(Database.TBL_PROJECT.TITLE)));
            holder.mTextViewProjectDescription.setText(cursor.getString(cursor.getColumnIndex(Database.TBL_PROJECT.DESCRIPTION)));
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            @BindView(R.id.tv_project_name) TextView mTextViewProjectName;
            @BindView(R.id.tv_project_description) TextView mTextViewProjectDescription;

            ViewHolder (View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }
    }
}