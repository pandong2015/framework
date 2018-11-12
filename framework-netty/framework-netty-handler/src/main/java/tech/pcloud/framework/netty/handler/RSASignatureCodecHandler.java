package tech.pcloud.framework.netty.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;
import lombok.extern.slf4j.Slf4j;
import tech.pcloud.framework.exception.InvalidDataException;
import tech.pcloud.framework.security.RSASignatureUtil;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.List;

@Slf4j
public abstract class RSASignatureCodecHandler extends ByteToMessageCodec<ByteBuf> {

  @Override
  protected void encode(ChannelHandlerContext ctx, ByteBuf msg, ByteBuf out) throws Exception {
    byte[] data = new byte[msg.arrayOffset()];
    msg.readBytes(data);
    byte[] signData = RSASignatureUtil.signature(data, getPrivateKey());
    out.writeBytes(signData).writeBytes(data);
  }

  @Override
  protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
    byte[] signData = new byte[64];
    in.readBytes(signData);
    byte[] data = new byte[in.arrayOffset() - 64];
    in.readBytes(data);
    if (RSASignatureUtil.verify(data, signData, getPublicKey())) {
      out.add(Unpooled.copiedBuffer(data));
    } else {
      ctx.writeAndFlush(getErrorMessage(new InvalidDataException("verify data fail")))
              .addListeners(ChannelFutureListener.CLOSE);
    }
  }

  public abstract Object getErrorMessage(Throwable e);

  public abstract PublicKey getPublicKey();

  public abstract PrivateKey getPrivateKey();
}
