package me.phoenix.netherwartmacro.fastbreak;

import gg.essential.vigilance.Vigilant;
import gg.essential.vigilance.data.JVMAnnotationPropertyCollector;
import gg.essential.vigilance.data.Property;
import gg.essential.vigilance.data.PropertyType;

import java.io.File;

public class Config extends Vigilant {

    @Property(
            type = PropertyType.SWITCH,
            name = "Fastbreak enabled",
            category = "Netherwart Macro",
            subcategory = "Fastbreak"
    )
    public static boolean fastbreak;

    public Config() {
        super(new File("./config/netherwartmacro/config.toml"), "Netherwart Macro", new JVMAnnotationPropertyCollector());
        initialize();
    }

}
