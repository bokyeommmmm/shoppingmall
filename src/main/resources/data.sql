insert into User(userName, email, password)
values ('관리자', 'hanaro', '$2a$10$z.5rnfu2KqpzOJPU0LHh4endzgFBwvmxriuSEs.nxFyt.LgPUHVA.'),
       ('김보겸','kbkyeom@naver.com','$2a$10$..aINRWy6y0HsNNnIKOq9e6PcOScaWS1jzHzHUOZdjUIIXhPZj8q.');

INSERT INTO Item (`price`, `quantity`, `itemName`)
VALUES ('1000', '10', '소주'),
       ('2000', '5', '맥주');


INSERT INTO Cart (`user`)
VALUES ('2');

INSERT INTO CartItem (`cart`, `item`, `amount`)
VALUES ('1', '1', 2),
       ('1', '2', 3);

INSERT INTO orders (`user_id`)
VALUES ( '2');

INSERT INTO `orderItem` (`amount`, `id`, `order_id`)
VALUES ('2', '1', '1'),
       ('3','2','1');
