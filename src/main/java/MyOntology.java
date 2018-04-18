import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntProperty;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * 本体概念类
 * 包装了OntModel类 提供一些便于算法实用的属性和方法
 */
public class MyOntology {

    /**
     * 构造函数 初始化部分本体属性
     * @param ontClass 用来包装的OntClass类
     */
    public MyOntology(OntClass ontClass) {
        ontProperties = new ArrayList<OntProperty>();
        this.ontClass = ontClass;
        for(Iterator<?> j = ontClass.listDeclaredProperties(); j.hasNext(); ) {
            OntProperty p = (OntProperty) j.next();
            ontProperties.add(p);
            //System.out.println(p.getLocalName() + " " + p.getDomain().getLocalName());
        }
    }

    // 本体中的域（class）
    private OntClass ontClass;

    // 本体中的所有元素（property）
    private ArrayList<OntProperty> ontProperties;

    /**
     * 获取本体property的数量
     * @return 本体property的数量
     */
    int getSize() {
        return ontProperties.size();
    }

    /**
     * 获取本体中所有property
     * @return 本题中所有property
     */
    ArrayList<OntProperty> getProperties() {
        return ontProperties;
    }

    /**
     * 当前本体概念是否包含指定本体概念
     * @param B 指定本体概念
     * @return 是否包含
     */
    boolean contains(MyOntology B) {
        return ontClass.hasSubClass(B.getOntClass());
    }

    // Getters

    public OntClass getOntClass() {
        return ontClass;
    }
}