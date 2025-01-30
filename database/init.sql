CREATE TABLE IF NOT EXISTS edge (
    from_id integer,
    to_id integer,
    PRIMARY KEY (from_id, to_id)
);

CREATE INDEX IF NOT EXISTS to_id_idx ON edge (to_id);