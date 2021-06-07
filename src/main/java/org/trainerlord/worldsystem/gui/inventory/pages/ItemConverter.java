package org.trainerlord.worldsystem.gui.inventory.pages;

import org.trainerlord.worldsystem.gui.inventory.OrcItem;

public interface ItemConverter<T> {

    OrcItem convert(T element);

}
