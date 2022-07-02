package com.alcatrazescapee.notreepunching.platform.event;

@FunctionalInterface
public interface CanHarvestCallback
{
    void accept(boolean canHarvest);
}
