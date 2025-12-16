-- 创建数据库
CREATE DATABASE IF NOT EXISTS library_system DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE library_system;

-- 1. 用户表
CREATE TABLE `user` (
                        `id` INT(11) NOT NULL AUTO_INCREMENT COMMENT '用户ID',
                        `username` VARCHAR(50) NOT NULL UNIQUE COMMENT '用户名',
                        `password` VARCHAR(255) NOT NULL COMMENT '密码',
                        `phone` VARCHAR(20) DEFAULT NULL COMMENT '电话',
                        `role` VARCHAR(20) NOT NULL DEFAULT 'USER' COMMENT '角色：ADMIN-管理员, USER-普通用户',
                        `status` TINYINT(1) NOT NULL DEFAULT 1 COMMENT '状态：1-正常, 0-禁用',
                        `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                        `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                        PRIMARY KEY (`id`),
                        INDEX `idx_username` (`username`),
                        INDEX `idx_role` (`role`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- 2. 图书分类表（简化设计，可根据需要调整）
CREATE TABLE `book_category` (
                                 `id` INT(11) NOT NULL AUTO_INCREMENT COMMENT '分类ID',
                                 `name` VARCHAR(50) NOT NULL COMMENT '分类名称',
                                 `description` VARCHAR(255) DEFAULT NULL COMMENT '分类描述',
                                 `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                 PRIMARY KEY (`id`),
                                 UNIQUE KEY `uk_name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='图书分类表';

-- 3. 图书表
CREATE TABLE `book` (
                        `id` INT(11) NOT NULL AUTO_INCREMENT COMMENT '图书ID',
                        `isbn` VARCHAR(20) NOT NULL UNIQUE COMMENT 'ISBN号',
                        `title` VARCHAR(200) NOT NULL COMMENT '书名',
                        `author` VARCHAR(100) NOT NULL COMMENT '作者',
                        `publisher` VARCHAR(100) DEFAULT NULL COMMENT '出版社',
                        `publish_date` DATE DEFAULT NULL COMMENT '出版日期',
                        `category_id` INT(11) DEFAULT NULL COMMENT '分类ID',
                        `description` TEXT COMMENT '图书描述',
                        `cover_image` VARCHAR(500) DEFAULT NULL COMMENT '封面图片URL',
                        `total_copies` INT(11) NOT NULL DEFAULT 1 COMMENT '总数量',
                        `available_copies` INT(11) NOT NULL DEFAULT 1 COMMENT '可借数量',
                        `is_recommended` TINYINT(1) DEFAULT 0 COMMENT '是否推荐：0-不推荐, 1-推荐',
                        `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                        `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                        PRIMARY KEY (`id`),
                        INDEX `idx_title` (`title`),
                        INDEX `idx_author` (`author`),
                        INDEX `idx_is_recommended` (`is_recommended`),
                        INDEX `idx_category` (`category_id`),
                        CONSTRAINT `fk_book_category` FOREIGN KEY (`category_id`) REFERENCES `book_category` (`id`) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='图书表';

-- 4. 借阅记录表
CREATE TABLE `borrow_record` (
                                 `id` INT(11) NOT NULL AUTO_INCREMENT COMMENT '借阅记录ID',
                                 `user_id` INT(11) NOT NULL COMMENT '用户ID',
                                 `book_id` INT(11) NOT NULL COMMENT '图书ID',
                                 `borrow_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '借阅时间',
                                 `due_time` DATETIME NOT NULL COMMENT '应还时间',
                                 `return_time` DATETIME DEFAULT NULL COMMENT '实际归还时间',
                                 `status` VARCHAR(20) NOT NULL DEFAULT 'BORROWED' COMMENT '状态：BORROWED-借阅中, RETURNED-已归还, OVERDUE-逾期',
                                 `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                 `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                 PRIMARY KEY (`id`),
                                 INDEX `idx_user_id` (`user_id`),
                                 INDEX `idx_book_id` (`book_id`),
                                 INDEX `idx_status` (`status`),
                                 INDEX `idx_due_time` (`due_time`),
                                 INDEX `idx_user_status` (`user_id`, `status`),
                                 CONSTRAINT `fk_borrow_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE,
                                 CONSTRAINT `fk_borrow_book` FOREIGN KEY (`book_id`) REFERENCES `book` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='借阅记录表';

-- 5. 系统配置表（可选，用于存储借阅规则等）
CREATE TABLE `system_config` (
                                 `id` INT(11) NOT NULL AUTO_INCREMENT COMMENT '配置ID',
                                 `config_key` VARCHAR(50) NOT NULL UNIQUE COMMENT '配置键',
                                 `config_value` VARCHAR(500) NOT NULL COMMENT '配置值',
                                 `description` VARCHAR(200) DEFAULT NULL COMMENT '配置描述',
                                 `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                 PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统配置表';


-- 插入图书分类
INSERT INTO `book_category` (`name`, `description`) VALUES
                                                        ('计算机科学', '计算机相关书籍'),
                                                        ('文学小说', '小说、散文等文学作品'),
                                                        ('自然科学', '物理、化学、生物等自然科学'),
                                                        ('社会科学', '社会学、心理学、经济学等'),
                                                        ('工程技术', '工程、技术相关书籍');

-- 插入图书示例数据
INSERT INTO `book` (`isbn`, `title`, `author`, `publisher`, `category_id`, `description`, `total_copies`, `available_copies`, `is_recommended`) VALUES
                                                                                                                                                    ('9787111128068', 'Java编程思想', 'Bruce Eckel', '机械工业出版社', 1, 'Java编程经典书籍', 5, 5, 1),
                                                                                                                                                    ('9787302275954', 'Spring Boot实战', 'Craig Walls', '人民邮电出版社', 1, 'Spring Boot开发指南', 3, 3, 1),
                                                                                                                                                    ('9787020002207', '红楼梦', '曹雪芹', '人民文学出版社', 2, '中国古典文学名著', 8, 8, 0),
                                                                                                                                                    ('9787505738967', '时间简史', '史蒂芬·霍金', '湖南科学技术出版社', 3, '科普经典著作', 4, 4, 1);

-- 插入系统配置
INSERT INTO `system_config` (`config_key`, `config_value`, `description`) VALUES
                                                                              ('borrow_duration', '30', '借阅时长（天）'),
                                                                              ('max_borrow_books', '5', '最大借阅数量'),
                                                                              ('overdue_fine_per_day', '0.1', '逾期罚款（元/天）');
