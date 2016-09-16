package de.hofuniversity.iisys.schub.openstack.model;

public enum EntityState
{
    UNKNOWN,
    NEW,
    CREATING,
    RUNNING,
    OFFLINE,
    MODIFYING,
    DELETING,
    DELETED,
    ERROR,
    MISSING;
}
