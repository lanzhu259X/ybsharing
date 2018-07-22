package com.yiban.sharing.dto;

import org.springframework.util.StringUtils;

public class SharingQuery {

    private String search;
    private Integer page;
    private Integer pageSize;

    private String sharingNo;

    private int limit;
    private int offset;

    public int getLimit() {
        return (pageSize == null || pageSize <= 0) ? 20 : pageSize;
    }

    public int getOffset() {
        return (page == null || page <= 0 ? 0 : page - 1) * getLimit();
    }

    public String getSearch() {
        if (StringUtils.isEmpty(this.search)) {
            return null;
        }
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public String getSharingNo() {
        return sharingNo;
    }

    public void setSharingNo(String sharingNo) {
        this.sharingNo = sharingNo;
    }
}
