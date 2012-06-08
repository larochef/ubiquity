package org.ubiquity.bytecode;

import com.google.common.base.Stopwatch;
import junit.framework.Assert;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;
import org.junit.Test;
import org.ubiquity.Ubiquity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.TimeUnit;

/**
 * Benchmark test testing the speed of different copy frameworks.
 * This test should be run using the following arguments :
 * -XX:+PrintCompilation -verbose:gc
 *
 * Date: 03/06/12
 *
 * @author François LAROCHE
 */
public class BenchmarkTest {

    private static final int WARM_LOOP_NUMBER = 200000;
    private static final int LOOP_NUMBER = 10000;
    private static final int NANO_TO_MS = 1000000;

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
        Stopwatch watch = new Stopwatch();
        Collection<Order> orders = new ArrayList<Order>(LOOP_NUMBER);
        Order order = createOrder();

        System.out.println("Warming up ORIKA...");
        watch.start();
        for(int i = 0; i < WARM_LOOP_NUMBER; i++) {
            ORIKA.map(order, Order.class);
        }
        watch.stop();
        long orikaWarmTime = watch.elapsedTime(TimeUnit.NANOSECONDS);
        System.gc();
        System.out.println("Executing ORIKA");
        watch.reset().start();
        for(int i = 0; i < LOOP_NUMBER; i++) {
            orders.add(ORIKA.map(order, Order.class));
        }
        watch.stop();
        long orikaTime = watch.elapsedTime(TimeUnit.NANOSECONDS);
        System.out.println("Finished executing ORIKA");
        Assert.assertEquals(LOOP_NUMBER, orders.size());
        orders.clear();

        System.out.println("Warming up UBIQUITY...");
        watch.reset().start();
        for(int i = 0; i < WARM_LOOP_NUMBER; i++) {
            UBIQUITY.map(order, Order.class);
        }
        watch.stop();
        long ubiquityWarmTime = watch.elapsedTime(TimeUnit.NANOSECONDS);
        System.gc();
        System.out.println("Executing UBIQUITY");
        watch.reset().start();
        for(int i = 0; i < LOOP_NUMBER; i++) {
            orders.add(UBIQUITY.map(order, Order.class));
        }
        watch.stop();
        long ubiquityTime = watch.elapsedTime(TimeUnit.NANOSECONDS);
        System.out.println("Finished executing UBIQUITY");
        Assert.assertEquals(LOOP_NUMBER, orders.size());
        orders.clear();

        System.out.println("Warming up DOZER...");
        watch.reset().start();
        for(int i = 0; i < WARM_LOOP_NUMBER; i++) {
            DOZER.map(order, Order.class);
        }
        watch.stop();
        long dozerWarmTime = watch.elapsedTime(TimeUnit.NANOSECONDS);
        System.gc();

        System.out.println("Executing DOZER");
        watch.reset().start();
        for(int i = 0; i < LOOP_NUMBER; i++) {
            orders.add(DOZER.map(order, Order.class));
        }
        watch.stop();
        long dozerTime = watch.elapsedTime(TimeUnit.NANOSECONDS);
        System.out.println("Finished executing DOZER");
        Assert.assertEquals(LOOP_NUMBER, orders.size());
        orders.clear();

        System.out.println("Ubiquity copying took " + ubiquityWarmTime + "ns, which makes " + ubiquityWarmTime / NANO_TO_MS + "ms");
        System.out.println("Orika copying took " + orikaWarmTime + "ns, which makes " + orikaWarmTime / NANO_TO_MS + "ms");
        System.out.println("Dozer copying took " + dozerWarmTime + "ns, which makes " + dozerWarmTime / NANO_TO_MS + "ms");

        System.out.println("Ubiquity copying took (initialized) " + ubiquityTime + "ns, which makes " + ubiquityTime / NANO_TO_MS + "ms");
        System.out.println("Orika copying took (initialized) " + orikaTime + "ns, which makes " + orikaTime / NANO_TO_MS + "ms");
        System.out.println("Dozer copying took (initialized) " + dozerTime + "ns, which makes " + dozerTime / NANO_TO_MS + "ms");
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
