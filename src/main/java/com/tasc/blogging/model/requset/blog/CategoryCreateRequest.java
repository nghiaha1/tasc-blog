package com.tasc.blogging.model.requset.blog;

import com.tasc.blogging.util.Constant;
import lombok.Data;

@Data
public class CategoryCreateRequest {
    private String title;
    private String description;
    private Integer isRoot;
    private Long parent;
    public boolean checkIsRoot(){
        return isRoot != null && isRoot == Constant.ONOFF.ON;
    }
}
