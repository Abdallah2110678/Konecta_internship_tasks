// Global variables
let products = [];
let processedProducts = [];

// Cart state
let cart = [];
let currentProduct = null;
let currentQuantity = 1;

// DOM elements
const productsGrid = document.getElementById("productsGrid");
const cartToggle = document.getElementById("cartToggle");
const cartSidebar = document.getElementById("cartSidebar");
const closeCart = document.getElementById("closeCart");
const cartContent = document.getElementById("cartContent");
const cartEmpty = document.getElementById("cartEmpty");
const cartTotal = document.getElementById("cartTotal");
const cartCount = document.getElementById("cartCount");
const cartItemCount = document.getElementById("cartItemCount");
const totalAmount = document.getElementById("totalAmount");
const confirmOrderBtn = document.getElementById("confirmOrderBtn");
const modalOverlay = document.getElementById("modalOverlay");
const orderSummary = document.getElementById("orderSummary");
const modalTotalAmount = document.getElementById("modalTotalAmount");
const newOrderBtn = document.getElementById("newOrderBtn");
const quantityInput = document.getElementById("quantityInput");
const quantityProductName = document.getElementById("quantityProductName");
const currentQuantitySpan = document.getElementById("currentQuantity");
const decreaseQty = document.getElementById("decreaseQty");
const increaseQty = document.getElementById("increaseQty");
const cancelQuantity = document.getElementById("cancelQuantity");
const addToCartConfirm = document.getElementById("addToCartConfirm");

// Load products from JSON file
async function loadProducts() {
  try {
    const response = await fetch("./data.json");
    products = await response.json();

    // Add IDs to products for cart functionality
    processedProducts = products.map((product, index) => ({
      ...product,
      id: index + 1,
    }));

    renderProducts();
  } catch (error) {
    console.error("Error loading products:", error);
    // Fallback: show error message
    productsGrid.innerHTML =
      '<div style="text-align: center; color: #666; padding: 40px;">Error loading products. Please check if data.json exists.</div>';
  }
}

// Initialize the app
document.addEventListener("DOMContentLoaded", function () {
  loadProducts();
  setupEventListeners();
  updateCartDisplay();
});

// Function to get responsive image source
function getResponsiveImage(imageObj) {
  const screenWidth = window.innerWidth;

  if (screenWidth <= 480) {
    return imageObj.mobile;
  } else if (screenWidth <= 768) {
    return imageObj.tablet;
  } else {
    return imageObj.desktop;
  }
}

// Render products
function renderProducts() {
  productsGrid.innerHTML = processedProducts
    .map(
      (product) => `
        <div class="product-card" id="product-${product.id}">
            <picture class="product-image-container">
                <source media="(max-width: 480px)" srcset="${
                  product.image.mobile
                }">
                <source media="(max-width: 768px)" srcset="${
                  product.image.tablet
                }">
                <source media="(min-width: 769px)" srcset="${
                  product.image.desktop
                }">
                <img src="${product.image.desktop}" alt="${
        product.name
      }" class="product-image">
            </picture>
            <button class="add-to-cart-btn" id="btn-${
              product.id
            }" onclick="addToCartDirect(${product.id})">
                🛒 Add to Cart
            </button>
            <div class="quantity-controls-inline" id="controls-${product.id}">
                <button onclick="decreaseQuantity(${product.id})">-</button>
                <span class="quantity-display" id="qty-${product.id}">1</span>
                <button onclick="increaseQuantity(${product.id})">+</button>
            </div>
            <div class="product-info">
                <div class="product-category">${product.category}</div>
                <div class="product-name">${product.name}</div>
                <div class="product-price">${product.price.toFixed(2)}</div>
            </div>
        </div>
    `
    )
    .join("");
}

// Setup event listeners
function setupEventListeners() {
  cartToggle.addEventListener("click", () => {
    cartSidebar.classList.add("open");
  });

  closeCart.addEventListener("click", () => {
    cartSidebar.classList.remove("open");
  });

  confirmOrderBtn.addEventListener("click", () => {
    showConfirmationModal();
  });

  newOrderBtn.addEventListener("click", () => {
    cart = [];
    modalOverlay.classList.remove("show");
    cartSidebar.classList.remove("open");
    updateCartDisplay();
  });

  modalOverlay.addEventListener("click", (e) => {
    if (e.target === modalOverlay) {
      modalOverlay.classList.remove("show");
    }
  });

  // Quantity input controls
  decreaseQty.addEventListener("click", () => {
    if (currentQuantity > 1) {
      currentQuantity--;
      currentQuantitySpan.textContent = currentQuantity;
    }
  });

  increaseQty.addEventListener("click", () => {
    currentQuantity++;
    currentQuantitySpan.textContent = currentQuantity;
  });

  cancelQuantity.addEventListener("click", () => {
    hideQuantityInput();
  });

  addToCartConfirm.addEventListener("click", () => {
    addToCart(currentProduct, currentQuantity);
    hideQuantityInput();
  });

  // Close quantity input when clicking outside
  quantityInput.addEventListener("click", (e) => {
    e.stopPropagation();
  });

  document.addEventListener("click", (e) => {
    if (
      quantityInput.classList.contains("show") &&
      !quantityInput.contains(e.target)
    ) {
      hideQuantityInput();
    }
  });
}

// Add item to cart directly
function addToCartDirect(productId) {
  const product = processedProducts.find((p) => p.id === productId);
  const existingItem = cart.find((item) => item.id === productId);

  if (existingItem) {
    existingItem.quantity += 1;
  } else {
    cart.push({
      ...product,
      quantity: 1,
    });
  }

  updateProductButton(productId);
  updateCartDisplay();
}

// Increase quantity
function increaseQuantity(productId) {
  const cartItem = cart.find((item) => item.id === productId);
  if (cartItem) {
    cartItem.quantity += 1;
    updateProductQuantityDisplay(productId);
    updateCartDisplay();
  }
}

// Decrease quantity
function decreaseQuantity(productId) {
  const cartItem = cart.find((item) => item.id === productId);
  if (cartItem) {
    if (cartItem.quantity > 1) {
      cartItem.quantity -= 1;
      updateProductQuantityDisplay(productId);
    } else {
      removeFromCart(productId);
      updateProductButton(productId);
    }
    updateCartDisplay();
  }
}

function updateProductButton(productId) {
  const cartItem = cart.find((item) => item.id === productId);
  const productCard = document.getElementById(`product-${productId}`);
  const addBtn = document.getElementById(`btn-${productId}`);
  const controls = document.getElementById(`controls-${productId}`);

  if (cartItem) {
    productCard.classList.add("selected");
    addBtn.style.display = "none";
    controls.classList.add("show");
    updateProductQuantityDisplay(productId);
  } else {
    productCard.classList.remove("selected");
    addBtn.style.display = "flex";
    controls.classList.remove("show");
  }
}

function updateProductQuantityDisplay(productId) {
  const cartItem = cart.find((item) => item.id === productId);
  const qtyDisplay = document.getElementById(`qty-${productId}`);
  if (cartItem && qtyDisplay) {
    qtyDisplay.textContent = cartItem.quantity;
  }
}

function addToCart(product, quantity) {
  const existingItem = cart.find((item) => item.id === product.id);

  if (existingItem) {
    existingItem.quantity += quantity;
  } else {
    cart.push({
      ...product,
      quantity: quantity,
    });
  }

  updateCartDisplay();
}

function removeFromCart(productId) {
  cart = cart.filter((item) => item.id !== productId);
  updateProductButton(productId);
  updateCartDisplay();
}

function updateQuantity(productId, newQuantity) {
  if (newQuantity <= 0) {
    removeFromCart(productId);
    return;
  }

  const item = cart.find((item) => item.id === productId);
  if (item) {
    item.quantity = newQuantity;
    updateCartDisplay();
  }
}

function updateCartDisplay() {
  const totalItems = cart.reduce((sum, item) => sum + item.quantity, 0);
  const total = cart.reduce((sum, item) => sum + item.price * item.quantity, 0);

  cartItemCount.textContent = totalItems;

  if (cart.length === 0) {
    cartContent.innerHTML =
      '<div class="cart-empty"><div class="cart-empty-icon">🛒</div><p>Your added items will appear here</p></div>';
    cartTotal.style.display = "none";
  } else {
    cartEmpty.style.display = "none";
    cartTotal.style.display = "block";

    cartContent.innerHTML = cart
      .map(
        (item) => `
            <div class="cart-item">
                <div class="cart-item-info">
                    <div class="cart-item-name">${item.name}</div>
                    <div class="cart-item-details">
                        <span class="cart-item-quantity">${
                          item.quantity
                        }x</span>
                        <span class="cart-item-unit-price">@ ${item.price.toFixed(
                          2
                        )}</span>
                        <span class="cart-item-total-price">${(
                          item.price * item.quantity
                        ).toFixed(2)}</span>
                    </div>
                </div>
                <button class="remove-item" onclick="removeFromCart(${
                  item.id
                })">×</button>
            </div>
        `
      )
      .join("");

    cartTotal.innerHTML = `
            <div class="order-total-row">
                <span class="order-total-label">Order Total</span>
                <span class="order-total-amount">${total.toFixed(2)}</span>
            </div>
            <div class="carbon-neutral">
                This is a <strong>carbon-neutral</strong> delivery
            </div>
            <button class="confirm-order-btn" onclick="showConfirmationModal()">Confirm Order</button>
        `;
  }
}
function showConfirmationModal() {
  const total = cart.reduce((sum, item) => sum + item.price * item.quantity, 0);

  orderSummary.innerHTML = cart
    .map(
      (item) => `
        <div class="order-item">
            <div class="order-item-info">
                <img src="${item.image.thumbnail}" alt="${
        item.name
      }" class="order-item-image">
                <div>
                    <div style="font-weight: 600;">${item.name}</div>
                    <div style="color: #666; font-size: 14px;">${
                      item.quantity
                    }x @ $${item.price.toFixed(2)}</div>
                </div>
            </div>
            <div style="font-weight: bold; color: #d2691e;">$${(
              item.price * item.quantity
            ).toFixed(2)}</div>
        </div>
    `
    )
    .join("");

  modalTotalAmount.textContent = total.toFixed(2);
  modalOverlay.classList.add("show");
  cartSidebar.classList.remove("open");
}

window.addToCartDirect = addToCartDirect;
window.increaseQuantity = increaseQuantity;
window.decreaseQuantity = decreaseQuantity;
window.removeFromCart = removeFromCart;
window.showConfirmationModal = showConfirmationModal;
