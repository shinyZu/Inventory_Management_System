package lk.ijse.im_system.model;

public class Category {

    private String categoryId;
    private String categoryTitle;

    public Category() {}

    public Category(String categoryId, String categoryTitle) {
        this.setCategoryId(categoryId);
        this.setCategoryTitle(categoryTitle);
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryTitle() {
        return categoryTitle;
    }

    public void setCategoryTitle(String categoryTitle) {
        this.categoryTitle = categoryTitle;
    }

    @Override
    public String toString() {
        return "Category{" +
                "categoryId='" + categoryId + '\'' +
                ", categoryTitle='" + categoryTitle + '\'' +
                '}';
    }
}
