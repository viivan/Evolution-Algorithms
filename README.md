# Algorithms
算法采用Java代码实现，其中OWL的解析采用jena实现，OWL-S的解析采用jena+dom4j实现  

# MyOntology
本体概念类，存储OWL文件中解析得到的本体概念

# MyService
语义服务类，表示服务的语义描述信息，存储OWL-S文件中解析得到的服务

# QoSProperty
QoS属性类，表示QoS属性，由OWL-S文件解析得到

# QoSCharacteristic
QoS属性特征类，表示QoS的属性特征，包括value，unit，type等，由OWL-S文件解析得到

# QoSConstraint
QoS约束，用户请求中的QoS约束信息，包括QoS阈值和权重，QoS阈值用于服务过滤算法，QoS权重用于服务综合评分计算