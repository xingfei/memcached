/**
 * 
 */
package com.xuetangx.memcached.codec;

import com.xuetangx.memcached.servlet.ServletContext;

/**
 * @author xingfei
 *
 */
public class MemcacheRequests {
	public static class SimpleRequest implements MemcacheRequest {
		protected String command;
		private ServletContext context;

		@Override
		public String getCommand() {
			return command;
		}

		public ServletContext getServletContext() {
			return context;
		}

		public void setServletContext(ServletContext context) {
			this.context = context;
		}

	}

	public static abstract class StorageRequest extends SimpleRequest {
		// <key> <flags> <exptime> <bytes> [noreply]
		private String key;
		private long flags, exptime;
		private int bytes;
		private byte[] data;
		private Boolean noreply;
		
		public boolean needData() {
			return true;
		}

		public String getKey() {
			return key;
		}

		public void setKey(String key) {
			this.key = key;
		}

		public long getFlags() {
			return flags;
		}

		public void setFlags(long flags) {
			this.flags = flags;
		}

		public long getExptime() {
			return exptime;
		}

		public void setExptime(long exptime) {
			this.exptime = exptime;
		}

		public int getBytes() {
			return bytes;
		}

		public void setBytes(int bytes) {
			this.bytes = bytes;
		}

		public byte[] getData() {
			return data;
		}

		public void setData(byte[] data) {
			this.data = data;
		}

		public Boolean getNoreply() {
			return noreply;
		}

		public void setNoreply(Boolean noreply) {
			this.noreply = noreply;
		}

	}

	public static class AddRequest extends StorageRequest {
		public AddRequest() {
			command = ADD;
		}
	}

	public static class SetRequest extends StorageRequest {
		public SetRequest() {
			command = SET;
		}
	}

	public static class AppendRequest extends StorageRequest {
		public AppendRequest() {
			command = APPEND;
		}
	}

	public static class PrependRequest extends StorageRequest {
		public PrependRequest() {
			command = PREPEND;
		}
	}

	public static class ReplaceRequest extends StorageRequest {
		public ReplaceRequest() {
			command = REPLACE;
		}
	}

	public static class CasRequest extends StorageRequest {
		private long casUnique;

		public CasRequest() {
			command = CAS;
		}

		public long getCasUnique() {
			return casUnique;
		}

		public void setCasUnique(long casUnique) {
			this.casUnique = casUnique;
		}
	}

	public static class QuitRequest extends SimpleRequest {
		public QuitRequest() {
			command = QUIT;
		}
	}

	public static class StatsRequest extends SimpleRequest {
		public StatsRequest() {
			command = STATS;
		}
	}

	public static class GetRequest extends SimpleRequest {

		private String[] keys;

		public GetRequest(String[] keys) {
			super();
			this.keys = keys;
			this.command = GET;
		}

		public String[] getKeys() {
			return keys;
		}

	}
}
