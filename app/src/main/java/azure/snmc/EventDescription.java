package azure.snmc;

/**
 * Created by acer on 2018-01-27.
 */

public class EventDescription {

    private String title;
    private String description;
    private String link;
    private String content;
    private String category;
    private Long event_begin;
    private Long event_end;
    private String rrule;
    private boolean isEvent;

    public EventDescription (boolean _isEvent, String _title, String _description, String _link, String _content, String _category){
        this(_isEvent, _title, _description, _link, _content);
        category = _category;
    }

    public EventDescription (boolean _isEvent, String _title, String _description, String _link, String _content) {
        isEvent = _isEvent;
        title = _title;
        description = _description;
        link = _link;
        content = _content;
    }

    public EventDescription(boolean _isEvent, String _title,  String _description, String _link, String _content, String _category, Long _event_begin, Long _event_end, String _rrule) {
        this(_isEvent, _title,_description, _link,_content,_category);
        event_begin=_event_begin;
        event_end =_event_end;
        rrule=_rrule;

    }


    public String GetTitle() {
        return title;
    }
    public String GetDescription() {
        return description;
    }

    public String GetLink() {
        return link;
    }

    public String GetContent() {
        return content;
    }

    public String GetCategory() {
        return category;
    }

    public boolean IsEvent() { return isEvent;}
    public Long GetBegin() { return event_begin;}
    public Long GetEnd() { return event_end;}
    public String GetRrule() { return rrule;}
}
