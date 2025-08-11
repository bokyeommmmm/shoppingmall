CREATE TABLE `User` (
	`userId`	int	NOT NULL,
	`loginId`	varchar(30)	NOT NULL,
	`password`	varchar(30)	NOT NULL,
	`role`	enum	NOT NULL	DEFAULT user,
	`name`	varchar(20)	NOT NULL
);

CREATE TABLE `BaseEntity` (
	`createdAt`	timestamp	NOT NULL,
	`updatedAt`	timestamp	NOT NULL
);

CREATE TABLE `Orders` (
	`orderId`	int	NOT NULL,
	`totalPrice`	int	NOT NULL,
	`status`	enum	NOT NULL,
	`userId`	int	NOT NULL
);

CREATE TABLE `Item` (
	`itemId`	int	NOT NULL,
	`itemName`	varchar(21)	NOT NULL,
	`price`	int	NOT NULL,
	`stock`	int	NOT NULL
);

CREATE TABLE `OrderItem` (
	`orderItemId`	int	NOT NULL,
	`orderId`	int	NOT NULL,
	`quantity`	int	NOT NULL,
	`price`	int	NOT NULL,
	`itemId`	int	NOT NULL
);

CREATE TABLE `Cart` (
	`Key`	VARCHAR(255)	NOT NULL,
	`count`	int	NOT NULL,
	`userId`	int	NOT NULL,
	`itemId`	int	NOT NULL
);

CREATE TABLE `ItemImage` (
	`ItemImageId`	int	NOT NULL,
	`imageSrc`	varchar(255)	NOT NULL,
	`itemId`	int	NOT NULL
);

ALTER TABLE `User` ADD CONSTRAINT `PK_USER` PRIMARY KEY (
	`userId`
);

ALTER TABLE `Orders` ADD CONSTRAINT `PK_ORDERS` PRIMARY KEY (
	`orderId`
);

ALTER TABLE `Item` ADD CONSTRAINT `PK_ITEM` PRIMARY KEY (
	`itemId`
);

ALTER TABLE `OrderItem` ADD CONSTRAINT `PK_ORDERITEM` PRIMARY KEY (
	`orderItemId`,
	`orderId`
);

ALTER TABLE `Cart` ADD CONSTRAINT `PK_CART` PRIMARY KEY (
	`Key`
);

ALTER TABLE `ItemImage` ADD CONSTRAINT `PK_ITEMIMAGE` PRIMARY KEY (
	`ItemImageId`
);

ALTER TABLE `Orders` ADD CONSTRAINT `FK_User_TO_Orders_1` FOREIGN KEY (
	`userId`
)
REFERENCES `User` (
	`userId`
);

ALTER TABLE `OrderItem` ADD CONSTRAINT `FK_Orders_TO_OrderItem_1` FOREIGN KEY (
	`orderId`
)
REFERENCES `Orders` (
	`orderId`
);

ALTER TABLE `OrderItem` ADD CONSTRAINT `FK_Item_TO_OrderItem_1` FOREIGN KEY (
	`itemId`
)
REFERENCES `Item` (
	`itemId`
);

ALTER TABLE `Cart` ADD CONSTRAINT `FK_User_TO_Cart_1` FOREIGN KEY (
	`userId`
)
REFERENCES `User` (
	`userId`
);

ALTER TABLE `Cart` ADD CONSTRAINT `FK_Item_TO_Cart_1` FOREIGN KEY (
	`itemId`
)
REFERENCES `Item` (
	`itemId`
);

ALTER TABLE `ItemImage` ADD CONSTRAINT `FK_Item_TO_ItemImage_1` FOREIGN KEY (
	`itemId`
)
REFERENCES `Item` (
	`itemId`
);

