INSERT INTO User(userName, email, password, role)
VALUES
    ('관리자', 'hanaro', '$2a$10$z.5rnfu2KqpzOJPU0LHh4endzgFBwvmxriuSEs.nxFyt.LgPUHVA.', 'ROLE_ADMIN'),
    ('김보겸','kbkyeom@naver.com','$2a$10$..aINRWy6y0HsNNnIKOq9e6PcOScaWS1jzHzHUOZdjUIIXhPZj8q.','ROLE_USER');

INSERT INTO Item (price, quantity, itemName)
VALUES
    (1000, 10, '소주'),
    (2000, 5, '맥주');

INSERT INTO Cart (user) VALUES (2);

INSERT INTO CartItem (cart, item, amount)
VALUES
    (1, 1, 2),
    (1, 2, 3);

INSERT INTO Orders (user_id, totalPrice, createdAt)
VALUES (2, (1000*2 + 2000*3), NOW());

INSERT INTO OrderItem (amount, totalPrice, item, orders)
VALUES
    (2, (SELECT price FROM Item WHERE id = 1) * 2, 1, 1), -- 소주 2개
    (3, (SELECT price FROM Item WHERE id = 2) * 3, 2, 1); -- 맥주 3개

-- -----------------------------------------------더미데이터

INSERT INTO User (userName, email, password, role)
VALUES
    ('홍길동', 'hong@example.com', '$2a$10$abcdefghijABCDEFGHIJklmnopqrstuvWXyz0123456789abc', 'ROLE_USER'),
    ('이순신', 'lee@example.com', '$2a$10$abcdefghijABCDEFGHIJklmnopqrstuvWXyz0123456789abc', 'ROLE_USER'),
    ('김철수', 'kim@example.com', '$2a$10$abcdefghijABCDEFGHIJklmnopqrstuvWXyz0123456789abc', 'ROLE_USER');

INSERT INTO Item (price, quantity, itemName)
VALUES
    (5000, 20, '와인'),
    (1500, 50, '콜라'),
    (1200, 30, '사이다');

INSERT INTO Cart (user)
VALUES
    (3), -- 홍길동
    (4), -- 이순신
    (5); -- 김철수

INSERT INTO CartItem (cart, item, amount)
VALUES
    (2, 3, 1), -- 홍길동 - 와인 1병
    (2, 4, 5), -- 홍길동 - 콜라 5개
    (3, 5, 2), -- 이순신 - 사이다 2개
    (4, 1, 1), -- 김철수 - 소주 1병
    (4, 2, 1); -- 김철수 - 맥주 1병

INSERT INTO Orders (user_id, totalPrice, createdAt)
VALUES
    (3, (5000*1 + 1500*5), NOW()), -- 홍길동 (와인1, 콜라5)
    (4, (1200*2), DATE_SUB(NOW(), INTERVAL 10 MINUTE)), -- 이순신 (사이다2)
    (5, (1000*1 + 2000*1), DATE_SUB(NOW(), INTERVAL 30 MINUTE)); -- 김철수 (소주1, 맥주1)

INSERT INTO OrderItem (amount, totalPrice, item, orders)
VALUES
    (1, (SELECT price FROM Item WHERE id = 3) * 1, 3, 2), -- 홍길동 - 와인 1
    (5, (SELECT price FROM Item WHERE id = 4) * 5, 4, 2), -- 홍길동 - 콜라 5
    (2, (SELECT price FROM Item WHERE id = 5) * 2, 5, 3), -- 이순신 - 사이다 2
    (1, (SELECT price FROM Item WHERE id = 1) * 1, 1, 4), -- 김철수 - 소주 1
    (1, (SELECT price FROM Item WHERE id = 2) * 1, 2, 4); -- 김철수 - 맥주 1
