package org.ubiquity.bytecode;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;
import org.junit.Test;
import org.ubiquity.Ubiquity;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Date: 03/06/12
 *
 * @author François LAROCHE
 */
public class BenchmarkTest {

    private static final int LOOP_NUMBER = 1000;

    private static final MapperFacade ORIKA;
    private static final Ubiquity UBIQUITY = new Ubiquity();
    private static final Mapper DOZER = new DozerBeanMapper();

    static {
        MapperFactory factory = new DefaultMapperFactory.Builder().build();
        factory.build();
        ORIKA = factory.getMapperFacade();

    }

    @Test
    public void displayStats() {
        Order order = createOrder();
        long start, end;
        start = System.nanoTime();
        for(int i = 0; i < LOOP_NUMBER; i++) {
            UBIQUITY.map(order, Order.class);
        }
        end = System.nanoTime();
        long ubiquityTime = end - start;
        start = System.nanoTime();
        for(int i = 0; i < LOOP_NUMBER; i++) {
            ORIKA.map(order, Order.class);
        }
        end = System.nanoTime();
        long orikaTime = end - start;
        start = System.nanoTime();
        for(int i = 0; i < LOOP_NUMBER; i++) {
            DOZER.map(order, Order.class);
        }
        end = System.nanoTime();
        long dozerTime = end - start;
        System.out.println("Ubiquity copying took " + ubiquityTime + "ns, which makes " + ubiquityTime / 1000000 + "ms");
        System.out.println("Orika copying took " + orikaTime + "ns, which makes " + orikaTime / 1000000 + "ms");
        System.out.println("Dozer copying took " + dozerTime + "ns, which makes " + dozerTime / 1000000 + "ms");


        start = System.nanoTime();
        for(int i = 0; i < LOOP_NUMBER; i++) {
            UBIQUITY.map(order, Order.class);
        }
        end = System.nanoTime();
        ubiquityTime = end - start;
        start = System.nanoTime();
        for(int i = 0; i < LOOP_NUMBER; i++) {
            ORIKA.map(order, Order.class);
        }
        end = System.nanoTime();
        orikaTime = end - start;
        start = System.nanoTime();
        for(int i = 0; i < LOOP_NUMBER; i++) {
            DOZER.map(order, Order.class);
        }
        end = System.nanoTime();
        dozerTime = end - start;
        System.out.println("Ubiquity copying took (initialized) " + ubiquityTime + "ns, which makes " + ubiquityTime / 1000000 + "ms");
        System.out.println("Orika copying took (initialized) " + orikaTime + "ns, which makes " + orikaTime / 1000000 + "ms");
        System.out.println("Dozer copying took (initialized) " + dozerTime + "ns, which makes " + dozerTime / 1000000 + "ms");
    }

    @Test
    public void testUbiquity() {
        Order order = createOrder();

        long start = System.nanoTime();
        for(int i = 0; i < LOOP_NUMBER; i++) {
            UBIQUITY.map(order, Order.class);
        }
        long end = System.nanoTime();
        System.out.println("Ubiquity Test took " + (end - start) / 1000 + " ms");
    }

    @Test
    public void testOrika() {
        Order order = createOrder();

        long start = System.nanoTime();
        for(int i = 0; i < LOOP_NUMBER; i++) {
            ORIKA.map(order, Order.class);
        }
        long end = System.nanoTime();
        System.out.println("Orika Test took " + (end - start) / 1000 + " ms");
    }

    @Test
    public void testDozer() {
        Order order = createOrder();

        long start = System.nanoTime();
        for(int i = 0; i < LOOP_NUMBER; i++) {
            DOZER.map(order, Order.class);
        }
        long end = System.nanoTime();
        System.out.println("Dozer Test took " + (end - start) / 1000 + " ms");
    }

    private Order createOrder() {
        Order order = new Order();
        order.setId(200l);
        order.setOrderType(new ReferenceObject(10l, "Direct sell order", "DIRECT"));
        Collection<Product> products = new ArrayList<Product>();
        Product p = new Product();
        p.setId(123456l);
        p.setName("product 1");
        p.setDescription("First test product, yeay !");
        p.setProductType(new ReferenceObject(25l, "Nice and lovely products", "NICE"));
        products.add(p);
        p = new Product();
        p.setId(123452l);
        p.setName("product 2");
        p.setDescription("Second test product, yeay !");
        p.setProductType(new ReferenceObject(22l, "Kawaii japanese stuff", "KAWAII"));
        products.add(p);
        order.setProducts(products);
        return order;
    }

    public static class ReferenceObject {
        private Long id;
        private String label;
        private String code;

        public ReferenceObject() {}

        public ReferenceObject(Long id, String label, String code) {
            this.id = id;
            this.label = label;
            this.code = code;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }
    }

    public static class Product {
        private Long id;
        private String name;
        private ReferenceObject productType;
        private String description;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public ReferenceObject getProductType() {
            return productType;
        }

        public void setProductType(ReferenceObject productType) {
            this.productType = productType;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }
    }

    public static final class Order {
        private Long id;
        private ReferenceObject orderType;
        private Collection<Product> products;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public ReferenceObject getOrderType() {
            return orderType;
        }

        public void setOrderType(ReferenceObject orderType) {
            this.orderType = orderType;
        }

        public Collection<Product> getProducts() {
            return products;
        }

        public void setProducts(Collection<Product> products) {
            this.products = products;
        }
    }

}
