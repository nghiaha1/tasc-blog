package com.tasc.blogging.entity.enums;

public enum ERROR {

    INVALID_EMAIL_REQUEST(1000, "Invalid email request"),
    INVALID_PASSWORD_REQUEST(1001, "Invalid password request"),
    INVALID_CONFIRM_PASSWORD_REQUEST(1002, "Invalid password request"),
    INVALID_PARAM(1003, "Invalid parameter"),
    INVALID_EMAIL(1004, "Invalid email"),
    INVALID_PASSWORD(1005, "Invalid password"),
    INVALID_USER(1006, "Invalid user"),
    INVALID_ROLE(1007, "Invalid role"),
    USER_NOT_FOUND(1008, "User not found"),
    PASSWORD_NOT_MATCH(1009, "Password not match"),
    EMAIL_EXISTED(1010, "email existed"),
    CATEGORY_TITLE_IS_EMPTY(1011, "Category title is empty"),
    CATEGORY_DESCRIPTION_IS_EMPTY(1012, "Category description is empty"),
    CATEGORY_LEVEL_IS_INVALID(1013, "Category level is invalid"),
    CATEGORY_PARENT_IS_BLANK(1014, "Category parent is blank"),
    CATEGORY_PARENT_NOT_FOUND(1015, "Category parent not found"),
    CATEGORY_NOT_FOUND(1016, "Category not found"),
    CATEGORY_TITLE_EXISTED(1017, "Category title existed"),
    CATEGORY_NOT_DELETED(1018, "Category not deleted"),
    CATEGORY_NOT_UPDATED(1019, "Category not updated"),
    CATEGORY_NOT_CREATED(1020, "Category not created"),
    BLOG_TITLE_IS_EMPTY(1021, "Blog title is empty"),
    BLOG_DESCRIPTION_IS_EMPTY(1022, "Blog description is empty"),
    BLOG_CONTENT_IS_EMPTY(1023, "Blog content is empty"),
    BLOG_CATEGORIES_IS_EMPTY(1024, "Blog categories is empty"),
    BLOG_THUMBNAILS_IS_EMPTY(1025, "Blog thumbnails is empty"),
    BLOG_CATEGORY_NOT_FOUND(1026, "Blog category not found"),
    BLOG_NOT_FOUND(1027, "Blog not found"),
    BLOG_NOT_DELETED(1028, "Blog not deleted"),
    BLOG_NOT_UPDATED(1029, "Blog not updated"),
    BLOG_NOT_CREATED(1030, "Blog not created"),
    BLOG_TITLE_EXISTED(1031, "Blog title existed"),
    BLOG_THUMBNAIL_NOT_FOUND(1032, "Blog thumbnail not found"),
    INVALID_VERIFICATION_CODE(1033, "Invalid verification code"),
    ROLE_NOT_FOUND(1034, "Role not found"),
    COMMENT_NOT_FOUND(1035, "Comment not found"),
    COMMENT_NOT_DELETED(1036, "Comment not deleted"),
    COMMENT_NOT_UPDATED(1037, "Comment not updated"),
    COMMENT_NOT_CREATED(1038, "Comment not created"),
    COMMENT_CONTENT_IS_EMPTY(1039, "Comment content is empty"),
    COMMENT_BLOG_NOT_FOUND(1040, "Comment blog not found"),
    COMMENT_USER_NOT_FOUND(1041, "Comment user not found"),
    INVALID_COMMENT_REQUEST(1042, "Invalid comment request"),
    INVALID_ACCESS_TOKEN(1043, "Invalid access token"),
    USER_NOT_AUTHORIZED(1044, "User not authorized"),
    BLOG_REQUEST_IS_NULL(1045, "Blog request is null"),
    BLOG_ID_IS_NULL(1046, "Blog id is null"),;

    private int code;
    private String message;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    ERROR(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
