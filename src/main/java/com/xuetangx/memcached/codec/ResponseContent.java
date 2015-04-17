/**
 * 
 */
package com.xuetangx.memcached.codec;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

/**
 * @author xingfei
 *
 */
public interface ResponseContent {
	byte[] CRLF = "\r\n".getBytes();
	byte[] toBytes();

	public class ValueContent implements ResponseContent {
		private String key;
		private byte[] data;
		private long flags;
		private long casUnique = -1L;

		public ValueContent(String key, byte[] data, long flags, long casUnique) {
			super();
			this.key = key;
			this.data = data;
			this.flags = flags;
			this.casUnique = casUnique;
		}

		public ValueContent(String key, byte[] data, long flags) {
			this(key, data, flags, -1L);
		}

		public ValueContent(String key, String data, long flags) {
			this(key, data.getBytes(), flags, -1L);
		}

		@Override
		public byte[] toBytes() {
			ByteArrayOutputStream out = new ByteArrayOutputStream();

			try (Writer w = new OutputStreamWriter(out);) {
				w.write("VALUE ");
				w.write(key);
				w.write(" ");
				w.write(Long.toString(flags));
				w.write(" ");
				w.write(Integer.toString(data.length));
				if (casUnique > 0L) {
					w.write(" ");
					w.write(Long.toString(casUnique));
				}
				
				w.flush();

				out.write(CRLF);
				out.write(data);
				out.write(CRLF);

			} catch (IOException ioE) {
				// never happen
				ioE.printStackTrace();
			}

			return out.toByteArray();
		}

	}

	public class SimpleContent implements ResponseContent {
		private byte[] content;

		public SimpleContent(String c) {
			this.content = (c + "\r\n").getBytes();
		}

		@Override
		public byte[] toBytes() {
			return content;
		}
	}
	
	public class EndContent extends SimpleContent {

		public EndContent() {
			super("END");
		}

	}

	public class ErrorContent extends SimpleContent {

		public ErrorContent() {
			super("ERROR");
		}

	}

	public class ServerErrorContent extends SimpleContent {

		public ServerErrorContent(String msg) {
			super("SERVER_ERROR " + msg);
		}

	}

	public class ClientErrorContent extends SimpleContent {

		public ClientErrorContent(String msg) {
			super("CLIENT_ERROR " + msg);
		}

	}
}
