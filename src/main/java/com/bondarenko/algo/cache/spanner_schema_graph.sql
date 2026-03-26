-- schema.sdl

CREATE TABLE Items
(
    id         STRING(64) NOT NULL,
    created_at TIMESTAMP OPTIONS (allow_commit_timestamp=true)
) PRIMARY KEY (id),
  ROW DELETION POLICY (OLDER_THAN(created_at, INTERVAL 365 DAY));

CREATE TABLE SimilarEdges
(
    source_id STRING(64) NOT NULL,
    target_id STRING(64) NOT NULL,
    reason    STRING(MAX)
) PRIMARY KEY (source_id, target_id),
  INTERLEAVE IN PARENT Items ON
DELETE
CASCADE;

CREATE TABLE DifferentEdges
(
    source_id STRING(64) NOT NULL,
    target_id STRING(64) NOT NULL,
    reason    STRING(MAX)
) PRIMARY KEY (source_id, target_id),
  INTERLEAVE IN PARENT Items ON
DELETE
CASCADE;

CREATE
PROPERTY GRAPH CacheGraph
    NODE TABLES (
        Items
    )
    EDGE TABLES (
        SimilarEdges
            SOURCE KEY (source_id) REFERENCES Items (id)
            DESTINATION KEY (target_id) REFERENCES Items (id)
            LABEL Similar
            PROPERTIES (reason),
        DifferentEdges
            SOURCE KEY (source_id) REFERENCES Items (id)
            DESTINATION KEY (target_id) REFERENCES Items (id)
            LABEL Different
            PROPERTIES (reason)
    );