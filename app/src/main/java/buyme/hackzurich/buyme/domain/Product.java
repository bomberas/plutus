package buyme.hackzurich.buyme.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aminendah on 9/16/2017.
 */

public class Product {
    private String category;
    private ArrayList<Instance> instances;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public ArrayList<Instance> getInstances() {
        return instances;
    }

    public void setInstances(ArrayList<Instance> instances) {
        this.instances = instances;
    }
}
