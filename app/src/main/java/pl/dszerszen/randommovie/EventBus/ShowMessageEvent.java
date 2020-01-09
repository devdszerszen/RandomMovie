package pl.dszerszen.randommovie.EventBus;

public class ShowMessageEvent {

    public MessageType msgType;

    public ShowMessageEvent(MessageType type) {
        this.msgType = type;
    }

        public enum  MessageType {
            MOVIE_ADDED,
            MOVIE_REMOVED,
            FILTER_SAVED,
            FILTER_CLEARED
    }
}
