import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

public class Server {
    public static void main(String[] args) throws IOException {
        final ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.bind(new InetSocketAddress("127.0.0.1", 23334));

        System.out.println("Server started!");
        while (true) {
            try (SocketChannel socketChannel = serverSocketChannel.accept()) {
                final ByteBuffer inputBuf = ByteBuffer.allocate(2 << 10);

                while (socketChannel.isConnected()) {
                    int byteCount = socketChannel.read(inputBuf);
                    if (byteCount == -1) {
                        break;
                    }

                    final String msg = new String(inputBuf.array(), 0, byteCount, StandardCharsets.UTF_8);
                    inputBuf.clear();
                    System.out.println("Сообение клиента: " + msg);
                    String fixedMsg = deleteBlank(msg);

                    socketChannel.write(ByteBuffer.wrap(("эхо: " + fixedMsg).getBytes(StandardCharsets.UTF_8)));
                }
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    static String deleteBlank(String msg) {
        return msg.trim().replace(" ", "");
    }
}
