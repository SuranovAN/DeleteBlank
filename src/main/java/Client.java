import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) throws IOException {
        InetSocketAddress socketAddress = new InetSocketAddress("127.0.0.1", 23334);
        final SocketChannel socketChannel = SocketChannel.open();

        socketChannel.connect(socketAddress);

        try (Scanner scanner = new Scanner(System.in)) {
            final ByteBuffer inputBuf = ByteBuffer.allocate(2 << 10);

            String msg;
            while (true) {
                System.out.println("Введите строку или end для завершения");
                msg = scanner.nextLine();
                if ("end".equals(msg)) {
                    break;
                }
                socketChannel.write(ByteBuffer.wrap(msg.getBytes(StandardCharsets.UTF_8)));

                int bytesCount = socketChannel.read(inputBuf);
                System.out.println("исправленая строка: " +
                        new String(inputBuf.array(), 0, bytesCount, StandardCharsets.UTF_8).trim());
                inputBuf.clear();
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } finally {
            socketChannel.close();
        }
    }
}
