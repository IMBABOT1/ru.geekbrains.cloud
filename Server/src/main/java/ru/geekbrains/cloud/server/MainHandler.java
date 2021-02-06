package ru.geekbrains.cloud.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import ru.geekbrains.cloud.common.FileMessage;
import ru.geekbrains.cloud.common.FileRequest;
import ru.geekbrains.cloud.common.FileSend;
import ru.geekbrains.cloud.common.GetServerListFiles;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class MainHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        try {
            System.out.println(msg.toString());
            if (msg instanceof FileRequest) {
                FileRequest fr = (FileRequest) msg;
                if (Files.exists(Paths.get("ServerStorage/" + fr.getFilename()))) {
                    FileMessage fm = new FileMessage(Paths.get("ServerStorage/" + fr.getFilename()));
                    ctx.writeAndFlush(fm);
                }
            }
            if (msg instanceof FileSend) {
                FileSend fs = (FileSend) msg;
                Files.write(Paths.get("serverStorage/" + fs.getName()), fs.getData(), StandardOpenOption.CREATE);
            }
            if (msg instanceof GetServerListFiles){
                GetServerListFiles clientsFiles = (GetServerListFiles) msg;
                Files.list(Paths.get("serverStorage/")).map(path -> path.getFileName().toString()).forEach(o -> clientsFiles.setList(o));
                ctx.writeAndFlush(clientsFiles);
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