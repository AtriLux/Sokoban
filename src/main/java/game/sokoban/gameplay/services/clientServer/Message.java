package game.sokoban.gameplay.services.clientServer;

import java.nio.ByteBuffer;
import java.util.UUID;

public class Message {
    public int TYPE;
    private static final int SIZE = Integer.BYTES + Long.BYTES * 2;
    private final UUID id;


    public Message(UUID id, int type) {
        this.id = id;
        TYPE = type;
    }

    public Message(byte[] bytes) {
        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        this.id = new UUID(buffer.getLong(), buffer.getLong());
    }

    public byte[] getBytes() {
        ByteBuffer buffer = ByteBuffer.allocate(SIZE);

        buffer.putInt(TYPE);
        buffer.putLong(id.getMostSignificantBits());
        buffer.putLong(id.getLeastSignificantBits());

        return buffer.array();
    }

    public UUID getId() {
        return id;
    }
}
