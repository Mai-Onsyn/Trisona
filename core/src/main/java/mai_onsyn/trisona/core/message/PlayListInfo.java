package mai_onsyn.trisona.core.message;

import mai_onsyn.trisona.core.data.Date;
import mai_onsyn.trisona.core.data.User;

public class PlayListInfo extends Message {

    public String name;
    public User creator;
    public Date createDate;
    public UniversalPath coverPath;

}
