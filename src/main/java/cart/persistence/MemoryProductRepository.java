package cart.persistence;

import cart.business.ProductRepository;
import cart.business.domain.Product;
import cart.business.domain.ProductImage;
import cart.business.domain.ProductName;
import cart.business.domain.ProductPrice;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Repository
public class MemoryProductRepository implements ProductRepository {

    private final Map<Integer, Product> store = new ConcurrentHashMap<>();
    private int sequence = 1;

    @Override
    public Integer insert(Product product) {
        store.put(sequence, product);
        return sequence++;
    }

    @Override
    public List<Product> findAll() {
        return store.entrySet()
                .stream()
                .map(entry -> new Product(entry.getKey(),
                        new ProductName(entry.getValue().getName()),
                        new ProductImage(entry.getValue().getImage()),
                        new ProductPrice(entry.getValue().getProductPrice())))
                .collect(Collectors.toList());
    }

    @Override
    public Product remove(Integer productId) {
        return store.remove(productId);
    }
}
