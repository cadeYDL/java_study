package design_pattern.strategy;

public enum Stage {
    Low("后"),
    Mid("中间"),
    High("前"),
    Unknown("unkown");

    private final String stage;
    Stage(String stage) {
        this.stage = stage;
    }

    @Override
    public String toString() {
        return stage;
    }
}
