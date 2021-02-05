package ru.geekbrains.cloud.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import ru.geekbrains.cloud.common.FileMessage;
import ru.geekbrains.cloud.common.FileRequest;

import java.nio.file.Files;
import java.nio.file.Paths;

public class MainHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        try {
            if (msg instanceof FileRequest) {
                System.out.println(3);
                FileRequest fr = (FileRequest) msg;
                System.out.println(fr.getFilename());
                if (Files.exists(Paths.get("ServerStorage/" + fr.getFilename()))) {
                    System.out.println(5);
                    FileMessage fm = new FileMessage(Paths.get("ServerStorage/" + fr.getFilename()));
                    System.out.println(6);
                    ctx.writeAndFlush(fm);
                    System.out.println(7);
                }
            }
        }finally {
            ReferenceCountUtil.release(msg);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
