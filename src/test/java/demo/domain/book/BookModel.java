package demo.domain.book;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.validator.constraints.NotEmpty;

/**
 *  由Mybatis代码生成器生成BookModel实体类
 *  @author Winston.Wang
 *  @date 2019-08-11 23:52:03
 **/
public class BookModel implements Serializable {
    /**
     * ID
     */
    @Id
    @GeneratedValue
    private String id;

    /**
     * 分类ID
     */
    @NotEmpty
    private String categoryId;

    /**
     * 背景图片
     */
    @NotEmpty
    private String bookImage;

    /**
     * 所属人
     */
    @NotEmpty
    private String owner;

    /**
     * 书籍
     */
    @NotEmpty
    private String name;

    /**
     * seo 标题
     */
    @NotEmpty
    private String seoTitle;

    /**
     * seo 关键字
     */
    @NotEmpty
    private String seoKeywords;

    /**
     * seo 描述
     */
    @NotEmpty
    private String seoDescription;

    /**
     * 创建时间
     */
    private Date createDate;

    /**
     * 修改时间
     */
    private Date updateDate;

    private static final long serialVersionUID = 1L;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getBookImage() {
        return bookImage;
    }

    public void setBookImage(String bookImage) {
        this.bookImage = bookImage;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSeoTitle() {
        return seoTitle;
    }

    public void setSeoTitle(String seoTitle) {
        this.seoTitle = seoTitle;
    }

    public String getSeoKeywords() {
        return seoKeywords;
    }

    public void setSeoKeywords(String seoKeywords) {
        this.seoKeywords = seoKeywords;
    }

    public String getSeoDescription() {
        return seoDescription;
    }

    public void setSeoDescription(String seoDescription) {
        this.seoDescription = seoDescription;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
}