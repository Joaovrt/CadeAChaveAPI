CREATE TABLE IF NOT EXISTS users (
  id SERIAL NOT NULL,
  user_name varchar(255) NOT NULL,
  password varchar(255) NOT NULL,
  account_non_expired boolean DEFAULT NULL,
  account_non_locked boolean DEFAULT NULL,
  credentials_non_expired boolean DEFAULT NULL,
  enabled boolean DEFAULT NULL,
  PRIMARY KEY (id),
  CONSTRAINT uk_user_name UNIQUE (user_name)
) ;