CREATE TABLE `suggestions` (
    `message` BIGINT(20) DEFAULT NULL,
    `message_channel` BIGINT(20) DEFAULT NULL,
    `popular_message` BIGINT(20) DEFAULT NULL,
    `discussion_message` BIGINT(20) DEFAULT NULL,
    `author_id` BIGINT(20) DEFAULT NULL,
    `date` TIMESTAMP DEFAULT NULL,
    `upvotes` INT(11) DEFAULT NULL,
    `downvotes` INT(11) DEFAULT NULL,
    `special_reactions` VARCHAR(255) DEFAULT NULL
);