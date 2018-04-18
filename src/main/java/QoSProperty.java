public class QoSProperty {

    /**
     * 构造函数
     * @param qoSCharacteristic 用于初始化的QoSCharacteristic List
     */
    public QoSProperty(String name,QoSCharacteristic qoSCharacteristic) {
        this.name = name;
        this.qoSCharacteristic = qoSCharacteristic;

    }

    // 名称
    private String name;

    // 本类维护的QoSCharacteristic List
    private QoSCharacteristic qoSCharacteristic;


    // Getters
    public QoSCharacteristic getQoSCharacteristic() {
        return qoSCharacteristic;
    }

    public String getName() {
        return name;
    }
}
