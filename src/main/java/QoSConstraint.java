/**
 * QoS约束类
 */
public class QoSConstraint {

    /**
     * 构造函数
     * @param value 值
     * @param unit 单位
     * @param type 类型
     * @param confidence 置信度
     * @param weight 权重
     */
    public QoSConstraint(double value, String unit, String type, double confidence, double weight) {
        this.value = value;
        this.unit = unit;
        this.type = type;
        this.confidence = confidence;
        this.weight = weight;
    }

    // 阈值
    private double value;
    // 单位
    private String unit;
    // 类型
    private String type;
    // 置信度
    private double confidence;
    // 权重
    private double weight;

    public double getValue() {
        return value;
    }

    public double getConfidence() {
        return confidence;
    }

    public double getWeight() {
        return weight;
    }

    public String getUnit() {
        return unit;
    }

    public String getType() {
        return type;
    }
}
