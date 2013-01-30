package lv.k2611a.domain;

public enum ArmorType {
    INFANTRY {
        @Override
        public double getDamage(DamageType damageType) {
            return damageType.vsInfantry();
        }
    },
    LIGHT {
        @Override
        public double getDamage(DamageType damageType) {
            return damageType.vsLight();
        }
    },
    HEAVY {
        @Override
        public double getDamage(DamageType damageType) {
            return damageType.vsHeavy();
        }

    },
    BUILDING {
        @Override
        public double getDamage(DamageType damageType) {
            return damageType.vsBuilding();
        }

    },
    TESTING {
        @Override
        public double getDamage(DamageType damageType) {
            // ignore the type of damage
            return 1;
        }

    };

    public abstract double getDamage(DamageType damageType);


}
