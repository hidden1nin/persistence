package com.hiddentech.persistantance.types;

import org.bukkit.configuration.serialization.ConfigurationSerializable;

public interface Persistent
{
    int getId();

    void destroy();
}
