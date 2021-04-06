package com.fy.gamegateway.thirdpartner.yijie.sdk;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class HTTPHelper {
	public static class SimpleHTTPResult {
		public int code;
		public byte[] data;
	}
	
	public static SimpleHTTPResult simpleInvoke (String method, String url, String contentType, byte[] outdata) throws IOException {
		SimpleHTTPResult res = new SimpleHTTPResult ();
		HttpURLConnection http = (HttpURLConnection)(new URL (url)).openConnection ();
		http.setRequestMethod (method);
		if (contentType != null)
			http.setRequestProperty ("Content-Type", contentType);
		if (outdata != null) {
			http.setRequestProperty ("Content-Length", Integer.toString (outdata.length));
		}
		http.setDoOutput (outdata != null ? true : false);
		http.setDoInput (true);
		http.connect ();
		if (outdata != null) {
			OutputStream outs = http.getOutputStream ();
			outs.write (outdata);
			outs.close ();
		}
		res.code = http.getResponseCode ();
		if (res.code == 404) {
			return res;
		}
		InputStream stream = http.getInputStream ();
		try {
			int len = http.getContentLength ();
			byte[] data;
			if (len >= 0) {
				data = new byte[len];
				int off = 0;
				while (off < len) {
					int read = stream.read (data, off, len - off);
					if (read < 0)
						throw new IOException ();
					off += read;
				}
			} else {
				ByteArrayOutputStream baos = new ByteArrayOutputStream ();
				byte[] buffer = new byte[4096];
				for (;;) {
					int read = stream.read (buffer, 0, buffer.length);
					if (read < 0)
						break;
					baos.write (buffer, 0, read);
				}
				baos.close ();
				data = baos.toByteArray ();
			}
			res.data = data;
		} finally {
			stream.close ();
		}
		return res;
	}
	
	public static byte[] post (URL url, String contentType, byte[] outdata) throws IOException {
		HttpURLConnection http = (HttpURLConnection)url.openConnection ();
		http.setRequestProperty ("Content-Type", contentType);
		http.setDoOutput (true);
		http.setDoInput (true);
		http.connect ();
		OutputStream outs = http.getOutputStream ();
		outs.write (outdata);
		outs.close ();
		int code = http.getResponseCode ();
		if (code != 200) {
			throw new IOException ("Bad RPC '" + url.toString () + "': " + code);
		}
		InputStream stream = http.getInputStream ();
		try {
			int len = http.getContentLength ();
			byte[] data;
			if (len >= 0) {
				data = new byte[len];
				int off = 0;
				while (off < len) {
					int read = stream.read (data, off, len - off);
					if (read < 0)
						throw new IOException ();
					off += read;
				}
			} else {
				ByteArrayOutputStream baos = new ByteArrayOutputStream ();
				byte[] buffer = new byte[4096];
				for (;;) {
					int read = stream.read (buffer, 0, buffer.length);
					if (read < 0)
						break;
					baos.write (buffer, 0, read);
				}
				baos.close ();
				data = baos.toByteArray ();
			}
			return data;
		} finally {
			stream.close ();
		}
	}
	
}
