package iching.android.activities;

import iching.android.R;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.Contacts;
import android.telephony.SmsManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;

public class DivinationSMS extends Activity
{

	private static final int CONTACT_PICKER_RESULT = 1;
	public static final String SMS_BODY = "sms_body";
	private EditText recipientNumber;
	private EditText smsBody;
	private List<String> phoneNumbers;
	private String divinations;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.divination_sms);
		divinations = (String) getIntent().getExtras().get(SMS_BODY);
		View browseButton = findViewById(R.id.browse_contacts);
		recipientNumber = (EditText) findViewById(R.id.sms_number);
		smsBody = (EditText) findViewById(R.id.sms_content);
		smsBody.setText(divinations);
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
		        StringBuilder errorMessage = new StringBuilder();
		        if(number.trim().length() == 0)
		        {
		        	errorMessage.append(getString(R.string.empty_number));
		        }
		        if(body.trim().length() == 0)
		        {
		        	errorMessage.append("\n" + getString(R.string.empty_sms));
		        }
		        if(errorMessage.length()!=0)
		        {
		        	Toast message = Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_SHORT);
		    		View messageView = message.getView();
		    		messageView.setBackgroundResource(R.drawable.button_pressed);
		    		message.show();
		        }
		        else
		        {
		        	ArrayList<String> messages = sms.divideMessage(body);
		        	sms.sendMultipartTextMessage(number, null, messages, null, null);
		        	finish();
		        }
			}
		});
	}
	
	private void launchContactPicker(View view)
	{
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
					getRecipientNumber(data);
					break;
				default:
					break;
			}
		}
	}

	private void getRecipientNumber(Intent data)
	{
		getContactMobileNumber(data);
		final String[] numbers = new String[phoneNumbers.size()];
		phoneNumbers.toArray(numbers);
		if(numbers.length == 1)
		{
			recipientNumber.setText(numbers[0]);
		}
		else
		{
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle(getString(R.string.select_number));
			builder.setSingleChoiceItems(numbers, -1, new DialogInterface.OnClickListener()
			{
				public void onClick(DialogInterface dialog, int item)
				{
					DivinationSMS.this.recipientNumber.setText(numbers[item]);
					dialog.cancel();
				}
			});
			builder.create().show();
		}
	}

	private void getContactMobileNumber(Intent data)
	{
		String contactId = data.getData().getLastPathSegment();
		Cursor phones = getContentResolver().query(Phone.CONTENT_URI, null, Phone.CONTACT_ID + " = " + contactId, null, null);
		phoneNumbers = new ArrayList<String>();
		while (phones.moveToNext())
		{
		    phoneNumbers.add(phones.getString(phones.getColumnIndex(Phone.NUMBER)));
		}
		phones.close();
	}
	
}
