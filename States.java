interface State {
    State MouseLeft();
    State MouseRight();
}

class Idle implements State {
    public State MouseLeft() {
        return new Idle();
    }

    public State MouseRight() {
        return new Idle();
    }
}

class FirstPoint implements State {
    public State MouseLeft() {
        return new SecondPoint();
    }

    public State MouseRight() {
        return new Idle();
    }
}

class SecondPoint implements State {
    public State MouseLeft() {
        return new FirstPoint();
    }

    public State MouseRight() {
        return new Idle();
    }
}

class TextState implements State {
    public State MouseLeft() {
        return new Idle();
    }

    public State MouseRight() {
        return new Idle();
    }
}

class BrokenLineState implements State {
    public State MouseLeft() {
        return new Idle();
    }

    public State MouseRight() {
        return new Idle();
    }
}

class PolygonState implements State {
    public State MouseLeft() {
        return new Idle();
    }

    public State MouseRight() {
        return new Idle();
    }
}