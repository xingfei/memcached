/**
 * 
 */
package com.xuetangx.memcached.codec;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufProcessor;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.TooLongFrameException;
import io.netty.util.CharsetUtil;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import com.xuetangx.memcached.ChannelMemcacheResponse;
import com.xuetangx.memcached.codec.MemcacheRequests.GetRequest;
import com.xuetangx.memcached.codec.MemcacheRequests.QuitRequest;
import com.xuetangx.memcached.codec.MemcacheRequests.StorageRequest;
import com.xuetangx.memcached.exceptions.UnknownCommandException;

/**
 * @author xingfei
 *
 */
public class MemcacheRequestDecoder extends ByteToMessageDecoder {

	enum DecoderState {
		DECODE_COMMAND, DECODE_DATA
	}
	Pattern whitespace = Pattern.compile("\\s");

	private ByteBuf seq = Unpooled.buffer();
	private RequestLineParser lineParser = new RequestLineParser(seq, 2048);

	private DecoderState state = DecoderState.DECODE_COMMAND;
	private MemcacheRequest request = null;

	private int dataLength = -1;

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf buffer,
			List<Object> out) throws Exception {
		switch (state) {
		case DECODE_COMMAND:
			ByteBuf line = lineParser.parse(buffer);
			if (line == null) {
				break;
			}

			if (line.readableBytes() > 0) {
				String lineStr = line.toString(CharsetUtil.UTF_8);
				System.out.println("decoding line:" + lineStr);
				String[] t = whitespace.split(lineStr);
				try {
					DecoderState _state = decodeRequest(t);
					if (_state != null) {
						state = _state;
					} else {
						out.add(request);
						reset();
						break;
					}
				} catch (UnknownCommandException uce) {
					new ChannelMemcacheResponse(ctx).writeError();
					break;
				}
			}
			break;
		case DECODE_DATA:
			if (buffer.readableBytes() < dataLength) {
				break;
			}

			byte[] data = new byte[dataLength];
			buffer.readBytes(data);
			buffer.readBytes(2); // consume \r\n

			((StorageRequest) request).setData(data);
			out.add(request);
			reset();
			break;
		}
	}

	private void reset() {
		state = DecoderState.DECODE_COMMAND;
		lineParser.reset();
		request = null;
		dataLength = -1;
	}

	private DecoderState decodeRequest(String[] t) {
		DecoderState state = null;
		String command = t[0];
		switch (command) {
		case MemcacheRequest.GET:
		case MemcacheRequest.GETS:
			request = new GetRequest(Arrays.copyOfRange(t, 1, t.length));
			break;
		case MemcacheRequest.QUIT:
			request = new QuitRequest();
			break;
		default:
			throw new UnknownCommandException(command);
		}

		return state;
	}

	private static class RequestLineParser implements ByteBufProcessor {
		private final ByteBuf seq;
		private int size = 0;
		private int maxLength = 2048;

		RequestLineParser(ByteBuf seq, int maxLength) {
			this.seq = seq;
			this.maxLength = maxLength;
		}

		public ByteBuf parse(ByteBuf buffer) {
			final int oldSize = size;
			seq.clear();
			int i = buffer.forEachByte(this);
			if (i == -1) {
				size = oldSize;
				return null;
			}
			buffer.readerIndex(i + 1);
			return seq;
		}

		public void reset() {
			size = 0;
		}

		@Override
		public boolean process(byte value) throws Exception {
			if (value == '\r') {
				return true;
			}

			if (value == '\n') {
				return false;
			}

			if (++size > maxLength) {
				throw new TooLongFrameException("request line must less than "
						+ maxLength + " bytes");
			}

			seq.writeByte(value);
			return true;
		}

	}

}
