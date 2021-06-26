package pl.polsl.pp.backapp.topic;

import pl.polsl.pp.backapp.user.User;

import java.util.Date;

public class TopicDTO {

    private String id;
    private String login;
    private String role;
    private Boolean status;
    private Integer postsNumber;
    private Date dateJoined;
    private Date lastLogin;
    private String title;
    private Date createDate;
    private Date lastChange;
    private String description;
    private Integer pageViews;

    public TopicDTO() {
    }

    public TopicDTO(String id, String login, String role, Boolean status, Integer postsNumber,
                    Date dateJoined, Date lastLogin, String title, Date createDate, Date lastChange,
                    String description, Integer pageViews) {
        this.id = id;
        this.login = login;
        this.role = role;
        this.status = status;
        this.postsNumber = postsNumber;
        this.dateJoined = dateJoined;
        this.lastLogin = lastLogin;
        this.title = title;
        this.createDate = createDate;
        this.lastChange = lastChange;
        this.description = description;
        this.pageViews = pageViews;
    }

    public TopicDTO(User user, Topic topic) {
        this.id = user.getId();
        this.login = user.getLogin();
        this.role = user.getRole();
        this.status = user.getStatus();
        this.postsNumber = user.getPostsNumber();
        this.dateJoined = user.getDateJoined();
        this.lastLogin = user.getLastLogin();
        this.title = topic.getTitle();
        this.createDate = topic.getCreateDate();
        this.lastChange = topic.getLastChange();
        this.description = topic.getDescription();
        this.pageViews = topic.getPageViews();
    }

    public String getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    public String getRole() {
        return role;
    }

    public Boolean getStatus() {
        return status;
    }

    public Integer getPostsNumber() {
        return postsNumber;
    }

    public Date getDateJoined() {
        return dateJoined;
    }

    public Date getLastLogin() {
        return lastLogin;
    }

    public String getTitle() {
        return title;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public Date getLastChange() {
        return lastChange;
    }

    public String getDescription() {
        return description;
    }

    public Integer getPageViews() {
        return pageViews;
    }
}
