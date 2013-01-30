package lv.k2611a.domain;

public enum DamageType {
    BULLET{
        @Override
        public double vsInfantry() {
            return 2;
        }

        @Override
        public double vsLight() {
            return 0.75;
        }

        @Override
        public double vsHeavy() {
            return 0.2;
        }

        @Override
        public double vsBuilding() {
            return 1;
        }
    },
    SHELL{
        @Override
        public double vsInfantry() {
            return 0.2;
        }

        @Override
        public double vsLight() {
            return 1.5;
        }

        @Override
        public double vsHeavy() {
            return 1.5;
        }

        @Override
        public double vsBuilding() {
            return 1;
        }
    },
    MISSILE{
        @Override
        public double vsInfantry() {
            return 1;
        }

        @Override
        public double vsLight() {
            return 1;
        }

        @Override
        public double vsHeavy() {
            return 1;
        }

        @Override
        public double vsBuilding() {
            return 2;
        }
    };


    public abstract double vsInfantry();
    public abstract double vsLight();
    public abstract double vsHeavy();
    public abstract double vsBuilding();


}
