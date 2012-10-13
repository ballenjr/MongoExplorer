package com.innodroid.mongobrowser;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;

public class DocumentListActivity extends FragmentActivity implements DocumentListFragment.Callbacks {
	private static final int REQUEST_EDIT_DOCUMENT = 101;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String name = getIntent().getStringExtra(Constants.ARG_COLLECTION_NAME);
        setTitle(name);
        setContentView(R.layout.activity_document_list);
        
        if (savedInstanceState == null) {
        	Bundle args = getIntent().getExtras();
	        DocumentListFragment fragment = new DocumentListFragment();
	        args.putBoolean(Constants.ARG_ACTIVATE_ON_CLICK, false);
	        fragment.setArguments(args);
	        getSupportFragmentManager().beginTransaction().add(R.id.document_list, fragment).commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch (item.getItemId()) {
    		case android.R.id.home:
    			finish();
                return true;
    		default:
    	        return super.onOptionsItemSelected(item);
    	}
    }
    
    @Override
	public void onAddDocument() {
		Intent intent = new Intent(this, DocumentEditActivity.class);
		intent.putExtra(Constants.ARG_POSITION, -1);
		intent.putExtra(Constants.ARG_DOCUMENT_CONTENT, Constants.NEW_DOCUMENT_CONTENT);
		intent.putExtra(Constants.ARG_COLLECTION_NAME, getIntent().getExtras().getString(Constants.ARG_COLLECTION_NAME));
		startActivityForResult(intent, REQUEST_EDIT_DOCUMENT);		
	}

	@Override
	protected void onActivityResult(int request, int result, Intent data) {
		switch (request) {
			case REQUEST_EDIT_DOCUMENT:
				if (result == RESULT_OK) {
					DocumentListFragment fragment = (DocumentListFragment)getSupportFragmentManager().findFragmentById(R.id.document_list);
					fragment.onDocumentSaved(data.getIntExtra(Constants.ARG_POSITION, -1), data.getStringExtra(Constants.ARG_DOCUMENT_CONTENT));
				}
				break;
		}
		
		super.onActivityResult(request, result, data);
	}
	
	@Override
    public void onDocumentItemSelected(int position, String content) {
		Intent intent = new Intent(this, DocumentDetailActivity.class);
		intent.putExtra(Constants.ARG_POSITION, position);
		intent.putExtra(Constants.ARG_DOCUMENT_CONTENT, content);
		startActivity(intent);
    }

	@Override
	public void onCollectionEdited(String name) {
		setTitle(name);
	}

	@Override
	public void onCollectionDropped(String name) {
		finish();
	}
}
