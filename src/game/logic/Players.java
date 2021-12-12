package game.logic;

public enum Players {
    WHITE("0", 1) {
        @Override
        public Players getOtherPlayer() {
            return BLACK;
        }
    },
    BLACK("X", -1) {
        @Override
        public Players getOtherPlayer() {
            return WHITE;
        }
    },
    NONE(" ", 0) {
        @Override
        public Players getOtherPlayer() {
            return null;
        }
    };

    private final String stringRepresentation;
    private final int value;

    Players(String stringRepresentation, int value) {
        this.stringRepresentation = stringRepresentation;
        this.value = value;
    }


    public int getValue() {
        return value;
    }

    public abstract Players getOtherPlayer();

    @Override
    public String toString() {
        return stringRepresentation;
    }
}
