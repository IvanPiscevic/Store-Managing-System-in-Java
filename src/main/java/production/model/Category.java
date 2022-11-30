package production.model;

import java.io.Serializable;
import java.util.Objects;

/**
 * Represents a Category.
 * Extends Item NamedEntity
 */
public class Category extends NamedEntity implements Serializable {

    private String description;

    /**
     * Constructor with parameters which creates Category object.
     *
     * @param name String name of category.
     * @param description String description of category.
     */
    public Category(Long id, String name, String description) {
        super(id, name);
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Category category = (Category) o;
        return Objects.equals(description, category.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), description);
    }

    @Override
    public String toString() {
        return this.getName();
    }
}
