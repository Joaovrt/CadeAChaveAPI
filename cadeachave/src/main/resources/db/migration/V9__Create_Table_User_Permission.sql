CREATE TABLE IF NOT EXISTS user_permission (
  id_user INTEGER NOT NULL REFERENCES users (id),
  id_permission INTEGER NOT NULL REFERENCES permission (id),
  PRIMARY KEY (id_user,id_permission),
) ;