DROP TABLE IF EXISTS stats cascade;
CREATE TABLE IF NOT EXISTS stats (
  id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
  time TIMESTAMP WITHOUT TIME ZONE,
  app VARCHAR(25),
  uri VARCHAR(45),
  ip VARCHAR(25),
  CONSTRAINT pk_stats PRIMARY KEY (id)
);