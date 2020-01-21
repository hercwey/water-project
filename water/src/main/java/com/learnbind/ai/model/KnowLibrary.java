package com.learnbind.ai.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import javax.persistence.*;

import org.springframework.format.annotation.DateTimeFormat;

import com.learnbind.ai.common.util.dict.DataDictType;

@Table(name = "KNOW_LIBRARY")
public class KnowLibrary {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy=GenerationType.IDENTITY, generator="SELECT KNOW_LIBRARY_SEQ_GENERATOR.CURRVAL FROM DUAL")//前提是先创建了oracle序列
    private Long id;

    @Column(name = "TITLE")
    private String title;

    @Column(name = "IMG_PATH")
    private String imgPath;

    @Column(name = "CONTENT")
    private String content;

    @Column(name = "PUBLIC_DATE")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date publicDate;

    @Column(name = "AUTHOR")
    private String author;

    @Column(name = "LABELS")
    private String labels;
    
    @Column(name = "TYPE")
    private String type;

    /**
     * @return ID
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return TITLE
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title
     */
    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    /**
     * @return IMG_PATH
     */
    public String getImgPath() {
        return imgPath;
    }

    /**
     * @param imgPath
     */
    public void setImgPath(String imgPath) {
        this.imgPath = imgPath == null ? null : imgPath.trim();
    }

    /**
     * @return CONTENT
     */
    public String getContent() {
        return content;
    }

    /**
     * @param content
     */
    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }

    /**
     * @return PUBLIC_DATE
     */
    public Date getPublicDate() {
        return publicDate;
    }

    /**
     * @param publicDate
     */
    public void setPublicDate(Date publicDate) {
        this.publicDate = publicDate;
    }
    
    /**
     * @Title: getPublicDateStr
     * @Description: 获取发布日期字符串
     * @return 
     */
    public String getPublicDateStr() {
    	if(publicDate != null) {
    		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        	return sdf.format(publicDate);
    	}
    	return null;
    }

    /**
     * @return AUTHOR
     */
    public String getAuthor() {
        return author;
    }

    /**
     * @param author
     */
    public void setAuthor(String author) {
        this.author = author == null ? null : author.trim();
    }

    /**
     * @return LABELS
     */
    public String getLabels() {
        return labels;
    }

    /**
     * @param labels
     */
    public void setLabels(String labels) {
        this.labels = labels == null ? null : labels.trim();
    }
    
    public String getLabelsStr() {
		if(labels!=null) {
    		return DataDictType.getValue(labels);  	
    	}
    	return null;
	}
    
    

    public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	public String getTypeStr() {
		if(type!=null) {
    		return DataDictType.getValue(type);  	
    	}
    	return null;
	}

	@Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", title=").append(title);
        sb.append(", imgPath=").append(imgPath);
        sb.append(", content=").append(content);
        sb.append(", publicDate=").append(publicDate);
        sb.append(", author=").append(author);
        sb.append(", labels=").append(labels);
        sb.append(", type=").append(type);
        sb.append("]");
        return sb.toString();
    }
}