public class  QoSCharacteristic {

    /**
     * 构造函数
     * @param type 类型
     * @param unit 单位
     * @param value 值
     */
    public QoSCharacteristic(String type, String unit, double value) {
        this.type = type;
        this.unit = unit;
        this.value = value;
    }


    // 类型
    private String type;

    // 单位
    private String unit;

    // 值
    private double value;

    // Getters

    public String getType() {
        return type;
    }

    public String getUnit() {
        return unit;
    }

    public double getValue() {
        return value;
    }

}