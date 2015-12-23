package com.coopery.notes;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;


public class MainActivity extends Activity {
	// insertion sort
	private void sort(String array[]) {
		for(int i = 1; i < array.length; ++i) {
			for(int j = i; j > 0 && array[j-1].compareTo(array[j]) < 0; --j) {
				String temp = array[j-1];
				array[j-1] = array[j];
				array[j] = temp;
			}
		}
	}

	public final static String EXTRA_TITLE = "com.coopery.notes.TITLE";

	ListView listOfNotes;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		listOfNotes = (ListView) findViewById(R.id.listOfNotes);

		final String notes[] = getFilesDir().list();

		sort(notes);

		String noteNames[] = new String[notes.length];
		for(int i=0; i<notes.length; ++i) {
			noteNames[i] = notes[i].substring(14);
		}

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.list_item, noteNames);
		listOfNotes.setAdapter(adapter);

		listOfNotes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent intent = new Intent(MainActivity.this, NoteActivity.class);
				//TextView t = (TextView) view.findViewById(android.R.id.text1);
				//Toast.makeText(MainActivity.this, notes[position], Toast.LENGTH_SHORT).show();
				intent.putExtra(EXTRA_TITLE, notes[position]);
				startActivity(intent);
			}
		});

		listOfNotes.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
	}

	@Override
	protected void onResume() {
		super.onResume();

		final String notes[] = getFilesDir().list();

		sort(notes);

		String noteNames[] = new String[notes.length];
		for(int i=0; i<notes.length; ++i) {
			noteNames[i] = notes[i].substring(14);
		}

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.list_item, noteNames);
		listOfNotes.setAdapter(adapter);

		listOfNotes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent intent = new Intent(MainActivity.this, NoteActivity.class);
				intent.putExtra(EXTRA_TITLE, notes[position]);
				startActivity(intent);
			}
		});
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.

		switch(item.getItemId()) {
			case R.id.action_settings:
				actionSettings();
				return true;
			case R.id.action_new:
				actionNew();
				return true;
			case R.id.action_search:
				actionSearch();
				return true;
		}

		return super.onOptionsItemSelected(item);
	}

	private void actionSettings() {

	}

	private void actionNew() {
		Intent intent = new Intent(this, NoteActivity.class);
		String title = "00000000000000";
		intent.putExtra(EXTRA_TITLE, title);
		startActivity(intent);
	}

	private void actionSearch() {

	}
}
