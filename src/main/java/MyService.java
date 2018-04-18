import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.mindswap.owls.service.Service;

import java.io.File;
import java.util.*;

/**
 * 语义服务类 用于抽象表示服务
 */
public class MyService {

    /**
     * 构造函数 读取owls文件初始化service list
     */
    public MyService(String pathname) {
        qoSProperties = new ArrayList<QoSProperty>();
        inputs = new ArrayList<MyOntology>();
        outputs = new ArrayList<MyOntology>();
        vmax = new HashMap<String, Double>();
        vmin = new HashMap<String, Double>();
        SAXReader saxReader = new SAXReader();

        try {
            Document document = saxReader.read(new File(pathname));
            Element root = document.getRootElement();
            for (Iterator<?> iter = root.element("Qos").elementIterator(); iter.hasNext(); ) {
                Element element = (Element) iter.next();
                if (element.getName().equals("QosProperty")) {
                    // 以下获取Qos的各种信息
                    String propName = "";
                    double propValue = 0.0;
                    String propUnit = "";
                    String propType = "";
                    for (Iterator<?> iter2 = element.elementIterator(); iter2.hasNext(); ) {
                        Element element2 = (Element) iter2.next();
                        //System.out.println(element2.getName());
                        if (element2.getName().equals("qosPropertyName")) {
                            propName = element2.getText();
                        } else if (element2.getName().equals("hasValue")) {
                            propValue = Double.valueOf(element2.getText());
                        } else if (element2.getName().equals("hasUnit")) {
                            propUnit = element2.getText();
                        } else if (element2.getName().equals("hasType")) {
                            propType = element2.getText();
                        }
                    }

                    if (vmin.containsKey(propName)) {
                        if (propValue < vmin.get(propName)) {
                            vmin.put(propName, propValue);
                        }
                    } else {
                        vmin.put(propName, propValue);
                    }

                    if (vmax.containsKey(propName)) {
                        if (propValue > vmax.get(propName)) {
                            vmax.put(propName, propValue);
                        }
                    } else {
                        vmax.put(propName, propValue);
                    }

                    qoSProperties.add(new QoSProperty(propName, new QoSCharacteristic(propType, propUnit, Double.valueOf(propValue))));


                }
            }


            // 此处读取owls文件  放弃OWLS-API  使用Jena+dom4j
            ArrayList<String> inputNames = new ArrayList<String>();
            ArrayList<String> outputNames = new ArrayList<String>();
            OntModel model = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);

            for (Iterator<?> iter = root.element("AtomicProcess").elementIterator(); iter.hasNext(); ) {
                Element element = (Element) iter.next();
                if (element.getName().equals("hasInput")) {
                    inputNames.add(element.attribute("resource").getValue().substring(1));
                } else if (element.getName().equals("hasOutput")) {
                    outputNames.add(element.attribute("resource").getValue().substring(1));
                }
            }

            // 根据输入输出的名称 去获取对应名称（ID）的地址
            Set<String> inputAddr = new HashSet<String>();
            Set<String> outputAddr = new HashSet<String>();
            for (String name : inputNames) {
                for (Element element : (ArrayList<Element>) root.elements("Input")) {
                    if (element.attribute("ID").getText().equals(name)) {
                        String addr = element.element("parameterType").getText();
                        if (!addr.equals("")) {
                            inputAddr.add(addr.split("#")[0]);
                        } else {
                            inputs.add(new MyOntology(model.createClass(name)));
                        }

                    }
                }

            }

            for (String name : outputNames) {
                for (Element element : (ArrayList<Element>) root.elements("Output")) {
                    if (element.attribute("ID").getText().equals(name)) {
                        String addr = element.element("parameterType").getText();
                        if (!addr.equals("")) {
                            outputAddr.add(addr.split("#")[0]);
                        } else {
                            outputs.add(new MyOntology(model.createClass(name)));
                        }

                    }
                }
            }

            // 读取inputAddr和outputAddr中的地址
            for (String addr : inputAddr) {
                ArrayList<MyOntology> ontologies =  Algorithms.readOWL(addr);
                for (MyOntology myOntology : ontologies) {
                    if (inputNames.contains(myOntology.getOntClass().getLocalName())) {
                        inputs.add(myOntology);
                    }
                }
            }

            for (String addr : outputAddr) {
                ArrayList<MyOntology> ontologies =  Algorithms.readOWL(addr);
                for (MyOntology myOntology : ontologies) {
                    if (outputNames.contains(myOntology.getOntClass().getLocalName())) {
                        outputs.add(myOntology);
                    }
                }
            }


        } catch (DocumentException e) {
                e.printStackTrace();
        }

    }

    // 本类维护的Service
    private Service service;

    // 本类维护的QoSProperties
    private ArrayList<QoSProperty> qoSProperties;

    // Service的输入
    private ArrayList<MyOntology> inputs;

    // Service的输出
    private ArrayList<MyOntology> outputs;

    // 最大value map
    private Map<String, Double> vmax;

    // 最小value map
    private Map<String, Double> vmin;
    /**
     * 归一化
     * @param qoSProperty QoSCharacteristic实例
     * @return qoSProperty中的value归一化的结果
     */
    public double Normalize(QoSProperty qoSProperty) {
        double tmin = vmin.get(qoSProperty.getName());
        double tmax = vmax.get(qoSProperty.getName());
        if (qoSProperty.getQoSCharacteristic().getType().equals("benefit")) {
            return tmin == tmin ? 1 : (qoSProperty.getQoSCharacteristic().getValue() - tmin) / (tmax - tmin);
        } else if (qoSProperty.getQoSCharacteristic().getType().equals("cost")) {
            return tmax == tmin ? 1 : (tmax - qoSProperty.getQoSCharacteristic().getValue()) / (tmax - tmin);
        } else {
            return -1;
        }
    }


    // Getters
    public Service getService() {
        return service;
    }

    public ArrayList<MyOntology> getInputs() {
        return inputs;
    }

    public ArrayList<MyOntology> getOutputs() {
        return outputs;
    }

    public ArrayList<QoSProperty> getQoSProperties() {
        return qoSProperties;
    }
}
