package model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;


@MappedSuperclass
public class Entity< ID > implements Serializable {
    private static final long serialVersionUID = 7331115341259248461L;
    private ID id ;

    @Id
    @GeneratedValue(generator="increment")
    @GenericGenerator(name="increment", strategy="increment")

    public ID getId() {return id;}
    public void setId (ID id) {this.id = id;}
}
