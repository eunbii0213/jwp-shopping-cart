package cart.business;

import cart.business.domain.Cart;
import cart.business.domain.Carts;
import cart.business.domain.Member;
import cart.business.domain.Product;
import cart.business.domain.Products;
import cart.persistence.CartProductDao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class CartProductService {

    private CartProductDao cartProductDao;
    private Carts carts;
    private Products products;

    public CartProductService(CartProductDao cartProductDao) {
        initSetting();
        this.cartProductDao = cartProductDao;
    }

    private void initSetting() {
        Product dog = new Product("강아지", "https://search.pstatic.net/common/?src=http%3A%2F%2Fblogfiles.naver.net%2FMjAyMzA0MTJfMTM5%2FMDAxNjgxMjYzNTU2NDI2.wlJys88BgEe2MzQrd2k5jjtXsObAZaOM4eidDcM3iLUg.5eE5nUvqLadE0MwlF9c8XLOgqghimMWQU2psfcRuvFYg.PNG.noblecase%2F20230412_102917_5.png&type=a340", 10000);
        Product cat = new Product("고양이", "https://search.pstatic.net/common/?src=http%3A%2F%2Fblogfiles.naver.net%2FMjAyMzA0MTBfMjcz%2FMDAxNjgxMTAwOTc5Nzg3.MEOt2vmlKWIlW4PQFfgHPILk0dJxwX42KzrDVu4puSwg.GcSSR6FJWup8Uo1H0xo0_4FuIMhJYJpw6tUmpKP9-Wsg.JPEG.catopia9%2FDSC01276.JPG&type=a340", 20000);
        Product hamster = new Product("햄스터", "https://search.pstatic.net/common/?src=http%3A%2F%2Fblogfiles.naver.net%2FMjAyMzA0MjJfMTcx%2FMDAxNjgyMTMzNzQ5MjQ2.DPd6D6NUbKSAOLBVosis9Ptz_lBGkyT4lncgLV0buZUg.KK0-N7fzYAy43jlHd9-4hQJ2CYu7RRqV3UWUi29FQJgg.JPEG.smkh15112%2FIMG_4554.JPG&type=a340", 5000);

        products = new Products(new ArrayList<>(List.of(dog, cat, hamster)));

        Member judy = new Member("coding_judith@gmail.com", "judy123");
        Cart cartJudy = new Cart(judy.getId(), new Products(new ArrayList<>()));

        Member teo = new Member("coding_teo@gmail.com", "teo123");
        Cart cartTeo = new Cart(teo.getId(), new Products(new ArrayList<>()));

        carts = new Carts(new ArrayList<>(List.of(cartJudy, cartTeo)));
    }

    /*
     * 객체지향적으로 cart라는 객체가 product를 담는다
     * 담는 행위는 cart 스스로가 할 수 있다
     * cart는 product담을 method가 필요하다
     */
    @Transactional
    public Integer create(Integer productId, Integer memberId) {
        Cart cart = carts.findCartByMemberId(memberId);
        Product product = products.findProductById(productId);
        cart.addProduct(product);

        return cartProductDao.insert(cart, product);
    }

//    @Transactional(readOnly = true)
//    public List<CartProductEntity> read() {
//        return cartProductDao.findAll();
//    }

    @Transactional
    public Integer delete(Integer id) {
        return cartProductDao.remove(id);
    }

//    private CartProductEntity makeCartProductFromRequest(Integer productId, Integer memberId) {
//        return new CartProductEntity(null, productId, memberId);
//    }
//
//    private CartProductEntity makeCartProductFromResponse(CartProductResponse response) {
//        return new CartProductEntity(response.getId(), response.getProductId(), response.getCartId());
//    }

    public Carts getCarts() {
        return carts;
    }

    public Products getProducts() {
        return products;
    }
}
