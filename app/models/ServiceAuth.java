package models;

import lib.PasswordGenerator;
import play.Play;
import play.data.validation.Constraints.Required;
import play.db.ebean.Model;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by fernando on 5/9/14.
 */
@Entity
public class ServiceAuth extends Model {

    @Id
    Long id;

    @Required
    private String email;

    @Required
    private String authToken;

    @OneToMany(mappedBy = "commenter", cascade = CascadeType.ALL)
    List<Comment> comments = new ArrayList<Comment>();

    @OneToMany(mappedBy = "annotator", cascade = CascadeType.ALL)
    List<Annotation> annotations = new ArrayList<Annotation>();

    public static Finder<String,ServiceAuth> find = new Finder<String,ServiceAuth>(Play.application().configuration().getString("datasource"), String.class, ServiceAuth.class);

    public ServiceAuth(String email){
        PasswordGenerator generator = new PasswordGenerator();
        this.email = email;
        this.authToken = generator.next();
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public static boolean verifyService(String token){
        return find.where().eq("authToken",token).findUnique() != null;
    }

    public List<Annotation> getAnnotations() {
        return annotations;
    }

    public void setAnnotations(List<Annotation> annotations) {
        this.annotations = annotations;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }
}