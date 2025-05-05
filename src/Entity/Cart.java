
package Entity;

import java.util.*;

public class Cart {
    private String cartID;
    private CartItem[] items = new CartItem[100];
    private int size = 0;
    private Coupon appliedDiscount;
    private int shippingFee;
    
    //getters and setters
    // Optional Getter for size
    public String getCartID(){
        return cartID;
    }
    public void setCartID(String cartId){
        this.cartID = cartId;
}
    public int getSize() {
        return size;
    }

    // Optional Getter and Setter for appliedDiscount
    public void setAppliedDiscount(Coupon coupon) {
        this.appliedDiscount = coupon;
    }

    public Coupon getAppliedDiscount() {
        return appliedDiscount;
    }

    public int getQuantity(String productId) {
        for (CartItem item : items) {
            if (item.getProduct().getProductid().equals(productId)) {
                return item.getQuantity();
            }
        }
        return 0; // Product not in cart
    }

    private void ensureCapacity() {
        if (size >= items.length) {
            items = Arrays.copyOf(items, items.length * 2);
        }
    }

    public void addItem(Product product, int quantity) {
        if (product == null) {
            System.out.println("ERROR: Tried to add null product to cart");
            return;
        }
        for (int i = 0; i < size; i++) {
            if (items[i].getProduct().getProductid().equals(product.getProductid())) {
                items[i].setQuantity(items[i].getQuantity() + quantity);
                return;
            }
        }
        ensureCapacity();
        items[size++] = new CartItem(product, quantity);
    }

    public void updateQuantity(String productId, int quantity) {
        for (int i = 0; i < size; i++) {
            if (items[i].getProduct().getProductid().equals(productId)) {
                if (quantity <= 0) {
                    removeItem(productId);
                } else {
                    items[i].setQuantity(quantity);
                }
                return;
            }
        }
        System.out.println("Product not found in cart!");
    }

    public void removeItem(String productId) {
        for (int i = 0; i < size; i++) {
            if (items[i].getProduct().getProductid().equals(productId)) {
                System.arraycopy(items, i + 1, items, i, size - i - 1);
                items[--size] = null;
                return;
            }
        }
    }
    public void setItems(List<CartItem> newItems) {
        // Reset the array and size
        this.items = new CartItem[100]; // Use your default capacity
        this.size = 0;

        // Add all items from the provided list
        if (newItems != null) {
            for (CartItem item : newItems) {
                addItem(item.getProduct(), item.getQuantity());
            }
        }
    }

    public void applyCoupon(Coupon coupon) {
        this.appliedDiscount = (coupon != null && coupon.isValid()) ? coupon : null;
    }

    public double getSubtotal() {
        double subtotal = 0;
        for (int i = 0; i < size; i++) {
            subtotal += items[i].getSubtotal();
        }
        return subtotal;
    }
        // Calculate subtotal (without discount)
    public double calculateSubtotal() {
        double subtotal = 0.0;
        for (CartItem item : getItems()) {
            subtotal += item.getProduct().getProductprice() * item.getQuantity();
        }
        return subtotal;
    }

    public double getDiscountAmount() {
        return appliedDiscount != null ? appliedDiscount.calculateDiscount(getSubtotal(), shippingFee) : 0;
    }

    public double getTaxAmount() {
        double discountedSubtotal = getSubtotal() - getDiscountAmount();
        return discountedSubtotal * 0.06;
    }

    public double getTotal() {
        double total = getSubtotal() - getDiscountAmount() + getTaxAmount();
        return Math.max(total, 0);
    }

    public String getAppliedCouponCode() {
        return appliedDiscount != null ? appliedDiscount.getCouponCode() : null;
    }

    public void clearCoupon() {
        appliedDiscount = null;
    }

    

    public CartItem[] getItems() {
        CartItem[] currentItems = new CartItem[size];
        System.arraycopy(items, 0, currentItems, 0, size);
        return currentItems;
    }

    public void clearCart() {
        Arrays.fill(items, 0, size, null);
        size = 0;
        clearCoupon();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Cart {\n");
        sb.append("  Items:\n");
        for (int i = 0; i < size; i++) {
            sb.append("    - ").append(items[i].toString()).append("\n");
        }
        sb.append("  Subtotal: RM").append(String.format("%.2f", getSubtotal())).append("\n");
        sb.append("  Discount: RM").append(String.format("%.2f", getDiscountAmount()));
        if (appliedDiscount != null) {
            sb.append(" (").append(appliedDiscount.getCouponCode()).append(" - ")
              .append(appliedDiscount.getDiscountType()).append(")");
        }
        sb.append("\n");
        sb.append("  Tax (6% SST): RM").append(String.format("%.2f", getTaxAmount())).append("\n");
        sb.append("  Total: RM").append(String.format("%.2f", getTotal())).append("\n");
        sb.append("}");
        return sb.toString();
    }
}
