package com.coopery.notes;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Calendar;

//TODO	Implement external storage access.

//TODO	Make the data reading/writing make more sense
//TODO 			(especially the new line stuff).

public class NoteActivity extends Activity {

//	Typeface customFont;

	EditText etTitle;
	EditText etNote;

	String oldTitle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_note);

		String fileName = getIntent().getStringExtra(MainActivity.EXTRA_TITLE);
		oldTitle = fileName;

		etTitle = (EditText) findViewById(R.id.etTitle);
		etNote = (EditText) findViewById(R.id.etNote);

		// For a custom font:
		//customFont = Typeface.createFromAsset(getAssets(), "fonts/ARCENA.ttf");
		//etTitle.setTypeface(customFont);
		//etNote.setTypeface(customFont);

		FileInputStream fis;
		final StringBuffer storedString = new StringBuffer();

		File file = new File(getFilesDir().getAbsolutePath() + '/' + fileName);

		// Try to read the file:
		if(file.exists()) {
			try {
				fis = openFileInput(fileName);
				InputStreamReader isr = new InputStreamReader(fis);
				BufferedReader br = new BufferedReader(isr);
				StringBuilder sb = new StringBuilder();
				String line;

				while((line = br.readLine()) != null) {
					sb.append(line);
					sb.append("\n");
				}

				etNote.setText(sb);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		etTitle.setText(fileName.substring(14));
		etTitle.setSelection(etTitle.getText().length());

		etNote.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				//save();
			}

			@Override
			public void afterTextChanged(Editable s) {
				save();
			}
		});
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_note, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.

		switch(item.getItemId()) {
			case R.id.action_settings:
				return true;
//			case R.id.action_save:		Remove Save button?
//				actionSave();
//				return true;
			case R.id.action_delete:
				actionDelete();
				return true;
		}

		return super.onOptionsItemSelected(item);
	}

	private void actionDelete() {
		if(delete()) {
			Toast.makeText(this, "Deleted successfully", Toast.LENGTH_SHORT).show();

			Intent intent = new Intent(this, MainActivity.class);
			startActivity(intent);
		}
		else {
			Toast.makeText(this, "Couldn't delete...", Toast.LENGTH_SHORT).show();
		}
	}

	private boolean delete() {
		File thisFile = new File(getFilesDir(), oldTitle);

		return thisFile.delete();
	}

//	private void actionSave() {
//		if(save()) {
//			Toast.makeText(this, "Saved!", Toast.LENGTH_SHORT).show();
//
//			Intent intent = new Intent(this, MainActivity.class);
//			startActivity(intent);
//		}
//		else {
//			Toast.makeText(NoteActivity.this, "Couldn't save...", Toast.LENGTH_SHORT).show();
//		}
//	}
//
	private boolean save() {
		String noteName = etTitle.getText().toString();

		// Delete the former file, if there is one
		String files[] = getFilesDir().list();
		for(String file : files) {
			// Might just need the second part of this v v v v v v?
			if(file.substring(14).equals(noteName) || file.equals(oldTitle)) {
				this.deleteFile(file);
			}
		}

		// Get the current time to save the file
		Calendar c = Calendar.getInstance();
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
		String formattedDate = df.format(c.getTime());

		FileOutputStream fos;
		String fileName = formattedDate + noteName;

		// Write the note to a file
		try {
			fos = openFileOutput(fileName, Context.MODE_PRIVATE);
			fos.write(etNote.getText().toString().getBytes());
			fos.close();

			return true;
		} catch (Exception e) {
			e.printStackTrace();

			return false;
		}
	}
}
