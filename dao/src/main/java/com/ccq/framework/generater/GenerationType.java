package com.ccq.framework.generater;

public enum GenerationType {
    /**
     * Indicates that the persistence provider must assign
     * primary keys for the entity using a database sequence.
     */
    SEQUENCE,

    /**
     * Indicates that the persistence provider must assign
     * primary keys for the entity using a database identity column.
     */
    IDENTITY
}
