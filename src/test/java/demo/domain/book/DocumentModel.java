package demo.domain.book;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.validator.constraints.NotEmpty;

/**
 *  由Mybatis代码生成器生成DocumentModel实体类
 *  @author Winston.Wang
 *  @date 2019-08-11 23:52:03
 **/
public class DocumentModel implements Serializable {
    /**
     * ID
     */
    @Id
    @GeneratedValue
    private String id;

    /**
     * 批次号
     */
    @NotEmpty
    private String batchNo;

    /**
     * 父级ID
     */
    @NotEmpty
    private String parentId;

    /**
     * 书籍ID
     */
    @NotEmpty
    private String bookId;

    /**
     * 是否打开
     */
    @NotEmpty
    private String isOpen;

    /**
     * 所属人
     */
    @NotEmpty
    private String owner;

    /**
     * 名称
     */
    @NotEmpty
    private String name;

    /**
     * 状态
     */
    @NotEmpty
    private String status;

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

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public String getIsOpen() {
        return isOpen;
    }

    public void setIsOpen(String isOpen) {
        this.isOpen = isOpen;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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