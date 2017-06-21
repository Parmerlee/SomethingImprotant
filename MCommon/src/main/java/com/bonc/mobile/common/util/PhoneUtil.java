package com.bonc.mobile.common.util;

import java.io.IOException;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

import android.content.ActivityNotFoundException;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.telephony.TelephonyManager;
import android.widget.Toast;

public class PhoneUtil {
	public static String getPhoneNum(Context context) {
		TelephonyManager t = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);

		try {
			return t.getLine1Number();
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	public static void call(Context context, String phone_number) {
		Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel",
				phone_number, null));
		context.startActivity(intent);
	}

	public static void sendSMS(Context context, String phone_number, String body) {
		Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("sms",
				phone_number, null));
		intent.putExtra("sms_body", body);
		context.startActivity(intent);
	}

	public static void sendSMS(Context context, String phone_number) {
		sendSMS(context, phone_number, null);
	}

	public static void sendMail(Context context, String to, Uri attachment) {
		Intent intent = new Intent(Intent.ACTION_SEND);
		if (attachment != null) {
			intent.putExtra(Intent.EXTRA_STREAM, attachment);
			intent.setType("image/png");
		}
		try {
			context.startActivity(Intent.createChooser(intent, null));
		} catch (ActivityNotFoundException e) {
			Toast.makeText(context, "程序未安装", Toast.LENGTH_LONG).show();
		}
	}

	public static void sendMail(final String user, final String password,
			String from, String to, String subject, String file)
			throws MessagingException, IOException {
		Properties props = new Properties();
		props.setProperty("mail.smtp.host", "smtp.139.com");
		props.setProperty("mail.smtp.user", user);
		props.setProperty("mail.smtp.auth", "true");
		Session s = Session.getInstance(props, new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(user, password);
			}
		});
		MimeMessage msg = new MimeMessage(s);
		msg.setSubject(subject);
		msg.setFrom(new InternetAddress(from));
		msg.setRecipients(Message.RecipientType.TO,
				InternetAddress.parse(to, false));
		Multipart mp = new MimeMultipart();
		MimeBodyPart textPart = new MimeBodyPart();
		textPart.setText("");
		mp.addBodyPart(textPart);
		MimeBodyPart mbp = new MimeBodyPart();
		FileDataSource fds = new FileDataSource(file);
		mbp.setDataHandler(new DataHandler(fds));
		mbp.setFileName(MimeUtility.encodeText(fds.getName()));
		mp.addBodyPart(mbp);
		msg.setContent(mp);
		Transport.send(msg);
	}

	public static String[] queryContacts(Context context, Uri uri) {
		String[] s = new String[2];
		long id = ContentUris.parseId(uri);
		Cursor c = context.getContentResolver().query(
				ContactsContract.Contacts.CONTENT_URI,
				new String[] { ContactsContract.Contacts.DISPLAY_NAME },
				ContactsContract.Contacts._ID + "=?", new String[] { "" + id },
				null);
		if (c.moveToFirst()) {
			s[0] = c.getString(0);
		}
		c.close();
		c = context
				.getContentResolver()
				.query(ContactsContract.Data.CONTENT_URI,
						new String[] { ContactsContract.CommonDataKinds.Phone.NUMBER },
						ContactsContract.Data.CONTACT_ID + "=? AND "
								+ ContactsContract.Data.MIMETYPE + "=?",
						new String[] {
								"" + id,
								ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE },
						null);
		if (c.moveToFirst()) {
			s[1] = c.getString(0);
		}
		c.close();
		return s;
	}
}
