package com.tiotm.manage.query;

public class UserQuery {
    public static final String COUNT_USER_EMAIL_QUERY = "SELECT COUNT(*) FROM users WHERE email = :email";
    public static final String INSERT_USER_QUERY = "INSERT INTO users (first_name, last_name, email, password, address, phone, title, bio, enabled, is_not_locked, is_using_mfa, image_url, created_ts, updated_ts) VALUES (:firstName, :lastName, :email, :password, :address, :phone, :title, :bio, :enabled, :isNotLocked, :isUsingMFA, :imageUrl, :createdTS, :updatedTS)";
    

}
