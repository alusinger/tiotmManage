############################################
###                                     ####
###  Author: Anthony L                  ####
###  License: MIT                       ####
###  Date: Jan 25, 2024                 ####
###  Version: 1.0.0                     ####
###                                     ####
############################################

/*
 *  --- General Rules ---
 *  Use underscore_names instead of camelCase
 *  Table names should be plural
 *  Spell out id fields as "item_id" instead of "id"
 *  
 */

CREATE SCHEMA IF NOT EXISTS manage;

SET NAMES 'UTF8MB4';
 
USE manage;

DROP TABLE IF EXISTS Users;

CREATE TABLE Users (
    user_id     BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
    first_name  VARCHAR(50) NOT NULL,
    last_name   VARCHAR(50) NOT NULL,
    email       VARCHAR(255) NOT NULL,
    password    VARCHAR(255) DEFAULT NULL,
    address     VARCHAR(255) DEFAULT NULL,
    phone       VARCHAR(30) DEFAULT NULL,
    title       VARCHAR(50) DEFAULT NULL,
    bio         VARCHAR(255) DEFAULT NULL,
    enabled     BOOLEAN DEFAULT FALSE,
    locked      BOOLEAN DEFAULT FALSE,
    using_mfa   BOOLEAN DEFAULT FALSE,
    image_url   VARCHAR(255) DEFAULT 'https://cdn-icons-png.flaticon.com/512/149/149071.png',
    create_ts   DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_ts   DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (user_id),
    CONSTRAINT email_uq UNIQUE (email)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS Roles;

CREATE TABLE Roles (
    role_id     BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
    role_name   VARCHAR(50) NOT NULL,
    permissions VARCHAR(255) DEFAULT NULL,
    PRIMARY KEY (role_id),
    CONSTRAINT role_name_uq UNIQUE (role_name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS User_Roles;

CREATE TABLE User_Roles ( 
    user_role_id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
    user_id     BIGINT UNSIGNED NOT NULL,
    role_id     BIGINT UNSIGNED NOT NULL,
    create_ts   DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_ts   DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (user_role_id),
    FOREIGN KEY (user_id) REFERENCES Users (user_id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (role_id) REFERENCES Roles (role_id) ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT userroles_user_id_uq UNIQUE (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS Events;

Create Table Events 
(
    event_id    BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
    type        VARCHAR(50) NOT NULL CHECK(type IN ('LOGIN_ATTEMPT', 'LOGIN_ATTEMPT_FAILURE', 'LOGIN_ATTEMPT_SUCCESS', 'PROFILE_UPDATE', 'PROFILE_PICTURE_UPDATE', 'ROLE_UPDATE', 'ACCOUNT_SETTINGS_UPDATE', 'PASSWORD_UPDATE', 'MFA_UPDATE')),
    description VARCHAR(255) NOT NULL,
    PRIMARY KEY (event_id),
    CONSTRAINT Events_Type_UQ UNIQUE (type)
);

DROP TABLE IF EXISTS UserEvents;

Create Table UserEvents ( 
    user_event_id  BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
    user_id         BIGINT UNSIGNED NOT NULL,
    event_id        BIGINT UNSIGNED NOT NULL,
    device          VARCHAR(100) DEFAULT NULL,
    ip_address      VARCHAR(100) DEFAULT NULL,
    create_ts       DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (user_event_id),
    FOREIGN KEY (user_id) REFERENCES Users (user_id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (event_id) REFERENCES Events (event_id) ON DELETE RESTRICT ON UPDATE CASCADE
);

DROP TABLE IF EXISTS AccountVerifications;

Create Table AccountVerification ( 
    account_verification_id     BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
    user_id                     BIGINT UNSIGNED NOT NULL,
    verification_url            VARCHAR(255) NOT NULL,
    -- date                     DATETIME NOT NULL,
    PRIMARY KEY (account_verification_id),
    FOREIGN KEY (user_id) REFERENCES Users (user_id) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT AccountVerification_user_id_uq UNIQUE (user_id),
    CONSTRAINT AccountVerification_verification_url_uq UNIQUE (verification_url)
);

DROP TABLE IF EXISTS PasswordVerification;

Create Table PasswordVerification ( 
    password_verification_id    BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
    user_id                     BIGINT UNSIGNED NOT NULL,
    verification_url            VARCHAR(255) NOT NULL,
    expiration_date             DATETIME NOT NULL,
    PRIMARY KEY (password_verification_id),
    FOREIGN KEY (user_id) REFERENCES Users (user_id) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT PasswordVerification_user_id_uq UNIQUE (user_id),
    CONSTRAINT PasswordVerification_verification_url_uq UNIQUE (verification_url)
);

DROP TABLE IF EXISTS MFAVerification;

Create Table MFAVerification ( 
    mfa_verification_id         BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
    user_id                     BIGINT UNSIGNED NOT NULL,
    mfa_token                   VARCHAR(255) NOT NULL,
    expiration_date             DATETIME NOT NULL,
    PRIMARY KEY (mfa_verification_id),
    FOREIGN KEY (user_id) REFERENCES Users (user_id) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT mfaverification_user_id_uq UNIQUE (user_id),
    CONSTRAINT mfaverification_mfa_token_uq UNIQUE (mfa_token)
);

