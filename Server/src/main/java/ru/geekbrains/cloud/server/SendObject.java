package ru.geekbrains.cloud.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import io.netty.util.ReferenceCountUtil;
import ru.geekbrains.cloud.common.FileMessage;
import ru.geekbrains.cloud.common.FileSend;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class SendObject extends ChannelOutboundHandlerAdapter{


    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
       try {
           if (msg instanceof FileSend) {
               FileMessage fm = (FileMessage) msg;
               Files.write(Paths.get("serverStorage/" + fm.getName()), fm.getData(), StandardOpenOption.CREATE);
                 }
       }finally {
           ReferenceCountUtil.release(msg);
       }
    }
}
