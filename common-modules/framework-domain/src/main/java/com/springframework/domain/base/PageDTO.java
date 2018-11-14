package com.springframework.domain.base;

import lombok.Data;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.List;

/**
 * @author summer
 */
@Data
public class PageDTO<T> {
    private final long total;
    private final List<T> content;
    private final int page;
    private final int size;

    public PageDTO(List<T> content, Pageable pageable, long total) {
        this.total = total;
        this.content = content;
        this.page = pageable.getPageNumber();
        this.size = pageable.getPageSize();
    }

    public List<T> getContent() {
        return Collections.unmodifiableList(content);
    }

    public boolean hasContent() {
        return content != null && content.size() > 0;
    }
}
