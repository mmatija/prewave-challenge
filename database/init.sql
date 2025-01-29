CREATE TABLE IF NOT EXISTS edge (
    from_id bigint,
    to_id bigint,
    PRIMARY KEY (from_id, to_id)
);

CREATE INDEX IF NOT EXISTS to_id_idx ON edge (to_id);