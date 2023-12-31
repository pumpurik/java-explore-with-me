DROP TABLE IF EXISTS stats cascade;
CREATE TABLE IF NOT EXISTS hits (
  id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
  timestamp TIMESTAMP WITHOUT TIME ZONE,
  app VARCHAR(25),
  uri VARCHAR(45),
  ip VARCHAR(25),
  CONSTRAINT pk_hits PRIMARY KEY (id)
);