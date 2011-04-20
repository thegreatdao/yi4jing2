package iching.android.activities;

import iching.android.R;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.Contacts;
import android.telephony.SmsManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

public class DivinationSMS extends Activity
{

	private static final int CONTACT_PICKER_RESULT = 1;
	private EditText recipientNumber;
	private EditText smsBody;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.divination_sms);
		View browseButton = findViewById(R.id.browse_contacts);
		recipientNumber = (EditText) findViewById(R.id.sms_number);
		smsBody = (EditText) findViewById(R.id.sms_content);
		browseButton.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				launchContactPicker(view);
			}
		});
		View sendSms = findViewById(R.id.send_sms);
		sendSms.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
		        SmsManager sms = SmsManager.getDefault();
		        String number = recipientNumber.getText().toString();
		        String body = smsBody.getText().toString();
		        if(number.trim().length() == 0)
		        {
		        	
		        }
		        else if(body.trim().length() == 0)
		        {
		        	
		        }
		        else
		        {
		        	sms.sendTextMessage(number, null, body, null, null);
		        	finish();
		        }
			}
		});
	}
	
	private void launchContactPicker(View view) {
	    Intent contactPickerIntent = new Intent(Intent.ACTION_PICK, Contacts.CONTENT_URI);
	    startActivityForResult(contactPickerIntent, CONTACT_PICKER_RESULT);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if(resultCode == RESULT_OK)
		{
			switch (requestCode)
			{
				case CONTACT_PICKER_RESULT:
					getContactMobileNumber(data);
					break;
				default:
					break;
			}
		}
	}

	private void getContactMobileNumber(Intent data)
	{
		String contactId = data.getData().getLastPathSegment();
		Cursor phones = getContentResolver().query(Phone.CONTENT_URI, null, Phone.CONTACT_ID + " = " + contactId, null, null);
		String number = null;
		while (phones.moveToNext()) {
		    number = phones.getString(phones.getColumnIndex(Phone.NUMBER));
		    int type = phones.getInt(phones.getColumnIndex(Phone.TYPE));
		    switch (type) {
		        case Phone.TYPE_HOME:
		            break;
		        case Phone.TYPE_MOBILE:
		        	recipientNumber.setText(number);
		            break;
		        case Phone.TYPE_WORK:
		            break;
		        }
		}
		phones.close();
	}
	
}
