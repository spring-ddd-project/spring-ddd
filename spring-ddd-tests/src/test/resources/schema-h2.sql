-- H2 Schema for Testing (based on spring_ddd_0330.sql)

-- Common audit columns macro
-- create table if not exists leaf_alloc (
--     id bigint auto_increment primary key,
--     biz_tag varchar(255) not null,
--     max_id bigint not null,
--     step int not null,
--     description varchar(255)
-- );

create table if not exists sys_dept (
    id bigint auto_increment primary key,
    parent_id bigint,
    dept_name varchar(255) not null,
    sort_order int,
    dept_status boolean,
    delete_status boolean default false,
    create_by varchar(64),
    create_time timestamp,
    update_by varchar(64),
    update_time timestamp,
    version int default 0
);

create table if not exists sys_dict (
    id bigint auto_increment primary key,
    dict_name varchar(255) not null,
    dict_code varchar(255) not null unique,
    dict_status int,
    sort_order int,
    delete_status boolean default false,
    create_by varchar(64),
    create_time timestamp,
    update_by varchar(64),
    update_time timestamp,
    version int default 0
);

create table if not exists sys_dict_item (
    id bigint auto_increment primary key,
    dict_id bigint not null,
    item_label varchar(255) not null,
    item_value int not null,
    sort_order int,
    item_status int,
    delete_status boolean default false,
    create_by varchar(64),
    create_time timestamp,
    update_by varchar(64),
    update_time timestamp,
    version int default 0
);

create table if not exists sys_menu (
    id bigint auto_increment primary key,
    parent_id bigint,
    menu_name varchar(255) not null,
    menu_type int,
    menu_path varchar(255),
    menu_permission varchar(255),
    menu_icon varchar(255),
    menu_component varchar(255),
    menu_redirect varchar(255),
    menu_status int,
    delete_status boolean default false,
    create_by varchar(64),
    create_time timestamp,
    update_by varchar(64),
    update_time timestamp,
    version int default 0
);

create table if not exists sys_role (
    id bigint auto_increment primary key,
    role_name varchar(255) not null,
    role_code varchar(255) not null unique,
    role_status int,
    data_scope int,
    role_desc varchar(255),
    role_owner boolean,
    delete_status boolean default false,
    create_by varchar(64),
    create_time timestamp,
    update_by varchar(64),
    update_time timestamp,
    version int default 0
);

create table if not exists sys_user (
    id bigint auto_increment primary key,
    username varchar(255) not null unique,
    password varchar(255),
    email varchar(255),
    phone varchar(255),
    lock_status boolean,
    delete_status boolean default false,
    create_by varchar(64),
    create_time timestamp,
    update_by varchar(64),
    update_time timestamp,
    version int default 0
);

create table if not exists sys_role_menu (
    id bigint auto_increment primary key,
    role_id bigint not null,
    menu_id bigint not null,
    delete_status boolean default false,
    create_by varchar(64),
    create_time timestamp,
    update_by varchar(64),
    update_time timestamp,
    version int default 0
);

create table if not exists sys_user_role (
    id bigint auto_increment primary key,
    user_id bigint not null,
    role_id bigint not null,
    delete_status boolean default false,
    create_by varchar(64),
    create_time timestamp,
    update_by varchar(64),
    update_time timestamp,
    version int default 0
);

create table if not exists gen_project_info (
    id bigint auto_increment primary key,
    table_name varchar(255) not null,
    package_name varchar(255) not null,
    class_name varchar(255) not null,
    request_name varchar(255) not null,
    project_name varchar(30) not null,
    module_name varchar(30) not null,
    delete_status boolean default false,
    create_by varchar(64),
    create_time timestamp,
    update_by varchar(64),
    update_time timestamp,
    version int default 0
);

create table if not exists gen_columns (
    id bigint auto_increment primary key,
    info_id bigint not null,
    prop_column_key char(10),
    prop_column_name varchar(255) not null,
    prop_column_type varchar(255) not null,
    prop_column_comment varchar(255),
    prop_java_entity varchar(255) not null,
    prop_java_type varchar(255) not null,
    prop_dict_id bigint,
    table_visible boolean not null,
    table_order boolean not null,
    table_filter boolean not null,
    table_filter_component int,
    table_filter_type int,
    typescript_type int,
    form_component int not null,
    form_visible boolean not null,
    form_required boolean not null,
    en varchar(255),
    locale varchar(255),
    delete_status boolean default false,
    create_by varchar(64),
    create_time timestamp,
    update_by varchar(64),
    update_time timestamp,
    version int default 0
);

create table if not exists gen_aggregate (
    id bigint auto_increment primary key,
    info_id bigint not null,
    object_name varchar(255) not null,
    object_value varchar(255) not null,
    object_type int not null,
    has_created boolean not null,
    delete_status boolean default false,
    create_by varchar(64),
    create_time timestamp,
    update_by varchar(64),
    update_time timestamp,
    version int default 0
);

create table if not exists gen_column_bind (
    id bigint auto_increment primary key,
    column_type varchar(255) not null,
    entity_type varchar(255) not null,
    component_type int,
    typescript_type int,
    delete_status boolean default false,
    create_by varchar(64),
    create_time timestamp,
    update_by varchar(64),
    update_time timestamp,
    version int default 0
);

create table if not exists gen_template (
    id bigint auto_increment primary key,
    template_name varchar(50) not null,
    template_content text not null,
    delete_status boolean default false,
    create_by varchar(64),
    create_time timestamp,
    update_by varchar(64),
    update_time timestamp,
    version int default 0
);

create table if not exists leaf_alloc (
    id bigint auto_increment primary key,
    biz_tag varchar(255) not null unique,
    max_id bigint not null,
    step int not null,
    description varchar(255)
);
