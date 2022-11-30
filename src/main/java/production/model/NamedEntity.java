package production.model;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;

/**
 * Represents an abstract class of a Named Entity
 */
public abstract class NamedEntity implements Serializable {

    private Long id;
    private String name;

    /**
     * Constructor of an abstract class.
     * @param name String name.
     */
    public NamedEntity(Long id, String name) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NamedEntity that = (NamedEntity) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }


}
