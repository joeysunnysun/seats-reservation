-- Create DB
CREATE DATABASE IF NOT EXISTS esun;

-- Use DB
Use esun;

-- DDL
CREATE TABLE IF NOT EXISTS seating_chart (
    floor_seat_seq INT AUTO_INCREMENT PRIMARY KEY,
    floor_no INT NOT NULL,
    seat_no VARCHAR(10) NOT NULL
);

CREATE TABLE IF NOT EXISTS employee (
    emp_id CHAR(5) PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    email VARCHAR(100),
    floor_seat_seq INT,
    FOREIGN KEY (floor_seat_seq) REFERENCES seating_chart(floor_seat_seq)
);

-- seating_chart 座位資料
INSERT INTO seating_chart (floor_seat_seq, floor_no, seat_no) VALUES
(1, 1, 1), (2, 1, 2), (3, 1, 3), (4, 1, 4),
(5, 2, 1), (6, 2, 2), (7, 2, 3), (8, 2, 4),
(9, 3, 1), (10, 3, 2), (11, 3, 3), (12, 3, 4),
(13, 4, 1), (14, 4, 2), (15, 4, 3), (16, 4, 4);

-- employee 員工資料
INSERT INTO employee (emp_id, name, email) VALUES
('12001', '王小明', 'ming.wang@example.com'),
('12002', '李大華', 'hua.li@example.com'),
('12003', '陳美麗', 'mei.chen@example.com'),
('12004', '林志強', 'chiang.lin@example.com'),
('12005', '張雅婷', 'ting.chang@example.com'),
('12006', '黃國倫', 'lun.huang@example.com'),
('12007', '吳怡君', 'yi.wu@example.com'),
('12008', '周建宏', 'hong.chou@example.com'),
('12009', '鄭文彥', 'wen.cheng@example.com'),
('12010', '許家豪', 'hao.hsu@example.com'),
('12011', '簡淑芬', 'fen.chien@example.com'),
('12012', '曾偉哲', 'che.tseng@example.com'),
('12013', '賴怡廷', 'ting.lai@example.com'),
('12014', '謝孟儒', 'meng.hsieh@example.com'),
('12015', '蔡欣怡', 'yi.tsai@example.com'),
('12016', '鍾明哲', 'ming.chung@example.com');

-- Stored Procedure
-- 取得所有員工編號
DELIMITER //

CREATE PROCEDURE get_all_employees()
BEGIN
    SELECT emp_id, name, email, floor_seat_seq
    FROM employee;
END //

DELIMITER ;

-- 分配座位
DELIMITER //

CREATE PROCEDURE assign_seat(
    IN p_emp_id CHAR(5),
    IN p_floor_no INT,
    IN p_seat_no INT
)
BEGIN
    DECLARE v_floor_seat_seq INT;
    
    -- 取出 floor_seat_seq
    SELECT floor_seat_seq INTO v_floor_seat_seq
    FROM seating_chart
    WHERE floor_no = p_floor_no AND seat_no = p_seat_no
    LIMIT 1;
    
    -- 找到後才更新
    IF v_floor_seat_seq IS NOT NULL THEN
        START TRANSACTION;
        -- 清除原有座位資訊
        UPDATE employee SET floor_seat_seq = NULL WHERE emp_id = p_emp_id;
        -- 重分配新座位
        UPDATE employee SET floor_seat_seq = v_floor_seat_seq WHERE emp_id = p_emp_id;
        COMMIT;
    END IF;
    
END //

DELIMITER ;

-- 清除員工座位資訊
DELIMITER //

CREATE PROCEDURE clear_seat(
    IN p_emp_id CHAR(5)
)
BEGIN
    START TRANSACTION;

    UPDATE employee
    SET floor_seat_seq = NULL
    WHERE emp_id = p_emp_id;

    COMMIT;
END //

DELIMITER ;

-- 取得座位資訊
DELIMITER //

CREATE PROCEDURE get_seating_chart()
BEGIN

    SELECT 
        floor_seat_seq,
        floor_no,
        seat_no
    FROM 
        seating_chart
    ORDER BY 
        floor_no ASC, 
        seat_no ASC;
END //

DELIMITER ;

-- 清除所有座位資訊
DELIMITER //

CREATE PROCEDURE clear_all_seats()
BEGIN
    START TRANSACTION;

    UPDATE employee
    SET floor_seat_seq = NULL
    WHERE floor_seat_seq IS NOT NULL;

    COMMIT;
END //

DELIMITER ;