package pl.polsl.pp.backapp.post;

import pl.polsl.pp.backapp.user.User;

import java.util.Date;

public class PostDTO {

    private String id;
    private String userId;
    private String login;
    private String role;
    private Boolean status;
    private Integer postsNumber;
    private Date dateJoined;
    private Date lastLogin;
    private Date createDate;
    private Date lastChange;
    private String text;

    public PostDTO() {
    }

    public PostDTO(String id, String userId, String login, String role, Boolean status, Integer postsNumber,
                   Date dateJoined, Date lastLogin, Date createDate, Date lastChange, String text) {
        this.id = id;
        this.userId = userId;
        this.login = login;
        this.role = role;
        this.status = status;
        this.postsNumber = postsNumber;
        this.dateJoined = dateJoined;
        this.lastLogin = lastLogin;
        this.createDate = createDate;
        this.lastChange = lastChange;
        this.text = text;
    }

    public PostDTO(User user, Post post) {
        this.id = post.getId();
        this.userId = user.getId();
        this.login = user.getLogin();
        this.role = user.getRole();
        this.status = user.getStatus();
        this.postsNumber = user.getPostsNumber();
        this.dateJoined = user.getDateJoined();
        this.lastLogin = user.getLastLogin();
        this.createDate = post.getCreateDate();
        this.lastChange = post.getLastChange();
        this.text = post.getText();
    }

    public String getId() {
        return id;
    }

    public String getUserId() {
        return userId;
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

    public Date getCreateDate() {
        return createDate;
    }

    public Date getLastChange() {
        return lastChange;
    }

    public String getText() {
        return text;
    }
}
