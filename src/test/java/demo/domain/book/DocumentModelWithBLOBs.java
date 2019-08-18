package demo.domain.book;

import java.io.Serializable;

/**
 *  由Mybatis代码生成器生成DocumentModelWithBLOBs实体类
 *  @author Winston.Wang
 *  @date 2019-08-11 23:52:03
 **/
public class DocumentModelWithBLOBs extends DocumentModel implements Serializable {
    /**
     * 内容
     */
    private String content;

    /**
     * Markdown
     */
    private String markdown;

    private static final long serialVersionUID = 1L;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getMarkdown() {
        return markdown;
    }

    public void setMarkdown(String markdown) {
        this.markdown = markdown;
    }
}